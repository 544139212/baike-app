package com.smx.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	final static String PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	final static String DATE_PATTERN = "yyyy-MM-dd";
	
	public static String getTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat(PATTERN);
		return sdf.format(new Date());
	}
	
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		return sdf.format(new Date());
	}
	
	public static String getCurrentWeek() {
		Calendar c = Calendar.getInstance();
		int num = c.get(Calendar.DAY_OF_WEEK);
		
		String week = null;
		switch (num) {
		case 1:
			week = "星期日";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
			break;
		default:
			week = "";
			break;
		}
		return week;
	}
	
}
