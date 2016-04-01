package com.fpliu.newton.framework.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;

/**
 * 使用Fragment的Activity的基类
 * 
 * @author 792793182@qq.com 2014-9-22
 * 
 */
public class BaseFragmentActivity extends FragmentActivity {

	private static final String TAG = BaseFragmentActivity.class.getSimpleName();
	
	private FragmentMediator mFragmentMediator;

	private RelativeLayout contentView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//无论如何改变系统字体大小，我们的程序不受影响
		UIUtil.remainFont(this);
		
		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
		int bgColor = getResources().getColor(R.color.bg_color);
		window.setBackgroundDrawable(new ColorDrawable(bgColor));
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		try {
			super.onCreate(savedInstanceState);
		} catch (Exception e) {
			DebugLog.e("BaseFragmentActivity", "onCreate()", e);
		}

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver, intentFilter);
		
		mFragmentMediator = new FragmentMediator();

		// 根容器
		contentView = new RelativeLayout(this) {

			@Override
			protected void onSizeChanged(int w, final int h, int ow, int oh) {
				super.onSizeChanged(w, h, ow, oh);
				DebugLog.e("onGlobalLayout()",
						"base.onSizeChanged begin, h is " + h + " ,oh is " + oh);
				if (oh > 0) {
					// 100这个值不可随便修改
					int minHeight = UIUtil.dip2px(getContext(), 100);
					if (minHeight < 200) {
						minHeight = 200;
					}
					int dalta = h - oh;
					DebugLog.e("onGlobalLayout()", "dalta= " + dalta + " ,minHeight= " + minHeight);

					if (dalta <= -minHeight) {
						onSoftInputMethodChange(true);
					} else if (dalta >= minHeight) {
						onSoftInputMethodChange(false);
					}
				}
			}
			
			@Override
			protected void onDetachedFromWindow() {
				super.onDetachedFromWindow();
				
				removeAllViews();
			}
		};

		contentView.setId(BaseFragment.ID_CONTENT_VIEW);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(contentView);
	}
	
	public RelativeLayout getContentView() {
		return contentView;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		try {
			unregisterReceiver(receiver);
		} catch (Exception e) {
			DebugLog.e(TAG, "", e);
		}
	}
	
	// 此方法一定会被调用
	@Override
	public void finish() {
		super.finish();
		
		mFragmentMediator.destroy();
	}

	public FragmentMediator getFragmentMediator() {
		return mFragmentMediator;
	}

	/**
	 * 软键盘弹出或者隐藏的回调
	 * 
	 * @param isShowing
	 *            软键盘是否显示
	 */
	protected void onSoftInputMethodChange(boolean isShowing) {
		
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		FragmentMediator mediator = getFragmentMediator();
		if (!mediator.isEmpty()) {
			mediator.peek().dispatchKeyEvent(event);
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean intercepted = false;
		FragmentMediator mediator = getFragmentMediator();
		if (!mediator.isEmpty()) {
			intercepted = mediator.peek().onKeyDown(keyCode, event);
			if (intercepted) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		FragmentMediator mediator = getFragmentMediator();
		if (!mediator.isEmpty()) {
			mediator.peek().onKeyUp(keyCode, event);
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean needDispatch = false;
		FragmentMediator mediator = getFragmentMediator();
		if (!mediator.isEmpty()) {
			needDispatch = mediator.peek().dispatchTouchEvent(ev);
		}
		
		if (needDispatch) {
			//MIUI2S上会出现崩溃现象
			try {
				return super.dispatchTouchEvent(ev);
			} catch (Exception e) {
				DebugLog.e(TAG, "", e);
			}
		}
		
		return needDispatch;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean handled = false;
		
		FragmentMediator mediator = getFragmentMediator();
		if (!mediator.isEmpty()) {
			handled = mediator.peek().onTouchEvent(event);
		}
		
		if (!handled) {
			//这里曾经出现过崩溃的现象，所以加上异常捕获
			try {
				return super.onTouchEvent(event);
			} catch (Exception e) {
				DebugLog.e(TAG, "", e);
			}
		}
		
		return handled;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		FragmentMediator mediator = getFragmentMediator();
		if (!mediator.isEmpty()) {
			mediator.peek().onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
				FragmentMediator fragmentMediator = getFragmentMediator();
				if (!fragmentMediator.isEmpty()) {
					fragmentMediator.peek().onNetworkChange(Environment.getInstance().isNetworkAvailable());
				}
			}
		}
	};
}
