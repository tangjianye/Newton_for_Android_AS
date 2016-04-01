package com.fpliu.newton.framework.download;

import net.tsz.afinal.annotation.sqlite.Id;

/**
 * 一般信息，从HEAD请求中获取到的资源的一般信息
 * 
 * @author 792793182@qq.com 2014-10-8
 * 
 */
class GeneralInfo {

	private String filePath = "";
	
	/** 是否支持断点续传 */
	private boolean acceptRanges;

	/** 资源的唯一标志，用于判断该资源是否被修改过，如果修改过，那么词标志也会被修改 */
	private String eTag = "";

	/** 文件类型 */
	private String mimeType = "";

	/** 文件的总大小，单位是Byte */
	private long contentLength;

	/** 原始URL */
	@Id(column="id")
	private String originalUrl = "";

	/** 重定向后的URL */
	private String redirectUrl = "";

	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isAcceptRanges() {
		return acceptRanges;
	}

	public void setAcceptRanges(boolean acceptRanges) {
		this.acceptRanges = acceptRanges;
	}

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	@Override
	public String toString() {
		return "GeneralInfo [filePath=" + filePath + ", acceptRanges="
				+ acceptRanges + ", eTag=" + eTag + ", mimeType=" + mimeType
				+ ", contentLength=" + contentLength + ", originalUrl="
				+ originalUrl + ", redirectUrl=" + redirectUrl + "]";
	}
}
