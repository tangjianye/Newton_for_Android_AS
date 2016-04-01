package com.fpliu.newton.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 与UI相关的帮助类
 * 
 * @author 792793182@qq.com 2014-10-15
 *
 */
public final class StringUtil {

	public static String replaceBlank(String str) {
		String dest = "";
		
		if (TextUtils.isEmpty(str)) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		
		return dest;
	}
	
	public static boolean isPhomeNumber(String phoneNumber) {
		if (TextUtils.isEmpty(phoneNumber)) {
			return false;
		}
		
		phoneNumber = phoneNumber.trim();
		if (phoneNumber.length() == 11 && phoneNumber.startsWith("1")) {
			return true;
		}

		return false;
	}
}
