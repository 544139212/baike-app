package com.smx;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
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
            try {
                Class<?> argClass = searchView.getClass();
                Field ownField = argClass.getDeclaredField("mSearchPlate");
                ownField.setAccessible(true);
                View mView = (View) ownField.get(searchView);
                mView.setBackgroundColor(Color.TRANSPARENT);
            } catch(Exception e){
//                e.printStackTrace();
            }

            // 修改清除icon颜色和大小，颜色为#bebebe,大小为28x28
            int closeIconId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
            ImageView closeImageView = (ImageView) searchView.findViewById(closeIconId);
            closeImageView.setImageResource(R.mipmap.clear);

            // 修改搜索icon颜色和大小，颜色为#bebebe,大小为28x28
            int searchIconId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon", null, null);
            ImageView searchImageView = (ImageView) searchView.findViewById(searchIconId);
            searchImageView.setImageResource(R.mipmap.search);

            // 方式一：
            // 如果xml中指定android:iconifiedByDefault="false", 则放大镜在文本框之外，输入文字后放大镜仍在;
            // searchView.onActionViewExpanded();这行代码有没有都行;
            // 下面其它行代码只能改变文字大小位置及颜色，不能改变放大镜大小和颜色;放大镜默认是垂直居中，不需要修改，通过下面代码可将文字也垂直居中
            // 不足之处是: 放大镜的大小，颜色都不能改变，仍未深黑色（可通过上面方法强制替换）
            //方式二：
            // 如果xml不指定android:iconifiedByDefault="false"则放大镜在文本框之内, 输入文字后放大镜消失
            // 必须通过searchView.onActionViewExpanded()来指定搜索框为展开状态;
            // 下面其它行代码同时改变了放大镜的大小和颜色以及字体的大小和颜色；放大镜仍是垂直居中，不需要改
            // 不足之处是：字体的位置变得靠下，而放大镜是垂直居中
            searchView.onActionViewExpanded();
            int textId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = (TextView) searchView.findViewById(textId);
            textView.clearFocus();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setTextColor(getResources().getColor(R.color.textColor));
            textView.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
            android.widget.LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            textView.setLayoutParams(layoutParams);
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
