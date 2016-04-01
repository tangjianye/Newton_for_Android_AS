package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;

import com.fpliu.newton.base.DebugLog;

/**
 * 自定义弹出框，可以设置背景的模糊效果
 * 
 * @author 792793182@qq.com 2014-12-19
 *
 */
public class CustomDialog extends Dialog {

	private static final String TAG = CustomDialog.class.getSimpleName();

	private View contentView;
		
	//是否是正在显示，因为显示的时候有动画过程
	private boolean isShowing;
	
	//是否是正在消失，因为显消失的时候有动画过程
	private boolean isDismissing;

	private int width;
	
	private int heidht;
	
	private float dimAmount;
	
	private Activity activity;
	
	public CustomDialog(Activity activity) {
		this(activity, android.R.style.Theme_Dialog);
	}
	
	public CustomDialog(Activity activity, int theme) {
		super(activity, theme);
		
		this.activity = activity;
		
		//默认背景是透明的
		setWindowBackgroundColor(Color.TRANSPARENT);
		
		//去掉标题栏，标题栏自定义
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		contentView = LayoutInflater.from(getContext()).inflate(layoutResID, null);
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		contentView = view;
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		contentView = view;
	}
	
	@Override
	public void addContentView(View view, LayoutParams params) {
		super.addContentView(view, params);
		contentView = view;
	}

	public View getContentView() {
		return contentView;
	}
	
	/**
	 * 显示此Dialog
	 * @param anchorView  参照系，是一个View
	 */
	public void show(View anchorView) {
    	show(anchorView, 0, 0);
    }
	
	/**
	 * 显示此Dialog
	 * @param anchorView  参照系，是一个View
	 * @param xOff        相对于anchorView在X方向上的偏移量
	 * @param yOff        相对于anchorView在Y方向上的偏移量
	 */
	public void show(View anchorView, int xOff, int yOff) {
		show(anchorView, xOff, yOff, 0);
	}
	
	/**
	 * 显示此Dialog
	 * @param anchorView  参照系，是一个View
	 * @param duration    显示的时间（单位：ms）
	 */
	public void show(View anchorView, int duration) {
		show(anchorView, 0, 0, duration);
    }
    
	/**
	 * 显示此Dialog
	 * @param anchorView  参照系，是一个View
	 * @param xOff        相对于anchorView在X方向上的偏移量
	 * @param yOff        相对于anchorView在Y方向上的偏移量
	 * @param duration    显示的时间（单位：ms）
	 */
    public void show(View anchorView, int xOff, int yOff, int duration) {
    	int[] locationOfViewOnScreen = new int[2];
		// 获取此view在屏幕上的位置
		anchorView.getLocationOnScreen(locationOfViewOnScreen);
		
		
		//以屏幕左上角为参照点
		show(Gravity.LEFT | Gravity.TOP, locationOfViewOnScreen[0] + xOff, -locationOfViewOnScreen[1] + yOff, duration);
    }
    
	/**
	 * 显示此Dialog
	 * @param gravity     参照系，相对于屏幕的位置，参看{@link Gravity}
	 * @param xOff        相对于gravity在X方向上的偏移量
	 * @param yOff        相对于gravity在Y方向上的偏移量
	 */
	public void show(int gravity, int xOff, int yOff) {
		show(gravity, xOff, yOff, 0);
	}
	
	/**
	 * 显示此Dialog
	 * @param gravity     参照系，相对于屏幕的位置，参看{@link Gravity}
	 * @param xOff        相对于gravity在X方向上的偏移量
	 * @param yOff        相对于gravity在Y方向上的偏移量
	 * @param duration    显示的时间（单位：ms）
	 */
	public void show(int gravity, int xOff, int yOff, long duration) {
		if (gravity == Gravity.NO_GRAVITY) {
			gravity = Gravity.TOP | Gravity.LEFT;
		}
		
		if (!isShowing()) {
			isShowing = true;
			
			Window window = getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			
			if (width != 0) {
				lp.width = width;
			}
			
			if (heidht != 0) {
				lp.height = heidht;
			}
			
			if (dimAmount < 0.1) {
				dimAmount = 0.8f;
			}
			
			lp.dimAmount = dimAmount;
			
			lp.gravity = gravity;
			lp.x = xOff;
			lp.y = yOff;
			window.setAttributes(lp);
			
			if (duration >= 0) {
				show(duration);
			}
		}
	}
	
