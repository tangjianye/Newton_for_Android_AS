package com.fpliu.newton.framework.net;

import java.lang.reflect.Constructor;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

import com.fpliu.newton.base.DebugLog;

/**
 * 网络请求 - 使用HttpClient实现
 * 此类不依赖任何第三方库，可以用在小型SDK开发上
 * 
 * @author 792793182@qq.com 2015-06-15
 * 
 */
public final class HttpClientRequest {

	private static final String TAG = HttpClientRequest.class.getSimpleName();
	
	/** 请求头 */
	private static final String HEADER_ACCEPT = "Accept";
	private static final String HEADER_CONNECTION = "Connection";
	private static final String HEADER_AUTHORIZATION = "Authorization";
	private static final String HEADER_USER_AGENT = "User-Agent";
	private static final String HEADER_REFERER = "Referer";
	private static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	/** 请求体格式 - 表单 */
	private static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
	
	/** 请求体格式 - JSON */
	private static final String CONTENT_TYPE_JSON = "application/json";
	
	/** 请求体格式 - XML */
	private static final String CONTENT_TYPE_XML = "application/xml";
	
	private HttpClientRequest() { }
	
	/**
	 * HTTP请求
	 * @param httpRequest    8种请求方法的基类
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param type           结果类型
	 * @param callback       回调
	 */
	public static <R> void request(HttpRequestBase httpRequest, String authorization, Class<R> type, RequestCallback<R> callback) {
		DebugLog.d(TAG, "request()");
		
		RequestStatus requestStatus = new RequestStatus();
		try {
			httpRequest.addHeader(HEADER_ACCEPT, "*/*");
			httpRequest.addHeader(HEADER_CONNECTION, "Keep-Alive");
			httpRequest.addHeader(HEADER_USER_AGENT, "Android");
			httpRequest.addHeader(HEADER_REFERER, "http://www.fpliu.com");
			
			if (!TextUtils.isEmpty(authorization)) {
				httpRequest.addHeader(HEADER_AUTHORIZATION, authorization);
			}
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpParams httpParams = httpclient.getParams();
			
			//设置连接超时
			httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);
			//设置读取超时
			httpParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
			
			//获取请求行、请求头、请求体
			requestStatus.setHttpRequestInfo(HTTPUtil.getRequestInfo(httpRequest));
			
			//执行请求操作
			HttpResponse response = httpclient.execute(httpRequest);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			requestStatus.setHttpStatusCode(statusCode);
			requestStatus.setHttpDescription(statusLine.getReasonPhrase());
			
			
			//将HTTP响应体转换成字符串，实际返回的是JSON格式的数据
			String jsonResult = EntityUtils.toString(response.getEntity());
			requestStatus.setRawData(jsonResult);
			
			//获取状态行、响应头、响应体
			String responseInfo = HTTPUtil.getResponseInfo(response, jsonResult);
			requestStatus.setHttpRespondInfo(responseInfo);
		} catch (Exception e) {
			DebugLog.e(TAG, "request()", e);
			requestStatus.setHttpException(e);
		} finally {
			httpRequest.abort();
		}
		
