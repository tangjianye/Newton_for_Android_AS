package com.fpliu.newton.framework.browser;

/**
 * 在JavaScript中调用Java的接口
 * 
 * @author 792793182@qq.com 2015-04-16
 * 
 */
public interface JavaCallback {
	/**
	 * 
	 * @param jobject    Java对象
	 * @param method     Java对象中的函数
	 * @param paramJson  java对象中的函数的参数，JSON数组格式
	 */
	void exec(String jobject, String method, String paramJson);
}