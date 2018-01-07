package com.smx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            SharedPreferences preferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("INDICATOR", "Y");
            editor.commit();

            goAndFinish(LoginActivity.this, MainActivity.class);
        }
    }
}
