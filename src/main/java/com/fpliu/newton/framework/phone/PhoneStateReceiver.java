package com.fpliu.newton.framework.phone;

import com.fpliu.newton.base.DebugLog;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

/**
 * 电话状态广播接收者
 * 
 * @author 792793182@qq.com 2014-12-4
 * 
 */
public final class PhoneStateReceiver extends BroadcastReceiver {

	private static final String TAG = PhoneStateReceiver.class.getSimpleName();

	private boolean isincoming;
	
	private String phoneNumber;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// 如果是拨打电话
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			isincoming = false;
			
			phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			DebugLog.i(TAG, "call OUT:" + phoneNumber);
			
			notifyObservers(isincoming, phoneNumber, -1);
		} else {
			// 如果是来电
			TelephonyManager tm = (TelephonyManager) context.getSystemService(
					                                 Service.TELEPHONY_SERVICE);
			int phoneState = tm.getCallState();
			switch (phoneState) {
			case TelephonyManager.CALL_STATE_RINGING:
				
				// 标识当前是来电
				isincoming = true;
				phoneNumber = intent.getStringExtra("incoming_number");
				DebugLog.i(TAG, "RINGING :" + phoneNumber);
				
				notifyObservers(isincoming, phoneNumber, phoneState);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				if (isincoming) {
					DebugLog.i(TAG, "incoming ACCEPT :" + phoneNumber);
				}
				
				notifyObservers(isincoming, phoneNumber, phoneState);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (isincoming) {
					DebugLog.i(TAG, "incoming IDLE");
				}
				
				notifyObservers(isincoming, phoneNumber, phoneState);
				break;
			}
		}
	}
	
	/**
     * 通知所有的观察者
     * @param isinComing  是否是来电，否则就是去电
     * @param phoneNumber 电话号码
     */
    private static void notifyObservers(boolean isinComing, String phoneNumber, int phoneState) {
		PhoneStateObservable observable = PhoneManager.getInstance().getObservable();
		observable.setChanged();
		observable.notifyObservers(false, phoneNumber, phoneState);
	}
}
