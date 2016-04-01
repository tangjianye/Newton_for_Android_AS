package com.fpliu.newton.framework.ui.dialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
 * 时间选择的弹出框
 * 
 * @author 792793182@qq.com 2015-06-12
 *
 */
public final class DateTimeSelectDialog extends CustomDialog {

	/** 格式化小时和分钟的 */
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("00");

	/** 选中的日期 */
	private long selectedDate;

	/** 选中的小时 */
	private int selectedHour;

	/** 选中的分钟 */
	private int selectedMinites;

	public DateTimeSelectDialog(Activity activity, String titleText, final Callback callback) {
		super(activity);

		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
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

		LinearLayout body = new LinearLayout(activity);
		body.setOrientation(LinearLayout.HORIZONTAL);

		body.addView(getDateWheelView(activity), new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 2));
		
		ImageView seperator_1 = new ImageView(getActivity());
		seperator_1.setBackgroundColor(Color.parseColor("#dbdbdb"));
		body.addView(seperator_1, new LinearLayout.LayoutParams(2, LayoutParams.MATCH_PARENT));
		
		body.addView(getHourWheelView(activity), new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
		
		ImageView seperator_2 = new ImageView(getActivity());
		seperator_2.setBackgroundColor(Color.parseColor("#dbdbdb"));
		body.addView(seperator_2, new LinearLayout.LayoutParams(2, LayoutParams.MATCH_PARENT));
		
		body.addView(getMinitesWheelView(activity), new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));

		layout.addView(body, lp1);

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
					callback.onOK(selectedDate, selectedHour, selectedMinites);
				}
				dismiss();
			}
		});

		Button cancelBtn = new Button(activity);
		cancelBtn.setText("取 消");
		cancelBtn.setTextSize(18);
		cancelBtn.setTextColor(Color.WHITE);
		cancelBtn
				.setBackgroundResource(R.drawable.state_list_rounded_rectangle);
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
		lp2.leftMargin = UIUtil.dip2px(activity, 10);
		lp2.rightMargin = UIUtil.dip2px(activity, 10);
		lp2.bottomMargin = UIUtil.dip2px(activity, 10);

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

	/**
	 * 创建选择日期的控件
	 * @param context  上下文
	 * @return         控件
	 */
	private WheelView getDateWheelView(Context context) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 E", Locale.CHINESE);
		long baseDate = System.currentTimeMillis();

		// 日期选择
		final long[] dates = new long[30];
		String[] dateStrs = new String[30];
		for (int i = 0; i < dateStrs.length; i++) {
			dates[i] = baseDate + i * 24 * 3600 * 1000L;
			dateStrs[i] = dateFormat.format(new Date(dates[i]));
		}

		WheelView dateWheelView = new WheelView(context);
		dateWheelView.setVisibleItems(5);
		dateWheelView.setViewAdapter(new ItemAdapter(context, dateStrs));
		dateWheelView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				selectedDate = dates[wheel.getCurrentItem()];
				System.out.println("selectedDate = " + selectedDate);
			}
		});

		selectedDate = dates[3];
		dateWheelView.setCurrentItem(3);
		return dateWheelView;
	}

	/**
	 * 创建选择小时的控件
	 * @param context  上下文
	 * @return         控件
	 */
	private WheelView getHourWheelView(Context context) {
		// 小时选择
		final int[] hours = new int[24];
		String[] hourStrs = new String[24];
		for (int i = 0; i < hours.length; i++) {
			hours[i] = i;
			hourStrs[i] = DECIMAL_FORMAT.format(i) + "点";
		}

		WheelView hourWheelView = new WheelView(context);
		hourWheelView.setVisibleItems(5);
		hourWheelView.setViewAdapter(new ItemAdapter(context, hourStrs));
		hourWheelView.addScrollingListener(new OnWheelScrollListener() {
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
		hourWheelView.setCurrentItem(3);
		return hourWheelView;
	}

	/**
	 * 创建选择分钟的控件
	 * @param context  上下文
	 * @return         控件
	 */
	private WheelView getMinitesWheelView(Context context) {
		// 分钟选择
		final int[] minites = new int[60];
		final String[] minitesStrs = new String[60];
		for (int i = 0; i < minites.length; i++) {
			minites[i] = i;
			minitesStrs[i] = DECIMAL_FORMAT.format(i) + "分";
		}

		final WheelView minitesWheelView = new WheelView(context);
		minitesWheelView.setVisibleItems(5);
		minitesWheelView.setViewAdapter(new ItemAdapter(context, minitesStrs));
		minitesWheelView.addScrollingListener(new OnWheelScrollListener() {
			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				selectedMinites = minites[wheel.getCurrentItem()];
				System.out.println("selectedMinites = " + selectedMinites);
			}
		});

		selectedMinites = minites[3];
		minitesWheelView.setCurrentItem(3);
		return minitesWheelView;
	}

	private static final class ItemAdapter extends AbstractWheelTextAdapter {

		private String[] values;

		protected ItemAdapter(Context context, String[] values) {
			super(context);
			this.values = values;
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
			return values.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return values[index];
		}
	}

	public static interface Callback {

		/** 点击了确定按钮的回调 */
		void onOK(long selectedDate, int selectedHour, int selectedMinites);

		/** 点击了取消按钮的回调 */
		void onCancel();
	}
}
