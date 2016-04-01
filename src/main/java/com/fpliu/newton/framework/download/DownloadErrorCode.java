package com.fpliu.newton.framework.download;

/**
 * 
 * @author 792793182@qq.com 2014-12-28
 *
 */
public final class DownloadErrorCode {
	
	/**
	 * 无错误
	 */
	public static final int DOWNLOAD_OK = 0;
	
	/**
	 * 无网络连接
	 */
	public static final int NO_CONNECTION = 900;
	
	/**
	 * 存在正在下载的任务
	 */
	public static final int EXIST_RUNNING_TASK = 901;
	
	/**
	 * 存在停止或者出错的任务
	 */
	public static final int EXIST_STOPPED_TASK = 902;
	
	/**
	 * 不存在指定的任务
	 */
	public static final int NOT_EXIST_TASK = 903;
	
	/**
	 * 存在相同的任务
	 */
	public static final int EXIST_TASK = 904;
	
	/**
	 * 存在下载完成的任务
	 */
	public static final int EXIST_FINISH_TASK = 907;
	
	/**
	 * 数据库错误
	 */
	public static final int DATABASE_ERROR = 905;
	
	/**
	 * 任务数量达到上限
	 */
	public static final int TASK_REACH_LIMIT = 906;
	
}
