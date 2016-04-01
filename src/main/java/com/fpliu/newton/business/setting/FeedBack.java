package com.fpliu.newton.business.setting;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.view.Gravity;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.ThreadPoolManager;
import com.fpliu.newton.framework.lbs.LBS;
import com.fpliu.newton.framework.lbs.XAddress;
import com.fpliu.newton.framework.ui.dialog.ProgressDialog;
import com.fpliu.newton.framework.upload.UploadManager;
import com.fpliu.newton.framework.util.Helper;
import com.tencent.stat.StatService;

/**
 * 意见反馈
 * 
 * @author 792793182@qq.com 2015-04-17
 * 
 */
public final class FeedBack {

	private static final String TAG = FeedBack.class.getSimpleName();
	
	private FeedBack() { }
	
	public static final void send(final Activity activity, final String suggestContent, final String email, final FeedBackCallback callback) {
		final Context context = activity;
		final ProgressDialog progressDialog = new ProgressDialog(activity);
		progressDialog.setMessage(MyApp.getApp().getResources().getString(R.string.setting_suggest_sending));
		progressDialog.setCancelable(false);
		progressDialog.show(Gravity.CENTER, 0, 0, 0);
		progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				
			}
		});
		
		ThreadPoolManager.EXECUTOR.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					TelephonyManager telephoneManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					
					StringBuilder deviceInfo = new StringBuilder();
					deviceInfo.append("<设备信息> ").append("\n");
					deviceInfo.append("机器型号: ").append(android.os.Build.MODEL).append("\n");
					deviceInfo.append("系统版本: ").append(android.os.Build.VERSION.RELEASE).append("\n");
					deviceInfo.append("手机串号: ").append(telephoneManager.getDeviceId()).append("\n");
					deviceInfo.append("软件版本: ").append(Environment.getInstance().getMyVersionName()).append("\n");
					deviceInfo.append("消息发送本机时间: ").append(Helper.getSimpleDateFormat0(System.currentTimeMillis())).append("\n");
					deviceInfo.append("消息发送本机地址: ");
					
					XAddress address = LBS.getInstance().getAddress();
					if (address != null) {
						String lat = address.getLatitude();
						String lon = address.getLongtitude();
						deviceInfo.append("Lat:" + lat + ", Long:" + lon).append("\n");
					}
					
					deviceInfo.append("<网络信息>").append("\n");
					deviceInfo.append("SIM卡状态: ").append((telephoneManager.getSimState() == 1) ? "没有SIM卡" : "SIM卡正常").append("\n");
					deviceInfo.append("网络类型: ").append(Helper.isConnectNetwork(context)).append("\n");
					deviceInfo.append("WIFI信息: ").append(Helper.getWifiStatus(context)).append("\n");
					
					try {
						String emailContent = "<消息内容>" + suggestContent + "\n\n\n<联系方式>" + email + "\n\n\n" + deviceInfo.toString();

						UploadManager.getInstance().uploadTextToQueue("feedback", emailContent);
						
						//统计分析
						Properties properties = new Properties();
						properties.put("action", "send");
						properties.put("os_version", Environment.getInstance().getOSVersionName());
						properties.put("phone_model", Environment.getInstance().getPhoneModel());
						StatService.trackCustomKVEvent(context, "feedback", properties);
						
						progressDialog.dismiss();
						if (callback != null) {
							callback.onFinish(0);
						}
					} catch (Exception e) {
						DebugLog.e(TAG, "send()", e);
						
						progressDialog.dismiss();
						if (callback != null) {
							callback.onFinish(-1);
						}
					}
				} catch (Exception e) {
					DebugLog.e(TAG, "send()", e);
					
					progressDialog.dismiss();
					if (callback != null) {
						callback.onFinish(-1);
					}
				}
			}
		});
	}
	
	public static interface FeedBackCallback {
		/**
		 * 发送完成
		 * @param statusCode  状态码，0为成功，-1为失败
		 */
		void onFinish(int statusCode);
	}
}
