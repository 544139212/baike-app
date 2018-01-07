/**
 * 
 */
package com.smx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.smx.util.ServiceUtil;

/**
 * @author Administrator
 *
 */
public class MobileChangeReceiver extends BroadcastReceiver {

	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//			Toast.makeText(context, "电话号码：" + phoneNumber, Toast.LENGTH_SHORT).show();
			ServiceUtil.startService(context);
		}
	}

}
