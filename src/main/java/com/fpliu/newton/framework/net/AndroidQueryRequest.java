package com.fpliu.newton.framework.net;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;

/**
 * 网络请求 - 使用Android Query开源库
 * https://github.com/leleliu008/android-library-androidquery
 * 支持RESTful API的GET、POST、PUT、DELETE四种方式
 * 
 * @author 792793182@qq.com 2015-06-15
 * 
 */
public final class AndroidQueryRequest {
	
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
	
	/**
	 * HEAD请求
	 * @param context        上下文
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void head(Context context, String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback) {
		//暂不支持
	}
	
	/**
	 * GET请求
	 * @param context        上下文
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void get(Context context, String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.ajax(commonHeader(callback, authorization).url(url).type(resultClass));
	}
	
	/**
	 * GET请求
	 * @param context        上下文
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	public static <R> void get(Context context, String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback, String... params) {
		AQuery aQuery = new AQuery(context);
		aQuery.ajax(commonHeader(callback, authorization).url(HTTPUtil.getUrl(url, params)).type(resultClass));
	}
	
	/**
	 * GET请求
	 * @param context        上下文
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void get(Context context, String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.ajax(commonHeader(callback, authorization).url(HTTPUtil.getUrl(url, params)).type(resultClass));
	}
	
	/**
	 * 以POST方式提交表单
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	public static <R> void postForm(Context context, String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback, String... params) {
		AQuery aQuery = new AQuery(context);
		post(aQuery, url, CONTENT_TYPE_FORM, HTTPUtil.getFormEntity(params), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以POST方式提交表单
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postForm(Context context, String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		post(aQuery, url, CONTENT_TYPE_FORM, HTTPUtil.getFormEntity(params), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以POST方式提交JSON
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param json           JSON字符串，使用String类型可以支持GSON、fastJson、json-lib等库的转化，而不局限于一种
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postJson(Context context, String url, String authorization, String json, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		post(aQuery, url, CONTENT_TYPE_JSON, HTTPUtil.getStringEntity(json, CONTENT_TYPE_JSON), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以POST方式提交XML
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param xml            XML字符串
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postXML(Context context, String url, String authorization, String xml, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		post(aQuery, url, CONTENT_TYPE_XML, HTTPUtil.getStringEntity(xml, CONTENT_TYPE_XML), resultClass, commonHeader(callback, authorization));
	}

	/**
	 * 以POST方式提交MaltiPart
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param formBodyParts  请求体中表单数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void postMaltiPart(Context context, String url, String authorization, List<FormBodyPart> formBodyParts, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		MultipartEntity multipartEntity = HTTPUtil.getMultipartEntity(formBodyParts);
		post(aQuery, url, "", multipartEntity, resultClass, callback);
	}
	
	/**
	 * 以POST方式提交任意实体
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param entity         请求体
	 * @param contentType    请求体类型
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void post(Context context, String url, String authorization, HttpEntity entity, String contentType, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		post(aQuery, url, contentType, entity, resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以PUT方式提交表单
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	public static <R> void putForm(Context context, String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback, String... params) {
		AQuery aQuery = new AQuery(context);
		aQuery.put(url, CONTENT_TYPE_FORM, HTTPUtil.getFormEntity(params), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以PUT方式提交表单
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putForm(Context context, String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.put(url, CONTENT_TYPE_FORM, HTTPUtil.getFormEntity(params), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以PUT方式提交JSON
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param json           JSON字符串，使用String类型可以支持GSON、fastJson、json-lib等库的转化，而不局限于一种
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putJson(Context context, String url, String authorization, String json, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.put(url, CONTENT_TYPE_JSON, HTTPUtil.getStringEntity(json, CONTENT_TYPE_JSON), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以PUT方式提交XML
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param xml            XML字符串
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putXml(Context context, String url, String authorization, String xml, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.put(url, CONTENT_TYPE_XML, HTTPUtil.getStringEntity(xml, CONTENT_TYPE_XML), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * 以PUT方式提交MaltiPart
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param formBodyParts  请求体中表单数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void putMaltiPart(Context context, String url, String authorization, List<FormBodyPart> formBodyParts, Class<R> resultClass, AjaxCallback<R> callback) {
		MultipartEntity multipartEntity = HTTPUtil.getMultipartEntity(formBodyParts);
		put(context, url, authorization, multipartEntity, "", resultClass, callback);
	}
	
	/**
	 * 以PUT方式提交任意实体
	 * @param context        上下文
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param entity         请求体
	 * @param contentType    请求体类型
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void put(Context context, String url, String authorization, HttpEntity entity,
			String contentType, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.put(url, contentType, entity, resultClass, callback);
	}
	
	/**
	 * DETELE请求
	 * @param context        上下文
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据，语法糖
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public static <R> void delete(Context context, String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback) {
		AQuery aQuery = new AQuery(context);
		aQuery.delete(HTTPUtil.getUrl(url, params), resultClass, commonHeader(callback, authorization));
	}
	
	/**
	 * POST请求
	 * @param aQuery
	 * @param url
	 * @param contentHeader
	 * @param entity
	 * @param type
	 * @param callback
	 * @return
	 */
	private static <K> AQuery post(AQuery aQuery, String url, String contentHeader, HttpEntity entity, Class<K> type, AjaxCallback<K> callback){
		callback.url(url).type(type).method(AQuery.METHOD_POST).header(HEADER_CONTENT_TYPE, contentHeader).param(AQuery.POST_ENTITY, entity);		
		return aQuery.ajax(callback);
	}
	
	/**
	 * 添加公共的请求头
	 * @param callback     请求的回调
	 */
	private static <K> AjaxCallback<K> commonHeader(AjaxCallback<K> callback, String authorization) {
		return callback.header(HEADER_ACCEPT, "*/*")
					   .header(HEADER_CONNECTION, "Keep-Alive")
					   .header(HEADER_AUTHORIZATION, authorization)
					   .header(HEADER_USER_AGENT, "Android")
					   .header(HEADER_REFERER, "http://www.fpliu.com");
	}
}