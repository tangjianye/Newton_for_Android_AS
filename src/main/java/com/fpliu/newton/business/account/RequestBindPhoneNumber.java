package com.fpliu.newton.business.account;

import org.json.JSONObject;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.account.login.LoginResult;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求绑定手机号
 * 
 * @author 792793182@qq.com 2014-11-09
 *
 */
final class RequestBindPhoneNumber {

	private static final String TAG = RequestBindPhoneNumber.class.getSimpleName();
	
	public static void requestBindPhoneNumber(String phoneNumber, String userName, RequestCallback<String> callback) {
		JSONObject jsonObject = new JSONObject();
		
		try {
			jsonObject.put("account", userName);
			jsonObject.put("phoneNumber", phoneNumber);
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
		}
		
		String authorization = "";
		try {
			LoginResult loginResult = UserManager.getInstance().getLogin().getLoginResult();
			authorization = UrlConfig.getAuthorization(loginResult.getAccessToken());
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
		}
		
		HttpClientRequest.postJson(UrlConfig.getBindPhoneUrl, authorization, jsonObject.toString(), String.class, callback);
	}
}
