package com.fpliu.newton.business.account.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.net.RequestCallback;
import com.fpliu.newton.framework.net.RequestStatus;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * 新浪微博登录
 * 
 * @author 792793182@qq.com 2015-03-03
 * 
 */
final class SinaWeiboLogin implements ILogin {

	private static final String TAG = SinaWeiboLogin.class.getSimpleName();
	
	// 应用的回调页
	private static final String REDIRECT_URL = "http://crowd.datatang.com";
	
	// 应用申请的高级权限
	private static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	private LoginResult loginResult = new LoginResult();

	private boolean isLogined;

	private Oauth2AccessToken oauth2AccessToken;
	
	private SsoHandler ssoHandler;
	
	@Override
	public void login(final Activity activity, Bundle pararms, final LoginCallback callback) {
		DebugLog.d(TAG, "login()");
		
		AuthInfo authInfo = new AuthInfo(activity, MyApp.getApp().d(), REDIRECT_URL, SCOPE);
		ssoHandler = new SsoHandler(activity, authInfo);
		ssoHandler.authorizeClientSso(new WeiboAuthListener() {
			
			@Override
			public void onWeiboException(WeiboException e) {
				DebugLog.d(TAG, "onWeiboException() WeiboException = " + e);
				
				if (callback != null) {
					loginResult.setLoginType(LoginType.SinaWeibo);
					callback.onLoginFail(loginResult);
				}
			}
			
			@Override
			public void onComplete(final Bundle values) {
				DebugLog.d(TAG, "onComplete() values = " + values);
				
				// 从 Bundle 中解析 Token
				oauth2AccessToken = Oauth2AccessToken.parseAccessToken(values);
	            if (oauth2AccessToken.isSessionValid()) {
	            	//获取用户信息
					UsersAPI usersAPI = new UsersAPI(activity, MyApp.getApp().d(), oauth2AccessToken);
					final long uid = Long.parseLong(oauth2AccessToken.getUid());
					usersAPI.show(uid, new RequestListener() {
						
						@Override
						public void onWeiboException(WeiboException e) {
							DebugLog.d(TAG, "onWeiboException() WeiboException = " + e);
							
							if (callback != null) {
								loginResult.setLoginType(LoginType.SinaWeibo);
								callback.onLoginFail(loginResult);
							}
						}
						
						@Override
						public void onComplete(String response) {
							User user = User.parse(response);
							DebugLog.d(TAG, "user = " + user);
							
			                if (user == null) {
			                	if (callback != null) {
			    					loginResult.setLoginType(LoginType.SinaWeibo);
			    					callback.onLoginFail(loginResult);
								}
			                } else {
			                	RequestThirdPartLogin.requestThirdPartLogin(2, "" + uid, values.getString("access_token"), user.gender, user.name, user.avatar_large, new RequestCallback<LoginResult>() {
			                		public void callback(LoginResult result, RequestStatus status) {
										DebugLog.d(TAG, "onFinish() loginResult = " + loginResult);
										
										if (status.getHttpStatusCode() == 200) {
											isLogined = true;
											SinaWeiboLogin.this.loginResult = loginResult;
											
											if (callback != null) {
												loginResult.setLoginType(LoginType.SinaWeibo);
												callback.onLoginSuccess(loginResult);
											}
										} else {
											if (callback != null) {
												loginResult.setLoginType(LoginType.SinaWeibo);
												callback.onLoginFail(loginResult);
											}
										}
									}
								});
							}
						}
					});
	            } else {
	                // 以下几种情况，您会收到 Code：
	                // 1. 当您未在平台上注册的应用程序的包名与签名时；
	                // 2. 当您注册的应用程序包名与签名不正确时；
	                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
	                String code = values.getString("code");
	                DebugLog.d(TAG, code);
	                
	                if (callback != null) {
						loginResult.setLoginType(LoginType.SinaWeibo);
						callback.onLoginFail(loginResult);
					}
	            }
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
		isLogined = false;
		loginResult = new LoginResult();
		
		//实际上，下面的代码可以不用写
		if (oauth2AccessToken != null && oauth2AccessToken.isSessionValid()) {
			new LogoutAPI(context, MyApp.getApp().d(), oauth2AccessToken).logout(new RequestListener() {

				@Override
				public void onWeiboException(WeiboException arg0) {
					
				}

				@Override
				public void onComplete(String response) {
					if (!TextUtils.isEmpty(response)) {
						try {
							JSONObject obj = new JSONObject(response);
							String value = obj.getString("result");

							if ("true".equalsIgnoreCase(value)) {
								
							}
						} catch (JSONException e) {
							DebugLog.e(TAG, "onComplete()", e);
						}
					}
				}
			});
		}
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
		
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
}
