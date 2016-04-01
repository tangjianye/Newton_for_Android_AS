package com.fpliu.newton.framework.ui.drawable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

/**
 * 生产不同状态效果图片列表，为了使用View中的Protected属性，不得不继承自View
 * 
 * @author 792793182@qq.com 2014-10-11
 * 
 */
public final class StateListDrawableFactory extends View {

	/**
	 * 构造方法私有化，不允许外部new对象，本类中的方法都是静态的
	 */
	private StateListDrawableFactory(Context context) {
		super(context);
	}
	 
	 /**
	  * 设置普通、聚焦、高亮的背景
	  * @param normalDrawable      正常效果
	  * @param pressedDrawable     按下效果
	  * @param disabledDrawable    不可点击效果
	  */
	public static StateListDrawable getStateListDrawable(Drawable normalDrawable, Drawable pressedDrawable, Drawable disabledDrawable) {
		StateListDrawable stateListDrawable = new StateListDrawable();

		int enabled = android.R.attr.state_enabled;
		int focused = android.R.attr.state_focused;
		
		stateListDrawable.addState(new int[]{-View.ENABLED_STATE_SET[0]}, disabledDrawable);
		stateListDrawable.addState(View.PRESSED_ENABLED_STATE_SET, pressedDrawable);
		stateListDrawable.addState(View.ENABLED_FOCUSED_STATE_SET, pressedDrawable);
		stateListDrawable.addState(new int[]{enabled, -focused}, normalDrawable);
		stateListDrawable.addState(new int[]{-View.WINDOW_FOCUSED_STATE_SET[0]}, normalDrawable);
		stateListDrawable.addState(View.EMPTY_STATE_SET, normalDrawable);
		
		return stateListDrawable;
	}
	
	public static StateListDrawable getStateListDrawable(Drawable enabledAndUncheckedDrawable, 
														 Drawable enabledAndCheckedDrawable, 
														 Drawable disabledAndUncheckedDrawable,
														 Drawable disabledAndCheckedDrawable) {

		int enabled = android.R.attr.state_enabled;
		int checked = android.R.attr.state_checked;
		
		StateListDrawable bg = new StateListDrawable();
		bg.addState(new int[]{enabled, checked}, enabledAndCheckedDrawable);
		bg.addState(new int[]{enabled, -checked}, enabledAndUncheckedDrawable);
		bg.addState(new int[]{-enabled, checked}, disabledAndCheckedDrawable);
		bg.addState(new int[]{-enabled, -checked}, disabledAndUncheckedDrawable);
		
		return bg;
	}
}
