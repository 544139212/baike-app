package com.smx.util;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.smx.R;


/**
 * Created by vivo on 2017/10/5.
 */

public class NotifyUtil {

    public static void showNotify(Context context, String message) {
        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("消息提醒").setContentText(message);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.notify();
    }

}
