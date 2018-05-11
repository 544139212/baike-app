package com.smx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

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
                if ("Y".equals(indicator)) {
                    goAndFinish(SplashActivity.this, GuideActivity.class);
                } else {
                    goAndFinish(SplashActivity.this, GuideActivity.class);
//                    goAndFinish(SplashActivity.this, BillActivity.class);
                }
            }
        }, 3500);
    }

}
