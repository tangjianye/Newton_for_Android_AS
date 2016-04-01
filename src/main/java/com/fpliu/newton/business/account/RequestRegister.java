package com.fpliu.newton.business.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求注册
 * 
 * @author 792793182@qq.com 2014-11-09
 * 
 */
public final class RequestRegister {

	public static void requestRegister(String userName, String password, String email, RequestCallback<String> callback) {
		// 如果是Post提交可以将参数封装到集合中传递
		List<BasicNameValuePair> dataList = new ArrayList<BasicNameValuePair>();
		dataList.add(new BasicNameValuePair("personfrom", "mobile"));
		dataList.add(new BasicNameValuePair("password", password));
		dataList.add(new BasicNameValuePair("account", userName));
		dataList.add(new BasicNameValuePair("email", email));
		
		HttpClientRequest.postForm(UrlConfig.registerUrl, null, dataList, String.class, callback);;
	}
}
