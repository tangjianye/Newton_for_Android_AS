package com.fpliu.newton.framework.download;

import org.json.JSONObject;

import com.fpliu.newton.base.DebugLog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 一个块的信息
 * 
 * @author 792793182@qq.com 2014-10-8
 * 
 */
final class BlockInfo implements Parcelable {
	
	private static final String TAG = BlockInfo.class.getSimpleName();
	
	/** 块的状态 */
	private int status;
	
	/** 块的起始位置，单位是Byte */
	private long startPos;
	
	/** 块的结束位置，单位是Byte */
	private long endPos;

	/** 块的已经下载完的位置，单位是Byte */
	private long currentPos;
	
	/** 总的大小，单位是Byte */
	private long contentLength;
	
	/** 重试次数 */
	private int retryCount;
	

	private BlockInfo() { }
	
	BlockInfo(long startPos, long endPos, long contentLength) {
		this.currentPos = startPos;
		this.startPos = startPos;
		this.endPos = endPos;
		this.contentLength = contentLength;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getStartPos() {
		return startPos;
	}

	public void setStartPos(long startPos) {
		this.startPos = startPos;
	}

	public long getEndPos() {
		return endPos;
	}

	public void setEndPos(long endPos) {
		this.endPos = endPos;
	}

	public long getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(long currentPos) {
		this.currentPos = currentPos;
	}

	public long getContentLength() {
		return contentLength;
	}

	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	
	public int getRetryCount() {
		return retryCount;
	}
	
	public int increaseRetryCount() {
		return ++this.retryCount;
	}
	
	@Override
	public String toString() {
		return "BlockInfo [status=" + status + ", startPos=" + startPos
				+ ", endPos=" + endPos + ", currentPos=" + currentPos
				+ ", contentLength=" + contentLength + ", retryCount="
				+ retryCount + "]";
	}

	static BlockInfo parse(JSONObject jsonObject) {
		BlockInfo blockInfo = new BlockInfo(0, 0, 0);
		try {
			blockInfo.setStatus(jsonObject.getInt("status"));
			blockInfo.setStartPos(jsonObject.getLong("startPos"));
			blockInfo.setEndPos(jsonObject.getLong("endPos"));
			blockInfo.setCurrentPos(jsonObject.getLong("currentPos"));
			blockInfo.setContentLength(jsonObject.getLong("contentLength"));
		} catch (Exception e) {
			DebugLog.e(TAG, "parse()", e);
		}
		return blockInfo;
	}
	
	JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("status", status);
			jsonObject.put("startPos", startPos);
			jsonObject.put("endPos", endPos);
			jsonObject.put("currentPos", currentPos);
			jsonObject.put("contentLength", contentLength);
		} catch (Exception e) {
			DebugLog.e(TAG, "toJson()", e);
		}
		return jsonObject;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(status);
		dest.writeLong(startPos);
		dest.writeLong(endPos);
		dest.writeLong(currentPos);
		dest.writeLong(contentLength);
		dest.writeInt(retryCount);
	}
	
	public static final Parcelable.Creator<BlockInfo> CREATOR = new Parcelable.Creator<BlockInfo>() {

		@Override
		public BlockInfo createFromParcel(Parcel in) {
			BlockInfo blockInfo = new BlockInfo();
			blockInfo.status = in.readInt();
			blockInfo.startPos = in.readLong();
			blockInfo.endPos = in.readLong();
			blockInfo.currentPos = in.readLong();
			blockInfo.contentLength = in.readLong();
			blockInfo.retryCount = in.readInt();
			return blockInfo;
		}

		@Override
		public BlockInfo[] newArray(int size) {
			return new BlockInfo[size];
		}
	};
}
