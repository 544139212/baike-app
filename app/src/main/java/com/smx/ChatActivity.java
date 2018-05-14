package com.smx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.adapter.ChatAdapter;
import com.smx.adapter.FaceAdapter;
import com.smx.adapter.ChatPageAdapter;
import com.smx.dto.MessageListRespWsDTO;
import com.smx.dto.MessageWsDTO;
import com.smx.dto.ResultDTO;
import com.smx.jpush.JPushReceiver;
import com.smx.receiver.ConnectivityChangeReceiver;
import com.smx.util.BitmapUtils;
import com.smx.util.FaceUtil;
import com.smx.util.NetUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ChatActivity extends BasicActivity
        implements View.OnClickListener,
        View.OnLongClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        JPushReceiver.OnReceiverListener,
        ConnectivityChangeReceiver.OnChangeListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.tv_network)
    TextView tvNetwork;

    @BindView(R.id.wrl_chat)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.lv_chat)
    ListView listView;

    @BindView(R.id.iv_chat_voice)
    ImageView ivChatVoice;

    @BindView(R.id.iv_chat_keyboard_left)
    ImageView ivChatKeyboardLeft;

    @BindView(R.id.et_chat_input)
    EditText etChatInput;

    @BindView(R.id.b_chat_speak)
    Button bChatSpeak;

    @BindView(R.id.iv_chat_smile)
    ImageView ivChatSmile;

    @BindView(R.id.iv_chat_keyboard_right)
    ImageView ivChatKeyboardRight;

    @BindView(R.id.iv_chat_plus)
    ImageView ivChatPlus;

    @BindView(R.id.b_chat_send)
    Button bChatSend;

    @BindView(R.id.layout_smile)
    ViewPager layoutSmile;

    @BindView(R.id.layout_plus)
    ViewPager layoutPlus;

    InputMethodManager inputMethodManager;

    List<GridView> views;
    ChatPageAdapter chatPageAdapter;
    int currentFacePage = 0;
    int currentPlusPage = 0;

    String tPhone;
    List<MessageWsDTO> objects;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        JPushReceiver.mListeners.add(this);
        ConnectivityChangeReceiver.mListener = this;

        ivLeft.setOnClickListener(this);
        tvNetwork.setOnClickListener(this);
        ivChatVoice.setOnClickListener(this);
        ivChatKeyboardLeft.setOnClickListener(this);
        etChatInput.setOnClickListener(this);
        etChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    bChatSend.setVisibility(View.VISIBLE);
                    ivChatPlus.setVisibility(View.GONE);
                } else {
                    bChatSend.setVisibility(View.GONE);
                    ivChatPlus.setVisibility(View.VISIBLE);
                }
            }
        });
        bChatSpeak.setOnLongClickListener(this);
        ivChatSmile.setOnClickListener(this);
        ivChatKeyboardRight.setOnClickListener(this);
        ivChatPlus.setOnClickListener(this);
        bChatSend.setOnClickListener(this);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);


        // 初始化Face Page
        views = getFaceViews();
        chatPageAdapter = new ChatPageAdapter(views);
        layoutSmile.setAdapter(chatPageAdapter);
        layoutSmile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentFacePage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // 初始化Plus Page
        views = getPlusViews();
        chatPageAdapter = new ChatPageAdapter(views);
        layoutPlus.setAdapter(chatPageAdapter);
        layoutPlus.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPlusPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tPhone = getIntent().getStringExtra("T_PHONE");
        tvCenter.setText("与" + tPhone + "聊天中");
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        objects = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, objects);
        listView.setAdapter(chatAdapter);

        loadData(false);

        //因为推送服务不可用，无法接收推送消息，因此先通过构造定时器，以定时轮询方式获取消息，推送服务可用后屏蔽下面代码
        /*Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0x002);
            }
        }, 0, 30000);*/
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    private void loadData(final boolean isSwipeRefresh) {
        final Context context = this;
        SharedPreferences preferences = context.getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
        String oPhone = preferences.getString("O_PHONE", "");
        OkHttpUtils.get().url(Configuration.ws_url + "/message/getMessages")
                .addParams("oPhone", oPhone)
                .addParams("tPhone", tPhone)
                .build().execute(new Callback<MessageListRespWsDTO>() {
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
                chatAdapter.notifyDataSetChanged();
                listView.setSelection(chatAdapter.getCount());//默认显示位置为最后一条消息处

                if (isSwipeRefresh) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!NetUtils.isNetConnected(this)) {
            tvNetwork.setVisibility(View.VISIBLE);
        } else {
            tvNetwork.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JPushReceiver.mListeners.remove(this);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_left) {
            finish();
        }
        if (v.getId() == R.id.tv_network) {
            startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        }
        if (v.getId() == R.id.iv_chat_voice) {
            ivChatVoice.setVisibility(View.GONE);
            ivChatKeyboardLeft.setVisibility(View.VISIBLE);
            etChatInput.setVisibility(View.GONE);
//            etChatInput.requestFocus();
            bChatSpeak.setVisibility(View.VISIBLE);
            ivChatKeyboardRight.setVisibility(View.GONE);
            ivChatSmile.setVisibility(View.VISIBLE);
            bChatSend.setVisibility(View.GONE);
            ivChatPlus.setVisibility(View.VISIBLE);
            //表情区，附加区，软键盘
            layoutSmile.setVisibility(View.GONE);
            layoutPlus.setVisibility(View.GONE);
            inputMethodManager.hideSoftInputFromWindow(etChatInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (v.getId() == R.id.iv_chat_keyboard_left) {
            ivChatVoice.setVisibility(View.VISIBLE);
            ivChatKeyboardLeft.setVisibility(View.GONE);
            etChatInput.setVisibility(View.VISIBLE);
            etChatInput.requestFocus();
            bChatSpeak.setVisibility(View.GONE);
            ivChatKeyboardRight.setVisibility(View.GONE);
            ivChatSmile.setVisibility(View.VISIBLE);
            if (etChatInput.getText().length() > 0) {
                bChatSend.setVisibility(View.VISIBLE);
                ivChatPlus.setVisibility(View.GONE);
            } else {
                bChatSend.setVisibility(View.GONE);
                ivChatPlus.setVisibility(View.VISIBLE);
            }
            //表情区，附加区，软键盘
            layoutSmile.setVisibility(View.GONE);
            layoutPlus.setVisibility(View.GONE);
            inputMethodManager.showSoftInput(etChatInput, InputMethodManager.SHOW_IMPLICIT);
        }
        if (v.getId() == R.id.et_chat_input) {
            ivChatVoice.setVisibility(View.VISIBLE);
            ivChatKeyboardLeft.setVisibility(View.GONE);
            etChatInput.setVisibility(View.VISIBLE);
            etChatInput.requestFocus();
            bChatSpeak.setVisibility(View.GONE);
            ivChatKeyboardRight.setVisibility(View.GONE);
            ivChatSmile.setVisibility(View.VISIBLE);
//            bChatSend.setVisibility();
//            ivChatPlus.setVisibility();
            //表情区，附加区，软键盘
            layoutSmile.setVisibility(View.GONE);
            layoutPlus.setVisibility(View.GONE);
            inputMethodManager.showSoftInput(etChatInput, InputMethodManager.SHOW_IMPLICIT);
        }
        if (v.getId() == R.id.iv_chat_smile) {
            ivChatVoice.setVisibility(View.VISIBLE);
            ivChatKeyboardLeft.setVisibility(View.GONE);
            etChatInput.setVisibility(View.VISIBLE);
            etChatInput.requestFocus();
            bChatSpeak.setVisibility(View.GONE);
            ivChatKeyboardRight.setVisibility(View.VISIBLE);
            ivChatSmile.setVisibility(View.GONE);
            if (etChatInput.getText().length() > 0) {
                bChatSend.setVisibility(View.VISIBLE);
                ivChatPlus.setVisibility(View.GONE);
            } else {
                bChatSend.setVisibility(View.GONE);
                ivChatPlus.setVisibility(View.VISIBLE);
            }
            //表情区，附加区，软键盘
            layoutSmile.setVisibility(View.VISIBLE);
            layoutPlus.setVisibility(View.GONE);
            inputMethodManager.hideSoftInputFromWindow(etChatInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (v.getId() == R.id.iv_chat_plus) {
            ivChatVoice.setVisibility(View.VISIBLE);
            ivChatKeyboardLeft.setVisibility(View.GONE);
            etChatInput.setVisibility(View.VISIBLE);
            etChatInput.requestFocus();
            bChatSpeak.setVisibility(View.GONE);
            ivChatKeyboardRight.setVisibility(View.GONE);
            ivChatSmile.setVisibility(View.VISIBLE);
            bChatSend.setVisibility(View.GONE);
            ivChatPlus.setVisibility(View.VISIBLE);
            //表情区，附加区，软键盘
            layoutSmile.setVisibility(View.GONE);
            layoutPlus.setVisibility(View.VISIBLE);
            inputMethodManager.hideSoftInputFromWindow(etChatInput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (v.getId() == R.id.iv_chat_keyboard_right) {
            ivChatVoice.setVisibility(View.VISIBLE);
            ivChatKeyboardLeft.setVisibility(View.GONE);
            etChatInput.setVisibility(View.VISIBLE);
            etChatInput.requestFocus();
            bChatSpeak.setVisibility(View.GONE);
            ivChatKeyboardRight.setVisibility(View.GONE);
            ivChatSmile.setVisibility(View.VISIBLE);
//            bChatSend.setVisibility();
//            ivChatPlus.setVisibility();
            //表情区，附加区，软键盘
            layoutSmile.setVisibility(View.GONE);
            layoutPlus.setVisibility(View.GONE);
            inputMethodManager.showSoftInput(etChatInput, InputMethodManager.SHOW_IMPLICIT);
        }
        if (v.getId() == R.id.b_chat_send) {
            //推送消息并保存到服务器
            final Context context = this;
            SharedPreferences preferences = context.getSharedPreferences("CURRENT_USER", MODE_PRIVATE);
            String oPhone = preferences.getString("O_PHONE", "");
            OkHttpUtils.post().url(Configuration.ws_url + "/message/send")
                    .addParams("type", "02")
                    .addParams("oPhone", oPhone)
                    .addParams("tPhone", tPhone)
                    .addParams("message", etChatInput.getText().toString())
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
                        etChatInput.setText("");//清空输入框

                        loadData(false);

                        // 广播机制通信
                        Intent intent = new Intent(context.getPackageName() + ".action.REFRESH");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    } else {
                        Toast.makeText(ChatActivity.this, o.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onReceiverMessage(String message) {
        Message handlerMsg = handler.obtainMessage(0x001);
        handlerMsg.obj = message;
        handler.sendMessage(handlerMsg);
    }

    @Override
    public void onConnect() {
        tvNetwork.setVisibility(View.GONE);
    }

    @Override
    public void onDisConnect() {
        tvNetwork.setVisibility(View.VISIBLE);
    }



    private List<GridView> getFaceViews() {
        Map<String, Integer> map = FaceUtil.getFaceMap();
        final List<String> keys = new ArrayList<>();
        final List<Integer> values = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

        List<GridView> views = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            GridView gridView = new GridView(this);
            gridView.setNumColumns(7);
            gridView.setHorizontalSpacing(10);
            gridView.setVerticalSpacing(20);
            gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            gridView.setGravity(Gravity.CENTER);
            gridView.setBackgroundColor(Color.TRANSPARENT);

            final List<Integer> objects = new ArrayList<>();
            objects.addAll(values.subList(i * 20, (i + 1) * 20 < values.size() ? (i + 1) * 20 : values.size()));
            objects.add(R.mipmap.clear);
            gridView.setAdapter(new FaceAdapter(this, R.layout.item_img_chat, objects));

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 20) {
                        //删除键位置
                        int selectionStart = etChatInput.getSelectionStart();
                        if (selectionStart > 0) {
                            String text = etChatInput.getText().toString();
                            if ("]".equals(text.substring(selectionStart - 1))) {
                                etChatInput.getText().delete(text.lastIndexOf("["), selectionStart);
                                return;
                            }
                            etChatInput.getText().delete(selectionStart - 1, selectionStart);
                        }
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), objects.get(position));
                        Bitmap newBitmap = BitmapUtils.getScaleBitmap(bitmap, 30, 30);
                        ImageSpan imageSpan = new ImageSpan(ChatActivity.this, newBitmap);
                        String emojiStr = keys.get(currentFacePage * 20 + position);
                        SpannableString spannableString = new SpannableString(emojiStr);
                        spannableString.setSpan(imageSpan, emojiStr.indexOf('['), emojiStr.indexOf(']') + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        etChatInput.append(spannableString);
                    }
                }
            });



            views.add(gridView);
        }

        return views;
    }

    private List<GridView> getPlusViews() {
        List<GridView> views = new ArrayList<>();
        return views;
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
