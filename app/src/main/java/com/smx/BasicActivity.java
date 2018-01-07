package com.smx;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class BasicActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*@SuppressWarnings("unchecked")
    public final <E extends View> E getView (int id) {
        try {
            return (E) findViewById(id);
        } catch (ClassCastException ex) {
            Log.e("com.example.demo", "Could not cast View to concrete class.", ex);
            throw ex;
        }
    }*/

    @SuppressWarnings("rawtypes")
    protected void goAndFinish(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @SuppressWarnings("rawtypes")
    protected void go(Context packageContext, Class<?> cls) {
        Intent intent = new Intent(packageContext, cls);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

}
