package com.fpliu.newton.framework.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.home.Home;

/**
 * 推送消息接收器
 * 
 * @author 792793182@qq.com 2014-11-26
 * 
 */
public final class PushReceiver extends BroadcastReceiver {

	private static final String TAG = PushReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		PushMessage pushMessage = new PushMessage();
		
		Bundle bundle = intent.getExtras();
		String action = intent.getAction();
		
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
			// Action - cn.jpush.android.intent.REGISTRATION
			// SDK 向 JPush Server 注册所得到的注册 全局唯一的 ID ，可以通过此 ID 向对应的客户端发送消息和通知。
			
			String id = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			
			pushMessage.setRegistration_id(id);

			DebugLog.d(TAG, "registration_id = " + id);
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
			// 接收到的是自定义消息
			// 自定义消息不会展示在通知栏，完全要开发者写代码去处理
			pushMessage.setTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
			pushMessage.setMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
			pushMessage.setExtra(bundle.getString(JPushInterface.EXTRA_EXTRA));
			pushMessage.setRichpush_file_path(bundle.getString(JPushInterface.EXTRA_RICHPUSH_FILE_PATH));
			pushMessage.setMsg_id(bundle.getString(JPushInterface.EXTRA_MSG_ID));

			DebugLog.v(TAG, pushMessage.toString());
			
			PushManager.getInstance().save(pushMessage);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
			// 接收到的是通知
			pushMessage.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
			
			String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
			pushMessage.setAlert(alert);
			// 这里和JPushInterface的属性有变化
			pushMessage.setNotification_extra(bundle.getString(JPushInterface.EXTRA_EXTRA));
			pushMessage.setNotification_id(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_ID));
			pushMessage.setContent_type(bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));
			pushMessage.setRichpush_html_path(bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH));
			pushMessage.setRichpush_html_res(bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES));

			DebugLog.d(TAG, "收到了通知。消息内容是：" + alert);

			String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);

			DebugLog.d(TAG, "type = " + type);
			
			DebugLog.d(TAG, pushMessage.toString());
			
			PushManager.getInstance().save(pushMessage);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
			DebugLog.d(TAG, "用户点击打开了通知");
			// 在这里可以自己写代码去定义用户点击后的行为
			Intent i = new Intent(context, Home.class); // 自定义打开的界面
			// 设置启动模式
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("push", "true");
			context.startActivity(i);
		} else {
			DebugLog.v(TAG, "Unhandled intent - " + intent.getAction());
		}
	}
}
