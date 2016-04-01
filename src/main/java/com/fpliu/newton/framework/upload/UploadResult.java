package com.fpliu.newton.framework.upload;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 上传完成后返回的结果
 * 上传后返回的结果协议请参看：http://192.168.1.181:1107/Help/Api/POST-api-source-post
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public class UploadResult implements Parcelable {
	
	private int fileSize;
	private String serverTime;
	private String content;

	private int uploadCount;
	
	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUploadCount() {
		return uploadCount;
	}

	public void setUploadCount(int uploadCount) {
		this.uploadCount = uploadCount;
	}

	@Override
	public String toString() {
		return "UploadResult [fileSize=" + fileSize + ", serverTime="
				+ serverTime + ", content=" + content + ", uploadCount="
				+ uploadCount + super.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(fileSize);
		dest.writeString(serverTime);
		dest.writeString(content);
		dest.writeInt(uploadCount);
	}
	
	public static final Parcelable.Creator<UploadResult> CREATOR = new Creator<UploadResult>() {

		@Override
		public UploadResult createFromParcel(Parcel in) {
			UploadResult uploadResult = new UploadResult();
			
			uploadResult.fileSize = in.readInt();
			uploadResult.serverTime = in.readString();
			uploadResult.content = in.readString();
			uploadResult.uploadCount = in.readInt();
			
			return uploadResult;
		}

		@Override
		public UploadResult[] newArray(int size) {
			return new UploadResult[size];
		}
	};
}
