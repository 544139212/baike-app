package com.smx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.smx.util.NetUtils;
import com.smx.util.ServiceUtil;

import java.lang.reflect.Method;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//			Toast.makeText(context, "网络连接状态变化", Toast.LENGTH_SHORT).show();
			ServiceUtil.startService(context);

			boolean isConnect = NetUtils.isNetConnected(context);
			if (isConnect) {
				if (mListener != null) {
					mListener.onConnect();
				}
			} else {
				if (mListener != null) {
					mListener.onDisConnect();
				}

				//尝试开启连接
				//开启Wi-Fi网络连接
				/*if (!isWifiEnabled(context)) {
					WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
					wifiManager.setWifiEnabled(true);
				}*/
				//开启数据网络连接
				/*if (!isCellEnabled(context)) {
					try {
						Class<?> connectivityManagerClass;

						ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
						connectivityManagerClass = Class.forName(connectivityManager.getClass().getName());

						java.lang.reflect.Field connectivityManagerField = connectivityManagerClass.getDeclaredField("mService");
						connectivityManagerField.setAccessible(true);

						Object iConnectivityManagerObject = connectivityManagerField.get(connectivityManager);
						Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManagerObject.getClass().getName());
						Method setMobileDataEnabledMethod =
								iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
						setMobileDataEnabledMethod.setAccessible(true);
						setMobileDataEnabledMethod.invoke(iConnectivityManagerObject, true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}*/
			}
		}
	}

	public static OnChangeListener mListener;

	public interface OnChangeListener {
		void onConnect();
		void onDisConnect();
	}

	private boolean isWifiEnabled(Context context) {
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}

	private boolean isCellEnabled(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			Method getMobileDataEnabledMethod = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
			getMobileDataEnabledMethod.setAccessible(true);
			return (Boolean) getMobileDataEnabledMethod.invoke(connectivityManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
