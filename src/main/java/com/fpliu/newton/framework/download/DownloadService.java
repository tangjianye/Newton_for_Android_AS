package com.fpliu.newton.framework.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 下载服务
 * 
 * @author 792793182@qq.com 2014-12-28
 * 
 */
public class DownloadService extends Service {

	private DownloadBinder downloadBinder;
	
	@Override
	public synchronized IBinder onBind(Intent intent) {
		if (downloadBinder == null) {
			downloadBinder = new DownloadBinder();
		}
		return downloadBinder;
	}

}
