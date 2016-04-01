package com.fpliu.newton.business.account;

import org.json.JSONObject;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求校验验证码
 * 
 * @author 792793182@qq.com 2014-11-09
 *
 */
public final class RequestCheckVerificationCode {
	
	private static final String TAG = RequestCheckVerificationCode.class.getSimpleName();
	
	public static void requestCheckVerificationCode(int type, String phoneNumber, String verificationCode, RequestCallback<String> callback) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("type", type);
			jsonObject.put("recipient", phoneNumber);
			jsonObject.put("code", verificationCode);
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
		}
		
		HttpClientRequest.postJson(UrlConfig.registerVerificationUrl, null, jsonObject.toString(), String.class, callback);
	}
}
