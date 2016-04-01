package com.fpliu.newton.business;

import java.io.File;

import org.json.JSONObject;

import android.text.TextUtils;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.upload.UploadResult;
import com.fpliu.newton.framework.util.IOUtil;

public final class UploadUncaughtException {

	/**
	 * 上传奔溃日志和网络异常日志
	 */
	public static void uploadUncaughtException() {
		//如果是有网络，并且是wifi，才上传
		Environment environment = Environment.getInstance();
		if (environment.isNetworkAvailable() && environment.isWifi()) {
			String imei = environment.getIMEI();
			
			final File uncaughtExceptionFile = DebugLog.getUncaughtExceptionFile();
			final File httpExceptionFile = DebugLog.getHttpExceptionFile();
			
			boolean uncaughtExceptionFileExist = uncaughtExceptionFile.exists();
			boolean httpExceptionFileExist = httpExceptionFile.exists();
			
			if (uncaughtExceptionFileExist || httpExceptionFileExist) {
				String uncaughtException = "";
				if (uncaughtExceptionFileExist) {
					uncaughtException = IOUtil.readTextFile(uncaughtExceptionFile);
				}
				
				String httpException = "";
				if (httpExceptionFileExist) {
					httpException = IOUtil.readTextFile(httpExceptionFile);
				}
				
				if (TextUtils.isEmpty(uncaughtException) && TextUtils.isEmpty(httpException)) {
					return;
				}
				
				postUncaughtException(imei, uncaughtException + httpException, 
						                             new UploadFinishCallback(uncaughtExceptionFile, httpExceptionFile, uncaughtException, httpException));
			}
		}
	}
	
	private static class UploadFinishCallback implements RequestCallback<UploadResult> {

		private File uncaughtExceptionFile = DebugLog.getUncaughtExceptionFile();
		private File httpExceptionFile = DebugLog.getHttpExceptionFile();
		
		private String uncaughtException = "";
		private String httpException = "";
		
		private UploadFinishCallback(File uncaughtExceptionFile, File httpExceptionFile,
				                     String uncaughtException, String httpException) {
			this.uncaughtExceptionFile = uncaughtExceptionFile;
			this.httpExceptionFile = httpExceptionFile;
			this.uncaughtException = uncaughtException;
			this.httpException = httpException;
		}
		
		@Override
		public void callback(UploadResult result, RequestStatus status) {
			if (result.getFileSize() > 0) {
				File uploadedUncaughtExceptionFile = new File(uncaughtExceptionFile.getAbsoluteFile() + ".uploaded");
				if (uploadedUncaughtExceptionFile.exists()) {
					uncaughtExceptionFile.delete();
					try {
						IOUtil.writeFile(uploadedUncaughtExceptionFile, uncaughtException, true);
					} catch (Exception e) {
						DebugLog.e(getClass().getSimpleName(), "onFinish()", e);
					}
				} else {
					uncaughtExceptionFile.renameTo(uploadedUncaughtExceptionFile);
				}
				
				File uploadedHttpExceptionFile = new File(httpExceptionFile.getAbsoluteFile() + ".uploaded");
				if (uploadedHttpExceptionFile.exists()) {
					httpExceptionFile.delete();
					
					try {
						IOUtil.writeFile(uploadedHttpExceptionFile, httpException, true);
					} catch (Exception e) {
						DebugLog.e(getClass().getSimpleName(), "onFinish()", e);
					}
				} else {
					httpExceptionFile.renameTo(uploadedHttpExceptionFile);
				}
			}
		}
	}
	
	public static void saveHttpException(String requestResult) {
		String content = DebugLog.getBaseInfo().append(requestResult).append("\n").toString();
		DebugLog.syncSaveFile(DebugLog.FILE_HTTP_EXCEPTION_LOG, content);
	}
	
	public static void postUncaughtException(String imei, String exception, UploadFinishCallback callback) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("DeviceNumber", imei);
			jsonObject.put("Error", exception);
		} catch (Exception e) {
			DebugLog.e("", "postUncaughtException()", e);
		}
		
		HttpClientRequest.postJson(UrlConfig.POST_UNCAUGHT_EXCEPTION, null, jsonObject.toString(), UploadResult.class, callback);
	}
}
