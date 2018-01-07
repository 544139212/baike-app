package com.smx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.smx.util.ServiceUtil;

public class WifiChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {

			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			if (wifiInfo.getBSSID() != null) {
				String ip = intToIp(wifiInfo.getIpAddress());
				String mac = wifiInfo.getMacAddress();
				String ssid = wifiInfo.getSSID();
				int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5);
				int linkSpeed = wifiInfo.getLinkSpeed();
				String linkSpeedUnits = WifiInfo.LINK_SPEED_UNITS;
//				Toast.makeText(context, "Wi-Fi已连接到" + ssid + ",信号强度" + signalLevel + ",连接速度" + linkSpeed + linkSpeedUnits, Toast.LENGTH_LONG).show();
			}
		} else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
			NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
			if (networkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)) {
//				Toast.makeText(context, "Wi-Fi连接断开", Toast.LENGTH_LONG).show();
			} else if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//				Toast.makeText(context, "Wi-Fi已连接到" + wifiInfo.getSSID(), Toast.LENGTH_LONG).show();
			}
		} else if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
			if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
//				Toast.makeText(context, "Wi-Fi已关闭", Toast.LENGTH_LONG).show();
			}
			if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
//				Toast.makeText(context, "Wi-Fi已开启", Toast.LENGTH_LONG).show();
			}
		}

		ServiceUtil.startService(context);
	}

	public String intToIp(int i) {
		return ( i & 0xFF) + "." + ((i >> 8 ) & 0xFF) + "." + ((i >> 16 ) & 0xFF) + "." + ((i >> 24 ) & 0xFF );
	}

}
