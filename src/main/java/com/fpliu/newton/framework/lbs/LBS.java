package com.fpliu.newton.framework.lbs;

import java.util.Observable;
import java.util.Observer;

import android.content.Context;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;

/**
 * 获取手机的经纬度和地理描述
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class LBS implements Observer {
	
	private static final String TAG = LBS.class.getSimpleName();
	
	private static final String KEY_XADDRESS_FILE_PATH = "/.xaddress";
	
	/** 两次定位的时间间隔2分钟 */
	private static final long INTERVAL_TIME = 2 * 60 * 1000;
	
	/** 位置描述信息数据结构 */
	private XAddress mAddress;
    
    /** 百度定位服务代理 */
	private BaiduLocateManager mBaiduLocateManager;
	
	private LocatListener mLocatListener;
	
	/** 一次定位使用时间 */
	private long mStartTime;
	
	/** 一次定位使用时间 */
	private long mLocatingTime;
	
	/** 定位成功的标志 */
	private boolean mIsSuccess;
	
	private static final class InstanceHolder {
		private static LBS instance = new LBS();
	}
	
	private LBS() { }
	
	/** 只能调用一次 */
	public void init(Context context) {
		//百度定位必须在主线程中初始化
		mBaiduLocateManager = new BaiduLocateManager(context);
		mBaiduLocateManager.addObserver(this);
	}
	
	public static LBS getInstance() {
		return InstanceHolder.instance;
	}

	public void registerObserever(Observer observer) {
		if (observer != null && mBaiduLocateManager != null) {
			mBaiduLocateManager.addObserver(observer);
		}
	}
	
	public void unRegisterObserever(Observer observer) {
		if (observer != null && mBaiduLocateManager != null) {
			mBaiduLocateManager.deleteObserver(observer);
		}
	}
	
	public void locating() {
		locating(true);
	}
	
	/**
	 * 定位请求
	 * @param force 是否需要比较上次定位的时间差，如果false，超过时间差才会定位，否则不定位，如果true，直接定位
	 */
	public void locating(boolean force) {
		DebugLog.d(TAG, "------------>>> locating");
		
		long now = System.currentTimeMillis();
		
		//如果上一次定位是失败的，这次就强制定位
		if (!mIsSuccess) {
			force = true;
		}
		
		if (!force) {
			//如果两次定位时间间隔小于1分钟就不做任何处理
			long detla = now - mStartTime;
			if (detla > 0 && detla < INTERVAL_TIME) {
				return;
			}
		}
		
		mStartTime = now;
		
		//先读取缓存，将旧的数据填进来，然后用百度定位
		mAddress = getLastKnownAddress();  
		DebugLog.d(TAG, "------------>>> get address from permanent cache : " + mAddress);
		
		mLocatingTime = System.currentTimeMillis();
		
		//只有连了网才去定位
		if (Environment.getInstance().isNetworkAvailable()) {   
			DebugLog.d(TAG, "---------------->>> Network is Connected!");
			mBaiduLocateManager.start();
		} else {
			DebugLog.d(TAG, "---------------->>> Network is not Connected!");
			
			mIsSuccess = false;
			
			//没有网络的情况下也要通知观察者
//			update(null, null);
			// 通过观察者来更新
			mBaiduLocateManager.update(null);
		}
	}
	
	/**
	 * 定位成功还是失败，如果失败就使用缓存
	 */
	public boolean isLocatingSuccess() {
		return mIsSuccess;
	}
	
    public XAddress getAddress() {
        return mAddress;
    }
    
    /**
     * 当前定位任务是否运行中
     * @return
     */
    public boolean isLocating() {
    	return mBaiduLocateManager.isStarted();
    }
    
    /**
     * 设置定位结束回调
     * @param listener
     */
    public void setLoactListener(LocatListener listener) {
        mLocatListener = listener;
    }
    
	/**
	 * 获取缓存中的上一次获取到的位置信息
	 * @return 位置描述信息数据
	 */
	private static XAddress getLastKnownAddress() {
		return (XAddress) MyApp.getApp().getSetting().readObject(Environment.getInstance().getMyDir() + KEY_XADDRESS_FILE_PATH);
	}
	
	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof XAddress) {
			mIsSuccess = true;
			XAddress address = (XAddress) data;
			address.setTimeStamp(System.currentTimeMillis());
			address.setFromCache(true);
			
			MyApp.getApp().getSetting().saveObject(Environment.getInstance().getMyDir() + KEY_XADDRESS_FILE_PATH, data);
			
			address.setFromCache(false);
			mAddress = address;
			
			DebugLog.d(TAG, "----------->>> save address success! mAddress= " + mAddress);
		} else {
			mIsSuccess = false;
			DebugLog.d(TAG, "-------------->>>定位失败");
		}

		DebugLog.d(TAG,"----------Locat finish time=" + (System.currentTimeMillis() - mLocatingTime));
		
		if (null != mLocatListener) {
			mLocatListener.onLocatFinish();
			mLocatListener = null;
		}
	}
	
	/** 
	 * 定位Listener
	 */
	public interface LocatListener {
		
        public void onLocatFinish();
    }
}