	protected void show(long duration) {
    	show();
        
    	if (duration == 0) {
			return;
		} else if (duration < 100) {
			duration = 1000;
		}
		
    	new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
			
			@Override
			public void run() {
				dismiss();
			}
		}, duration);
    }
	
	@Override
	public void show() {
		DebugLog.d(TAG, "show()");
		
		super.show();
		
		Animation inAnimation = getDefaultInAnimation();
		inAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				DebugLog.d(TAG, "onAnimationStart()");
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				DebugLog.d(TAG, "onAnimationRepeat()");
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				DebugLog.d(TAG, "onAnimationEnd()");
			}
		});
		contentView.startAnimation(inAnimation);
	}
    
	public void dismiss(boolean hasAnimation) {
		if (!isDismissing) {
			isDismissing = true;
			
			if (hasAnimation) {
				Animation outAnimation = getDefaultOutAnimation();
				outAnimation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						DebugLog.d(TAG, "onAnimationStart()");
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						DebugLog.d(TAG, "onAnimationRepeat()");
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						DebugLog.d(TAG, "onAnimationEnd()");
						
						CustomDialog.super.dismiss();
						isDismissing = false;
						isShowing = false;
					}
				});
				contentView.startAnimation(outAnimation);
			} else {
				CustomDialog.super.dismiss();
				isDismissing = false;
				isShowing = false;
			}	
		}
	}
	
	@Override
	public void dismiss() {
		DebugLog.d(TAG, "dismiss()");

		dismiss(false);
	}
	
	@Override
	public boolean isShowing() {
		return isShowing || super.isShowing();
	}
	
	/**
	 * show的平移动画
	 */
	protected Animation getDefaultInAnimation() {
		TranslateAnimation inAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
												                Animation.RELATIVE_TO_SELF, 0, 
												                Animation.RELATIVE_TO_SELF, 1,
												                Animation.RELATIVE_TO_SELF, 0);
        inAnimation.setInterpolator(new AccelerateInterpolator());
        inAnimation.setFillAfter(true);
        inAnimation.setDuration(300);
		return inAnimation;
	}

	/**
	 * dismiss的平移动画
	 */
	protected Animation getDefaultOutAnimation() {
		TranslateAnimation outAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
												                 Animation.RELATIVE_TO_SELF, 0, 
												                 Animation.RELATIVE_TO_SELF, 0,
												                 Animation.RELATIVE_TO_SELF, 1);
        outAnimation.setInterpolator(new DecelerateInterpolator());
        outAnimation.setDuration(300);
		return outAnimation;
	}

	/**
	 * 设置Window的背景
	 */
	public final void setWindowBackground(Drawable background) {
		getWindow().setBackgroundDrawable(background);
	}
	
	/**
	 * 设置Window的背景颜色
	 */
	public final void setWindowBackgroundColor(int bgColor) {
		getWindow().setBackgroundDrawable(new ColorDrawable(bgColor));
	}
	
	/**
	 * 设置Window的宽度
	 * @param width 宽度
	 */
	public final void setWindowWidth(int width) {
		this.width = width;
	}
	
	/**
	 * 设置Window的高度
	 * @param width 高度
	 */
	public final void setWindowHeight(int height) {
		this.heidht = height;
	}
	
	/**
	 * 设置Window的模糊效果
	 * @param dimAmount  模糊程度
	 */
	public final void setDim(float dimAmount) {
		this.dimAmount = dimAmount;
	}
	
	public final Activity getActivity() {
		return activity;
	}
	
	public final void runOnUiThread(Runnable runnable) {
		if (activity != null && !activity.isFinishing()) {
			activity.runOnUiThread(runnable);
		}
	}
}
