package com.fpliu.newton.framework.download;

import java.net.HttpURLConnection;
import java.net.URL;

import android.text.TextUtils;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.framework.net.HTTPUtil;

/**
 * 获取一般信息
 * 
 * @author 792793182@qq.com 2014-12-28
 * 
 */
public final class RequestGeneralInfo {

	private static final String TAG = RequestGeneralInfo.class.getSimpleName();
	
	public static GeneralInfo syncRequest(String url) {
		GeneralInfo generalInfo = new GeneralInfo();
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			//设置连接超时
			connection.setConnectTimeout(5 * 1000);
			//设置读取超时
			connection.setReadTimeout(5 * 1000);
			
			//如果能走到这里，表明此URL是有效的，否则会发生异常
			generalInfo.setOriginalUrl(url);
			
			//只获取HTTP头部信息，所以用HEAD请求
			connection.setRequestMethod("HEAD");
			
			//打印HTTP头部信息
			DebugLog.d(TAG, HTTPUtil.getRequestInfo(connection));
			DebugLog.d(TAG, HTTPUtil.getResponseInfo(connection));
			
			//是否支持断点续传
			generalInfo.setAcceptRanges("bytes".equals(connection.getHeaderField("Accept-Ranges")));
			
			//查看文件是否修改过
			generalInfo.seteTag(connection.getHeaderField("ETag"));
			
			//获取文件的大小
			try {
				generalInfo.setContentLength(Long.parseLong(connection.getHeaderField("Content-Length")));
			} catch (Exception e) {
				DebugLog.e(TAG, "get()", e);
			}
			
			//获取文件类型
			generalInfo.setMimeType(connection.getHeaderField("Content-Type"));
			
			//获取重定向URL
			String redirectUrl = connection.getHeaderField("Location");
			
			while (!TextUtils.isEmpty(redirectUrl)) {
				generalInfo.setRedirectUrl(redirectUrl);
				
				//重新获取新位置的资源
				GeneralInfo generalInfo2 = syncRequest(redirectUrl);
				generalInfo.setOriginalUrl(url);
				
				if (TextUtils.isEmpty(generalInfo2.getRedirectUrl())) {
					generalInfo2.setRedirectUrl(generalInfo.getRedirectUrl());
					generalInfo = generalInfo2;
				}
			}
		} catch (Exception e) {
			DebugLog.e(TAG, "get()", e);
		}
		
		DebugLog.d(TAG, "generalInfo = " + generalInfo);
		
		return generalInfo;
	}
}
