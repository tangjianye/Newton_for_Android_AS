package com.fpliu.newton.framework.upload.service;

import com.fpliu.newton.framework.upload.UploadData;
import com.fpliu.newton.framework.upload.service.UploadingCallback;
 
/**
 * 上传管理
 */
interface IUploadManager {
	
	/**
	 * 向上传队列中添加数据
	 * @param uploadData    要上传的数据
	 */
	void addUploadData(in UploadData uploadData);
	
	/**
	 * 向上传队列中添加数据，并回调
	 * @param uploadData    要上传的数据
	 * @param callback      上传过程中的回调
	 */
	void addUploadDataWithCallback(in UploadData uploadData, in UploadingCallback callback);
	
	/**
	 * 发送消息
	 * @param queueName    队列名
	 * @param content      消息内容
	 * @return             是否发送成功
	 */
	void uploadTextToQueue(in String queueName, in String content);
}
