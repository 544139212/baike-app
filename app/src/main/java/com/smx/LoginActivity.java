package com.smx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.dto.ResultDTO;
import com.smx.util.AndroidBug5497Workaround;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.etPhone)
    EditText etPhone;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        AndroidBug5497Workaround.assistActivity(this);

        ButterKnife.bind(this);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(etPassword.getText())) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(etPhone.getText())) {
                    btnLogin.setEnabled(true);
                } else {
                    btnLogin.setEnabled(false);
                }
            }
        });
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            final String phone = etPhone.getText().toString();
            final String password = etPassword.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "请填写手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "请填写密码", Toast.LENGTH_SHORT).show();
                return;
            }

            String regId;
            do {
                regId = JPushInterface.getRegistrationID(getApplicationContext());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (TextUtils.isEmpty(regId));

            /*OkHttpUtils.post().url(Configuration.ws_url + "/user/add")
                    .addParams("phone", phone)
                    .addParams("password", password)
                    .addParams("regId", regId)
                    .build().execute(new Callback<ResultDTO>() {
                @Override
                public ResultDTO parseNetworkResponse(Response response, int i) throws Exception {
                    return new Gson().fromJson(response.body().string(), ResultDTO.class);
                }

                @Override
                public void onError(Call call, Exception e, int i) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(ResultDTO o, int i) {
                    if (o.getCode() == 200) {*/
                        // 成功
                        SharedPreferences preferences = getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("O_PHONE", phone);
                        editor.commit();

                        goAndFinish(LoginActivity.this, MainActivity.class);
                    /*} else {
                        Toast.makeText(LoginActivity.this, o.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });*/
        }
    }

}
