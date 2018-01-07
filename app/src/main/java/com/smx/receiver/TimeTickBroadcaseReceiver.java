package com.smx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smx.util.ServiceUtil;

public class TimeTickBroadcaseReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
//			Toast.makeText(context, "时间变化", Toast.LENGTH_SHORT).show();
			ServiceUtil.startService(context);
		}
	}

}
