package com.fpliu.newton.business.update;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.ThreadPoolManager;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.download.DownLoadListener;
import com.fpliu.newton.framework.download.DownloaderManager;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.ui.toast.CustomToast;
import com.fpliu.newton.framework.util.LauncherManager;

/**
 * 版本更新
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class UpdateManager {
	
	private static final String TAG = UpdateManager.class.getSimpleName();
	
	/** APK的文件名 */
	private static final String FILENAME = "updata.apk";
	
	private UpdateManager() { }
	
	/**
	 * 检查版本是否有更新
	 */
	private static boolean hasNewVersion(Context context, int versionCodeOnServer) {
		// 如果服务器上配置的版本号比本地的版本号大，说明有更新，提示更新
		return versionCodeOnServer > Environment.getInstance().getMyVersionCode();
	}
	
	public static void showUpdateImmediate(final Context context, int versionCodeOnServer) {
		if (context instanceof Activity
				&& !((Activity) context).isFinishing()) {
			//如果有新版本，显示版本更新的弹出框
			if (hasNewVersion(context, versionCodeOnServer)) {
				showUpdateDialog(context, versionCodeOnServer, true);
			} else {
				CustomToast.makeText(context, R.string.updata_newest_version, 2000).show();
			}
		}
	}
	
	/**
	 * 强制更新
	 * @param context
	 * @param notification
	 */
	public static void forceUpdate(final Context context) {
		HttpClientRequest.get(UrlConfig.UPDATE_VERSION, null, UpdateResult.class, new RequestCallback<UpdateResult>() {
			
			@Override
			public void callback(final UpdateResult result, RequestStatus status) {
				if (context instanceof Activity) {
					Activity activity = (Activity) context;
					if (!activity.isFinishing()) {
						//如果有新版本，显示版本更新的弹出框
						if (hasNewVersion(context, result.getVersionCodeOnServer())) {
							activity.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									showUpdateDialog(context, result.getVersionCodeOnServer(), false);
								}
							});
						}
					}
				}
			}
		});
	}

	/**
	 * 显示版本更新的弹出框
	 * @param context      上下文
	 * @param notification 通知
	 */
	private static void showUpdateDialog(final Context context, final int versionCodeOnServer, boolean cancelable) {
		final UpdateDialog updateDialog = new UpdateDialog((Activity)context);
		updateDialog.setMessage(MyApp.getApp().getResources().getString(R.string.updata_hava_new_version));
		updateDialog.setCancelable(cancelable);
		updateDialog.setCanceledOnTouchOutside(cancelable);
		
		updateDialog.setUpdateBtnClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//获取APK文件
				File updateFile = getUpdateAPKFile();
				if (updateFile.exists()) {
					int apkVersionCode = getAPKFileVersionCode(context, updateFile);
					//如果本地缓存的apk的版本与服务器上配置的版本号相同，就不用下载了，直接安装
					if (apkVersionCode == versionCodeOnServer) {
						updateDialog.dismiss();
						LauncherManager.installAPK(context, updateFile);
						return;
					}
				}
				downloadApk(context, updateFile, updateDialog);
			}
		});
		updateDialog.show(Gravity.CENTER, 0, 0);
	}
	
	/**
	 * 下载服务器上的APK文件
	 * @param context      上下文
	 * @param notification 通知
	 */
	private static void downloadApk(final Context context, final File desFile, final UpdateDialog dialog) {
		ThreadPoolManager.EXECUTOR.execute(new Runnable() {

			@Override
			public void run() {
				DownloaderManager.download(UrlConfig.UPDATE_APK, desFile.getAbsolutePath(), new DownLoadListener() {
					
					@Override
					public void onProgress(final long currentPos, final long total) {
						
						if (dialog.isShowing()) {
							dialog.setProgress((int)(currentPos * 100 / total));
							
							if (currentPos == total) {
								LauncherManager.installAPK(context, desFile);
								
								//在主线程中关闭Dialog
								new Handler(Looper.getMainLooper()).post(new Runnable() {
									
									@Override
									public void run() {
										if (dialog.isShowing()) {
											dialog.dismiss();
										}
									}
								});
							}
						}
					}
					
					@Override
					public void onError(int errorCode) {
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							
							@Override
							public void run() {
								if (dialog.isShowing()) {
									dialog.setError(MyApp.getApp().getResources().getString(R.string.updata_download_error));
								}
							}
						});
					}
				});
			}
		});
	}
	
	/**
	 * 获取更新的APK文件
	 * 
	 * @return   APK文件
	 */
	private static File getUpdateAPKFile() {
		File updataDir = new File(Environment.getInstance().getSDPath() + "/updata/");
		if (!updataDir.exists()) {
			updataDir.mkdirs();
		}
		return new File(updataDir, FILENAME);
	}
	
	/**
	 * 获取APK文件中的版本号
	 * 
	 * @param context 上下文
	 * @param apkFile APK文件
	 * @return        版本号
	 */
	private static int getAPKFileVersionCode(Context context, File apkFile) {
		//APK文件可能不完整（没有下载完全），所以解析可能会出错
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo packageInfo = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), 0);
			
			return packageInfo.versionCode;
		} catch (Exception e) {
			DebugLog.e(TAG, "", e);
		}
		return 0;
	}
}
