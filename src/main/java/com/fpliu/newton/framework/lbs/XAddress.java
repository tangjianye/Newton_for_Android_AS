package com.fpliu.newton.framework.lbs;

import java.io.Serializable;

/**
 * 物理位置地址
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class XAddress implements Serializable {

	private static final long serialVersionUID = 2099232884486157654L;

	private String mLatitude; // 纬度
	private String mLongitude; // 经度

	private String mAddressName;// 详细地址名称
	private String mCountry; // 国家
	private String mProvince; // 省
	private String mArea; // 区
	private String mCity; // 城市
	private String mStreet;// 街道
	private String mCityCode; // 城市区号
	private long mTimeStamp; // 获取到位置的时间戳
	private boolean mIsFromCache;

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		this.mCountry = country;
	}

	public String getProvince() {
		return mProvince;
	}

	public void setProvince(String province) {
		this.mProvince = province;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public void setArea(String mArea) {
		this.mArea = mArea;
	}

	public String getArea() {
		return mArea;
	}

	public String getStreet() {
		return mStreet;
	}

	public void setStreet(String street) {
		this.mStreet = street;
	}

	public String getAddressName() {
		return mAddressName;
	}

	public void setAddressName(String addressName) {
		this.mAddressName = addressName;
	}

	public String getLatitude() {
		return mLatitude;
	}

	public void setLatitude(String latitude) {
		this.mLatitude = latitude;
	}

	public String getLongtitude() {
		return mLongitude;
	}

	public void setLongtitude(String longtitude) {
		this.mLongitude = longtitude;
	}

	public String getCityCode() {
		return mCityCode;
	}

	public void setCityCode(String cityCode) {
		this.mCityCode = cityCode;
	}

	public boolean isFromCache() {
		return mIsFromCache;
	}

	public void setFromCache(boolean isFromCache) {
		this.mIsFromCache = isFromCache;
	}

	public long getTimeStamp() {
		return mTimeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.mTimeStamp = timeStamp;
	}

	/**
	 * 覆写此方法是为了测试方便 由eclipse自动生成的
	 */
	@Override
	public String toString() {
		return "XAddress [mLatitude=" + mLatitude + ", mLongitude="
				+ mLongitude + ", mAddressName=" + mAddressName + ", mCountry="
				+ mCountry + ", mProvince=" + mProvince + ", mArea=" + mArea
				+ ", mCity=" + mCity + ", mStreet=" + mStreet + ", mCityCode="
				+ mCityCode + ", mTimeStamp=" + mTimeStamp + ", mIsFromCache="
				+ mIsFromCache + "]";
	}

}
