package com.fpliu.newton.framework.download;

import android.content.Context;

import com.fpliu.newton.framework.download.DownloadInfo;

/**
 * 下载服务
 */
interface IDownload {
    
    /**
     * 异步请求
     */
    void download(String url, String filePath);
	
	void reDownload(String url, String filePath);
	
	void clear(String url);
	
	void clearAll();
	
	List<DownloadInfo> getDownloadList();
	
	DownloadInfo getDownloadInfo(String url);
}
