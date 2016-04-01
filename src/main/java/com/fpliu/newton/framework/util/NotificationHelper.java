package com.fpliu.newton.framework.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.home.Home;

public class NotificationHelper {

	private static final String TAG = "NotificationHelper";
	
	// 呼吸灯：亮灯时间
	private static final int LED_LIGHT_ON_TIME = 300;
	// 呼吸灯：灭灯时间
	private static final int LED_LIGHT_OFF_TIME = 3000;

	private static Object mLock = new Object();

	/**
	 * 通知栏显示
	 */
	public static void showNotification(String title,
			String content, int id, boolean isOngoing, String action) {
		Context context = MyApp.getApp();
		
		synchronized (mLock) {
			DebugLog.d(TAG, "showNotification() | title=" + title + ", content=" + content);
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Service.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);

			Notification notify = new Notification();
			notify.ledARGB = Color.GREEN;
			notify.ledOnMS = LED_LIGHT_ON_TIME;
			notify.ledOffMS = LED_LIGHT_OFF_TIME;
			notify.flags |= Notification.FLAG_SHOW_LIGHTS;
			if (isOngoing) {
				notify.flags |= Notification.FLAG_ONGOING_EVENT;
			} else {
				notify.flags |= Notification.FLAG_AUTO_CANCEL;
			}
			
			// 通知栏顶部
			notify.icon = R.drawable.ic_logo;
			notify.tickerText = title;
			// 不显示时间
			notify.when = 0;

			Intent notificationIntent = new Intent(context, Home.class);
			notificationIntent.setAction(action);
			notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			notificationIntent.putExtra("isFromNotification", true);

			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					0, notificationIntent, 0);
			notify.setLatestEventInfo(context, title, content, pendingIntent);

			if (notify.contentView != null) {
				try {
					notify.contentView.setImageViewResource(android.R.id.icon,
							R.drawable.ic_logo);
				} catch (Exception e) {
					DebugLog.e(TAG, "showNotification()", e);
				} catch (Error e) {
					DebugLog.e(TAG, "showNotification()", e);
				}
			}
			notificationManager.notify(id, notify);
		}
	}
}
