package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public final class ConfirmDialog extends CustomDialog {
	
	private LinearLayout layout;
	
	private LinearLayout footer;
	
	private LinearLayout.LayoutParams lpmw = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	
	public ConfirmDialog(final Activity activity) {
		super(activity);

		layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		layout.setBackgroundColor(Color.parseColor("#868686"));
		
		setContentView(layout);
		
		// 设置为屏幕宽度
		setWindowWidth(Environment.getInstance().getScreenWidth());
	}
	
	/**
	 * 添加标题栏
	 * @param titleText 标题栏文字
	 * @return
	 */
	public ConfirmDialog title(String titleText) {
		Context context = getActivity();
		
		int a = UIUtil.dip2px(context, 10);
		
		TextView titleTv = new TextView(context);
		titleTv.setText(titleText);
		titleTv.setTextSize(18);
		titleTv.setTextColor(Color.parseColor("#888888"));
		titleTv.setBackgroundColor(Color.parseColor("#D1D1D1"));
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
	public ConfirmDialog message(String message) {
		Context context = getActivity();
		
		int a = UIUtil.dip2px(context, 10);
		
		TextView messageTv = new TextView(context);
		messageTv.setText(message);
		messageTv.setTextSize(18);
		messageTv.setTextColor(Color.parseColor("#888888"));
		messageTv.setBackgroundColor(Color.parseColor("#D1D1D1"));
		messageTv.setGravity(Gravity.CENTER);
		messageTv.setPadding(0, a, 0, a);
		
		layout.addView(messageTv, lpmw);
		
		return this;
	}
	
	/**
	 * @param orientation Button的排列方向 {@see LinearLayout.HORIZONTAL}、{@see LinearLayout.VERTICAL}
	 * @return
	 */
	public ConfirmDialog buttonPanel(int orientation) {
		footer = new LinearLayout(getActivity());
		footer.setOrientation(orientation);
		
		layout.addView(footer, lpmw);
		
		return this;
	}
	
	public ConfirmDialog button(final String text, final OnDialogButtonClick onButtonClick) {
		if (footer == null) {
			buttonPanel(LinearLayout.HORIZONTAL);
		}
		
		Button button = new Button(getActivity());
		button.setText(text);
		button.setTextSize(18);
		button.setTextColor(Color.parseColor("#ec4949"));
		button.setBackgroundResource(R.drawable.state_list_rectangle);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (onButtonClick != null) {
					onButtonClick.onClick(ConfirmDialog.this, text);
				}
				dismiss();
			}
		});
		
		ImageView seperator = new ImageView(getActivity());
		seperator.setBackgroundColor(Color.parseColor("#dbdbdb"));
		
		int childCount = footer.getChildCount();
		
		//如果是水平方向
		if (footer.getOrientation() == LinearLayout.HORIZONTAL) {
			if (childCount > 0) {
				footer.addView(seperator, new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT));
			}
			
			footer.addView(button, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2));
		} 
		//如果是竖直方向
		else {
			if (childCount > 0) {
				footer.addView(seperator, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
			}
			
			footer.addView(button, lpmw);
		}
		
		return this;
	}
}
