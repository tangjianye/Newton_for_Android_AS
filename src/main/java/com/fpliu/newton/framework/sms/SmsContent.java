package com.fpliu.newton.framework.sms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fpliu.newton.base.DebugLog;

import android.app.Activity;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;

/*
 * 监听短信数据库
 */
public class SmsContent extends ContentObserver {
	
	private static final String TAG = SmsContent.class.getSimpleName();
	
	private Activity activity = null;
	private SmsContentChangeCallback smsContentChangeCallback;

	public SmsContent(Activity activity, SmsContentChangeCallback callback) {
		super(new Handler());
		this.activity = activity;
		smsContentChangeCallback = callback;
	}

	@Override
	public void onChange(boolean selfChange) {
		DebugLog.d(TAG, "onChange()");
		
		super.onChange(selfChange);
		// 读取收件箱中指定号码的短信
		Cursor cursor = activity.getContentResolver().query(
				Uri.parse("content://sms/inbox"),
				new String[] { "_id", "address", "read", "body" },
				" address=? and read=?", new String[] { "106901987291", "0" },
				"_id desc");
		// 按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
		if (cursor != null && cursor.getCount() > 0) {
			ContentValues values = new ContentValues();
			values.put("read", "1"); // 修改短信为已读模式
			cursor.moveToNext();
			int smsbodyColumn = cursor.getColumnIndex("body");
			String smsBody = cursor.getString(smsbodyColumn);

			if (smsContentChangeCallback != null) {
				smsContentChangeCallback.onChange(getDynamicPass(smsBody));
			}
		}
		// 在用managedQuery的时候，不能主动调用close()方法， 否则在<a class="keylink"
		// href="http://www.it165.net/pro/ydad/" target="_blank">Android</a>
		// 4.0+的系统上， 会发生崩溃
		if (Build.VERSION.SDK_INT < 14) {
			cursor.close();
		}
	}

	/**
	 * 从字符串中截取连续6位数字组合 ([0-9]{" + 6 + "})截取六位数字 进行前后断言不能出现数字 用于从短信中获取动态密码
	 * 
	 * @param str
	 *            短信内容
	 * @return 截取得到的6位动态密码
	 */
	private static String getDynamicPass(String str) {
		DebugLog.d(TAG, "getDynamicPass() str = " + str);
		
		// 6是验证码的位数一般为六位
		Pattern continuousNumberPattern = Pattern.compile("(?<![0-9])([0-9]{"
				+ 5 + "})(?![0-9])");
		Matcher m = continuousNumberPattern.matcher(str);
		String dynamicPassword = "";
		while (m.find()) {
			dynamicPassword = m.group();
		}

		return dynamicPassword;
	}

}
