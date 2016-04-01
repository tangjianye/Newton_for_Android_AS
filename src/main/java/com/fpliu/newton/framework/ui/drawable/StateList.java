package com.fpliu.newton.framework.ui.drawable;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;


/**
 * 状态列表
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class StateList {

	private StateList() { }
	
	public static StateListDrawable get() {
		//Drawable normalDrawable = ShapeFactory.getRoundRectShapeDrawable(Color.parseColor("#B5130E"));
		Drawable normalDrawable = ShapeFactory.getRoundRectShapeDrawable(Color.parseColor("#CC4242"));
		Drawable pressedDrawable = ShapeFactory.getRoundRectShapeDrawable(Color.parseColor("#ff8c00"));
		Drawable disabledDrawable = ShapeFactory.getRoundRectShapeDrawable(Color.parseColor("#A9A9A9"));
		return StateListDrawableFactory.getStateListDrawable(normalDrawable, pressedDrawable, disabledDrawable);
	}
	
	public static StateListDrawable get2() {
		Drawable normalDrawable = new ColorDrawable(Color.RED);
		Drawable pressedDrawable = new ColorDrawable(Color.parseColor("#ff8c00"));
		Drawable disabledDrawable = ShapeFactory.getRoundRectShapeDrawable(Color.parseColor("#A9A9A9"));
		return StateListDrawableFactory.getStateListDrawable(normalDrawable, pressedDrawable, disabledDrawable);
	}
}
