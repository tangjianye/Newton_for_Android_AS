package com.fpliu.newton.business.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.fpliu.newton.BackgroudService;
import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.business.account.UserManager;
import com.fpliu.newton.business.config.Configuration;
import com.fpliu.newton.framework.util.AlertWakeLock;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

/**
 * 闪屏，第一个界面
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public final class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//设置统计SDK的Log开关
		StatConfig.setDebugEnable(DebugLog.ENABLED);
		
		//设置统计SDK的渠道号
		StatConfig.setInstallChannel(Configuration.CHANNEL_ID);
		
		String appKey = (StatConfig.isDebugEnable() ? MyApp.getApp().g() : MyApp.getApp().f());
		//启动统计服务
		StatService.startStatService(this, appKey, com.tencent.stat.common.StatConstants.VERSION);
		
		getWindow().setFormat(PixelFormat.RGBA_8888);
		// 无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.start_up_activity);

		AlertWakeLock.acquire(this);

		ImageView infoOperatingIV = (ImageView) findViewById(R.id.infoOperating);
		Animation operatingAnim = AnimationUtils.loadAnimation(this,
				R.anim.rotate);
		if (operatingAnim != null && infoOperatingIV != null) {
			infoOperatingIV.startAnimation(operatingAnim);
			operatingAnim.setInterpolator(new LinearInterpolator());
		}

		// 延迟2000毫秒启动主界面
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, Home.class));
				finish();
				AlertWakeLock.release();
			}
		}, 1500);

		// 检测自动登录
		UserManager.getInstance().autoLogin();
		
		//启动后天进程
		startService(new Intent(this, BackgroudService.class));
	}

	@Override
	public void finish() {
		super.finish();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				findViewById(R.id.StartUpLayout).setBackgroundDrawable(null);
			}
		}, 2000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
}