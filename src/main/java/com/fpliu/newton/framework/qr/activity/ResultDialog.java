package com.fpliu.newton.framework.qr.activity;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.base.clipboard.ClipboardManager;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.dialog.CustomDialog;
import com.fpliu.newton.framework.ui.drawable.StateList;
import com.fpliu.newton.framework.util.LauncherManager;

/**
 * 二维码扫描结果展示
 * 
 * @author 792793182@qq.com 2015-03-18
 *
 */
public final class ResultDialog extends CustomDialog {

	public ResultDialog(final Activity activity, final String url) {
		super(activity);
		
		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(activity.getResources().getColor(R.color.bg_color));
		
		TextView textView = new TextView(activity);
		textView.setText(url);
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		
		int margin = UIUtil.dip2px(activity, 15);
		
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp2.gravity = Gravity.CENTER;
		lp2.topMargin = margin;
		lp2.bottomMargin = margin;
		lp2.leftMargin = margin;
		lp2.rightMargin = margin;
		
		layout.addView(textView, lp2);
		
		LinearLayout btnLayout = new LinearLayout(activity);
		btnLayout.setOrientation(LinearLayout.HORIZONTAL);
		layout.addView(btnLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		Button copyBtn = new Button(activity);
		copyBtn.setText(R.string.ResultDialog_copy);
		copyBtn.setTextSize(18);
		copyBtn.setTextColor(Color.WHITE);
		copyBtn.setBackgroundDrawable(StateList.get2());
		int paddingWidth = UIUtil.dip2px(activity, 20);
		int paddingHeight = UIUtil.dip2px(activity, 10);
		copyBtn.setPadding(paddingWidth, paddingHeight, paddingWidth, paddingHeight);
		copyBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClipboardManager.getInstance(activity).copy(url);
				dismiss();
			}
		});
		
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp1.weight = 1;
		
		btnLayout.addView(copyBtn, lp1);
		
		ImageView imageView = new ImageView(activity);
		imageView.setBackgroundColor(Color.WHITE);
		btnLayout.addView(imageView, new LinearLayout.LayoutParams(UIUtil.dip2px(activity, 1), LayoutParams.MATCH_PARENT));
		
		Button openBtn = new Button(activity);
		openBtn.setText(R.string.ResultDialog_open);
		openBtn.setTextSize(18);
		openBtn.setTextColor(Color.WHITE);
		openBtn.setBackgroundDrawable(StateList.get2());
		openBtn.setPadding(paddingWidth, paddingHeight, paddingWidth, paddingHeight);
		openBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LauncherManager.startBrowser(activity, url);
				dismiss();
			}
		});
		btnLayout.addView(openBtn, lp1);
		
		setContentView(layout);
		
		setWindowWidth(Environment.getInstance().getScreenWidth() * 9 / 10);
	}
}
