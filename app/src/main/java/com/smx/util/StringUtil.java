package com.smx.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class StringUtil {
	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNumeric(String str) {

		if (str == null || str.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}
	
	//http://www.cnblogs.com/wuhuisheng/archive/2011/02/27/1966197.html
	public static boolean isDecimal(String str) {
		
		if (str == null || str.length() == 0)
			return false;
		Pattern pattern = Pattern.compile("\\d+(\\.\\d*)?");
		return pattern.matcher(str).matches();
	}
	
	public static String getRoundValue(String str) {
		if (!isDecimal(str)) {
			return null;
		}
		return new BigDecimal(str).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
	}
	
}
