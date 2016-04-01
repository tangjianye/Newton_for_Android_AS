package com.fpliu.newton.business.account.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.fpliu.newton.business.account.RequestLogin;
import com.fpliu.newton.business.account.RequestPostClientInfo;
import com.fpliu.newton.business.account.UserManager;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.fpliu.newton.framework.util.TestSetting;

/**
 * 自己的账户登录
 * 
 * @author 792793182@qq.com 2015-03-03
 * 
 */
final class NewtonLogin implements ILogin {

	private static final String TAG = NewtonLogin.class.getSimpleName();

	private LoginResult loginResult = new LoginResult();

	private boolean isLogined;

	@Override
	public void login(final Activity activity, Bundle pararms, final LoginCallback callback) {
		if (pararms == null) {
			if (callback != null) {
				loginResult.setLoginType(LoginType.Newton);
				callback.onLoginFail(loginResult);
			}
			return;
		}

		String userName = pararms.getString("userName");
		String password = pararms.getString("password");

		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
			if (callback != null) {
				loginResult.setLoginType(LoginType.Newton);
				callback.onLoginFail(loginResult);
			}
			return;
		}
		RequestLogin.requestLogin(userName, password,  new RequestCallback<LoginResult>() {

			@Override
			public void callback(LoginResult result, final RequestStatus status) {

				new Handler(Looper.getMainLooper()).post(new Runnable() {

					@Override
					public void run() {
						// 如果自动登录成功
						if (status.getHttpStatusCode() == 200) {
							NewtonLogin.this.loginResult = loginResult;
							isLogined = true;
							
							if (TestSetting.TaskTestSettings.LonginFirst) {
								TestSetting.TaskTestSettings.RefreshAccesstoken = true;
								TestSetting.TaskTestSettings.LonginFirst = false;
								UserManager.scheduleGetAccessToken();
							}
							
							TestSetting.TraceSettings.TraceMessageType = TestSetting.TraceSettings.TraceMessageType_LOGIN;
							RequestPostClientInfo.requestPostClientInfo();
							
							if (callback != null) {
								loginResult.setLoginType(LoginType.Newton);
								callback.onLoginSuccess(loginResult);
							}
						}
						// 如果登录失败
						else {
							if (callback != null) {
								loginResult.setLoginType(LoginType.Newton);
								callback.onLoginFail(loginResult);
							}
						}
					}
				});
			}
		});
	}

	@Override
	public void logout(Context context) {
		isLogined = false;
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
		
	}
}
