package com.fpliu.newton.framework.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.config.Configuration;
import com.fpliu.newton.framework.net.HTTPUtil;

import android.os.RemoteException;
import android.text.TextUtils;

/**
 * 下载服务
 * 
 * @author 792793182@qq.com 2014-12-28
 * 
 */
final class DownloadBinder extends IDownload.Stub {

	private static final String TAG = DownloaderManager.class.getSimpleName();
	
	/** 最小的块大小，单位Byte，默认是1M */
	private static final long MIN_BLOCK_SIZE = 1024 * 1024;
	
	private Map<String, DownloadInfo> downloadInfoMap;
	
	DownloadBinder() {
		downloadInfoMap = new HashMap<String, DownloadInfo>();
	}
	
	@Override
	public void download(String url, String filePath) throws RemoteException {
		download(url, filePath, null);
	}

	@Override
	public void reDownload(String url, String filePath) throws RemoteException {
		download(url, filePath, null);
	}

	@Override
	public void clear(String url) throws RemoteException {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		downloadInfoMap.remove(url);
	}

	@Override
	public void clearAll() throws RemoteException {
		downloadInfoMap.clear();
	}

	@Override
	public List<DownloadInfo> getDownloadList() throws RemoteException {
		if (downloadInfoMap.isEmpty()) {
			return null;
		}
		
		List<DownloadInfo> downloadInfos = new ArrayList<DownloadInfo>();
		
		for (String url : downloadInfoMap.keySet()) {
			downloadInfos.add(downloadInfoMap.get(url));
		}
		
		return downloadInfos;
	}

	@Override
	public DownloadInfo getDownloadInfo(String url) throws RemoteException {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		return downloadInfoMap.get(url);
	}
	
	/**
	 * 下载
	 * @param url           要下载资源的URL
	 * @param filePath      下载后的文件位置
	 * @param listener      下载过程中的回调
	 */
	public static void download(String url, String filePath, DownLoadListener listener) {
		DebugLog.d(TAG, "download() url = " + url + " filePath = " + filePath);
		
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(filePath)) {
			if (listener != null) {
				listener.onError(-1);
			}
			return;
		}
		
		//获取资源的一般性信息
		GeneralInfo generalInfo = RequestGeneralInfo.syncRequest(url);
		generalInfo.setFilePath(filePath);
		
		if (generalInfo.getContentLength() == 0L) {
			if (listener != null) {
				listener.onError(-1);
			}
			return;
		}
		
		File desFile = new File(filePath);
		
		//读取缓存的下载信息
		FinalDb db = FinalDb.create(MyApp.getApp(), Configuration.DB_NAME);
		List<DownloadInfo> downloadInfos = db.findAllByWhere(DownloadInfo.class, "originalUrl = '" + url + "'");
		DownloadInfo cachedDownloadInfo = null;
		if (!(downloadInfos == null || downloadInfos.isEmpty())) {
			cachedDownloadInfo = downloadInfos.get(0);
		}
		
