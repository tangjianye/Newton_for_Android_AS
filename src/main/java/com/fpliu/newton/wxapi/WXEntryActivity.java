package com.fpliu.newton.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.account.UserManager;

/**
 * 接收微信返回的数据
 * @author 792793182@qq.com 2015-03-03
 *
 */
public final class WXEntryActivity extends Activity {

	private static final String TAG = WXEntryActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DebugLog.d(TAG, "onCreate()");
		
		//这里获取到的一定是微信登录
		UserManager.getInstance().getLogining().onActivityResult(0, RESULT_OK, getIntent());
		
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		DebugLog.d(TAG, "onNewIntent()");
		
		//这里获取到的一定是微信登录
		UserManager.getInstance().getLogining().onActivityResult(0, RESULT_OK, intent);
		
		finish();
	}
}
