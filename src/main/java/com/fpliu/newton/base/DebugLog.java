package com.fpliu.newton.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.fpliu.newton.R;
import com.fpliu.newton.business.config.Configuration;
import com.fpliu.newton.framework.ui.toast.CustomToast;

/**
 * 调试日志
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class DebugLog {

	/** 调试日志的开关，一般Debug版本中打开，便于开发人员观察日志，Release版本中关闭 */
	public static final boolean ENABLED = true;

	/** 崩溃日志 */
	private static final String FILE_UNCAUGHT_EXCEPITON_LOG = "UncaughtException.log";

	/** 网络异常日志 */
	public static final String FILE_HTTP_EXCEPTION_LOG = "HttpException.log";

	// private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; //日志最大5M
	
	/** TAG的前缀，便于过滤 */
	private static final String PREFIX = "Newton_";

	private DebugLog() {}
	
	
	public static int v(String tag, String msg) {
		return ENABLED ? Log.v(PREFIX + tag, msg) : 0;
	}

	public static int v(String tag, String msg, Throwable throwable) {
		return ENABLED ? Log.v(PREFIX + tag, msg, throwable) : 0;
	}

	public static int d(String tag, String msg) {
		return ENABLED ? Log.d(PREFIX + tag, msg) : 0;
	}

	public static int d(String tag, String msg, Throwable throwable) {
		return ENABLED ? Log.d(PREFIX + tag, msg, throwable) : 0;
	}

	public static int i(String tag, String msg) {
		return ENABLED ? Log.i(PREFIX + tag, msg) : 0;
	}

	public static int i(String tag, String msg, Throwable tr) {
		return ENABLED ? Log.i(PREFIX + tag, msg, tr) : 0;
	}

	public static int w(String tag, String msg) {
		return ENABLED ? Log.w(PREFIX + tag, msg) : 0;
	}

	public static int w(String tag, String msg, Throwable tr) {
		return ENABLED ? Log.w(PREFIX + tag, msg, tr) : 0;
	}

	public static int w(String tag, Throwable tr) {
		return ENABLED ? Log.w(PREFIX + tag, tr) : 0;
	}

	public static int e(String tag, String msg) {
		return ENABLED ? Log.e(PREFIX + tag, msg) : 0;
	}

	public static int e(String tag, String msg, Throwable tr) {
		return ENABLED ? Log.e(PREFIX + tag, msg, tr) : 0;
	}

	/**
	 * 保存异常堆栈信息
	 * 
	 * @param e
	 * @return
	 */
	public static String getExceptionTrace(Throwable e) {
		if (e != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			e.printStackTrace(printWriter);
			return stringWriter.toString();
		}
		return null;
	}

	/**
	 * 同步保存异常堆栈信息到文件
	 */
	public static void syncSaveFile(String fileName, String content) {
		if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(content)) {
			return;
		}
		
		File file = new File(getDebugLogDir(), fileName);

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file, true);
			fos.write(content.getBytes("utf-8"));
			fos.write('\n');
			fos.flush();
		} catch (Exception ex) {
			e("", "", ex);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ex) {
					e("", "", ex);
				}
			}
		}
	}

	/**
	 * 组装基本信息
	 */
	public static StringBuilder getBaseInfo() {
		Environment environment = Environment.getInstance();
		
		StringBuilder info = new StringBuilder();
		info.append("time = ").append(getFormatDateTime()).append("\n");
		info.append("versionName = ").append(environment.getMyVersionName()).append("\n");
		info.append("channelId = ").append(Configuration.CHANNEL_ID).append("\n");
		info.append(environment.getAllInfo().toString()).append("\n");
		
		return info;
	}
	
	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	private static String getFormatDateTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINESE)
				.format(System.currentTimeMillis());
	}

	/**
	 * 获取保存日志的路径
	 */
	private static File getDebugLogDir() {
		Environment environment = Environment.getInstance();
		File debugLogDir = new File(environment.getMyDir()+ "/log");
		if (!debugLogDir.exists()) {
			debugLogDir.mkdirs();
		}
		return debugLogDir;
	}
	
	/**
	 * 获取崩溃日志文件
	 */
	public static File getUncaughtExceptionFile() {
		return new File(getDebugLogDir(), FILE_UNCAUGHT_EXCEPITON_LOG);
	}

	/**
	 * 获取HTTP异常日志文件
	 */
	public static File getHttpExceptionFile() {
		return new File(getDebugLogDir(), FILE_HTTP_EXCEPTION_LOG);
	}
	
	
	
	/**
	 * 注册未捕获异常处理器
	 */
	public static void registerUncaughtExceptionHandler(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(
				context));
	}

	/**
	 * 未捕获异常处理器
	 * 
	 */
	private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
		
		private Context mContext;

		public MyUncaughtExceptionHandler(Context context) {
			mContext = context;
		}

		@Override
		public void uncaughtException(Thread thread, final Throwable ex) {
			// 使用 Toast 来显示异常信息
			new Thread() {

				@Override
				public void run() {
					Looper.prepare();
					CustomToast.makeText(mContext, R.string.warn_program_exception, 2000).show();
					Looper.loop();
				}
			}.start();
			ThreadPoolManager.EXECUTOR.execute(new Runnable() {

				@Override
				public void run() {
					SystemClock.sleep(2000);
					
					String content = getBaseInfo().append(getExceptionTrace(ex)).toString();
					// 保存堆栈信息
					syncSaveFile(FILE_UNCAUGHT_EXCEPITON_LOG, content);

					// 杀死进程
					Process.killProcess(Process.myPid());
				}
			});
		}
	}
}
