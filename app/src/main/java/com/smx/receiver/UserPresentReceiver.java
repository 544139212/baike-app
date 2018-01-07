package com.smx.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smx.util.ServiceUtil;

public class UserPresentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
//			Toast.makeText(context, "屏幕已解锁", Toast.LENGTH_SHORT).show();
			ServiceUtil.startService(context);
        }
    }

}