		// 如果支持断点续传
		if (generalInfo.isAcceptRanges()) {
			long contentLength = generalInfo.getContentLength();
			
			//虽然支持断点续传，但是由于文件太小，没必要进行多线程下载
			if (contentLength <= MIN_BLOCK_SIZE) {
				syncDownloadWhole(url, filePath, listener);
			} else {
				if (cachedDownloadInfo == null) {
					//没有缓存，就不知道上一次是否下载完整了，所以从头开始下载
					
					multiThreadDownload(generalInfo, db, listener);
				}
				//如果有缓存信息
				else {
					String cachedFilePath = cachedDownloadInfo.getFilePath();
					File cachedFile = new File(cachedFilePath);
					
					//如果缓存文件存在
					if (cachedFile.exists()) {
						//如果缓存的ETag与请求到的ETag相同，表明服务端的文件在这段时间内没有被修改过
						if (generalInfo.geteTag().equals(cachedDownloadInfo.getETag())) {
							if (!cachedFilePath.equals(filePath)) {
								//如果缓存的目的路径与要保存的路径不一样，将文件位置修改一下
								if (cachedFile.renameTo(desFile)) {
									//剪切成功后，将数据库更新一下
									cachedDownloadInfo.setFileName(desFile.getName());
									cachedDownloadInfo.setFilePath(filePath);
									
									//更新数据库
									db.update(cachedDownloadInfo, "originalUrl = '" + url + "'");
									
									downloadFromBreakpoint(cachedDownloadInfo, generalInfo, db, listener);
								} else {
									multiThreadDownload(generalInfo, db, listener);
								}
							} else {
								downloadFromBreakpoint(cachedDownloadInfo, generalInfo, db, listener);
							}
						} else {
							//ETag不相同，重新下载
							if (cachedFile.exists()) {
								cachedFile.delete();
							}
							
							//服务端的文件发生了变化，重新下载
							multiThreadDownload(generalInfo, db, listener);
						}
					} else {
						//缓存文件不存在，重新下载
						multiThreadDownload(generalInfo, db, listener);
					}
				}
			}
		} else {
			//如果不支持断点续传，就直接下载整个文件
			syncDownloadWhole(url, filePath, listener);
		}
	}

	/**
	 * 从头开始下载
	 */
	private static void multiThreadDownload(GeneralInfo generalInfo, final FinalDb db, final DownLoadListener listener) {
		DebugLog.d(TAG, "multiThreadDownload() generalInfo = " + generalInfo);
		
		if (generalInfo == null || db == null) {
			if (listener != null) {
				listener.onError(-1);
			}
			return;
		}
		
		String filePath = generalInfo.getFilePath();
		File desFile = new File(filePath);
		
		File parentDir = desFile.getParentFile();
		if (parentDir == null) {
			if (listener != null) {
				listener.onError(-1);
			}
			return;
		}
		
		//如果文件夹不存在，就创建文件夹
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}
		
		//如果文件存在，就先删除文件
		if (desFile.exists()) {
			desFile.delete();
		}
		
		//组装下载信息
		DownloadInfo downloadInfo = new DownloadInfo(generalInfo);
		downloadInfo.setFileName(desFile.getName());
		downloadInfo.setFilePath(filePath);
		
		String url = generalInfo.getOriginalUrl();
		db.deleteByWhere(DownloadInfo.class, "originalUrl = '" + url + "'");
		//保存到数据库
		db.save(downloadInfo);
		
		//块的数量
		int blockCount = 4;
		
		final long contentLength = generalInfo.getContentLength();
		
		//块的大小
		long blockSize = contentLength / blockCount;
		
		//块的其实位置
		long startPos = 0;
		
		//块的结束位置
		long endPos = 0;
		
		List<BlockInfo> blockInfos = new ArrayList<BlockInfo>();
		downloadInfo.setBlockInfos(blockInfos);
		
		int i = 0;
		while (endPos < contentLength) {
			startPos = i++ * blockSize;
			endPos = startPos + blockSize - 1;
			
			//最后一块的结束位置就是整个文件的结束位置
			if (i == blockCount) {
				endPos = contentLength;
			}
			
			BlockInfo blockInfo = new BlockInfo(startPos, endPos, contentLength);
			blockInfos.add(blockInfo);
			
			//多线程分段下载
			asyncDownloadPart(url, filePath, blockInfo);
		}
		
		if (listener != null) {
			updateProgress(downloadInfo, db, listener);
		}
	}
	
	/**
	 * 从断点处开始下载
	 */
	private static void downloadFromBreakpoint(final DownloadInfo cachedDownloadInfo, GeneralInfo generalInfo, final FinalDb db, final DownLoadListener listener) {
		DebugLog.d(TAG, "downloadFromBreakpoint() generalInfo = " + generalInfo);
		
		if (cachedDownloadInfo == null || generalInfo == null || db == null) {
			return;
		}
		
		//getBlocks
		if (generalInfo.getOriginalUrl().equals(cachedDownloadInfo.getOriginalUrl())
				&& generalInfo.getRedirectUrl().equals(cachedDownloadInfo.getRedirectUrl())
				&& generalInfo.getMimeType().equals(cachedDownloadInfo.getMimeType())
				&& generalInfo.geteTag().equals(cachedDownloadInfo.getETag())
				&& generalInfo.getFilePath().equals(cachedDownloadInfo.getFilePath())
				&& generalInfo.getContentLength() == cachedDownloadInfo.getContentLength()) {
			
			List<BlockInfo> blockInfos = new ArrayList<BlockInfo>();
			cachedDownloadInfo.setBlockInfos(blockInfos);
			
			String blocks = cachedDownloadInfo.getBlocks();
			try {
				JSONArray jsonArray = new JSONArray(blocks);
				for (int i = 0; i < jsonArray.length(); i++) {
					BlockInfo blockInfo = BlockInfo.parse(jsonArray.getJSONObject(i));
					blockInfos.add(blockInfo);
					
					long startPos = blockInfo.getStartPos();
					long endPos = blockInfo.getEndPos();
					long currentPos = blockInfo.getCurrentPos();
					
					//如果没有下载完，就接着下载
					if (startPos < endPos && currentPos < endPos) {
						asyncDownloadPart(generalInfo.getOriginalUrl(), cachedDownloadInfo.getFilePath(), blockInfo);
					}
				}
			} catch (Exception e) {
				DebugLog.e(TAG, "", e);
			}
			
			if (listener != null) {
				updateProgress(cachedDownloadInfo, db, listener);
			}
		} else {
			multiThreadDownload(generalInfo, db, listener);
		}
	}
	
	/**
	 * 更新分段下载的进度
	 * 
	 */
	private static void updateProgress(final DownloadInfo downloadInfo, final FinalDb db, final DownLoadListener listener) {
		//更新进度的线程
		ThreadPoolManager.EXECUTOR.execute(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					JSONArray jsonArray = new JSONArray();
					
					long currentPos = 0L;
					List<BlockInfo> blockInfos = downloadInfo.getBlockInfos();
					for (BlockInfo blockInfo : blockInfos) {
						currentPos += (blockInfo.getCurrentPos() - blockInfo.getStartPos());
						
						jsonArray.put(blockInfo.toJson());
					}
					downloadInfo.setBlocks(jsonArray.toString());
					
					db.update(downloadInfo, "originalUrl = '" + downloadInfo.getOriginalUrl() + "'");
					
					long contentLength = downloadInfo.getContentLength();
					
					DebugLog.d(TAG, "currentPos = " + currentPos + " ,contentLength = " + contentLength);
					
					listener.onProgress(currentPos, contentLength);
					
					//突然无网络了
					if (!Environment.getInstance().isNetworkAvailable()) {
						if (listener != null) {
							listener.onError(-1);
						}
						break;
					}
					//下载完成了，跳出循环
					if (currentPos == contentLength) {
						break;
					}
					
					//500ms刷新一次
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						DebugLog.e(TAG, "", e);
					}
				}
			}
		});
	}
	
	/**
	 * 下载文件的一块，进行分段下载
	 * 
	 */
	private static void asyncDownloadPart(final String url, final String filePath, final BlockInfo blockInfo) {
		DebugLog.d(TAG, "asyncDownloadPart() url = " + url + " filePath = " + filePath + " blockInfo = " + blockInfo);
		
		ThreadPoolManager.EXECUTOR.execute(new Runnable() {
			
			@Override
			public void run() {
				InputStream is = null;
				RandomAccessFile randomAccessFile = null;
				try {
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
					// 使用GET请求，实际上默认就是GET请求
					connection.setRequestMethod("GET");
					//设置连接超时
					connection.setConnectTimeout(5 * 1000);
					//设置读取超时
					connection.setReadTimeout(5 * 1000);
					
					connection.setRequestProperty("Accept", "*/*");
					connection.setRequestProperty("Accept-Language", "zh-CN");
					connection.setRequestProperty("Charset", "UTF-8");
					connection.setRequestProperty("User-Agent", "Android");
					connection.setRequestProperty("Connection", "Keep-Alive");
					
					long currentPos = blockInfo.getCurrentPos();
					long endPos = blockInfo.getEndPos();
					
					// 设置获取实体数据的范围
					connection.setRequestProperty("Range", "bytes=" + currentPos + "-" + endPos);
					
					// 打印HTTP头部信息
					DebugLog.d(TAG, HTTPUtil.getRequestInfo(connection));
					DebugLog.d(TAG, HTTPUtil.getResponseInfo(connection));

					is = connection.getInputStream();
					
					try {
						//用随机访问文件的方式写文件
						randomAccessFile = new RandomAccessFile(filePath, "rwd");
						//设置本地文件的长度
						randomAccessFile.setLength(blockInfo.getContentLength());
						randomAccessFile.seek(currentPos);
					} catch (Exception e) {
						DebugLog.e(TAG, "", e);
					}

					// 连接服务器
					connection.connect();
					
					//分段下载返回的状态码是206
					if (connection.getResponseCode() == 206) {
						byte[] buff = new byte[1024];
						int len = 0;
						while (-1 != (len = is.read(buff))) {
							randomAccessFile.write(buff, 0, len);
							
							blockInfo.setCurrentPos(currentPos = currentPos + len);
						}
					}

					// 断开连接
					connection.disconnect();
					
					blockInfo.setStatus(0);
				} catch (Exception e) {
					DebugLog.e(TAG, "", e);
					blockInfo.setStatus(-1);
				} finally {
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							DebugLog.e(TAG, "", e);
						}
					}

					if (randomAccessFile != null) {
						try {
							randomAccessFile.close();
						} catch (IOException e) {
							DebugLog.e(TAG, "", e);
						}
					}
				}
			}
		});
	}
	
	/**
	 * 下载整体文件，不进行分段下载
	 * 
	 */
	private static void syncDownloadWhole(String url, String filePath, DownLoadListener listener) {
		DebugLog.d(TAG, "syncDownloadWhole() url = " + url + " filePath = " + filePath);
		
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			// 使用GET请求，实际上默认就是GET请求
			connection.setRequestMethod("GET");
			//设置连接超时
			connection.setConnectTimeout(5 * 1000);
			//设置读取超时
			connection.setReadTimeout(5 * 1000);
			
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Accept-Language", "zh-CN");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("User-Agent", "Android");
			connection.setRequestProperty("Connection", "Keep-Alive");
			
			long contentLength = 0L;
			//获取文件的大小
			try {
				contentLength = Long.parseLong(connection.getHeaderField("Content-Length"));
			} catch (Exception e) {
				DebugLog.e(TAG, "get()", e);
			}
			
			// 打印HTTP头部信息
			DebugLog.d(TAG, HTTPUtil.getRequestInfo(connection));
			DebugLog.d(TAG, HTTPUtil.getResponseInfo(connection));

			is = connection.getInputStream();
			fos = new FileOutputStream(filePath);

			// 连接服务器
			connection.connect();

			int responseCode = connection.getResponseCode();
			if (responseCode == 200) {
				long currentPos = 0;
				byte[] buff = new byte[1024];
				int len = 0;
				while (-1 != (len = is.read(buff))) {
					fos.write(buff, 0, len);
					
					if (listener != null) {
						listener.onProgress(currentPos = currentPos + len, contentLength);
					}
				}
			}

			// 断开连接
			connection.disconnect();
		} catch (Exception e) {
			DebugLog.e(TAG, "", e);

			if (listener != null) {
				listener.onError(-1);
			}
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "", e);
				}
			}

			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					DebugLog.e(TAG, "", e);
				}
			}
		}
	}
}
