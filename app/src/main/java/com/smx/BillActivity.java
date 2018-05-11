package com.smx;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.smx.adapter.BillAdapter;
import com.smx.dto.BillListRespWsDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class BillActivity extends BasicActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.wrl_bill)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.elv_bill)
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        ButterKnife.bind(this);

        ivLeft.setOnClickListener(this);
        tvCenter.setText("账务明细");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go(BillActivity.this, CreateActivity.class);
            }
        });

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
        OkHttpUtils.get().url(Configuration.ws_url + "/bill/getBills").build().execute(new Callback<BillListRespWsDTO>() {
            @Override
            public BillListRespWsDTO parseNetworkResponse(Response response, int i) throws Exception {
                return new Gson().fromJson(response.body().string(), BillListRespWsDTO.class);
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(BillListRespWsDTO o, int i) {
                BillAdapter billAdapter = new BillAdapter(context, o.getData());
                expandableListView.setAdapter(billAdapter);
                int count = expandableListView.getCount();
                for (int groupPos = 0; groupPos < count; groupPos++) {
                    expandableListView.expandGroup(groupPos);
                }
                expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                        return true;
                    }
                });
//                billAdapter.notifyDataSetChanged();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
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
}
