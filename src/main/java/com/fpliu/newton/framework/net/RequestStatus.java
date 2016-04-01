package com.fpliu.newton.framework.net;

import android.os.Parcel;
import android.os.Parcelable;

import com.fpliu.newton.base.DebugLog;

/**
 * 请求后返回的结果
 * 
 * @author 792793182@qq.com 2014-10-18
 * 
 */
public final class RequestStatus implements Parcelable {

	/** HTTP的状态码 */
	private int httpStatusCode;

	/** HTTP的描述 */
	private String httpDescription = "";

	/** HTTP的异常，如果发生异常的话 */
	private Exception httpException;

	/** HTTP请求信息 */
	private String httpRequestInfo;

	/** HTTP响应信息 */
	private String httpRespondInfo;

	/** HTTP响应体 */
	private String rawData;

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getHttpDescription() {
		return httpDescription;
	}

	public void setHttpDescription(String httpDescription) {
		this.httpDescription = httpDescription;
	}

	public Exception getHttpException() {
		return httpException;
	}

	public void setHttpException(Exception httpException) {
		this.httpException = httpException;
	}

	public String getHttpRequestInfo() {
		return httpRequestInfo;
	}

	public void setHttpRequestInfo(String httpRequestInfo) {
		this.httpRequestInfo = httpRequestInfo;
	}

	public String getHttpRespondInfo() {
		return httpRespondInfo;
	}

	public void setHttpRespondInfo(String httpRespondInfo) {
		this.httpRespondInfo = httpRespondInfo;
	}

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	@Override
	public String toString() {
		return "RequestStatus [httpStatusCode=" + httpStatusCode
				+ ", httpDescription=" + httpDescription + ", httpException="
				+ httpException + ", httpRequestInfo=" + httpRequestInfo
				+ ", httpRespondInfo=" + httpRespondInfo + ", rawData="
				+ rawData + "]";
	}

	/**
	 * 获取HTTP请求过程，用于记录日志
	 */
	public final String getHttpProcess() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(httpRequestInfo);
		stringBuilder.append('\n');
		if (httpException == null) {
			stringBuilder.append(httpRespondInfo);
		} else {
			stringBuilder.append(DebugLog.getExceptionTrace(httpException));
		}

		stringBuilder.append('\n');
		stringBuilder
				.append("--------------------------------------------------------------");
		stringBuilder.append('\n');

		return stringBuilder.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(httpStatusCode);
		dest.writeString(httpDescription);
		dest.writeSerializable(httpException);
		dest.writeString(httpRequestInfo);
		dest.writeString(httpRespondInfo);
		dest.writeString(rawData);
	}
	
	public static final Parcelable.Creator<RequestStatus> CREATOR = new Creator<RequestStatus>() {

		@Override
		public RequestStatus createFromParcel(Parcel source) {
			RequestStatus requestStatus = new RequestStatus();
			requestStatus.httpStatusCode = source.readInt();
			requestStatus.httpDescription = source.readString();
			requestStatus.httpException = (Exception) source.readSerializable();
			requestStatus.httpRequestInfo = source.readString();
			requestStatus.httpRespondInfo = source.readString();
			requestStatus.rawData = source.readString();
			return requestStatus;
		}

		@Override
		public RequestStatus[] newArray(int size) {
			return new RequestStatus[size];
		}
	};
}
