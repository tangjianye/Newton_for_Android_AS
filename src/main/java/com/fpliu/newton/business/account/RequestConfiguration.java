package com.fpliu.newton.business.account;

import com.fpliu.newton.business.config.Configuration;
import com.fpliu.newton.business.config.UrlConfig;
import com.fpliu.newton.framework.net.HttpClientRequest;
import com.fpliu.newton.framework.net.RequestCallback;

/**
 * 请求配置信息
 * 
 * @author 792793182@qq.com 2014-11-09
 * 
 */
public final class RequestConfiguration {
	
	public static final void requestConfiguration(RequestCallback<Configuration> callback) {
		HttpClientRequest.get(UrlConfig.GET_CONFIGURATION, null, Configuration.class, callback);
	}
}
