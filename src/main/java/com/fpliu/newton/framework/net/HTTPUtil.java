package com.fpliu.newton.framework.net;

import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

import com.fpliu.newton.base.DebugLog;

/**
 * HTTP帮助类
 * 
 * @author 792793182@qq.com 2014-11-15
 *
 */
public final class HTTPUtil {

	private static final String TAG = HTTPUtil.class.getSimpleName();
	
	private HTTPUtil() {}
	
	/**
	 * 获取请求信息
	 * @param httpRequest  HTTP请求
	 * @return             HTTP协议内容
	 */
	public static String getRequestInfo(HttpRequest httpRequest) {
		StringBuilder builder = new StringBuilder();
		
		//请求行
		builder.append(httpRequest.getRequestLine()).append('\n');
		
		//请求头
		for (Header header : httpRequest.getAllHeaders()) {
			builder.append(header.getName()).append(':').append(header.getValue()).append('\n');
		}
		
		if (httpRequest instanceof HttpPost) {
			//空行
			builder.append('\n');
			
			//请求体
			HttpEntity httpEntity = ((HttpPost)httpRequest).getEntity();
			if (httpEntity instanceof MultipartEntity) {
				MultipartEntity multipartEntity = (MultipartEntity) httpEntity;
				builder.append(multipartEntity.toString());
			} else {
				try {
					builder.append(EntityUtils.toString(httpEntity));
				} catch (Exception e) {
					DebugLog.e(TAG, "getRequestInfo()", e);
				}
			}
			
			builder.append('\n');
		}
		
		return builder.toString();
	}
	
	/**
	 * 获取响应信息
	 * @param response 响应
	 * @return         HTTP协议内容
	 */
	public static String getResponseInfo(HttpResponse response, String entity) {
		StringBuilder builder = new StringBuilder();
		
		//状态行
		builder.append(response.getStatusLine()).append('\n');
		
		//响应头
		for (Header header : response.getAllHeaders()) {
			builder.append(header.getName()).append(':').append(header.getValue()).append('\n');
		}
		
		//空行
		builder.append('\n');
		
		//响应体
		builder.append(entity);
		
		builder.append('\n');
		
		return builder.toString();
	}
	
	/**
	 * 获取请求信息
	 * @param connection   HTTP连接
	 * @return             HTTP协议内容
	 */
	public static String getRequestInfo(URLConnection connection) {
		StringBuilder builder = new StringBuilder();
		
		//获取头信息，用于打印Log
		Map<String, List<String>> headers = connection.getRequestProperties();
		for (String key : headers.keySet()) {
			if (TextUtils.isEmpty(key)) {
				builder.append(headers.get(null).get(0)).append('\n');
			} else {
				List<String> values = headers.get(key);
				for (String value : values) {
					builder.append(key).append(':').append(value).append('\n');
				}
			}
		}
		builder.append('\n');
		return builder.toString();
	}
	
	/**
	 * 获取响应信息
	 * @param connection   HTTP连接
	 * @return             HTTP协议内容
	 */
	public static String getResponseInfo(URLConnection connection) {
		StringBuilder builder = new StringBuilder();
		
		//获取头信息，用于打印Log
		Map<String, List<String>> headers = connection.getHeaderFields();
		for (String key : headers.keySet()) {
			if (TextUtils.isEmpty(key)) {
				builder.append(headers.get(null).get(0)).append('\n');
			} else {
				List<String> values = headers.get(key);
				for (String value : values) {
					builder.append(key + ":" + value).append('\n');
				}
			}
		}
		builder.append('\n');
		return builder.toString();
	}
	
	/**
	 * 组装URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getUrl(String url, String... kvs) {
		return getUrl(url, to(kvs));
	}

	/**
	 * 组装URL
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getUrl(String url, List<BasicNameValuePair> params) {
		String urlParams = "";
		if (params != null && !params.isEmpty()) {
			urlParams = URLEncodedUtils.format(params, "UTF-8");
		}
		
		if (!TextUtils.isEmpty(urlParams)) {
			url += "?" + urlParams;
		}
		
		return url;
	}
	
	public static List<BasicNameValuePair> to(String... kvs) {
		if (kvs != null) {
			int length = kvs.length / 2;
			if (length > 0) {
				ArrayList<BasicNameValuePair> nvs = new ArrayList<BasicNameValuePair>(length);
				for (int i = 0; i < length; i += 2) {
					//如果Key为空，就跳过
					if (TextUtils.isEmpty(kvs[i])) {
						continue;
					}
					nvs.add(new BasicNameValuePair(kvs[i], kvs[i + 1]));
				}
				
				return nvs;
			}
		}
		return null;
	}
	
	public static UrlEncodedFormEntity getFormEntity(String... kvs) {
		List<BasicNameValuePair> nvs = to(kvs);
		if (nvs != null) {
			return getFormEntity(nvs);
		}
		return null;
	}
	
	/**
	 * 获取表单实体
	 * @param params
	 * @return
	 */
	public static UrlEncodedFormEntity getFormEntity(List<BasicNameValuePair> params) {
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			DebugLog.e(TAG, "getFormEntity()", e);
		}
		
		return entity;
	}
	
	/**
	 * 获取字符串类型的请求体
	 * @param str
	 * @param contentType 
	 * @return
	 */
	public static StringEntity getStringEntity(String str, String contentType) {
		StringEntity stringEntity = null;
		try {
			stringEntity = new StringEntity(str, HTTP.UTF_8);
			stringEntity.setContentType(contentType);
		} catch (UnsupportedEncodingException e) {
			DebugLog.e(TAG, "getStringEntity()", e);
		}
		return stringEntity;
	}
	
	public static MultipartEntity getMultipartEntity(List<FormBodyPart> formBodyParts) {
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName(HTTP.UTF_8));
		for (FormBodyPart formBodyPart : formBodyParts) {
			DebugLog.d(TAG, formBodyPart.getHeader().toString());
			multipartEntity.addPart(formBodyPart);
		}
		return multipartEntity;
	}
}
