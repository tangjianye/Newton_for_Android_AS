package com.fpliu.newton.framework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;

import com.fpliu.newton.base.DebugLog;

/**
 * 反射帮助类
 * 
 * @author 792793182@qq.com 2014-11-02
 * 
 */
public final class ReflactUtil {

	private ReflactUtil() { }
	
	/**
	 * 反射一个类的实例
	 * @param clazz  要反射的类
	 * @param tag    用于Debug日志
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked" })
	public static <R> R newInstance(Class clazz, String tag) {
		try {
			ParameterizedType superClass = (ParameterizedType) clazz.getGenericSuperclass();
			Class actualType = (Class) superClass.getActualTypeArguments()[0];
			String className = actualType.getName();
			DebugLog.d(tag, "className = " + className);

			Class resultClass = Class.forName(className);
			Constructor constructor = resultClass.getDeclaredConstructor();
			constructor.setAccessible(true);
			return (R) constructor.newInstance();
		} catch (Exception e) {
			DebugLog.e(tag, "createRequestResult()", e);
			return newInstance(clazz.getSuperclass(), tag);
		}
	}
}
