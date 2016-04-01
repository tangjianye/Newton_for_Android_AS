package com.fpliu.newton.framework.upload.service;

import com.fpliu.newton.framework.upload.UploadData;
 
/**
 * 上传过程中的回调
 */
interface UploadingCallback {
    
    /**
     * 上传到Blob中，并发送消息到队列中成功
     */
    void onSuccess(in UploadData uploadData);
    
    /**
     * 上传到Blob中失败，或者发送消息到队列中失败
     */
    void onFail(in UploadData uploadData, in int errorCode);
}
