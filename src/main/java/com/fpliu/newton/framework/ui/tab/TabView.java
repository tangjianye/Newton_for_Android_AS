package com.fpliu.newton.framework.ui.tab;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.fpliu.newton.base.DebugLog;
import com.fpliu.newton.base.Environment;
import com.fpliu.newton.framework.ui.UIUtil;

/**
 * 自定义Tab页面
 * 
 * @author 792793182@qq.com 2015-06-12
 * 
 */
public class TabView extends RelativeLayout {

	private static final String TAG = TabView.class.getSimpleName();

	//此值以0x6f开头，是为了避免与ADT自动生成的ID冲突
	private static final int ID_INDICATOR_VIEW = 0x6f0b0189;
	
	//此值以0x6f开头，是为了避免与ADT自动生成的ID冲突
	private static final int ID_RADIO_GROUP = 0x6f0b0191;
	
	//此值以0x6f开头，是为了避免与ADT自动生成的ID冲突
	private static final int ID_CONTENT_LATOUT = 0x6f0b0192;
	
	private OnTabChangeListener onTabChangeListener;
	
	private int oldIndex = 0;
	
	private SparseArray<Fragment> fragments = new SparseArray<Fragment>();
	
	/** 是否正在动画中 */
	private AtomicBoolean isAnimating = new AtomicBoolean(false);
	
	/**
	 * 
	 * @param context      上下文
	 * @param tabs         Tab列表
	 * @param orientation  排列方向，{@see RadioGroup.HORIZONTAL}和{@see RadioGroup.VERTICAL}
	 */
	public TabView(final FragmentActivity fragmentActivity, final ArrayList<Tab> tabs, int orientation, int initSelectionIndex) {
		super(fragmentActivity);
		
		//如果传入的tab列表是空的，就什么也不做
		if (tabs == null || tabs.isEmpty()) {
			return;
		}
		
		RadioGroup radioGroup = new RadioGroup(fragmentActivity);
		
		//如果传入的值不符合预期，给定一个符合预期的默认值
		if (orientation != RadioGroup.HORIZONTAL || orientation != RadioGroup.VERTICAL) {
			orientation = RadioGroup.HORIZONTAL;
		}
		
		//设置排列方向
		radioGroup.setOrientation(orientation);
		radioGroup.setId(ID_RADIO_GROUP);
		radioGroup.setBackgroundColor(Color.parseColor("#dbdbdb"));
		
		RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1);
		RadioGroup.LayoutParams lp2 = new RadioGroup.LayoutParams(2, LayoutParams.MATCH_PARENT);
		lp2.topMargin = UIUtil.dip2px(fragmentActivity, 5);
		lp2.bottomMargin = lp2.topMargin;
		
		int length = tabs.size();
		for (int i = 0; i < length; i++) {
			Tab tab = tabs.get(i);
			
			RadioButton radioButton = new RadioButton(fragmentActivity);
			radioButton.setId(i);
			radioButton.setGravity(Gravity.CENTER);
			radioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
			radioButton.setText(tab.getText());
			radioButton.setTextSize(18);
			radioButton.setTextColor(Color.BLACK);
			
			radioGroup.addView(radioButton, lp);
			
			if (i < length - 1) {
				ImageView seperator = new ImageView(fragmentActivity);
				seperator.setBackgroundColor(Color.parseColor("#6c7b8c"));
				radioGroup.addView(seperator, lp2);
			}
		}
		
		addView(radioGroup, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtil.dip2px(fragmentActivity, 60)));
		
		final ImageView indicator = new ImageView(fragmentActivity);
		indicator.setId(ID_INDICATOR_VIEW);
		indicator.setBackgroundColor(Color.parseColor("#FF8B02"));
		
		final int width = Environment.getInstance().getScreenWidth() / tabs.size();
		int heigth = UIUtil.dip2px(fragmentActivity, 2);
		RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(width, heigth);
		lp3.addRule(RelativeLayout.ALIGN_BOTTOM, ID_RADIO_GROUP);
		addView(indicator, lp3);
		
		LinearLayout contentLayout = new LinearLayout(fragmentActivity);
		contentLayout.setId(ID_CONTENT_LATOUT);
		RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp4.addRule(RelativeLayout.BELOW, ID_INDICATOR_VIEW);
		addView(contentLayout, lp4);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, final int checkedId) {
				//如果正在进行动画，不响应
				if (isAnimating.get()) {
					return;
				}
				
				isAnimating.set(true);
				
				DebugLog.d(TAG, "onCheckedChanged() checkedId = " + checkedId);
				
				TranslateAnimation animation = new TranslateAnimation(oldIndex * width, checkedId * width, 0, 0);
				animation.setFillBefore(false);
				animation.setFillBefore(false);
				//图片停在动画结束位置
				animation.setFillAfter(true);
				animation.setDuration(300);
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						animation.setAnimationListener(null);
						
						//动画结束后，执行真正的操作，避免抢占CPU，出现动画卡顿
						int currentIndex = checkedId;
						
						boolean needAdd = false;
						//将得当前的Fragment
						Fragment fragment = fragments.get(currentIndex);
						if (fragment == null) {
							fragment = tabs.get(currentIndex).getFragment();
							fragments.put(checkedId, fragment);
							
							needAdd = true;
						}
						
						FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
						
						if (oldIndex != currentIndex) {
							//先隐藏老的Fragment，然后添加或者显示当前的Fragment
							fragmentTransaction.hide(fragments.get(oldIndex));
						}
						
						if (needAdd) {
							fragmentTransaction.add(ID_CONTENT_LATOUT, fragment);
						} else {
							fragmentTransaction.show(fragment);
						}
						fragmentTransaction.commit();
						
						if (onTabChangeListener != null) {
							onTabChangeListener.onTabChange(oldIndex, currentIndex);
						}
						
						oldIndex = currentIndex;
						
						isAnimating.set(false);
					}
				});
				indicator.startAnimation(animation);
			}
		});

		//默认是第一个Tab页选中
		setCheckedId(radioGroup, initSelectionIndex);
	}
	
	/**
	 * RadioGroup的setCheckedId方法没有公布，所以通过反射进行设置
	 * @param radioGroup  父容器
	 * @param id          radioGroup中的radioButton的ID
	 */
	private void setCheckedId(RadioGroup radioGroup, int id) {
		Log.d(TAG, "setCheckedId()");
		
		Class<RadioGroup> clazz = RadioGroup.class;
		try {
			Method method = clazz.getDeclaredMethod("setCheckedId", int.class);
			method.setAccessible(true);
			method.invoke(radioGroup, id);
		} catch (Exception e) {
			Log.e(TAG, "setCheckedId()", e);
		}
	}
	
	public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener) {
		this.onTabChangeListener = onTabChangeListener;
	}
	
	/**
	 * Tab发生变化的回调
	 *
	 */
	public static interface OnTabChangeListener {
		
		/**
		 * Tab发生变化的回调
		 * @param oldTab 变化前的Tab
		 * @param newTab 变化后的Tab
		 */
		void onTabChange(int oldIndex, int currentIndex);
	}
}