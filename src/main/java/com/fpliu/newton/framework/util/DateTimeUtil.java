package com.fpliu.newton.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期和时间工具类
 * 
 * @author 792793182@qq.com 2014-12-04
 *
 */
public final class DateTimeUtil {

	public DateTimeUtil() { }
	
	/** 一分钟的毫秒数 */
	public static final long ONE_MINUTES = 1000 * 60;
	
	/** 一小时的毫秒数 */
	public static final long ONE_HOUR = 60 * ONE_MINUTES;
	
	/** 一天的毫秒数 */
	public static final long ONE_DAY = 24 * ONE_HOUR;
	
	/** 一周的毫秒数 */
	public static final long ONE_WEEK = 7 * ONE_DAY;
	
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINESE);
	
	/**
	 * 格式化
	 * @param dateTime  时间
	 * @return          格式化后的时间
	 */
	public static String format(long dateTime) {
		return DATE_FORMAT.format(dateTime);
	}
	
	/** 判断是否是昨天 */
	public static boolean isYestoday(long dateTime) {
		return compareDay(new Date(dateTime), new Date(System.currentTimeMillis())) == 1;
	}
	
	/** 判断是否是今天 */
	public static boolean isToday(long dateTime) {
		return compareDay(new Date(dateTime), new Date(System.currentTimeMillis())) == 0;
	}
	
	/** 判断是否是明天 */
	public static boolean isTomorrow(long dateTime) {
		return compareDay(new Date(dateTime), new Date(System.currentTimeMillis())) == -1;
	}
	
	/**
	 * 比较是同一天，还是前面的比后面的早，还是后面的比前面的早
	 * @param oldDate 形式上的更早者
	 * @param newDate 形式上的更晚者
	 * 返回天数之差
	 */
    public static int compareDay(Date oldDate, Date newDate) {
		Calendar oldCalendar = Calendar.getInstance();
		oldCalendar.setTime(oldDate);
		int oldYear = oldCalendar.get(Calendar.YEAR);
		int oldMonth = oldCalendar.get(Calendar.MONTH) + 1;
		int oldDay = oldCalendar.get(Calendar.DAY_OF_MONTH);
		
		Calendar newCalendar = Calendar.getInstance();
		newCalendar.setTime(newDate);
		int newYear = newCalendar.get(Calendar.YEAR);
		int newMonth = newCalendar.get(Calendar.MONTH) + 1;
		int newDay = newCalendar.get(Calendar.DAY_OF_MONTH);
		
		int year = newYear - oldYear;
		if (year == 0) {
			int month = newMonth - oldMonth;
			if (month == 0) {
				return newDay - oldDay;
			} else {
				return month * 30;
			}
		} else {
			return year * 365;
		}
	}
    
}
