package com.smx;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.dto.ResultDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class CreateActivity extends BasicActivity implements View.OnClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.tv_right)
    TextView tvRight;

    @BindView(R.id.et_name)
    EditText etName;

    @BindView(R.id.et_cost)
    EditText etCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(this);
        tvCenter.setText("账务录入");
        tvRight.setText("保存");
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            finish();
        } else if (v.getId() == R.id.tv_right) {
            String name = etName.getText().toString();
            String cost = etCost.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "请填写事项", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(cost)) {
                Toast.makeText(this, "请填写金额", Toast.LENGTH_SHORT).show();
                return;
            }

            OkHttpUtils.post().url(Configuration.ws_url + "/bill/add")
                    .addParams("name", name)
                    .addParams("cost", cost)
                    .build().execute(new Callback<ResultDTO>() {
                @Override
                public ResultDTO parseNetworkResponse(Response response, int i) throws Exception {
                    return new Gson().fromJson(response.body().string(), ResultDTO.class);
                }

                @Override
                public void onError(Call call, Exception e, int i) {

                }

                @Override
                public void onResponse(ResultDTO o, int i) {
                    if (o.getCode() == 200) {
                        finish();
                    } else {
                        Toast.makeText(CreateActivity.this, o.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}
