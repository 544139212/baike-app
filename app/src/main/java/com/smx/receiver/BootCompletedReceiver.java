package com.smx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smx.util.ServiceUtil;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
//			Toast.makeText(context, "启动完成", Toast.LENGTH_SHORT).show();
			ServiceUtil.startService(context);
		}
	}

}
