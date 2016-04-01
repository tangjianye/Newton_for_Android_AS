package com.fpliu.newton.business.update;

import java.text.MessageFormat;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.dialog.CustomDialog;
import com.fpliu.newton.framework.ui.drawable.StateList;

/**
 * 更新弹出框
 * 
 * @author 792793182@qq.com 2014-12-22
 *
 */
final class UpdateDialog extends CustomDialog {
	
	/** 文本内容 */
	private TextView textView;
	
	private Button button;
	
	private ImageView progressView;
	
	private RotateAnimation rotateAnimation;
	
	private OnClickListener listener;
	
	public UpdateDialog(final Activity context) {
		super(context);
		
		final LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.WHITE);
		
		rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(1200);
		rotateAnimation.setRepeatCount(Animation.INFINITE);
		rotateAnimation.setInterpolator(new LinearInterpolator());
		
		progressView = new ImageView(context);
		progressView.setBackgroundResource(R.drawable.viafly_dlg_recognizing_progress);
		progressView.setVisibility(View.GONE);
		
		int width = UIUtil.dip2px(context, 50);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(width, width);
		lp1.gravity = Gravity.CENTER;
		lp1.topMargin = UIUtil.dip2px(context, 15);
		
		layout.addView(progressView, 0, lp1);
		
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
		
		button = new Button(context);
		button.setTextSize(18);
		button.setText(R.string.dialog_refresh_data_ok);
		button.setTextColor(Color.WHITE);
		button.setGravity(Gravity.CENTER);
		button.setBackgroundDrawable(StateList.get());
		
		int padding = UIUtil.dip2px(context, 10);
		button.setPadding(0, padding, 0, padding);
		final String refresh = MyApp.getApp().getResources().getString(R.string.dialog_refresh_data_ok);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (button.getText().toString().equals(refresh)) {
					button.setVisibility(View.GONE);
					
					progressView.setVisibility(View.VISIBLE);
					progressView.startAnimation(rotateAnimation);
					
					String download = MyApp.getApp().getResources().getString(R.string.updata_download);
					String download_new = MessageFormat.format(download, 0);
					textView.setText(download_new);
					
					if (listener != null) {
						listener.onClick(UpdateDialog.this, 0);
					}
				} else {
					dismiss();
				}
			}
		});
		
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp3.gravity = Gravity.CENTER;
		lp3.topMargin = margin;
		lp3.bottomMargin = margin;
		lp3.leftMargin = margin;
		lp3.rightMargin = margin;
		
		layout.addView(button, lp3);
		
		setContentView(layout);
		
		setWindowWidth(Environment.getInstance().getScreenWidth() * 9 / 10);
		
		setDim(0.8f);
	}
	
	void setMessage(String msg) {
		rotateAnimation.cancel();
		//必须调用此方法，否则不会GONE掉
		progressView.clearAnimation();
		progressView.setVisibility(View.GONE);
		textView.setText(msg);
		button.setVisibility(View.VISIBLE);
	}
	
	void setError(String msg) {
		Message.obtain(handler, 1, msg).sendToTarget();
	}
	
	void setUpdateBtnClickListener(OnClickListener listener) {
		this.listener = listener;
	}
	
	void setProgress(int percent) {
		Message.obtain(handler, 0, percent, 0).sendToTarget();
	}
	
	private Handler handler = new Handler(Looper.getMainLooper()) {
		
		@Override
		public void handleMessage(Message msg) {
			if (isShowing()) {
				switch (msg.what) {
				case 0:
					String download = MyApp.getApp().getResources().getString(R.string.updata_download);
					String download_new = MessageFormat.format(download, msg.arg1);
					textView.setText(download_new);
					break;
				case 1:
					button.setText(R.string.updata_roger);
					setMessage((String)msg.obj);
					break;
				default:
					break;
				}
			}
		}
	};
}
