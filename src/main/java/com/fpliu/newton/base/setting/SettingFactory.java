package com.fpliu.newton.base.setting;

import android.content.Context;

/**
 * ISettings的工厂类，避免外部直接依赖他的实现类
 * 
 * @author 792793182@qq.com 2014-9-22
 *
 */
public final class SettingFactory {
	
	private SettingFactory() {}

	public static ISetting newInstance(Context context, String name) {
		return new SettingImpl(context, name);
	}
}
