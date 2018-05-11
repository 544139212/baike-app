package com.smx.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.smx.Configuration;
import com.smx.R;
import com.smx.adapter.MessageAdapter;
import com.smx.dto.BillListRespWsDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    public MessageFragment() {

    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        listView = (ListView) view.findViewById(R.id.listView);

        loadData(false);

        return view;
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(final boolean isSwipeRefresh) {
        final Context context = this.getActivity();
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
