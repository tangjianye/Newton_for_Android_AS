package com.fpliu.newton.business.account;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.account.login.LoginResult;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求用户信息
 * 
 *  @author 792793182@qq.com 2014-11-09
 * 
 */
public final class RequestUserInfo {
	
	private static final String TAG = RequestUserInfo.class.getSimpleName();
	
	public static void requestUserInfo(RequestCallback<UserInfo> callback) {
		DebugLog.d(TAG, "request()");
		
		LoginResult loginResult = UserManager.getInstance().getLogin().getLoginResult();
		UserInfo userInfo = null;
		
		try {
			String authorization = UrlConfig.getAuthorization(loginResult.getAccessToken());
			HttpClientRequest.get(UrlConfig.getUserInfoUrl, authorization, UserInfo.class, callback);
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
			userInfo = new UserInfo();
		}
	}
}
