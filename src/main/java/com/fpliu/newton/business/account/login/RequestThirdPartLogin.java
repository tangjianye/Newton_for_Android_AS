package com.fpliu.newton.business.account.login;

import org.json.JSONObject;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求第三方登录
 * 
 * @author 792793182@qq.com 2015-03-09
 * 
 */
final class RequestThirdPartLogin {
	
	private static final String TAG = RequestThirdPartLogin.class.getSimpleName();

	public static void requestThirdPartLogin(int type, String openId, String accessToken, String gender, String nickname, String avatar, RequestCallback<LoginResult> callback) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type", type);
			jsonObject.put("openId", openId);
			jsonObject.put("accessToken", accessToken);
			jsonObject.put("gender", gender);
			jsonObject.put("nickname", nickname);
			jsonObject.put("avatar", avatar);
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
		}

		HttpClientRequest.postJson(UrlConfig.thirdPartLoginUrl, null, jsonObject.toString(), LoginResult.class, callback);
	}
}
