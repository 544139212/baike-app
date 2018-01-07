package com.smx.baidupush;

import android.os.AsyncTask;
import android.text.TextUtils;

/**
 * Created by vivo on 2017/10/5.
 */

public class BaiduPushSender {

    public static OnSenderListener mListener;

    public interface OnSenderListener {
        void onSendSuccess();
        void onSendFailed();
    }

    static class BaiduPushAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... message) {
            String result;
            if(message.length == 1 || TextUtils.isEmpty(message[1])) {
                result = BaiduPush.getBaiduPush().PushMessage(message[0]);
            } else {
                result = BaiduPush.getBaiduPush().PushMessage(message[0], message[1]);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (result.contains(BaiduPush.SEND_MSG_ERROR)) {
                if (mListener != null) {
                    mListener.onSendFailed();
                }
            } else {
                if (mListener != null) {
                    mListener.onSendSuccess();
                }
            }
        }
    }

    public static void send(String... message) {
        BaiduPushAsyncTask mTask = new BaiduPushAsyncTask();
        mTask.execute(message);
    }
}
