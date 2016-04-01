package com.fpliu.newton.framework.upload.service;

import com.fpliu.newton.framework.upload.UploadResult;
 
/**
 * 上传完成的回调
 */
interface UploadFinishCallback {
    
    /**
     * 上传完成
     */
    void onFinish(in UploadResult uploadResult);  
}
