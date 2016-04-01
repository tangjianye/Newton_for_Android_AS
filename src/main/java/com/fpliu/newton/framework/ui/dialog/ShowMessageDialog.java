package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;

/**
 * 显示文字的弹出框
 * 
 * @author 792793182@qq.com 2014-12-05
 *
 */
public class ShowMessageDialog extends CustomDialog {

	/** 文本内容 */
	private TextView textView;
	
	public ShowMessageDialog(Activity context) {
		super(context);
		
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
		
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
		
//		Button btn = new Button(activity);
//		btn.setText("知道了");
//		btn.setTextSize(18);
//		btn.setTextColor(Color.WHITE);
//		btn.setBackgroundDrawable(StateList.get());
//		int paddingWidth = UIUtil.dip2px(activity, 20);
//		int paddingHeight = UIUtil.dip2px(activity, 10);
//		btn.setPadding(paddingWidth, paddingHeight, paddingWidth, paddingHeight);
//		btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dismiss();
//			}
//		});
//		
//		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		lp1.gravity = Gravity.CENTER;
//		lp1.topMargin = margin;
//		lp1.bottomMargin = margin;
//		
//		layout.addView(btn, lp1);
		
		setContentView(layout);
		
		setWindowWidth(Environment.getInstance().getScreenWidth() * 9 / 10);
	}
	
	public void setMessage(String msg) {
		textView.setText(msg);
	}
}
