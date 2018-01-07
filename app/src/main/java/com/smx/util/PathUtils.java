package com.smx.util;

import java.io.File;
import android.os.Environment;

public class PathUtils {
	public static String getBasePath() {
		String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "DEMO_PUSH";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}
	
	public static String getTempPath(String suffix) {
		return getBasePath() + File.separator + "temp" + suffix;
	}
	
	public static String getLogPath(String filename) {
		return getBasePath() + File.separator + filename;
	}
	
	public static String getPath(String aspect, String category, String type, String filename) {
		String path = getBasePath();
		if (StringUtil.isNotEmpty(aspect)) {
			path += File.separator + aspect;
		} 
		if (StringUtil.isNotEmpty(category)) {
			path += File.separator + category;
		}
		if (StringUtil.isNotEmpty(type)) {
			path += File.separator + type;
		}
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (StringUtil.isNotEmpty(filename)) {
			path += File.separator + filename;
		}
		return path;
	}
}
