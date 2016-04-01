package com.fpliu.newton;

import java.io.File;

import android.app.Application;
import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.setting.ISetting;
import com.fpliu.newton.base.setting.SettingFactory;
import com.fpliu.newton.config.ConfigFactory;
import com.fpliu.newton.config.IConfig;
import com.fpliu.newton.framework.API;
import com.fpliu.newton.framework.lbs.LBS;
import com.fpliu.newton.framework.upload.UploadManager;
import com.fpliu.newton.framework.util.ProcessUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * 应用程序入口
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class MyApp extends Application {

	private static final String TAG = MyApp.class.getSimpleName();
	
	private static MyApp mInstance;

	private volatile ISetting mSetting;

	private IConfig config;
	
	/** 腾讯开放平台接口 */
	private Tencent mTencent;
	
	/** 微信开放平台接口 */
	private IWXAPI wxapi;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		/**
         * 获取进程名称，每个进程启动都会进入该方法
         * 总共有3个进程，他们的名称分别为：
         *            PACKAGE_NAME                 主进程
         *            PACKAGE_NAME + ":remote"     百度定位进程
         *            PACKAGE_NAME + ":background" 后台进程
         */
        String processName = ProcessUtil.getCurrentProcessName(this);
        DebugLog.d(TAG, "processName = " + processName);
        
        //如果是百度定位的进程，就不做任何处理
        if ((getPackageName() + ":remote").equals(processName)) {
            return;
        }
        
		//加载动态库
        System.loadLibrary("newton");
        
		mInstance = this;
		
		//百度地图初始化
		SDKInitializer.initialize(this);
		
		//创建腾讯开放平台SDK接口实例
		mTencent = Tencent.createInstance(e(), this);
		
		//创建微信开放平台SDK接口实例
		wxapi = WXAPIFactory.createWXAPI(this, b(), true);
		//注册到微信
		wxapi.registerApp(b());

        config = ConfigFactory.newInstance();
		
		// 初始化环境
		Environment.getInstance().init(this);

		// 初始化定位服务
		LBS.getInstance().init(this);
		
		//需要网络
		UploadManager.getInstance().init();
		
		// 收集crash信息的
		DebugLog.registerUncaughtExceptionHandler(this);

		File cacheDir = StorageUtils.getCacheDirectory(this);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.discCacheExtraOptions(720, 1024, Bitmap.CompressFormat.PNG,
						100, null).discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.build();
		ImageLoader.getInstance().init(config);
		
		API.getInstance().init(this);
	}

	public static MyApp getApp() {
		return mInstance;
	}

	public ISetting getSetting() {
		if (mSetting == null) {
			synchronized (MyApp.class) {
				if (mSetting == null) {
					mSetting = SettingFactory.newInstance(this, "setting");
				}
			}
		}
		return mSetting;
	}

    public IConfig getConfig() {
        return config;
    }

    public Tencent getTencent() {
		return mTencent;
	}
	
	public IWXAPI getWXAPI() {
		return wxapi;
	}
	
	/**
	 * native方法不能混淆，为了避免反编译后容易被看出用途，故意采用随意的函数名
	 * 获取对称加密解密的key：
	 */
	public native String a();
	
	/**
	 * 微信开放平台的appKey：cccccc
	 */
	public native String b();
	
	/**
	 * 微信开放平台的appSecret：ccccc
	 */
	public native String c();
	
	/**
	 * 新浪微博SDK的appKey：ccccc
	 */
	public native String d();
	
	/**
	 * 腾讯开放平台的appKey：   222222
	 */
	public native String e();
	
	/**
	 * 腾讯云分析（统计分析SDK）Release版本的appKey：cccccc
	 */
	public native String f();
	
	/**
	 * 腾讯云分析（统计分析SDK）Debug版本的的appKey：cccccc
	 */
	public native String g();
	
	/**
	 * 
	 */
	public native String h();
}
