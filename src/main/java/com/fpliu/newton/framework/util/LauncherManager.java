package com.fpliu.newton.framework.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;

import com.fpliu.newton.base.DebugLog;

/**
 * 启动界面的工具类
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class LauncherManager {

	private static final String TAG = LauncherManager.class.getSimpleName();

	private LauncherManager() { }

	/**
	 * 打开网络设置
	 * 
	 * @param context
	 *            上下文
	 */
	public static void startNetSetting(Context context) {
		if (context == null) {
			return;
		}

		Intent intent = null;
		if (Build.VERSION.SDK_INT < 14) {
			intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		} else {
			intent = new Intent(Settings.ACTION_SETTINGS);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);

		try {
			context.startActivity(intent);
		} catch (Exception e) {
			DebugLog.e(TAG, "startNetSetting()", e);
		}
	}
	
	/**
	 * 打开位置设置
	 * 
	 * @param context
	 *            上下文
	 */
	public static void startLocationSetting(Context context) {
		if (context == null) {
			return;
		}

		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		try {
			context.startActivity(intent);
		} catch (Exception e) {
			DebugLog.e(TAG, "startLocationSetting()", e);
		}
	}

	/**
	 * 打开浏览器
	 * @param context  上下文
	 * @param url      网址
	 * @return
	 */
	public static boolean startBrowser(Context context, String url) {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(url));
    	try {
        	context.startActivity(intent);
        	return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "startBrowser()", e);
		}
    	return false;
	}
	
	/**
	 * 打开系统联系人应用
	 * 
	 * @param context
	 *            上下文
	 */
	public static void startContactsApp(Context context) {
		try {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("content://contacts/people/"));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(intent);
		} catch (Exception e) {
			DebugLog.e(TAG, "startContactsApp()", e);
		}
	}
	
	/**
	 * 打开相机，照完相片后没有提示保存和预览功能
	 * 
	 * @param context
	 *            上下文
	 */
	public static void startCamera(Activity activity, int requestCode) {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			DebugLog.e(TAG, "startCamera()", e);
		}
	}
	
	/**
	 * 打开相机，照完相片后有提示保存和预览功能
	 * 
	 * @param context
	 *            上下文
	 */
	public static void startCamera(Activity activity, int requestCode, Uri uri) {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			if (uri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			}
			
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			DebugLog.e(TAG, "startCamera()", e);
		}
	}
	
	
	/**
	 * 打开摄像机开始录像
	 * 
	 * 
	 */
	public static void startCameraForVideo(Activity activity,int requestCode,Uri uri){
		try {
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			if (uri != null) {
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				
			}
			
			activity.startActivityForResult(intent, requestCode);
		} catch (Exception e) {
			DebugLog.e(TAG, "startCameraForVideo()", e);
		}
	}
	
	/**
     * 安装应用程序
     * 
     * @param context     上下文
     * @param apkFilePath apk路径
     */
    public static boolean installAPK(Context context, File apkFile) {
        if (apkFile != null && apkFile.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            try {
            	context.startActivity(intent);
            	return true;
			} catch (Exception e) {
				DebugLog.e(TAG, "installAPK()", e);
			}
        }
        return false;
    }
    
    /**
     * 打开发送短信界面
     * @param context  山下文
     * @param message  短信内容
     * @return
     */
    public static boolean sendMessage(Context context, String message) {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.putExtra("sms_body", message);
    	intent.setType("vnd.android-dir/mms-sms");
    	try {
        	context.startActivity(intent);
        	return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "sendMessage()", e);
		}
    	return false;
	}
    
    /**
     * 打开发送邮件界面
     * @param context   山下文
     * @param title     主题
     * @param message   内容
     * @return
     */
    public static boolean sendEmail(Context context, String title, String message) {
    	Intent intent = new Intent(android.content.Intent.ACTION_SENDTO, Uri.parse("mailto:792793182@qq.com"));
    	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
    	intent.putExtra(android.content.Intent.EXTRA_TEXT, message);
        try {
        	context.startActivity(intent);
        	return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "sendEmail()", e);
		}
    	return false;
	}
    
    /**
     * 系统的分享接口
     * @param context  山下文
     * @param message  分享的内容
     * @return
     */
    public static boolean shareMore(Context context, String message, String imageFilePath) {
    	Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("image/png");
    	// 添加图片
    	intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imageFilePath)));
    	intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
    	intent.putExtra(Intent.EXTRA_TEXT, message);
    	
    	try {
    		context.startActivity(Intent.createChooser(intent, "分享"));
        	return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "shareMore()", e);
		}
    	return false;
	}
    
    /**
     * 进入拨号界面
     * @param context     上下文
     * @param phoneNumber 电话号码
     * @return
     */
    public static boolean dial(Context context, String phoneNumber) {
    	try {
    		Uri uri = Uri.parse("tel:" + phoneNumber);   
    		Intent intent = new Intent(Intent.ACTION_DIAL, uri);     
    		context.startActivity(intent);
    		return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "dial()", e);
		}
    	return false;
    }
    
    /**
     * 直接拨打电话
     * @param context     上下文
     * @param phoneNumber 电话号码
     * @return
     */
    public static boolean call(Context context, String phoneNumber) {
    	try {
    		Uri uri = Uri.parse("tel:" + phoneNumber);   
    		Intent intent = new Intent(Intent.ACTION_CALL, uri);     
    		context.startActivity(intent);
    		return true;
		} catch (Exception e) {
			DebugLog.e(TAG, "dial()", e);
		}
    	return false;
    }
    
    /**
     * 打开图片选择界面
     * @param activity
     * @param requestCode
     */
    public static void openImagePick(Activity activity, int requestCode) {
    	try {
    		Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
    		openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    		activity.startActivityForResult(openAlbumIntent, requestCode);
		} catch (Exception e) {
			DebugLog.e(TAG, "openImagePick()", e);
		}
	}
}
