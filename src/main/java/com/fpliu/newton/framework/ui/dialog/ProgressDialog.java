package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;

/**
 * 进度弹出框
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public class ProgressDialog extends CustomDialog {

	/** 文本内容 */
	private TextView textView;
	
	public ProgressDialog(Activity context) {
		super(context);
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.WHITE);
		
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1200);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		
		ImageView progressView = new ImageView(context);
		progressView.setBackgroundResource(R.drawable.viafly_dlg_recognizing_progress);
		progressView.startAnimation(rotateAnimation);
		
		int width = UIUtil.dip2px(context, 50);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, width);
		lp1.gravity = Gravity.CENTER;
		lp1.topMargin = UIUtil.dip2px(context, 15);
		
		layout.addView(progressView, lp1);
		
		textView = new TextView(context);
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		
		int margin = UIUtil.dip2px(context, 15);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.gravity = Gravity.CENTER;
		lp2.topMargin = margin;
		lp2.bottomMargin = margin;
		lp2.leftMargin = margin;
		lp2.rightMargin = margin;
		
		layout.addView(textView, lp2);
		
		setContentView(layout);
		
		setWindowWidth(Environment.getInstance().getScreenWidth() * 9 / 10);
	}
	
	public void setMessage(String msg) {
		textView.setText(msg);
	}
}
