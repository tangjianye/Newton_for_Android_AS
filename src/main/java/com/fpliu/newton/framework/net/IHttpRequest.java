package com.fpliu.newton.framework.net;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.message.BasicNameValuePair;

import com.androidquery.callback.AjaxCallback;

/**
 * 请求网络接口
 * 
 * @author 792793182@qq.com 2015-06-21
 *
 */
public interface IHttpRequest {
	
	/**
	 * HEAD请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void head(String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * GET请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void get(String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback);

	/**
	 * GET请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	<R> void get(String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback, String... params);
	
	/**
	 * GET请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void get(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以POST方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	<R> void postForm(String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback, String... params);
	
	/**
	 * 以POST方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void postForm(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback);

	/**
	 * 以POST方式提交JSON
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param json           JSON字符串，使用String类型可以支持GSON、fastJson、json-lib等库的转化，而不局限于一种
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void postJson(String url, String authorization, String json, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以POST方式提交XML
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param xml            XML字符串
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void postXML(String url, String authorization, String xml, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以POST方式提交MaltiPart
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param formBodyParts  请求体中表单数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void postMaltiPart(String url, String authorization, List<FormBodyPart> formBodyParts, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以POST方式提交任意实体
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param entity         请求体
	 * @param contentType    请求体类型
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void post(String url, String authorization, HttpEntity entity, String contentType, Class<R> resultClass, AjaxCallback<R> callback);

	/**
	 * 以PUT方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param resultClass    结果类型
	 * @param callback       回调
	 * @param params         请求体中的键值对数据，语法糖
	 */
	<R> void putForm(String url, String authorization, Class<R> resultClass, AjaxCallback<R> callback, String... params);
	
	/**
	 * 以PUT方式提交表单
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void putForm(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以PUT方式提交JSON
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param json           JSON字符串，使用String类型可以支持GSON、fastJson、json-lib等库的转化，而不局限于一种
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public <R> void putJson(String url, String authorization, String json, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以PUT方式提交XML
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param xml            XML字符串
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	public <R> void putXml(String url, String authorization, String xml, Class<R> resultClass, AjaxCallback<R> callback);
	

	/**
	 * 以PUT方式提交MaltiPart
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param formBodyParts  请求体中表单数据
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void putMaltiPart(String url, String authorization, List<FormBodyPart> formBodyParts, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * 以PUT方式提交任意实体
	 * @param url            资源地址
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param entity         请求体
	 * @param contentType    请求体类型
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void put(String url, String authorization, HttpEntity entity, String contentType, Class<R> resultClass, AjaxCallback<R> callback);
	
	/**
	 * DETELE请求
	 * @param url            要请求资源的URL
	 * @param authorization  验证用户，不需要验证的，传入空，即可
	 * @param params         请求体中的键值对数据，语法糖
	 * @param resultClass    结果类型
	 * @param callback       回调
	 */
	<R> void delete(String url, String authorization, List<BasicNameValuePair> params, Class<R> resultClass, AjaxCallback<R> callback);
}
