package com.fpliu.newton.business.account;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.fpliu.newton.MyApp;
import com.fpliu.newton.R;
import com.fpliu.newton.business.config.SettingConfig;
import com.fpliu.newton.framework.util.BitmapUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 头像管理
 * 
 * @author 792793182@qq.com 2014-10-17
 *
 */
final class AvatarManager {
	
	public static DisplayImageOptions getDisplayImageOptions() {
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		
		String uri = MyApp.getApp().getSetting().getString(SettingConfig.KEY_AVATAR_PATH, "");
		
		if (TextUtils.isEmpty(uri)) {
			builder.showImageForEmptyUri(R.drawable.ic_logo);
			builder.showImageOnFail(R.drawable.ic_logo);
		} else {
			Drawable drawable = Drawable.createFromPath(uri);
			builder.showImageForEmptyUri(drawable);
			builder.showImageOnFail(drawable);
		}
		
		return builder.build();
	}
	
	/**
	 * 保存头像到用户目录下
	 * 
	 * @param bitmap   头像
	 */
	public static void saveToFile(Bitmap bitmap) {
		String avatarPath = UserManager.getInstance().getCurrentUserDir() + "/avatar.jpg";
		boolean isSuccessed = BitmapUtil.saveBitmapToFile(bitmap, avatarPath, Bitmap.CompressFormat.JPEG);
        if (isSuccessed) {
        	MyApp.getApp().getSetting().setSetting(SettingConfig.KEY_AVATAR_PATH, avatarPath);
		}
	}
}
