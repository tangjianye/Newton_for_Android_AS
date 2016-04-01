package com.fpliu.newton.framework.net;

/**
 * 请求完成后的回调
 * 
 * @author 792793182@qq.com 2014-10-18
 *
 */
public interface RequestCallback<R> {

	/**
	 * 请求完成后的回调
	 * @param result    结果
	 * @param status    状态
	 */
	void callback(R result, RequestStatus status);
}
