package com.smx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BasicActivity {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.tv_test)
    TextView tvTest;

    @BindView(R.id.layout_about_us)
    RelativeLayout layoutAboutUs;

    @BindView(R.id.btnExit)
    Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAndFinish(SettingsActivity.this, MainActivity.class);
            }
        });
        tvCenter.setText("设置");

        tvTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TestActivity.class);
                startActivity(intent);
            }
        });
        layoutAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