		if (callback != null) {
			callback.callback(newInstance(type), requestStatus);
		}
	}
	
	/**
	 * HEAD请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void head(String url, String authorization, Class<R> resultClass, RequestCallback<R> callback) {
		request(new HttpHead(url), authorization, resultClass, callback);
	}
	
	/**
	 * GET请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void get(String url, String authorization, Class<R> type, RequestCallback<R> callback) {
		request(new HttpGet(url), authorization, type, callback);
	}
	
	/**
	 * GET请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	public static <R> void get(String url, String authorization, Class<R> resultClass, RequestCallback<R> callback, String... params) {
		request(new HttpGet(HTTPUtil.getUrl(url, params)), authorization, resultClass, callback);
	}
	
	/**
	 * GET请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void get(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, RequestCallback<R> callback) {
		request(new HttpGet(HTTPUtil.getUrl(url, params)), authorization, resultClass, callback);
	}
	
	/**
	 * 以POST方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	public static <R> void postForm(String url, String authorization, Class<R> resultClass, RequestCallback<R> callback, String... params) {
		DebugLog.d(TAG, "postForm()");
		
		post(url, authorization, HTTPUtil.getFormEntity(params), CONTENT_TYPE_FORM, resultClass, callback);
	}
	
	/**
	 * 以POST方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postForm(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "postForm()");
		
		post(url, authorization, HTTPUtil.getFormEntity(params), CONTENT_TYPE_FORM, resultClass, callback);
	}
	
	/**
	 * 以POST方式提交JSON
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param json           JSON字符串，使用String类型可以支持GSON、fastJson、json-lib等库的转化，而不局限于一种
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postJson(String url, String authorization, String json, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "postJson()");
		
		post(url, authorization, HTTPUtil.getStringEntity(json, CONTENT_TYPE_JSON), CONTENT_TYPE_JSON, resultClass, callback);
	}
	
	/**
	 * 以POST方式提交XML
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param xml            XML字符串
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postXML(String url, String authorization, String xml, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "postXML()");
		
		post(url, authorization, HTTPUtil.getStringEntity(xml, CONTENT_TYPE_XML), CONTENT_TYPE_XML, resultClass, callback);
	}
	
	/**
	 * 以POST方式提交MaltiPart
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param formBodyParts  请求体中表单数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postMaltiPart(String url, String authorization, List<FormBodyPart> formBodyParts, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "postMaltiPart()");
		
		post(url, authorization, HTTPUtil.getMultipartEntity(formBodyParts), "", resultClass, callback);
	}

	/**
	 * 以POST方式提交任意实体
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param entity         请求体
	 * @param contentType    请求体类型
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void post(String url, String authorization, HttpEntity entity, String contentType, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "post()");
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader(HEADER_CONTENT_TYPE, contentType);
		httpPost.setEntity(entity);
		
		request(httpPost, authorization, resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	public <R> void putForm(String url, String authorization,
			Class<R> resultClass, RequestCallback<R> callback, String... params) {
		DebugLog.d(TAG, "putForm()");
		
		put(url, authorization, HTTPUtil.getFormEntity(params), CONTENT_TYPE_FORM, resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putForm(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "putForm()");
		
		put(url, authorization, HTTPUtil.getFormEntity(params), CONTENT_TYPE_FORM, resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交JSON
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param json           JSON字符串，使用String类型可以支持GSON、fastJson、json-lib等库的转化，而不局限于一种
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putJson(String url, String authorization, String json, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "putJson()");
		
		put(url, authorization, HTTPUtil.getStringEntity(json, CONTENT_TYPE_JSON), CONTENT_TYPE_JSON, resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交XML
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param xml            XML字符串
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putXml(String url, String authorization, String xml, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "putXml()");
		
		put(url, authorization, HTTPUtil.getStringEntity(xml, CONTENT_TYPE_XML), CONTENT_TYPE_XML, resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交MaltiPart
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param formBodyParts  请求体中表单数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putMaltiPart(String url, String authorization, List<FormBodyPart> formBodyParts, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "putMaltiPart()");
		
		put(url, authorization, HTTPUtil.getMultipartEntity(formBodyParts), "", resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交任意实体
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param entity         请求体
	 * @param contentType    请求体类型
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void put(String url, String authorization, HttpEntity entity, String contentType, Class<R> resultClass, RequestCallback<R> callback) {
		DebugLog.d(TAG, "put()");
		
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader(HEADER_CONTENT_TYPE, contentType);
		httpPut.setEntity(entity);
		
		request(httpPut, authorization, resultClass, callback);
	}
	
	/**
	 * DETELE请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void delete(String url, String authorization, Class<R> resultClass, RequestCallback<R> callback) {
		request(new HttpDelete(url), authorization, resultClass, callback);
	}
	
	/**
	 * DETELE请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据，语法糖
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public <R> void delete(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, RequestCallback<R> callback) {
		request(new HttpDelete(HTTPUtil.getUrl(url, params)), authorization, resultClass, callback);
	}
	
	/**
	 * 反射一个类的实例
	 * @param clazz  要反射的类
	 * @param tag    用于Debug日志
	 * @return
	 */
	private static <R> R newInstance(Class<R> clazz) {
		try {
			Constructor<R> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return (R) constructor.newInstance();
		} catch (Exception e) {
			DebugLog.e(TAG, "newInstance()", e);
			return null;
		}
	}
}