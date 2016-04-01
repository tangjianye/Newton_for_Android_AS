package com.fpliu.newton.framework.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.fpliu.newton.framework.data.KV;

/**
 * 上传数据
 * 注意：此类重写了hashCode和equals方法，在ArrayList中不会出现内容相同的对象
 * 
 * @author 792793182@qq.com 2014-12-03
 * 
 */
public final class UploadData implements Parcelable {
	
	/** 要上传的文件，文件名中包含有用户Id和任务Id等信息 */
	private File file;
	
	/** 要上传文件的元数据 */
	private ArrayList<KV> metadata;

	/** 要上传的容器名 */
	private String containerName = "";
	
	/** 要上传消息的队列名 */
	private String queueName = "";
	
	/** 账户名 */
	private String accoutName = "";
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<KV> getMetadata() {
		return metadata;
	}

	public void setMetadata(ArrayList<KV> metadata) {
		this.metadata = metadata;
	}
	
	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	
	public String getAccoutName() {
		return accoutName;
	}

	public void setAccoutName(String accoutName) {
		this.accoutName = accoutName;
	}

	@Override
	public String toString() {
		return "UploadData [file=" + file + ", metadata=" + metadata
				+ ", containerName=" + containerName + ", queueName="
				+ queueName + ", accoutName=" + accoutName + "]" + " hashCode = " + hashCode();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(file);
		dest.writeList(metadata);
		dest.writeString(containerName);
		dest.writeString(queueName);
		dest.writeString(accoutName);
	}
	
	public static final Parcelable.Creator<UploadData> CREATOR = new Creator<UploadData>() {

		@Override
		public UploadData createFromParcel(Parcel in) {
			UploadData uploadData = new UploadData();
			uploadData.file = (File) in.readSerializable();
			uploadData.metadata = in.readArrayList(KV.class.getClassLoader());
			uploadData.containerName = in.readString();
			uploadData.queueName = in.readString();
			uploadData.accoutName = in.readString();
			return uploadData;
		}

		@Override
		public UploadData[] newArray(int size) {
			return new UploadData[size];
		}
	};

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accoutName == null) ? 0 : accoutName.hashCode());
		result = prime * result
				+ ((containerName == null) ? 0 : containerName.hashCode());
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result
				+ ((metadata == null) ? 0 : metadata.hashCode());
		result = prime * result
				+ ((queueName == null) ? 0 : queueName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UploadData other = (UploadData) obj;
		if (accoutName == null) {
			if (other.accoutName != null)
				return false;
		} else if (!accoutName.equals(other.accoutName))
			return false;
		if (containerName == null) {
			if (other.containerName != null)
				return false;
		} else if (!containerName.equals(other.containerName))
			return false;
		if (file == null) {
			if (other.file != null)
				return false;
		} else if (!file.equals(other.file))
			return false;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		if (queueName == null) {
			if (other.queueName != null)
				return false;
		} else if (!queueName.equals(other.queueName))
			return false;
		return true;
	}
}
