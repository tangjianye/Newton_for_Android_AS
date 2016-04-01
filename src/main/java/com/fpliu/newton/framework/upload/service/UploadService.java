package com.fpliu.newton.framework.upload.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 上传服务
 * 
 * @author 792793182@qq.com 2014-12-03
 * 
 */
public class UploadService extends Service {

	private UploadBinder uploadBinder;
	
	@Override
	public IBinder onBind(Intent intent) {
		if (uploadBinder == null) {
			uploadBinder = new UploadBinder();
		}
		return uploadBinder;
	}
}
