package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;

/**
 * 选择图片来源的弹出框
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public class ImageChooseDialog extends CustomDialog {
	
	/** 打开相机按钮 */
	private TextView openBtn;
	
	/** 选择相册按钮 */
	private TextView selectBtn;
	
	private OnOpenCameraClick onOpenCameraClick;
	private OnPickImageClick onPickImageClick;
	
	
	public ImageChooseDialog(Activity activity) {
		super(activity);
		
		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.WHITE);
		
		openBtn = new TextView(activity);
		openBtn.setText(R.string.imageChooseDialog_open_carmera);
		openBtn.setTextColor(Color.BLACK);
		openBtn.setTextSize(20);
		openBtn.setGravity(Gravity.CENTER);
		openBtn.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onOpenCameraClick != null) {
					onOpenCameraClick.onClick();
				}
			}
		});
		
		TextView seperator = new TextView(activity);
		seperator.setBackgroundColor(Color.BLACK);
		seperator.setHeight(1);
		
		selectBtn = new TextView(activity);
		selectBtn.setText(R.string.imageChooseDialog_select_album);
		selectBtn.setTextColor(Color.BLACK);
		selectBtn.setTextSize(20);
		selectBtn.setGravity(Gravity.CENTER);
		selectBtn.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onPickImageClick != null) {
					onPickImageClick.onClick();
				}
			}
		});
		
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		lp.topMargin = 20;
		lp.bottomMargin = 20;
		
		layout.addView(openBtn, lp);
		layout.addView(seperator);
		layout.addView(selectBtn, lp);
		
		setContentView(layout);
		
		setWindowWidth(Environment.getInstance().getScreenWidth() * 2 / 3);
	}

	public void setOnOpenCameraClick(OnOpenCameraClick onOpenCameraClick) {
		this.onOpenCameraClick = onOpenCameraClick;
	}
	
	public void setOnPickImageClick(OnPickImageClick onPickImageClick) {
		this.onPickImageClick = onPickImageClick;
	}
	
	public static interface OnOpenCameraClick {
		void onClick();
	}
	
	public static interface OnPickImageClick {
		void onClick();
	}
}
