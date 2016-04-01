package com.fpliu.newton.framework.download;

public class DownloadStatus {
	//-------------下载状态常量--------------------
	
	public static final int STATUS_UNKNOWN = -1;
	/** 空闲状态*/
	public static final int STATUS_WAITING = 0;
	/** 悬挂状态*/
	public static final int STATUS_PENDDING = 1;
	/** 下载运行状态*/
	public static final int STATUS_RUNNING = 2;
	/** 完成状态*/
	public static final int STATUS_FINISHED = 3;
	/** 停止状态*/
	public static final int STATUS_STOPPED = 4;
	/** 出错状态*/
	public static final int STATUS_ERROR = 5;
	
	///** 已经删除 */
	//public static final int STATUS_REMOVED = 6;
	
	public static boolean isStatusStopped(int status) {
		if (status == STATUS_STOPPED || status == STATUS_FINISHED) {
			return true;
		}
		return false;
	}
}
