package com.fpliu.newton.framework.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.util.LauncherManager;

/**
 * 显示状态的视图
 * 
 * @author 792793182@qq.com 2014-10-14
 * 
 */
public class StateView extends RelativeLayout implements OnClickListener {

	private ImageView progressView;

	private TextView textView;

	private Button button;

	private RotateAnimation rotateAnimation;

	public StateView(Context context) {
		this(context, null);
	}

	public StateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init(context);
	}
	
	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.stateview, this);

		textView = (TextView) findViewById(R.id.stateview_text);

		progressView = (ImageView) findViewById(R.id.stateview_progress);
		
		rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(1200);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		rotateAnimation.setInterpolator(new LinearInterpolator());

		button = (Button) findViewById(R.id.stateview_btn);
		button.setBackgroundDrawable(StateList.get());
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == button) {
			LauncherManager.startNetSetting(getContext());
		}
	}
	
	public void setButtonVisibility(boolean isVisibility){
		if(isVisibility){
			button.setVisibility(VISIBLE);
		}else{
			button.setVisibility(GONE);
		}
	}

	public void showError(CharSequence errorText) {
		rotateAnimation.cancel();
		progressView.clearAnimation();
		progressView.setVisibility(GONE);
		textView.setVisibility(VISIBLE);
		textView.setText(errorText);
		button.setVisibility(VISIBLE);
	}

	public void showProgress(CharSequence text) {
		progressView.setAnimation(rotateAnimation);
		rotateAnimation.startNow();
		progressView.setVisibility(VISIBLE);
		textView.setVisibility(VISIBLE);
		textView.setText(text);
		button.setVisibility(GONE);
	}

	public void showErrorTextOnly(CharSequence text) {
		rotateAnimation.cancel();
		progressView.clearAnimation();
		progressView.setVisibility(GONE);
		textView.setVisibility(VISIBLE);
		textView.setText(text);
	}

	public void showErrorNoButton(CharSequence errorText) {
		rotateAnimation.cancel();
		progressView.clearAnimation();
		progressView.setVisibility(GONE);
		textView.setVisibility(VISIBLE);
		textView.setText(errorText);
		button.setVisibility(GONE);
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		
		if (rotateAnimation != null) {
			if (visibility == GONE) {
				rotateAnimation.cancel();
			} else if (visibility == VISIBLE) {
				rotateAnimation.startNow();
			}
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		removeAllViews();

		progressView = null;
		textView = null;
		button = null;
		rotateAnimation = null;
	}
}
