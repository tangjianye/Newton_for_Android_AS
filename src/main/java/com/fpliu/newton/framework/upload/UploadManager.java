package com.fpliu.newton.framework.upload;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.upload.service.IUploadManager;


/**
 * 上传数据管理
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class UploadManager {
	
	private static final String TAG = UploadManager.class.getSimpleName();
	
	private static final String ACTION = "com.datatang.clinet.action.UPLOAD_DATA";
	
	private ServiceConnection mConnection;
	
	private IUploadManager uploadService;
	
	private static class InstanceHolder {
		private static UploadManager instance = new UploadManager();
	}
	
	private UploadManager() { }
	
	public static UploadManager getInstance() {
		return InstanceHolder.instance;
	}
	
	public void init() {
		Context applicationContext = MyApp.getApp();
		
		Intent intent = new Intent();
		intent.setAction(ACTION);
		
		// 如果有默认包，设置默认包名
		String pkgName = applicationContext.getPackageName();
		if (!TextUtils.isEmpty(pkgName)) {
			intent.setPackage(pkgName);
		}
		
		mConnection = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				uploadService = IUploadManager.Stub.asInterface(service);
				DebugLog.d(TAG, "uploadService = " + uploadService);
			}
		};
		
		//使用ApplicationContext在Activity退出时候就不用解绑定了
		if (applicationContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)) {
			DebugLog.d(TAG, "service is success");
		}
	}
	
	public void addData(UploadData uploadData) {
		if (uploadData == null) {
			return;
		} else {
			File uploadFile = uploadData.getFile();
			if (uploadFile == null || !uploadFile.exists()) {
				return;
			}
		}
		
		try {
			uploadService.addUploadData(uploadData);
		} catch (RemoteException e) {
			DebugLog.e(TAG, "addData()", e);
		}
	}
	
	public void addData(UploadData uploadData, final UploadingCallback callback) {
		if (uploadData == null) {
			return;
		} else {
			File uploadFile = uploadData.getFile();
			if (uploadFile == null || !uploadFile.exists()) {
				return;
			}
		}
		
		if (callback == null) {
			addData(uploadData);
		} else {
			try {
				uploadService.addUploadDataWithCallback(uploadData, new com.fpliu.newton.framework.upload.service.UploadingCallback.Stub() {
					
					@Override
					public void onSuccess(UploadData uploadData) throws RemoteException {
						callback.onSuccess(uploadData);
					}
					
					@Override
					public void onFail(UploadData uploadData, int errorCode) throws RemoteException {
						callback.onFail(uploadData, errorCode);
					}
				});
			} catch (RemoteException e) {
				DebugLog.e(TAG, "addData()", e);
			}
		}
	}
	
	public void uploadTextToQueue(String queueName, String content) {
		try {
			uploadService.uploadTextToQueue(queueName, content);
		} catch (RemoteException e) {
			DebugLog.e(TAG, "addData()", e);
		}
	}
}
