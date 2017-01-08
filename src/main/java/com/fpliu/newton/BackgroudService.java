package com.fpliu.newton;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.fpliu.newton.business.home.Home;

/**
 * 应用程序后台进程，常驻进程
 * 进程名与包名相同
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class BackgroudService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//成为前台进程
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_logo;
		notification.tickerText = getText(R.string.app_name);
		notification.when = System.currentTimeMillis();
		
		Intent notificationIntent = new Intent(this, Home.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//		notification.setLatestEventInfo(this, getText(R.string.app_name),
//		        "正在运行...", pendingIntent);
		startForeground(1000, notification);
		
		return START_REDELIVER_INTENT;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		stopForeground(false);
	}
}
