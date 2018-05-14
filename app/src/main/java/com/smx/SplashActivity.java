package com.smx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

public class SplashActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("GUIDE", MODE_PRIVATE);
                String indicator = preferences.getString("INDICATOR", "N");
                if (!TextUtils.equals(indicator, "Y")) {
                    goAndFinish(SplashActivity.this, GuideActivity.class);
                } else {
                    preferences = getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
                    String phone = preferences.getString("O_PHONE", "");
                    if (TextUtils.isEmpty(phone)) {
                        goAndFinish(SplashActivity.this, LoginActivity.class);
                    } else {
                        goAndFinish(SplashActivity.this, MainActivity.class);
                    }
                }
            }
        }, 1000);
    }

}
