package com.fpliu.newton.business.account.login;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * QQ登录
 * @author 792793182@qq.com 2015-03-03
 *
 */
final class QQLogin implements ILogin {

	private static final String TAG = QQLogin.class.getSimpleName();
	
	private LoginResult loginResult = new LoginResult();
	private boolean isLogined;
	
	@Override
	public void login(final Activity activity, Bundle pararms, final LoginCallback callback) {
		DebugLog.d(TAG, "login()");
		
		final Tencent tencent = MyApp.getApp().getTencent();
		
		//如果登录过了，就退出，重新登录
		if (tencent.isSessionValid()) {
			tencent.logout(activity);
		}
		
		tencent.login(activity, "all", new IUiListener() {
			
			@Override
			public void onError(UiError uiError) {
				DebugLog.d(TAG, "onError()" + uiError);
				
				if (callback != null) {
					loginResult.setLoginType(LoginType.QQ);
					callback.onLoginFail(loginResult);
				}
			}
			
			@Override
			public void onComplete(final Object jsonResult) {
				DebugLog.d(TAG, "onComplete() loginInfo = " + jsonResult);
				
				com.tencent.connect.UserInfo userInfo = new com.tencent.connect.UserInfo(activity, tencent.getQQToken());
				userInfo.getUserInfo(new IUiListener() {
					
					@Override
					public void onError(UiError uiError) {
						if (callback != null) {
							loginResult.setLoginType(LoginType.QQ);
							callback.onLoginFail(loginResult);
						}
					}
					
					@Override
					public void onComplete(Object jsonObject) {
						DebugLog.d(TAG, "onComplete() userInfo = " + jsonObject);
						
						try {
							JSONObject jsonObject2 = new JSONObject(jsonResult.toString());
							String openId = jsonObject2.getString("openid");
							String accessToken = jsonObject2.getString("access_token");
							
							JSONObject jsonObject3 = new JSONObject(jsonObject.toString());
							String gender = jsonObject3.getString("gender");
							String nickname = jsonObject3.getString("nickname");
							String avatar = jsonObject3.getString("figureurl_qq_2");
							
							RequestThirdPartLogin.requestThirdPartLogin(1, openId, accessToken, gender, nickname, avatar, new RequestCallback<LoginResult>() {
								
								public void callback(LoginResult result, RequestStatus status) {
									DebugLog.d(TAG, "onFinish() loginResult = " + loginResult);
									
									if (status.getHttpStatusCode() == 200) {
										isLogined = true;
										QQLogin.this.loginResult = loginResult;
										
										if (callback != null) {
											loginResult.setLoginType(LoginType.QQ);
											callback.onLoginSuccess(loginResult);
										}
									} else {
										if (callback != null) {
											loginResult.setLoginType(LoginType.QQ);
											callback.onLoginFail(loginResult);
										}
									}
								}
							});
						} catch (Exception e) {
							DebugLog.e(TAG, "onComplete()", e);
						}
					}
					
					@Override
					public void onCancel() {
						
					}
				});
			}
			
			@Override
			public void onCancel() {
				DebugLog.d(TAG, "onCancel()");
			}
		});
	}

	@Override
	public void logout(Context context) {
		DebugLog.d(TAG, "logout()");
		MyApp.getApp().getTencent().logout(context);
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
		Tencent tencent = MyApp.getApp().getTencent();
		if (tencent != null) {
			tencent.onActivityResult(requestCode, resultCode, data);
		}
	}
}
