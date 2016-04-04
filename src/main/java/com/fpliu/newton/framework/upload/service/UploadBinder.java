package com.fpliu.newton.framework.upload.service;

import java.io.File;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import android.os.RemoteException;
import android.text.TextUtils;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.config.Configuration;
import com.fpliu.newton.framework.data.KV;
import com.fpliu.newton.framework.security.Base64;
import com.fpliu.newton.framework.security.SymmetricalEncryption;
import com.fpliu.newton.framework.security.SymmetricalEncryption.Algorithm;
import com.fpliu.newton.framework.storage.AzureStorage;
import com.fpliu.newton.framework.upload.UploadData;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;

/**
 * 上传服务的实现者
 * 
 * @author 792793182@qq.com 2014-12-03
 * 
 */
final class UploadBinder extends IUploadManager.Stub {
	
	private static final String TAG = UploadBinder.class.getSimpleName();
	
	private static final int SLEEP_TIME = 6000;
	
	/** 线程池中线程的创建工厂 */
	private static final ThreadFactory threadFactory = new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, "UploadManager");
			thread.setPriority(Thread.NORM_PRIORITY);
			return thread;
		}
	};
	
	/** 线程池 */
	private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2, threadFactory);
	
	/** 要上传到Blob中的数据的队列 */
	private static final LinkedBlockingQueue<UploadData> uploadToBlobBQ = new LinkedBlockingQueue<UploadData>();
	
	/** 要上传到Queue中的数据的队列 */
	private static final LinkedBlockingQueue<KV> uploadToQueueBQ = new LinkedBlockingQueue<KV>();
	
	private static final ConcurrentHashMap<UploadData, UploadingCallback> callbacks = new ConcurrentHashMap<UploadData, UploadingCallback>();
	
	private CloudBlobClient blobClient;
	private CloudQueueClient queueClient;
	
	private String accountName;
	
	public UploadBinder() {
		EXECUTOR.execute(new Runnable() {
			
			@Override
			public void run() {
				Environment environment = Environment.getInstance();
				
				while (true) {
					try {
						//如果是WIFI状态才进行上传
						if (environment.isNetworkAvailable() && environment.isWifi()) {
							//如果队列里面没有数据，就阻塞在这里
							UploadData uploadData = uploadToBlobBQ.take();
							
							//等到有数据要上传的时候才初始化
							//如果没有创建好，就创建
							if (blobClient == null || queueClient == null) {
								init();
							}
							
							//文件不存在，表明是一个无效的上传数据
							File file = uploadData.getFile();
							if (file == null || !file.exists()) {
								continue;
							}
							
							DebugLog.d(TAG, "uploadData = " + uploadData);
							
							boolean isSuccess = false;
							int count = 0;
							
							String blobName = file.getName();
							String containerName = uploadData.getContainerName();
							
							CloudBlobContainer container = AzureStorage.getContainer(blobClient, containerName);
							CloudQueue queue = AzureStorage.getQueue(queueClient, uploadData.getQueueName());
							
							//对应take操作
							UploadingCallback callback = callbacks.remove(uploadData);
							
							//Step1：上传文件，上传文件失败后重试，重试次数不超过5次
							while (!(isSuccess = AzureStorage.uploadFile(container, blobName, file.getAbsolutePath()))) {
								if (count++ > 5) {
									break;
								}
							}
							
							//Step2：上传元数据，如果文件上传成功了，就上传元数据
							//这里没有使用Blob的元数据功能，是因为Blob的元数据只支持ASCII字符，不支持汉字
							if (isSuccess) {
								while (!(isSuccess = AzureStorage.uploadText(container, blobName + ".metadata", KV.toJson(uploadData.getMetadata()).toString()))) {
									if (count++ > 5) {
										break;
									}
								}
							} else {
								try {
									callback.onFail(uploadData, 0);
								} catch (RemoteException e) {
									DebugLog.e(TAG, "", e);
								}
								
								//已经失败了，就直接跳过下面的步骤
								continue;
							}
							
							//Step3：发送消息到队列，如果元数据上传成功了，就上传消息到队列
							if (isSuccess) {
								String message = "{'container':'" + containerName + "','blob':'" + blobName + "'}";
								
								//发送消息失败后重试，重试次数不超过5次
								while (!(isSuccess = AzureStorage.sendMessage(queue, message))) {
									if (count++ > 5) {
										break;
									}
								}
							} else {
								try {
									callback.onFail(uploadData, 0);
								} catch (RemoteException e) {
									DebugLog.e(TAG, "", e);
								}
								
								//已经失败了，就直接跳过下面的步骤
								continue;
							}
							
							//如果消息上传成功了，将文件后缀名修改
							if (isSuccess) {
								//通知已经成功了
								if (callback != null) {
									uploadData.setAccoutName(accountName);
									
									try {
										callback.onSuccess(uploadData);
									} catch (RemoteException e) {
										DebugLog.e(TAG, "", e);
									}
								}
							} else {
								// 最后一步失败后加入队列中重新上传
								try {
									addUploadData(uploadData);
								} catch (RemoteException e) {
									DebugLog.e(TAG, "", e);
								}
								
								try {
									callback.onFail(uploadData, 0);
								} catch (RemoteException e) {
									DebugLog.e(TAG, "", e);
								}
							}
						} else {
							try {
								Thread.sleep(SLEEP_TIME);
							} catch (InterruptedException e) {
								DebugLog.e(TAG, "", e);
							}
						}
					} catch (InterruptedException e) {
						DebugLog.e(TAG, "", e);
					}
				}
			}
		});
		
		EXECUTOR.execute(new Runnable() {
			
			@Override
			public void run() {
				Environment environment = Environment.getInstance();
				
				while (true) {
					try {
						//如果是WIFI状态才进行上传
						if (environment.isNetworkAvailable()) {
							//如果队列里面没有数据，就阻塞在这里
							KV kv = uploadToQueueBQ.take();
							
							//等到有数据要上传的时候才初始化
							//如果没有创建好，就创建
							if (queueClient == null) {
								init();
							}
							
							CloudQueue queue = AzureStorage.getQueue(queueClient, kv.getKey());
							int count = 0;
							while (!(AzureStorage.sendMessage(queue, kv.getValue()))) {
								if (count++ > 5) {
									break;
								}
							}
						} else {
							try {
								Thread.sleep(SLEEP_TIME);
							} catch (InterruptedException e) {
								DebugLog.e(TAG, "", e);
							}
						}
					} catch (InterruptedException e) {
						DebugLog.e(TAG, "", e);
					}
				}
			}
		});
	}
	
	/**
	 * 初始化
	 * @return  初始化成功
	 */
	private synchronized boolean init() {
		DebugLog.d(TAG, "init()");
		
		if (blobClient != null && queueClient != null) {
			return true;
		}
		
		//先查看缓存
		Configuration configuration = null;
		String azure = configuration.getAzureConf();
		
		Charset charset = Charset.forName("utf-8");
		String result = new String(SymmetricalEncryption.decrypt(Algorithm.AES, Base64.decode(azure.getBytes(charset)), MyApp.getApp().a().getBytes(charset)));
		String[] result2 = result.split("\\|");
		accountName = result2[0];
		String key = result2[1];
		
		CloudStorageAccount account = AzureStorage.getAccount(accountName, key);
		
		if (account != null) {
			blobClient = account.createCloudBlobClient();
			queueClient = account.createCloudQueueClient();
		}
		
		DebugLog.d(TAG, "blobClient = " + blobClient);
		DebugLog.d(TAG, "queueClient = " + queueClient);
		
		return blobClient != null && queueClient != null;
	}
	
	@Override
	public void addUploadData(UploadData uploadData) throws RemoteException {
		addUploadDataWithCallback(uploadData, null);
	}
	
	@Override
	public void addUploadDataWithCallback(UploadData uploadData, UploadingCallback callback) throws RemoteException {
		DebugLog.d(TAG, "addUploadData() uploadData = " + uploadData);
		
		if (uploadData != null) {
			if (callback != null) {
				callbacks.put(uploadData, callback);
			}
			
			uploadToBlobBQ.add(uploadData);
		}
	}
	
	@Override
	public void uploadTextToQueue(String queueName, String content) throws RemoteException {
		DebugLog.d(TAG, "uploadTextToQueue() queueName = " + queueName + ", content = " + content);
		
		if (!TextUtils.isEmpty(queueName) && !TextUtils.isEmpty(content)) {
			uploadToQueueBQ.add(new KV(queueName, content));
		}
	}
}
