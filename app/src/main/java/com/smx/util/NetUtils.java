package com.smx.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] infos = manager.getAllNetworkInfo();
		if (infos != null) {
			for (int i = 0; i < infos.length; i++) {
				if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isNetConnected(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} 
		return false;
	}
}
