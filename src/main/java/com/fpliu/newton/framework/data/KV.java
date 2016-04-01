package com.fpliu.newton.framework.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.fpliu.newton.base.DebugLog;

/**
 * 键值对
 * 注意：此类重写了hashCode和equals方法，在ArrayList中不会出现内容相同的对象
 * 
 * @author 792793182@qq.com 2014-11-12
 *
 */
public final class KV implements Parcelable {

	private static final String TAG = KV.class.getSimpleName();
	
	private String key;
	
	private String value;

	public KV() { }
	
	public KV(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		KV other = (KV) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KV [key=" + key + ", value=" + value + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(key);
		dest.writeString(value);
	}
	
	public static final Parcelable.Creator<KV> CREATOR = new Parcelable.Creator<KV>() {

		@Override
		public KV createFromParcel(Parcel in) {
			return new KV(in.readString(), in.readString());
		}

		@Override
		public KV[] newArray(int size) {
			return new KV[size];
		}
	};
	
	/**
	 * 将Json转换为List
	 * @param jsonStr   json数据，不能包含对象，只能是基本类型
	 * @return
	 */
	public static ArrayList<KV> toList(String jsonStr) {
		ArrayList<KV> kvs = new ArrayList<KV>();
		
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				String value = jsonObject.getString(key);
				kvs.add(new KV(key, value));
			}
		} catch (Exception e) {
			DebugLog.e(TAG, "toList()", e);
		}
		
		return kvs;
	}
	
	/**
	 * 将键值对集合转换为Json
	 * @param kvs   键值对集合
	 * @return
	 */
	public static JSONObject toJson(List<KV> kvs) {
		JSONObject jsonObject = new JSONObject();
		
		if (kvs != null && !kvs.isEmpty()) {
			for (KV kv : kvs) {
				try {
					jsonObject.put(kv.getKey(), kv.getValue());
				} catch (JSONException e) {
					DebugLog.e(TAG, "toJson()", e);
				}
			}
		}
		
		return jsonObject;
	}
	
	/**
	 * 将键值对集合转换为Map
	 * @param kvs   键值对集合
	 * @return
	 */
	public static HashMap<String, String> toHashMap(List<KV> kvs) {
		HashMap<String, String> map = new HashMap<String, String>();
		
		if (kvs != null && !kvs.isEmpty()) {
			for (KV kv : kvs) {
				map.put(kv.getKey(), kv.getValue());
			}
		}
		
		return map;
	}
}
