package com.smx.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.smx.Configuration;
import com.smx.R;
import com.smx.adapter.MessageAdapter;
import com.smx.dto.MessageListRespWsDTO;
import com.smx.dto.MessageWsDTO;
import com.smx.jpush.JPushReceiver;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class MessageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, JPushReceiver.OnReceiverListener {

    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;

    RefreshReceiver refreshReceiver;

    List<MessageWsDTO> objects;
    MessageAdapter messageAdapter;

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

        JPushReceiver.mListeners.add(this);
        refreshReceiver = new RefreshReceiver();
        IntentFilter intentFilter = new IntentFilter(this.getActivity().getPackageName() + ".action.REFRESH");
        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(refreshReceiver, intentFilter);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        listView = (ListView) view.findViewById(R.id.listView);

        objects = new ArrayList<>();
        messageAdapter = new MessageAdapter(this.getActivity(), R.layout.item_message, objects);
        listView.setAdapter(messageAdapter);

        loadData(false);

        return view;
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(final boolean isSwipeRefresh) {
        final Context context = this.getActivity();
        SharedPreferences preferences = context.getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
        String oPhone = preferences.getString("O_PHONE", "");
        OkHttpUtils.get().url(Configuration.ws_url + "/message/getRecentContacts").addParams("oPhone", oPhone).build().execute(new Callback<MessageListRespWsDTO>() {
            @Override
            public MessageListRespWsDTO parseNetworkResponse(Response response, int i) throws Exception {
                return new Gson().fromJson(response.body().string(), MessageListRespWsDTO.class);
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(MessageListRespWsDTO o, int i) {
                objects.clear();
                objects.addAll(o.getData());
                messageAdapter.notifyDataSetChanged();

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JPushReceiver.mListeners.remove(this);
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(refreshReceiver);
    }

    @Override
    public void onReceiverMessage(String message) {
        Message handlerMsg = handler.obtainMessage(0x001);
        handlerMsg.obj = message;
        handler.sendMessage(handlerMsg);
    }

    class RefreshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            loadData(false);
        }
    }

    //处理消息
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
            if (msg.what == 0x001) {
                // 已收到消息内容
//                Object obj = msg.obj;

                loadData(false);
            }
            if (msg.what == 0x002) {
                loadData(false);
            }
        }
    };
}
