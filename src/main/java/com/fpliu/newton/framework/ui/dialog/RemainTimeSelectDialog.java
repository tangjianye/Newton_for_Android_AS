package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.ui.wheel.OnWheelScrollListener;
import com.fpliu.newton.framework.ui.wheel.WheelView;
import com.fpliu.newton.framework.ui.wheel.adapter.AbstractWheelTextAdapter;

/**
 * 时长选择的弹出框
 * 
 * @author 792793182@qq.com 2015-06-12
 *
 */
public final class RemainTimeSelectDialog extends CustomDialog {

	/** 选中的时间值 */
	private int selectedHour;

	public RemainTimeSelectDialog(Activity activity, String titleText, final Callback callback) {
		super(activity);

		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(Color.WHITE);

		TextView textView = new TextView(activity);
		textView.setText(titleText);
		textView.setTextSize(18);
		textView.setTextColor(Color.GRAY);
		textView.setGravity(Gravity.CENTER);
		int a = UIUtil.dip2px(activity, 10);
		textView.setPadding(0, a, 0, a);

		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.addView(textView, lp1);
		
		ImageView seperator1 = new ImageView(getActivity());
		seperator1.setBackgroundColor(Color.parseColor("#dbdbdb"));
		layout.addView(seperator1, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));

		// 24小时
		final int[] hours = new int[24];
		String[] hourStrs = new String[24];
		for (int i = 0; i < hours.length; i++) {
			hours[i] = i + 1;
			hourStrs[i] = hours[i] + " 小时";
		}

		WheelView wheelView = new WheelView(activity);
		wheelView.setVisibleItems(5);
		wheelView.setViewAdapter(new TimeAdapter(activity, hourStrs));
		wheelView.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				selectedHour = hours[wheel.getCurrentItem()];
				System.out.println("selectedHour = " + selectedHour);
			}
		});

		selectedHour = hours[3];
		wheelView.setCurrentItem(3);

		layout.addView(wheelView, lp1);

		ImageView seperator2 = new ImageView(getActivity());
		seperator2.setBackgroundColor(Color.parseColor("#dbdbdb"));
		layout.addView(seperator2, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
		
		LinearLayout footer = new LinearLayout(activity);
		footer.setOrientation(LinearLayout.HORIZONTAL);

		Button okBtn = new Button(activity);
		okBtn.setText("确 定");
		okBtn.setTextSize(18);
		okBtn.setTextColor(Color.WHITE);
		okBtn.setBackgroundResource(R.drawable.state_list_rounded_rectangle);
		okBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onOK(selectedHour);
				}
				dismiss();
			}
		});

		Button cancelBtn = new Button(activity);
		cancelBtn.setText("取 消");
		cancelBtn.setTextSize(18);
		cancelBtn.setTextColor(Color.WHITE);
		cancelBtn.setBackgroundResource(R.drawable.state_list_rounded_rectangle);
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (callback != null) {
					callback.onCancel();
				}
				dismiss();
			}
		});

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		lp2.topMargin = UIUtil.dip2px(activity, 10);
		lp2.leftMargin = lp2.topMargin;
		lp2.rightMargin = lp2.topMargin;
		lp2.bottomMargin = lp2.topMargin;

		footer.addView(okBtn, lp2);
		footer.addView(cancelBtn, lp2);

		layout.addView(footer, lp1);

		setContentView(layout);

		DisplayMetrics metric = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(metric);
		int screenWidth = metric.widthPixels;

		// 设置为屏幕宽度
		setWindowWidth(screenWidth);
	}

	private static final class TimeAdapter extends AbstractWheelTextAdapter {

		private String[] hours;

		protected TimeAdapter(Context context, String[] hours) {
			super(context);
			this.hours = hours;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			TextView textView = new TextView(context);
			textView.setText(getItemText(index));
			textView.setTextSize(18);
			textView.setTextColor(Color.BLACK);
			textView.setGravity(Gravity.CENTER);

			int a = UIUtil.dip2px(context, 10);
			textView.setPadding(0, a, 0, a);

			return textView;
		}

		@Override
		public int getItemsCount() {
			return hours.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return hours[index];
		}
	}

	public static interface Callback {

		/** 点击了确定按钮的回调 */
		void onOK(int selectedHour);

		/** 点击了取消按钮的回调 */
		void onCancel();
	}
}
