package com.fpliu.newton.framework;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.ThreadPoolManager;
import com.fpliu.newton.base.clipboard.ClipboardManager;
import com.fpliu.newton.framework.qr.QRCode;
import com.fpliu.newton.framework.qr.activity.CaptureActivity;
import com.fpliu.newton.framework.share.ShareContent;
import com.fpliu.newton.framework.share.ShareDialog;
import com.fpliu.newton.framework.ui.toast.CustomToast;
import com.fpliu.newton.framework.util.LauncherManager;
import com.tencent.stat.StatService;

/**
 * Framework层的统一入口
 * 
 * @author 792793182@qq.com 2014-12-17
 * 
 */
public final class API {
	
	private Context applicationContext;
	
	private static final class InstanceHolder {
		private static API instance = new API();
	}
	
	public static API getInstance() {
		return InstanceHolder.instance;
	}
	
	/**
	 * 此方法只能被调用一次
	 * @param context  山下文环境
	 */
	public synchronized void init(Context context) {
		if (applicationContext == null) {
			applicationContext = context.getApplicationContext();
		}
	}
	
	public void run(Runnable runnable) {
		ThreadPoolManager.EXECUTOR.execute(runnable);
	}
	
	/**
	 * 扫描二维码
	 * @param activity
	 */
	public void scanQrCode(Activity activity) {
		//扫码
		Intent openCameraIntent = new Intent(activity, CaptureActivity.class);
		activity.startActivityForResult(openCameraIntent, QRCode.REQUST_CODE_QR_CODE);
		
		//统计
		Properties properties = new Properties();
		properties.put("action", "scan");
		properties.put("os_version", Environment.getInstance().getOSVersionName());
		properties.put("phone_model", Environment.getInstance().getPhoneModel());
		StatService.trackCustomKVEvent(activity, "qr_code", properties);
	}
	
	/**
	 * 解码二维码
	 * @param activity
	 */
	public void parseQrCode(Activity activity) {
		//选择或者拍照
		LauncherManager.openImagePick(activity, QRCode.REQUST_CODE_PICK_IMAGE);
		
		Properties properties = new Properties();
		properties.put("action", "parse");
		properties.put("os_version", Environment.getInstance().getOSVersionName());
		properties.put("phone_model", Environment.getInstance().getPhoneModel());
		StatService.trackCustomKVEvent(activity, "qr_code", properties);
	}
	
	/**
	 * 分享
	 * @param activity
	 * @param rootView
	 * @param shareContent
	 */
	public void share(Activity activity, View rootView, ShareContent shareContent) {
		ShareDialog shareDialog = new ShareDialog(activity, rootView, shareContent);
		shareDialog.show(Gravity.BOTTOM, 0, 0, 0);
	}
	
	/**
	 * 获取屏幕宽度
	 * @return
	 */
	@JavascriptInterface
	public int getScreenWidth() {
		return Environment.getInstance().getScreenWidth();
	}
	
	/**
	 * 获取屏幕高度
	 * @return
	 */
	@JavascriptInterface
	public int getScreenHeight() {
		return Environment.getInstance().getScreenHeight();
	}
	
	/**
	 * 获取屏幕密度
	 * @return
	 */
	@JavascriptInterface
	public float getScreenDensity() {
		return Environment.getInstance().getScreenDensity();
	}
	
	/**
	 * 获取软件版本号
	 * @return
	 */
	@JavascriptInterface
	public String getMyVersionName() {
		return Environment.getInstance().getMyVersionName();
	}
	
	/**
	 * 显示Toast
	 * @param text 要显示的文本
	 */
	@JavascriptInterface
	public void showToast(String text) {
		CustomToast.makeText(applicationContext, text, 2000).show();
	}
	
	/**
	 * 复制操作，即将文本临时保存在剪贴板中
	 * @param text 要复制的文本
	 */
	@JavascriptInterface
	public void copy(CharSequence text) {
		ClipboardManager.getInstance(applicationContext).copy(text);
	}
	
	/**
	 * 粘贴操作，即从剪贴板中获取文本
	 */
	@JavascriptInterface
	public CharSequence paste() {
		return ClipboardManager.getInstance(applicationContext).paste();
	}
}
