package com.fpliu.newton.business.account;

import org.json.JSONObject;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.account.login.LoginResult;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求上传头像
 * 
 * @author 792793182@qq.com 2014-11-09
 * 
 */
final class RequestModifyAvatar {
	
	private static final String TAG = RequestModifyAvatar.class.getSimpleName();

	public static final void requestModifyAvatar(String avatar, String account, RequestCallback<String> callback) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("avatar", avatar);
			jsonObject.put("account", account);
		} catch (Exception e) {
			DebugLog.e(TAG, "requestModifyAvatar()", e);
		}

		try {
			LoginResult loginResult = UserManager.getInstance().getLogin().getLoginResult();
			String authorization = UrlConfig.getAuthorization(loginResult.getAccessToken());
			
			HttpClientRequest.postJson(UrlConfig.getPostAvatarUrl, authorization, jsonObject.toString(), String.class, callback);
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
		}
	}
}
