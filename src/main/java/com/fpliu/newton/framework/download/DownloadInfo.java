package com.fpliu.newton.framework.download;

import java.util.List;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.OneToMany;
import android.os.Parcel;
import android.os.Parcelable;

import com.fpliu.newton.framework.ui.adapter.Item;

/**
 * 一个下载信息
 * 
 * @author 792793182@qq.com 2014-10-8
 * 
 */
public final class DownloadInfo implements Parcelable, Item {
	
	/** 状态码 */
	private int statusCode;
	
	/** 是否支持断点续传 */
	private boolean acceptRanges;

	/** 资源的唯一标志，用于判断该资源是否被修改过，如果修改过，那么词标志也会被修改 */
	private String eTag = "";

	/** 文件类型 */
	private String mimeType = "";

	/** 文件的总大小，单位是Byte */
	private long contentLength;

	/** 原始URL */
	@Id
	private String originalUrl = "";

	/** 重定向后的URL */
	private String redirectUrl = "";
	
	/** 文件名 */
	private String fileName = "";
	
	/** 文件的完整路径 */
	private String filePath = "";
	
	/** 文件是否要覆盖，默认是false */
	private boolean overWrite;
	
	/** json数组 */
	private String blocks;
	
	/** 断点续传重试次数*/
	private int retryCount;
	
	/** 在下载列表中的可见性 */
	private boolean visiable;
	
	/** 是否查看过，默认没有查看 false */
	private boolean consumed;
	
	/** 前台或后台处理，默认后台处理 */
	private boolean foreground;
	
	/** 多个块 */
	@OneToMany(manyColumn="blockInfos")
	private List<BlockInfo> blockInfos;
	
	public DownloadInfo() { }
	
	DownloadInfo(GeneralInfo generalInfo) {
		this.acceptRanges = generalInfo.isAcceptRanges();
		this.contentLength = generalInfo.getContentLength();
		this.eTag = generalInfo.geteTag();
		this.mimeType = generalInfo.getMimeType();
		this.originalUrl = generalInfo.getOriginalUrl();
		this.redirectUrl = generalInfo.getRedirectUrl();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isAcceptRanges() {
		return acceptRanges;
	}

	public void setAcceptRanges(boolean acceptRanges) {
		this.acceptRanges = acceptRanges;
	}

	public String getETag() {
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
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isOverWrite() {
		return overWrite;
	}

	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public boolean isVisiable() {
		return visiable;
	}

	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}

	public boolean isConsumed() {
		return consumed;
	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}

	public boolean isForeground() {
		return foreground;
	}

	public void setForeground(boolean foreground) {
		this.foreground = foreground;
	}

	public List<BlockInfo> getBlockInfos() {
		return blockInfos;
	}

	public void setBlockInfos(List<BlockInfo> blockInfos) {
		this.blockInfos = blockInfos;
	}
	
	public String getBlocks() {
		return blocks;
	}

	public void setBlocks(String blocks) {
		this.blocks = blocks;
	}

	@Override
	public String toString() {
		return "DownloadInfo [statusCode=" + statusCode + ", acceptRanges="
				+ acceptRanges + ", eTag=" + eTag + ", mimeType=" + mimeType
				+ ", contentLength=" + contentLength + ", originalUrl="
				+ originalUrl + ", redirectUrl=" + redirectUrl + ", fileName="
				+ fileName + ", filePath=" + filePath + ", overWrite="
				+ overWrite + ", blocks=" + blocks + ", retryCount="
				+ retryCount + ", visiable=" + visiable + ", consumed="
				+ consumed + ", foreground=" + foreground + ", blockInfos="
				+ blockInfos + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(statusCode);
		dest.writeString(acceptRanges ? "true" : "false");
		dest.writeString(eTag);
		dest.writeString(mimeType);
		dest.writeLong(contentLength);
		dest.writeString(originalUrl);
		dest.writeString(redirectUrl);
		dest.writeString(fileName);
		dest.writeString(overWrite ? "true" : "false");
		dest.writeString(blocks);
		dest.writeInt(retryCount);
		dest.writeString(visiable ? "true" : "false");
		dest.writeString(consumed ? "true" : "false");
		dest.writeString(foreground ? "true" : "false");
		dest.writeList(blockInfos);
	}
	
	public static final Parcelable.Creator<DownloadInfo> CREATOR = new Parcelable.Creator<DownloadInfo>() {

		@Override
		public DownloadInfo createFromParcel(Parcel in) {
			DownloadInfo downloadInfo = new DownloadInfo();
			downloadInfo.statusCode = in.readInt();
			downloadInfo.acceptRanges = Boolean.parseBoolean(in.readString());
			downloadInfo.eTag = in.readString();
			downloadInfo.mimeType = in.readString();
			downloadInfo.contentLength = in.readLong();
			downloadInfo.originalUrl = in.readString();
			downloadInfo.redirectUrl = in.readString();
			downloadInfo.fileName = in.readString();
			downloadInfo.overWrite = Boolean.parseBoolean(in.readString());
			downloadInfo.blocks = in.readString();
			downloadInfo.retryCount = in.readInt();
			downloadInfo.visiable = Boolean.parseBoolean(in.readString());
			downloadInfo.consumed = Boolean.parseBoolean(in.readString());
			downloadInfo.foreground = Boolean.parseBoolean(in.readString());
			in.readList(downloadInfo.blockInfos, BlockInfo.class.getClassLoader());
			return downloadInfo;
		}

		@Override
		public DownloadInfo[] newArray(int size) {
			return new DownloadInfo[size];
		}
	};
}
