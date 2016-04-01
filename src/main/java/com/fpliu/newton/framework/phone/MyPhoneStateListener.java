package com.fpliu.newton.framework.phone;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

final class MyPhoneStateListener extends PhoneStateListener {

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		switch (state) {
		// 空闲
		case TelephonyManager.CALL_STATE_IDLE:

			break;
		// 来电（响铃中）
		case TelephonyManager.CALL_STATE_RINGING:

			break;
		// 摘机（正在通话中）
		case TelephonyManager.CALL_STATE_OFFHOOK:

			break;
		}
	}
}