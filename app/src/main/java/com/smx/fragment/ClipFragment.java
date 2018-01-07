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
import com.smx.adapter.ClipAdapter;
import com.smx.dto.LinkListWsDTO;
import com.smx.dto.LinkListRespWsDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

public class ClipFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    public ClipFragment() {

    }

    public static ClipFragment newInstance() {
        ClipFragment fragment = new ClipFragment();
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
        View view = inflater.inflate(R.layout.fragment_clip, container, false);

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
        OkHttpUtils.get().url(Configuration.ws_url + "/link/getLinks").build().execute(new Callback<LinkListRespWsDTO>() {
            @Override
            public LinkListRespWsDTO parseNetworkResponse(Response response, int i) throws Exception {
                return new Gson().fromJson(response.body().string(), LinkListRespWsDTO.class);
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(LinkListRespWsDTO o, int i) {
                ClipAdapter clipAdapter = new ClipAdapter(context, R.layout.item_clip, o.getData().getList());
                listView.setAdapter(clipAdapter);
                clipAdapter.notifyDataSetChanged();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}
