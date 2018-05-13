package com.smx;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.dto.ResultDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class RequestActivity extends BasicActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.searchView)
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(this);
        tvCenter.setText("添加联系人");

        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        if (searchView != null) {// 去掉SearchView下划线
            try {        //--拿到字节码
                Class<?> argClass = searchView.getClass();
                //--指定某个私有属性,mSearchPlate是搜索框父布局的名字
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                //--暴力反射,只有暴力反射才能拿到私有属性
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                //--设置背景
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        final String tPhone = searchView.getQuery().toString();
        if (TextUtils.isEmpty(tPhone)) {
            Toast.makeText(this, "请填写手机号", Toast.LENGTH_SHORT).show();
            return false;
        }

        //推送消息并保存到服务器
        SharedPreferences preferences = getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
        String oPhone = preferences.getString("O_PHONE", "");
        OkHttpUtils.post().url(Configuration.ws_url + "/message/send")
                .addParams("type", "01")
                .addParams("oPhone", oPhone)
                .addParams("tPhone", tPhone)
                .addParams("message", "/勾引/勾引，快来聊天吧")
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
                    // 成功
                    finish();
                } else {
                    Toast.makeText(RequestActivity.this, o.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
