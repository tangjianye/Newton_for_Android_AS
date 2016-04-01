package com.fpliu.newton.framework.phone;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public final class PhoneManager {

	private static PhoneManager instance;
	
	private PhoneStateObservable observable;
	
	private PhoneManager(Context context) {
		observable = new PhoneStateObservable();
		
		// 获取电话通讯服务
		TelephonyManager tm = (TelephonyManager) context.getSystemService(
				                                 Context.TELEPHONY_SERVICE);
		// 创建一个监听对象，监听电话状态改变事件
		tm.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	public static synchronized PhoneManager init(Context context) {
		return (instance = new PhoneManager(context));
	}
	
	public static PhoneManager getInstance() {
		return instance;
	}
	
	public void register(PhoneStateObserver observer) {
		observable.addObserver(observer);
	}
	
	PhoneStateObservable getObservable() {
		return observable;
	}
}
