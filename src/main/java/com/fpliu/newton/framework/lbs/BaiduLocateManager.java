package com.fpliu.newton.framework.lbs;

import java.util.Observable;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;

/**
 * 百度定位
 * 有关接口文档在下面的地址：http://developer.baidu.com/map/geosdk-android.htm
 * 
 * @author 792793182@qq.com 2014-9-22
 */
final class BaiduLocateManager extends Observable {
	
	private final String TAG = BaiduLocateManager.class.getSimpleName();
	
	private LocationClient mLocationClient;
	private MyLocationListenner mLocationListenner;
	
	BaiduLocateManager(Context context) {
		mLocationListenner = new MyLocationListenner();
		mLocationClient = new LocationClient(context);
		mLocationClient.registerLocationListener(mLocationListenner);
	}
	
	/**
	 * 开始定位，第一次使用
	 */
	public void start() {
		//如果不是正在定位，才去定位
		if (null != mLocationClient && !mLocationClient.isStarted()) { 
			setLocationOption();
		    mLocationClient.start(); 
		    DebugLog.d(TAG, "------------>>> start()");
		} else {
			DebugLog.d(TAG, "------------>> 正在定位中");
		}
	}
	
	/**
	 * 停止定位
	 */
	public void stop() {
		DebugLog.d(TAG, "------------>>> stop()");
		
		if (null != mLocationClient && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}
	
	/**
	 * 一次定位之后，再来一次定位过程
	 */
	public void requestLocation() {
		if (null != mLocationClient && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		}
	}
	
	/**
	 * 设置参数
	 */
	private void setLocationOption(){
		DebugLog.d(TAG, "------------>>> setLocationOption()");
		
    	if (null == mLocationClient) {
    		return ;
    	}
    		
		LocationClientOption option = new LocationClientOption();
		option.setAddrType("all");	  //返回的定位结果包含地址信息
//		option.setAddrType("detail"); //返回地理的信息类型——详细
		option.setOpenGps(false);     //不打开GPS模块
		option.setCoorType("bd09ll"); //返回的定位结果是百度经纬度bd09ll,默认值gcj02
		option.setScanSpan(1000*60*3);	  //扫描时间间隔
//		option.disableCache(true);	  //禁止启用缓存定位
//		option.setPriority(LocationClientOption.NetWorkFirst);	//网络优先
	    option.setProdName("iiiiiiiiii");//产品线名
/*	    option.setPoiNumber(5);		  //最多返回POI个数	
	    option.setPoiDistance(1000);  //poi查询距离		
	    option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息
*/		mLocationClient.setLocOption(option);
    }
	
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			DebugLog.d(TAG, "------------>>> onReceiveLocation()");
			
			//如果此时没有网络了，就停止掉
			//虽然开始的时候判断了网络，但是网络有可能变化
			if (!Environment.getInstance().isNetworkAvailable()) {
				stop();
				update(null);
				return;
			}
			
			if (null == location) { 
				return;
			} else {
				if (location.getLocType() == BDLocation.TypeCriteriaException
						|| location.getLocType() == BDLocation.TypeNetWorkException
						|| location.getLocType() == BDLocation.TypeServerError
						|| location.getLocType() == BDLocation.TypeNone) { 
					return;
				}
			}
			
			final XAddress address = new XAddress();
			
			double latitude = location.getLatitude();
			if (0 != latitude) {
				address.setLatitude("" + latitude);
			}
			
			double longitude = location.getLongitude();
			if (0 != longitude) {
				address.setLongtitude("" + longitude);
			}
			
			String province = location.getProvince();
			if (null != province && !"".equals(province.trim())) {
				address.setProvince(province);
			}
			
			String city = location.getCity();
			if (null != province && !"".equals(province.trim())) {
				address.setCity(city);
			}
			
			String district = location.getDistrict();
			if (null != province && !"".equals(province.trim())) {
				address.setArea(district);
			}
			
			String street = location.getStreet();
			if (null != province && !"".equals(province.trim())) {
				address.setStreet(street);
			}
			
			String addressName = location.getAddrStr();
			if (null != addressName && !"".equals(addressName.trim())) {
				address.setAddressName(addressName);
			}
			
			if ((0 != latitude) && (0 != longitude) && (null != addressName && !"".equals(addressName.trim()))) {
				update(address); 
				//stop();
			} else { 
				return;
			}
			
/*			if ((0 != longitude) && (0 != longitude) && (null != addressName && !"".equals(addressName.trim()))) {
			    ThreadPoolManager.EXECUTOR.execute(new Runnable() {	                
	                @Override
	                public void run() {
	                    splitAddressName(address);
	                }
	            });
			} else { 
				return;
			}*/
		}

		/*@Override
		public void onReceivePoi(BDLocation arg0) {
			
		} */
	}

    public boolean isStarted() { 
        return mLocationClient.isStarted();
    }
	
	/**
	 * 更新结果，通知给观察者
	 */
	public void update(Object data) {
		setChanged();
		notifyObservers(data); 
	}
}
