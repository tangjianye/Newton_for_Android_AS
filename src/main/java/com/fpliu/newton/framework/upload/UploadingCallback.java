package com.fpliu.newton.framework.upload;

/**
 * 上传过程的回调
 * @author 792793182@qq.com 2015-03-27
 * 
 */
public interface UploadingCallback {
	
	public static final int EROR_CODE_XX = 1;
	
	public static final int EROR_CODE_YY = 2;
	
	public static final int EROR_CODE_ZZ = 3;
	
    
    /**
     * 上传到Blob中，并上传了元数据，并发送消息到队列中成功
     */
    void onSuccess(UploadData uploadData);
    
    /**
     * 上传到Blob中失败，或者上传元数据失败，或者发送消息到队列中失败
     * @param uploadData 上传数据
     * @param errorCode  错误码{ @see EROR_CODE_XX }
     */
    void onFail(UploadData uploadData, int errorCode);
}
