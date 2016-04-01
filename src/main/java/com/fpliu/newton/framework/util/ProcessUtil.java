package com.fpliu.newton.framework.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

/**
 * 进程相关的帮助类
 * 
 * @author 792793182@qq.com 2015-03-19
 *
 */
public final class ProcessUtil {

	private ProcessUtil() { }
	
	/**
	 * 获取当前进程的进程名
	 * @param context  上下文
	 * @return         进程名
	 */
	public static String getCurrentProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningAppProcessInfo processInfo : activityManager.getRunningAppProcesses()) {
			if (processInfo.pid == pid) {
				return processInfo.processName;
			}
		}
		return "";
	}
}
