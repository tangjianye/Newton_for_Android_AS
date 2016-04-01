package com.fpliu.newton.framework.util;

import com.fpliu.newton.base.DebugLog;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.PowerManager;

/**
 * 屏幕锁屏的控制类
 * 
 * @author 792793182@qq.com 2014-12-04
 *
 */
public class AlertWakeLock {

	private static final String TAG = AlertWakeLock.class.getSimpleName();

	private static PowerManager.WakeLock sWakeLock;

	/**
	 * 激活屏幕，不锁屏。 每次唤醒屏幕前都release。
	 * 
	 * @param context
	 */
	public static void acquire(Context context) {
		DebugLog.v(TAG, "Acquiring wake lock");
		if (sWakeLock != null) {
			sWakeLock.release();
		}

		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);

		sWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, TAG);

		sWakeLock.acquire();

		// 解锁
		KeyguardManager keyguardManager = (KeyguardManager) context
				.getSystemService(Context.KEYGUARD_SERVICE);
		KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
		keyguardLock.disableKeyguard();
	}

	/**
	 * 释放屏幕锁
	 */
	public static void release() {
		DebugLog.v(TAG, "Releasing wake lock");
		if (sWakeLock != null) {
			sWakeLock.release();
			sWakeLock = null;
		}
	}
}
