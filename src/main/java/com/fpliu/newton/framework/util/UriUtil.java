package com.fpliu.newton.framework.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

/**
 * 
 * 资源定位符工具类
 * 
 * @author 792793182@qq.com 2014-9-28 
 *
 */
public final class UriUtil {

	private UriUtil() {}
	
	public static Uri fromRes(Context context, int resId) {
		if (context ==  null || resId <= 0) {
			return null;
		}
		
		Resources r = context.getResources();
		return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
		        + r.getResourcePackageName(resId) + "/"
				+ r.getResourceTypeName(resId) + "/"
				+ r.getResourceEntryName(resId));
	}
}
