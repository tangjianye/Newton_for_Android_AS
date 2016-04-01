package com.fpliu.newton.framework.ui.popup;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;

import com.fpliu.newton.base.Environment;

/**
 * 自定义PopupWindow
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public class CustomPopup extends PopupWindow {

	private Activity activity;

	private boolean needInAnimation = true;

	private boolean needOutAnimation = false;

	private boolean isRunning;

	public CustomPopup(Activity activity) {
		this.activity = activity;

		setWidth(Environment.getInstance().getScreenWidth() / 2);
		setHeight(Environment.getInstance().getScreenHeight() / 2);

		// 使用下面这个方法前必须设置PopWindow的宽度和高度为一个确切的随意的大于0的数值，否则不起作用
		setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		setBackgroundColor(Color.TRANSPARENT);

		// 只有具有焦点才能处理KEY事件，一般是需要处理key事件的
		setFocusable(true);
	}

	public void show(View anchorView, int xoff, int yoff) {
		if (!activity.isFinishing()) {
			show(anchorView, xoff, yoff, 1500);
		}
	}

	public void show(View anchorView, int duration) {
		if (!activity.isFinishing()) {
			show(anchorView, 0, 0, duration);
		}
	}

	public void show(View anchorView) {
		if (!activity.isFinishing()) {
			show(anchorView, 1500);
		}
	}

	public void show(View anchorView, int xoff, int yoff, int duration) {
		showAsDropDown(anchorView, xoff, yoff);

		show(duration);
	}

	public void show(int gravity, int x, int y, int duration) {
		if (gravity == Gravity.NO_GRAVITY) {
			gravity = Gravity.TOP | Gravity.LEFT;
		}
		showAtLocation(getContentView(), gravity, x, y);

		if (duration >= 0) {
			show(duration);
		}
	}

	protected void show(int duration) {
		// MIUI2系统会奔溃
		if (needInAnimation && !Environment.isMIUIRom()) {
			Animation animation = getInAnimation();
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

					// 变成阴影
					Window window = activity.getWindow();
					WindowManager.LayoutParams windowLP = window
							.getAttributes();
					windowLP.dimAmount = 0.3f; // 0.0-1.0
					window.setAttributes(windowLP);
				}
			});
			getContentView().startAnimation(animation);
		} else {
			// 变成阴影
			Window window = activity.getWindow();
			WindowManager.LayoutParams windowLP = window.getAttributes();
			windowLP.dimAmount = 0.3f; // 0.0-1.0
			window.setAttributes(windowLP);
		}

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
	public void dismiss() {
		if (isRunning) {
			return;
		}

		isRunning = true;

		Window window = CustomPopup.this.activity.getWindow();
		WindowManager.LayoutParams windowLP = window.getAttributes();
		windowLP.dimAmount = 1.0f; // 0.0-1.0
		window.setAttributes(windowLP);

		// MIUI2系统会奔溃
		if (needOutAnimation && !Environment.isMIUIRom()) {
			Animation animation = getOutAnimation();
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

					CustomPopup.super.dismiss();
					isRunning = false;
				}
			});
			getContentView().startAnimation(animation);
		} else {
			CustomPopup.super.dismiss();
			isRunning = false;
		}
	}

	public final void setNeedInAnimation(boolean needInAnimation) {
		this.needInAnimation = needInAnimation;
	}

	public final void setNeedOutAnimation(boolean needOutAnimation) {
		this.needOutAnimation = needOutAnimation;
	}

	protected Animation getInAnimation() {
		TranslateAnimation inAnimation = new TranslateAnimation(
										Animation.RELATIVE_TO_SELF, 0, 
										Animation.RELATIVE_TO_SELF, 0,
										Animation.RELATIVE_TO_SELF, 1, 
										Animation.RELATIVE_TO_SELF, 0);
		inAnimation.setInterpolator(new AccelerateInterpolator());
		inAnimation.setFillAfter(true);
		inAnimation.setDuration(300);
		return inAnimation;
	}

	protected Animation getOutAnimation() {
		TranslateAnimation outAnimation = new TranslateAnimation(
										Animation.RELATIVE_TO_SELF, 0, 
										Animation.RELATIVE_TO_SELF, 0,
										Animation.RELATIVE_TO_SELF, 0, 
										Animation.RELATIVE_TO_SELF, 1);
		outAnimation.setInterpolator(new DecelerateInterpolator());
		outAnimation.setDuration(300);
		return outAnimation;
	}
	
	@Override
	public final void setWidth(int width) {
		super.setWidth(width);
		
		if (width > 0) {
			if (getHeight() > 0) {
				setWindowLayoutMode(0, 0);
			} else {
				setWindowLayoutMode(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} else {
			if (getHeight() > 0) {
				setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
			} else {
				setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		}
	}
	
	@Override
	public final void setHeight(int height) {
		super.setHeight(height);
		
		if (height > 0) {
			if (getWidth() > 0) {
				setWindowLayoutMode(0, 0);
			} else {
				setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
			}
		} else {
			if (getWidth() > 0) {
				setWindowLayoutMode(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			} else {
				setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		}
	}
	
	public final void setWidthAndHeight(int width, int height) {
		if (width > 0) {
			if (height > 0) {
				setWindowLayoutMode(0, 0);
			} else {
				setWindowLayoutMode(0, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} else {
			if (height > 0) {
				setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
			} else {
				setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		}
	}
	
	public final void setBackgroundColor(int color) {
		setBackgroundDrawable(new ColorDrawable(color));
	}
}
