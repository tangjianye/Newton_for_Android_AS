package com.fpliu.newton.framework.storage;

import java.net.URI;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.annotation.SuppressLint;

import com.fpliu.newton.base.DebugLog;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageCredentials;
import com.microsoft.azure.storage.StorageCredentialsAccountAndKey;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

/**
 * Windows Azure Storage管理类
 * 
 * @author 792793182@qq.com 2015-01-29
 * 
 */
public final class AzureStorage {

	private static final String TAG = AzureStorage.class.getSimpleName();

	private AzureStorage() { }
	
	
	/**
	 * 获取存储账户
	 * @param accountName  账户名
	 * @param key          访问key
	 * @return
	 */
	@SuppressLint("TrulyRandom")
	public static CloudStorageAccount getAccount(String accountName, String key) {
		StorageCredentials credentials = null;
		
		/**
		 * java.lang.ArrayIndexOutOfBoundsException: length=128; index=-3
	at com.microsoft.azure.storage.core.Base64.decode(Base64.java:78)
	at com.microsoft.azure.storage.StorageCredentialsAccountAndKey.<init>(StorageCredentialsAccountAndKey.java:55)
	at com.fpliu.newton.framework.storage.AzureStorage.getAccount(AzureStorage.java:49)
	at com.fpliu.newton.framework.upload.service.UploadBinder.init(UploadBinder.java:197)
	at com.fpliu.newton.framework.upload.service.UploadBinder.access$2(UploadBinder.java:182)
	at com.fpliu.newton.framework.upload.service.UploadBinder$3.run(UploadBinder.java:154)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1080)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:573)
	at java.lang.Thread.run(Thread.java:841)
		 */
		try {
			credentials = new StorageCredentialsAccountAndKey(accountName, key);
		} catch (Exception e) {
			DebugLog.e(TAG, "getAccount()", e);
			return null;
		}

		String pattern = "https://" + accountName + ".{0}.core.chinacloudapi.cn";

		URI blobEndpoint = URI.create(MessageFormat.format(pattern, "blob"));
		URI queueEndpoint = URI.create(MessageFormat.format(pattern, "queue"));
		URI tableEndpoint = URI.create(MessageFormat.format(pattern, "table"));

		CloudStorageAccount account = new CloudStorageAccount(credentials, blobEndpoint, queueEndpoint, tableEndpoint);

		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			DebugLog.e(TAG, "getAccount()", e);
		}

		return account;
	}

	/**
	 * 获取容器
	 * @param blobClient    Blob存储管理类  
	 * @param containerName 容器名
	 */
	public static CloudBlobContainer getContainer(CloudBlobClient blobClient, String containerName) {
		try {
			//获取容器，如果容器不存在，则创建容器
			CloudBlobContainer container = blobClient.getContainerReference(containerName);
			container.createIfNotExists();

			BlobContainerPermissions containerPermissions = new BlobContainerPermissions();
			containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
			container.uploadPermissions(containerPermissions);
			
			return container;
		} catch (Exception e) {
			DebugLog.e(TAG, "getContainer()", e);
			return null;
		}
	}
	
	/**
	 * 上传文件
	 * @param container   容器
	 * @param blobName    上传到容器中的Blob名
	 * @param filePath    本地的文件按路径
	 * @return            是否上传成功
	 */
	public static boolean uploadFile(CloudBlobContainer container, String blobName, String filePath) {
		DebugLog.d(TAG, "uploadFile() blobName = " + blobName + " , filePath = " + filePath);
		
		try {
			CloudBlockBlob blob = container.getBlockBlobReference(blobName);
			blob.uploadFromFile(filePath);
			return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "uploadFile()", e);
			return false;
		}
	}
	
	/**
	 * 上传文件
	 * @param container   容器
	 * @param blobName    上传到容器中的Blob名
	 * @param text        文本内容
	 * @return            是否上传成功
	 */
	public static boolean uploadText(CloudBlobContainer container, String blobName, String text) {
		DebugLog.d(TAG, "uploadText() blobName = " + blobName + " , text = " + text);
		
		try {
			CloudBlockBlob blob = container.getBlockBlobReference(blobName);
			blob.uploadText(text);
			return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "uploadText()", e);
			return false;
		}
	}
	
	/**
	 * 获取队列
	 * @param queueClient  队列管理类
	 * @param queueName    队列名
	 * @return
	 */
	public static CloudQueue getQueue(CloudQueueClient queueClient, String queueName) {
		DebugLog.d(TAG, "getQueue() queueName = " + queueName);
		
		try {
			//获取队列，如果队列不存在，则创建队列
			CloudQueue queue = queueClient.getQueueReference(queueName);
			queue.createIfNotExists();
			
			return queue;
		} catch (Exception e) {
			DebugLog.e(TAG, "getQueue()", e);
			return null;
		}
	}
	
	/**
	 * 发送消息
	 * @param queue    队列
	 * @param message  消息内容
	 * @return         是否发送成功
	 */
	public static boolean sendMessage(CloudQueue queue, String message) {
		DebugLog.d(TAG, "sendMessage() queue = " + queue + " , message = " + message);
		
		try {
			CloudQueueMessage queueMessage = new CloudQueueMessage(message);
			queue.addMessage(queueMessage);
			
			return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "sendMessage()", e);
			return false;
		}
	}
}
