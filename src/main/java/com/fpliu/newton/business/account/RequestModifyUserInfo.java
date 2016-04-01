package com.fpliu.newton.business.account;

import org.json.JSONObject;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.account.login.LoginResult;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求修改用户信息，目前只能修改昵称，性别，城市
 * 
 * @author 792793182@qq.com 2014-11-09
 * 
 */
final class RequestModifyUserInfo {

	private static final String TAG = RequestModifyUserInfo.class.getSimpleName();
	
	/** 修改什么 */
	public static enum What {
		nickName, location, gender
	}

	public static final void requestModifyUserInfo(What what, String newValue, RequestCallback<String> callback) {
		UserInfo userInfo = UserManager.getInstance().getUserInfo();
		if (userInfo != null) {
			String cachedNickName = userInfo.getNickName();
			String cachedLocation = userInfo.getCity();
			String cachedGender = "" + userInfo.getGender();

			String nickName = cachedNickName;
			String location = cachedLocation;
			String gender = cachedGender;

			switch (what) {
			case nickName:
				nickName = newValue;
				break;
			case location:
				location = newValue;
				break;
			case gender:
				gender = newValue;
				break;
			default:
				break;
			}

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("nickName", nickName);
				jsonObject.put("location", location);
				jsonObject.put("gender", gender);
			} catch (Exception e) {
				DebugLog.e(TAG, "request()", e);
			}
			
			try {
				LoginResult loginResult = UserManager.getInstance().getLogin().getLoginResult();
				String authorization = UrlConfig.getAuthorization(loginResult.getAccessToken());
				HttpClientRequest.postJson(UrlConfig.getsavePersonUrl, authorization, jsonObject.toString(), String.class, callback);
			} catch (Exception e) {
				DebugLog.e(TAG, "request()", e);
			}
		}
	}
}
