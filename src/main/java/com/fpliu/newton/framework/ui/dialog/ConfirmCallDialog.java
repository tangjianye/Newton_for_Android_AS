package com.fpliu.newton.framework.ui.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.fpliu.newton.R;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;
import com.fpliu.newton.framework.util.LauncherManager;

/**
 * 确认拨打电话的弹出框
 * 
 * @author 792793182@qq.com 2015-06-12
 *
 */
public final class ConfirmCallDialog extends CustomDialog {
	
	public ConfirmCallDialog(final Activity activity, final String phoneNumber) {
		super(activity);

		RelativeLayout parentLayout = new RelativeLayout(activity);
		parentLayout.setBackgroundColor(Color.TRANSPARENT);
		
		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		
		float r = 10;
		float[] outerR = new float[] { r, r, r, r, r, r, r, r };
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(rr);
		drawable.getPaint().setColor(Color.WHITE);
		layout.setBackgroundDrawable(drawable);
		
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.topMargin = UIUtil.dip2px(activity, 20);
		lp.rightMargin = UIUtil.dip2px(activity, 20);
		parentLayout.addView(layout, lp);
		
		//关闭按钮
		ImageButton closeBtn = new ImageButton(activity);
		closeBtn.setBackgroundResource(R.drawable.close_btn);
		closeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		parentLayout.addView(closeBtn, lp1);
		
		int a = UIUtil.dip2px(activity, 10);
		
		TextView titleTv = new TextView(activity);
		titleTv.setText("提示");
		titleTv.setTextSize(16);
		titleTv.setTextColor(Color.parseColor("#888888"));
		titleTv.setGravity(Gravity.CENTER);
		titleTv.setPadding(0, a, 0, a);

		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout.addView(titleTv, lp2);
		
		TextView messageTv = new TextView(activity);
		messageTv.setText("确定拨打电话：" + phoneNumber + "吗？");
		messageTv.setTextSize(18);
		messageTv.setTextColor(Color.parseColor("#222222"));
		messageTv.setGravity(Gravity.CENTER);
		messageTv.setPadding(0, a, 0, a);

		layout.addView(messageTv, lp2);

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
				LauncherManager.call(activity, phoneNumber);
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
				dismiss();
			}
		});

		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
		lp3.topMargin = UIUtil.dip2px(activity, 10);
		lp3.leftMargin = UIUtil.dip2px(activity, 10);
		lp3.rightMargin = UIUtil.dip2px(activity, 10);
		lp3.bottomMargin = UIUtil.dip2px(activity, 10);

		footer.addView(cancelBtn, lp3);
		footer.addView(okBtn, lp3);

		layout.addView(footer, lp2);

		setContentView(parentLayout);

		// 设置为屏幕宽度的9/10
		setWindowWidth(Environment.getInstance().getScreenWidth() * 9 / 10);
	}
	
	/**
	 * show的平移动画
	 */
	@Override
	protected Animation getDefaultInAnimation() {
		AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setInterpolator(new AccelerateInterpolator());
        inAnimation.setFillAfter(true);
        inAnimation.setDuration(300);
		return inAnimation;
	}

	/**
	 * dismiss的平移动画
	 */
	@Override
	protected Animation getDefaultOutAnimation() {
		AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setInterpolator(new DecelerateInterpolator());
        outAnimation.setDuration(300);
		return outAnimation;
	}
}
