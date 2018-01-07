package com.smx;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.adapter.MessageAdapter;
import com.smx.dto.LocationListWsDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class MessageActivity extends BasicActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.wrl_message)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.lv_message)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAndFinish(MessageActivity.this, MainActivity.class);
            }
        });
        tvCenter.setText("消息");

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        loadData(false);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(final boolean isSwipeRefresh) {
        final Context context = this;
        OkHttpUtils.get().url(Configuration.ws_url + "/location/getLocations").build().execute(new Callback<LocationListWsDTO>() {
            @Override
            public LocationListWsDTO parseNetworkResponse(Response response, int i) throws Exception {
                return new Gson().fromJson(response.body().string(), LocationListWsDTO.class);
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(LocationListWsDTO o, int i) {
                MessageAdapter messageAdapter = new MessageAdapter(context, R.layout.item_message, o.getData());
                listView.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
