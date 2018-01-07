package com.smx.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;

import com.smx.service.LocationService;

import java.util.List;

public class ServiceUtil {
	public static void startService(Context context) {
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(Integer.MAX_VALUE);
		
		boolean isServiceRunning = false;
		if (runningServiceInfoList != null && !runningServiceInfoList.isEmpty()) {
			for (RunningServiceInfo runningServiceInfo : runningServiceInfoList) {
				if ("com.example.demo.service.LocationUpdateService".equals(runningServiceInfo.service.getClassName())) {
					isServiceRunning = true;
					break;
				}
			}
		}
		
//		Toast.makeText(context, "位置服务" + (isServiceRunning ? "已启动" : "未启动"), Toast.LENGTH_LONG).show();

		if (!isServiceRunning) {
			Intent i = new Intent(context, LocationService.class);
			context.startService(i);
		}
	} 
}
