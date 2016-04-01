package com.fpliu.newton.business.account.login;

import com.fpliu.newton.base.DebugLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 没有登录，将没有登录看成是一种登录状态
 * @author 792793182@qq.com 2015-03-03
 *
 */
final class NoLogin implements ILogin {
	
	private static final String TAG = NoLogin.class.getSimpleName();
	
	private LoginResult loginResult = new LoginResult();
	
	private boolean isLogined;
	
	@Override
	public void login(final Activity activity, Bundle pararms, final LoginCallback callback) {
		DebugLog.d(TAG, "login()");
	}

	@Override
	public void logout(Context context) {
		DebugLog.d(TAG, "logout()");
	}

	@Override
	public boolean isLogin() {
		return isLogined;
	}
	
	@Override
	public LoginResult getLoginResult() {
		return loginResult;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		DebugLog.d(TAG, "onActivityResult()");
	}
}
