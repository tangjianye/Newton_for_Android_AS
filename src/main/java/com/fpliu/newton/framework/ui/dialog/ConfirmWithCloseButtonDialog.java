package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;

/**
 * 确认弹出框
 * 
 * @author 792793182@qq.com 2015-06-12
 *
 */
public final class ConfirmWithCloseButtonDialog extends CustomDialog {
	
	private LinearLayout layout;
	
	private LinearLayout footer;
	
	private LinearLayout.LayoutParams lpmw = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	public ConfirmWithCloseButtonDialog(final Activity activity) {
		super(activity);
		
		RelativeLayout parentLayout = new RelativeLayout(activity);
		parentLayout.setBackgroundColor(Color.TRANSPARENT);
		
		setContentView(parentLayout, lpmw);
		
		float r = 10;
		float[] outerR = new float[] { r, r, r, r, r, r, r, r };
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(rr);
		drawable.getPaint().setColor(Color.WHITE);
		
		layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		layout.setBackgroundDrawable(drawable);
		
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp1.topMargin = UIUtil.dip2px(activity, 10);
		lp1.rightMargin = UIUtil.dip2px(activity, 10);
		parentLayout.addView(layout, lp1);
		
		//关闭按钮
		ImageButton closeBtn = new ImageButton(activity);
		closeBtn.setBackgroundResource(R.drawable.emotionstore_progresscancelbtn);
		closeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		parentLayout.addView(closeBtn, lp2);
		
		setDim(0.5f);
		
		// 设置为屏幕宽度
		setWindowWidth(Environment.getInstance().getScreenWidth());
	}
	
	/**
	 * 添加标题栏
	 * @param titleText 标题栏文字
	 * @return
	 */
	public ConfirmWithCloseButtonDialog title(String titleText) {
		Context context = getActivity();
		
		int a = UIUtil.dip2px(context, 10);
		
		TextView titleTv = new TextView(context);
		titleTv.setText(titleText);
		titleTv.setTextSize(16);
		titleTv.setTextColor(Color.parseColor("#888888"));
		titleTv.setGravity(Gravity.CENTER);
		titleTv.setPadding(0, a, 0, a);
		
		layout.addView(titleTv, lpmw);
		
		return this;
	}
	
	/**
	 * 添加提示消息
	 * @param message 消息
	 * @return
	 */
	public ConfirmWithCloseButtonDialog message(String message) {
		Context context = getActivity();
		
		int a = UIUtil.dip2px(context, 10);
		
		TextView messageTv = new TextView(context);
		messageTv.setText(message);
		messageTv.setTextSize(18);
		messageTv.setTextColor(Color.parseColor("#222222"));
		messageTv.setGravity(Gravity.CENTER);
		messageTv.setPadding(0, a, 0, a);
		
		layout.addView(messageTv, lpmw);
		
		return this;
	}
	
	/**
	 * @param orientation Button的排列方向 {@see LinearLayout.HORIZONTAL}、{@see LinearLayout.VERTICAL}
	 * @return
	 */
	public ConfirmWithCloseButtonDialog buttonPanel(int orientation) {
		footer = new LinearLayout(getActivity());
		footer.setOrientation(orientation);
		
		layout.addView(footer, lpmw);
		
		return this;
	}
	
	public ConfirmWithCloseButtonDialog button(final String text, final OnDialogButtonClick onButtonClick) {
		if (footer == null) {
			buttonPanel(LinearLayout.HORIZONTAL);
		}
		
		Button button = new Button(getActivity());
		button.setText(text);
		button.setTextSize(18);
		button.setTextColor(Color.WHITE);
		button.setBackgroundResource(R.drawable.state_list_rounded_rectangle);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onButtonClick != null) {
					onButtonClick.onClick(ConfirmWithCloseButtonDialog.this, text);
				}
				dismiss();
			}
		});
		
		//如果是水平方向
		if (footer.getOrientation() == LinearLayout.HORIZONTAL) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
			lp.leftMargin = UIUtil.dip2px(getActivity(), 10);
			lp.rightMargin = lp.leftMargin;
			lp.topMargin = lp.leftMargin;
			lp.bottomMargin = lp.leftMargin;
			
			footer.addView(button, lp);
		} 
		//如果是竖直方向
		else {
			footer.addView(button, lpmw);
		}
		
		return this;
	}
}
