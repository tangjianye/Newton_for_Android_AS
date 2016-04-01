package com.fpliu.newton.framework.download;

/**
 * 下载
 * 
 * @author 792793182@qq.com 2014-10-8
 * 
 */
public interface DownLoadListener {

	/**
	 * 
	 * @param currentPos
	 * @param total
	 */
	void onProgress(long currentPos, long total);
	
	/**
	 * 
	 * @param errorCode
	 */
	void onError(int errorCode);
}
