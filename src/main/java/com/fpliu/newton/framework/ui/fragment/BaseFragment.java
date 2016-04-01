package com.fpliu.newton.framework.ui.fragment;

import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.toast.CustomToast;
import com.fpliu.newton.framework.util.ClickChecker;
import com.tencent.stat.StatService;

/**
 * Fragment的基类
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public abstract class BaseFragment extends Fragment implements Callback {

	//此值以0x6f开头，是为了避免与ADT自动生成的ID冲突
	public static final int ID_CONTENT_VIEW = 0x6f0b0189;
	
	public static final int ID_TITLE_BAR = 0x6f0b0190;
	
	public static final int ID_SHARE_BTN = 0x6f0b0191;
	
	
	private RelativeLayout mRootView;
    // 标题栏
    private RelativeLayout mTitleBar;
    // 标题栏中的文字
    private TextView mTitleText;
    // 标题栏左边的返回按钮
    private ImageView mBackBtn;

    // 标题栏右边的功能按钮
    private ImageView mRightBtn;
    
    private FragmentMediator mFragmentMediator;
    
    //返回前一个界面时，返回的数据
    private Bundle resultBundle;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        DebugLog.d(getClass().getSimpleName(), "onCreate() savedInstanceState = " + savedInstanceState);
        
        if (savedInstanceState == null) {
        	//统计页面进入次数功能
    		Properties properties = new Properties();
    		properties.put("which", getClass().getSimpleName());
    		properties.put("os_version", Environment.getInstance().getOSVersionName());
    		properties.put("phone_model", Environment.getInstance().getPhoneModel());
    		StatService.trackCustomKVEvent(getActivity(), "page_enter_times", properties);
    		
    		Properties properties2 = new Properties();
    		properties2.put("which", getClass().getSimpleName());
    		properties2.put("os_version", Environment.getInstance().getOSVersionName());
    		properties2.put("phone_model", Environment.getInstance().getPhoneModel());
    		StatService.trackCustomBeginKVEvent(getActivity(), "page_remain_time", properties2);
		}
    }

    @Override
    public RelativeLayout onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
    	DebugLog.d(getClass().getSimpleName(), "onCreateView()");
    	
    	Context context = getActivity();
    	
    	mRootView = new RelativeLayout(context);
    	mRootView.setClickable(true);
    	mRootView.setBackgroundResource(R.color.bg_color);
    	mRootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    	if (needAnimation()) {
    		mRootView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.push_left_in));
		}
    	
    	UIUtil.hideSoftInput(context, mRootView);
    	
    	return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DebugLog.d(getClass().getSimpleName(), "onResume()");
    }
    
    @Override
    public void onStop() {
        super.onStop();
        DebugLog.d(getClass().getSimpleName(), "onStop()");
    }

    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	DebugLog.d(getClass().getSimpleName(), "onDestroy()");
    	
    	setFragmentResult(resultBundle);
    	
    	if (mRootView != null) {
    		mRootView.removeAllViews();
    		mRootView = null;
		}
    	
    	if (null != mTitleBar) {
    		mTitleBar.removeAllViews();
    		mTitleBar = null;
		}
    	
    	mBackBtn = null;
    	mRightBtn = null;
    	mTitleText = null;
    	mFragmentMediator = null;
    	
    	Properties properties = new Properties();
		properties.put("which", getClass().getSimpleName());
		properties.put("os_version", Environment.getInstance().getOSVersionName());
		properties.put("phone_model", Environment.getInstance().getPhoneModel());
		StatService.trackCustomEndKVEvent(getActivity(), "page_remain_time", properties);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	DebugLog.d(getClass().getSimpleName(), "onSaveInstanceState()");
    }
    
    protected void setTitleText(CharSequence text) {
        setTitleBarVisible(true);
        if (mTitleText != null) {
        	mTitleText.setText(text);
		}
    }
    
    protected void setTitleText(int id) {
        setTitleBarVisible(true);
        String title = getActivity().getResources().getString(id);
        if (mTitleText != null) {
        	mTitleText.setText(title);
		}
    }


    protected void setTitleBarVisible(boolean visible) {
    	if (mTitleBar != null) {
    		mTitleBar.setVisibility(visible ? View.VISIBLE : View.GONE);
		}
	}
    
    protected void setTitleBarBg(Drawable bg) {
    	if (mTitleBar != null) {
    		mTitleBar.setBackgroundDrawable(bg);
    	}
    }
    
    protected RelativeLayout getTitleBar() {
		return mTitleBar;
	}
    
    /**
     * 返回按钮的点击事件处理
     * @param v 返回按钮
     */
    protected void onClickBackButton(View v) {
    	if (getFragmentMediator().getCount() == 1) {
			finish(false);
		} else {
			finish();
		}
    }
    
    public final void removeFragment(BaseFragment fragment) {
    	if (mFragmentMediator != null) {
    		mFragmentMediator.removeFragment(getActivity(), fragment);
    	}
	}
    
    public final void addFragment(BaseFragment fragment) {
    	addFragment(fragment, false);
	}
    
    public final void addFragment(BaseFragment fragment, boolean needFinishCurrentFragment) {
    	if (mFragmentMediator != null) {
			mFragmentMediator.addFragment(getActivity(), fragment, needFinishCurrentFragment);
		}
	}
    
    public final void replaceFragment(BaseFragment fragment) {
    	if (mFragmentMediator != null) {
    		mFragmentMediator.replaceFragment(getActivity(), fragment);
    	}
	}
    
    /**
     * 带有标题栏
     * @param view
     */
    protected final void addContentView(View view, RelativeLayout.LayoutParams layoutParams) {
    	mRootView.removeAllViews();
    	
		Context context = getActivity();
    	
		mTitleBar = new RelativeLayout(context);
    	mTitleBar.setId(ID_TITLE_BAR);
    	mTitleBar.setPadding(0, 0, 0, 0);
    	mTitleBar.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtil.dip2px(context, 45)));
    	mTitleBar.setBackgroundResource(R.drawable.bg_head);
    	//mTitleBar.setBackgroundResource(R.color.title_bg_color);
    	
    	mTitleText = new TextView(context);
    	mTitleText.setFocusable(false);
    	mTitleText.setTextColor(Color.WHITE);
    	mTitleText.setTextSize(20);
        mTitleText.setBackgroundColor(Color.TRANSPARENT);
        
    	mBackBtn = new ImageView(context);
    	mBackBtn.setScaleType(ScaleType.CENTER_INSIDE);
    	mBackBtn.setImageDrawable(context.getResources().getDrawable(R.drawable.back_page));
    	mBackBtn.setBackgroundResource(R.drawable.button_focused);
    	mBackBtn.setOnClickListener(new View.OnClickListener() {
			
    		private ClickChecker clickChecker = new ClickChecker();
    		
			@Override
			public void onClick(View v) {
				if (!clickChecker.isClickTooMuch()) {
					onClickBackButton(v);
				}
			}
		});
    	
    	 mRightBtn = new ImageView(context);
         mRightBtn.setScaleType(ScaleType.CENTER_INSIDE);
         mRightBtn.setBackgroundResource(R.drawable.button_focused);
         mRightBtn.setVisibility(View.GONE);
         mRightBtn.setImageResource(R.drawable.next_page);
         mRightBtn.setOnClickListener(new View.OnClickListener() {
        	
    		private ClickChecker clickChecker = new ClickChecker();
    		
			@Override
			public void onClick(View v) {
				if (!clickChecker.isClickTooMuch()) {
					onClickRightButton(v);
				}
			}
		});
        
    	RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mTitleBar.addView(mTitleText, layoutParams1);
    	
    	RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(UIUtil.dip2px(context, 40), LayoutParams.MATCH_PARENT);
    	layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL);
    	mTitleBar.addView(mBackBtn, layoutParams2);
    	
    	RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(UIUtil.dip2px(context, 40), LayoutParams.MATCH_PARENT);
    	layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    	mTitleBar.addView(mRightBtn, layoutParams3);
    	
    	mRootView.addView(mTitleBar);
    	layoutParams.addRule(RelativeLayout.BELOW, ID_TITLE_BAR);
    	mRootView.addView(view, layoutParams);
	}
    
    protected final void addContentView(View view) {
    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	addContentView(view, layoutParams);
    }
    
    protected final View findViewById(int id) {
    	return mRootView.findViewById(id);
    }
    
    /**
     * 不带标题栏
     * @param view
     */
    protected final void setContentView(View view) {
    	mRootView.removeAllViews();
    	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	mRootView.addView(view, layoutParams);
    }
    /**
     * 不带标题栏
     * @param view
     */
    protected final void appendView(View view, RelativeLayout.LayoutParams layoutParams) {
    	mRootView.addView(view, layoutParams);
    }
    
    public void setWindowBackground(Drawable drawable) {
    	mRootView.setBackgroundDrawable(drawable);
    }
    

    public void setRightButton(int visibility) {
    	mRightBtn.setVisibility(visibility);
    }

    public void addViewInTitleBar(View view, RelativeLayout.LayoutParams lp) {
		if (mTitleBar != null) {
			mTitleBar.addView(view, lp);
		}
	}
    
    public ImageView addBtnInTitleBarRight() {
    	Activity activity = getActivity();
    	// 添加分组按钮
    	ImageView shareBtn = new ImageView(activity);
		shareBtn.setId(ID_SHARE_BTN);
		shareBtn.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_share_white));
		shareBtn.setPadding(UIUtil.dip2px(activity, 5), 0, UIUtil.dip2px(activity, 5), 0);
		shareBtn.setBackgroundResource(R.drawable.button_focused);
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(UIUtil.dip2px(activity, 40), LayoutParams.MATCH_PARENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp2.addRule(RelativeLayout.CENTER_VERTICAL);
		addViewInTitleBar(shareBtn, lp2);
		
		return shareBtn;
	}
    
    public void hideTitleBackBtn() {
		if (mBackBtn != null) {
			mBackBtn.setVisibility(View.GONE);
		}
	}
    
    public void showTitleBackBtn() {
		if (mBackBtn != null) {
			mBackBtn.setVisibility(View.VISIBLE);
		}
	}

    public void onClickRightButton(View v) {
		
	}
    
    /**
     * 后一个Fragment销毁时候，给前一个Fragment返回结果
     * @param bundle 结果的数据
     */
    private void setFragmentResult(Bundle bundle) {
    	DebugLog.d(getClass().getSimpleName(), "setFragmentResult()");
    	if (mFragmentMediator != null && !mFragmentMediator.isEmpty()) {
    		if (mFragmentMediator.isTop(this)) {
    			mFragmentMediator.pop();
    			if (!mFragmentMediator.isEmpty()) {
        			BaseFragment parentFragment = mFragmentMediator.peek();
                	if (parentFragment != null) {
                		try {
                			parentFragment.onFragmentResult(bundle);
						} catch (Exception e) {
							DebugLog.e(BaseFragment.class.getSimpleName(), "setFragmentResult()", e);
						}
        			}
    			}
			}
		}
	}
    
    protected void onFragmentResult(Bundle bundle) {
    	DebugLog.d(getClass().getSimpleName(), "onFragmentResult()");
    }
    
    protected final void setResult(Bundle bundle) {
    	resultBundle = bundle;
	}
    
    public final FragmentMediator getFragmentMediator() {
		return mFragmentMediator;
	}
    
    public boolean dispatchKeyEvent(KeyEvent event) {
    	DebugLog.d(getClass().getSimpleName(), "dispatchKeyEvent()");
    	return false;
    }
    
    private ClickChecker clickChecker = new ClickChecker();
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	DebugLog.d(getClass().getSimpleName(), "onKeyDown()");
    	if (keyCode == KeyEvent.KEYCODE_BACK) {
    		if (clickChecker.isClickTooMuch()) {
				return true;
			}
    		if (getFragmentMediator().getCount() == 1) {
				finish(false);
			} else {
				finish();
			}
    		
    		return true;
		}
    	return false;
    }
    
    public boolean onKeyUp(int keyCode, KeyEvent event) {
    	DebugLog.d(getClass().getSimpleName(), "onKeyUp()");
    	return false;
    }
    
	public boolean dispatchTouchEvent(MotionEvent ev) {
		DebugLog.d(getClass().getSimpleName(), "dispatchTouchEvent()");
		return true;
	}
    
	public boolean onTouchEvent(MotionEvent event) {
		DebugLog.d(getClass().getSimpleName(), "onTouchEvent()");
		return false;
	}
	
	public void finish() {
		finish(true);
	}
	
	public void finish(boolean needAnimation) {
		DebugLog.d(getClass().getSimpleName(), "finish()");
		
		if (isFinished()) {
			return;
		}
		
		final Activity activity = getActivity();
		
		hideSoftInput(activity);
		
		if (needAnimation) {
			Animation animation = AnimationUtils.loadAnimation(activity, R.anim.back_right_out);
			animation.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					animation.setAnimationListener(null);
					try {
						activity.onBackPressed();
					} catch (Exception e) {
						DebugLog.e(BaseFragment.class.getSimpleName(), "finish()", e);
					}
				}
			});
			mRootView.startAnimation(animation);
		} else {
			try {
				activity.onBackPressed();
			} catch (Exception e) {
				DebugLog.e(BaseFragment.class.getSimpleName(), "finish()", e);
			}
		}
	}
	
	BaseFragment setMediator(FragmentMediator mediator) {
    	mFragmentMediator = mediator;
    	return this;
	}
	
	protected boolean needAnimation() {
		return true;
	}
	
	public void onNetworkChange(boolean isNetworkAvailabe) {
		DebugLog.d(getClass().getSimpleName(), "onNetworkChange() isNetworkAvailabe = " + isNetworkAvailabe);
	}
	
	protected final void hideSoftInput(Activity activity) {
		if (activity != null) {
			UIUtil.hideSoftInput(activity, activity.getCurrentFocus());
		}
	}
	
	public RelativeLayout getRootView() {
		return mRootView;
	}
	
	public boolean isFinished() {
		return getActivity() == null;
	}
	
	protected final void postMessageDelay(int what, Object object, long delayMillis) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = object;
		uiHandler.sendMessageDelayed(msg, delayMillis);
	}
	
	protected final void postMessage(int what, Object object) {
		Message.obtain(uiHandler, what, object).sendToTarget();
	}
	
	protected final void post(Runnable runnable) {
		uiHandler.post(runnable);
	}
	
	/**
	 * 在线程中也可以使用
	 */
	public final void postShowToast(String text) {
		postMessage(MSG_SHOW_TOAST, text);
	}
	
	/**
	 * 在线程中也可以使用
	 */
	public final void postShowToast(int textId) {
		
		try {
			postMessage(MSG_SHOW_TOAST,MyApp.getApp().getResources().getString(textId));
		} catch (Exception e) {
			DebugLog.e(getClass().getSimpleName(), "showToast()", e);
		}
	}
	
	
	/**
	 * 在线程中也可以使用
	 */
	protected final void postFinish() {
		postMessage(MSG_FINISH, null);
	}
	
	/**
	 * 只能在主线程中使用
	 */
	public final void showToast(String text) {
		if (!TextUtils.isEmpty(text)) {
			CustomToast.makeText(MyApp.getApp(), text, 2000).show();
		}
	}
	
	public final void showToast(int textId) {
		try {
			showToast(MyApp.getApp().getResources().getString(textId));
		} catch (Exception e) {
			DebugLog.e(getClass().getSimpleName(), "showToast()", e);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
	
	private static final int MSG_SHOW_TOAST = 1000;
	private static final int MSG_FINISH = 1001;
	
	private Handler uiHandler = new Handler(this) {
		
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHOW_TOAST:
				showToast((String) msg.obj);
				break;
			case MSG_FINISH:
				finish();
				break;
			default:
				break;
			}
		}
	};
}
