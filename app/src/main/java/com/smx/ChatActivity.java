package com.smx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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

import com.google.gson.Gson;
import com.smx.adapter.ChatAdapter;
import com.smx.adapter.FaceAdapter;
import com.smx.adapter.FacePageAdapter;
import com.smx.dto.MessageListRespWsDTO;
import com.smx.dto.MessageWsDTO;
import com.smx.dto.ResultDTO;
import com.smx.jpush.JPushReceiver;
import com.smx.receiver.ConnectivityChangeReceiver;
import com.smx.util.NetUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

public class ChatActivity extends BasicActivity
        implements View.OnClickListener,
        View.OnLongClickListener,
        TextWatcher,
        ViewPager.OnPageChangeListener,
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
    TextView layoutPlus;

    InputMethodManager inputMethodManager;

    int currentPage = 0;

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
        etChatInput.addTextChangedListener(this);
        bChatSpeak.setOnLongClickListener(this);
        ivChatSmile.setOnClickListener(this);
        ivChatKeyboardRight.setOnClickListener(this);
        ivChatPlus.setOnClickListener(this);
        bChatSend.setOnClickListener(this);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);



        List<GridView> views = getFaceViews();
        FacePageAdapter facePageAdapter = new FacePageAdapter(views);
        layoutSmile.setAdapter(facePageAdapter);
        layoutSmile.addOnPageChangeListener(this);

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
                    }
                }
            });
        }
    }

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

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        currentPage = i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

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

    private Map<String, Integer> getFaceMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("[呲牙]", R.mipmap.f000);
        map.put("[调皮]", R.mipmap.f001);
        map.put("[流汗]", R.mipmap.f002);
        map.put("[偷笑]", R.mipmap.f003);
        map.put("[再见]", R.mipmap.f004);
        map.put("[敲打]", R.mipmap.f005);
        map.put("[擦汗]", R.mipmap.f006);
        map.put("[猪头]", R.mipmap.f007);
        map.put("[玫瑰]", R.mipmap.f008);
        map.put("[流泪]", R.mipmap.f009);
        map.put("[大哭]", R.mipmap.f010);
        map.put("[嘘]", R.mipmap.f011);
        map.put("[酷]", R.mipmap.f012);
        map.put("[抓狂]", R.mipmap.f013);
        map.put("[委屈]", R.mipmap.f014);
        map.put("[便便]", R.mipmap.f015);
        map.put("[炸弹]", R.mipmap.f016);
        map.put("[菜刀]", R.mipmap.f017);
        map.put("[可爱]", R.mipmap.f018);
        map.put("[色]", R.mipmap.f019);
        map.put("[害羞]", R.mipmap.f020);
        map.put("[得意]", R.mipmap.f021);
        map.put("[吐]", R.mipmap.f022);
        map.put("[微笑]", R.mipmap.f023);
        map.put("[发怒]", R.mipmap.f024);
        map.put("[尴尬]", R.mipmap.f025);
        map.put("[惊恐]", R.mipmap.f026);
        map.put("[冷汗]", R.mipmap.f027);
        map.put("[爱心]", R.mipmap.f028);
        map.put("[示爱]", R.mipmap.f029);
        map.put("[白眼]", R.mipmap.f030);
        map.put("[傲慢]", R.mipmap.f031);
        map.put("[难过]", R.mipmap.f032);
        map.put("[惊讶]", R.mipmap.f033);
        map.put("[疑问]", R.mipmap.f034);
        map.put("[睡]", R.mipmap.f035);
        map.put("[亲亲]", R.mipmap.f036);
        map.put("[憨笑]", R.mipmap.f037);
        map.put("[爱情]", R.mipmap.f038);
        map.put("[衰]", R.mipmap.f039);
        map.put("[撇嘴]", R.mipmap.f040);
        map.put("[阴险]", R.mipmap.f041);
        map.put("[奋斗]", R.mipmap.f042);
        map.put("[发呆]", R.mipmap.f043);
        map.put("[右哼哼]", R.mipmap.f044);
        map.put("[拥抱]", R.mipmap.f045);
        map.put("[坏笑]", R.mipmap.f046);
        map.put("[飞吻]", R.mipmap.f047);
        map.put("[鄙视]", R.mipmap.f048);
        map.put("[晕]", R.mipmap.f049);
        map.put("[大兵]", R.mipmap.f050);
        map.put("[可怜]", R.mipmap.f051);
        map.put("[强]", R.mipmap.f052);
        map.put("[弱]", R.mipmap.f053);
        map.put("[握手]", R.mipmap.f054);
        map.put("[胜利]", R.mipmap.f055);
        map.put("[抱拳]", R.mipmap.f056);
        map.put("[凋谢]", R.mipmap.f057);
        map.put("[饭]", R.mipmap.f058);
        map.put("[蛋糕]", R.mipmap.f059);
        map.put("[西瓜]", R.mipmap.f060);
        map.put("[啤酒]", R.mipmap.f061);
        map.put("[飘虫]", R.mipmap.f062);
        map.put("[勾引]", R.mipmap.f063);
        map.put("[OK]", R.mipmap.f064);
        map.put("[爱你]", R.mipmap.f065);
        map.put("[咖啡]", R.mipmap.f066);
        map.put("[钱]", R.mipmap.f067);
        map.put("[月亮]", R.mipmap.f068);
        map.put("[美女]", R.mipmap.f069);
        map.put("[刀]", R.mipmap.f070);
        map.put("[发抖]", R.mipmap.f071);
        map.put("[差劲]", R.mipmap.f072);
        map.put("[拳头]", R.mipmap.f073);
        map.put("[心碎]", R.mipmap.f074);
        map.put("[太阳]", R.mipmap.f075);
        map.put("[礼物]", R.mipmap.f076);
        map.put("[足球]", R.mipmap.f077);
        map.put("[骷髅]", R.mipmap.f078);
        map.put("[挥手]", R.mipmap.f079);
        map.put("[闪电]", R.mipmap.f080);
        map.put("[饥饿]", R.mipmap.f081);
        map.put("[困]", R.mipmap.f082);
        map.put("[咒骂]", R.mipmap.f083);
        map.put("[折磨]", R.mipmap.f084);
        map.put("[抠鼻]", R.mipmap.f085);
        map.put("[鼓掌]", R.mipmap.f086);
        map.put("[糗大了]", R.mipmap.f087);
        map.put("[左哼哼]", R.mipmap.f088);
        map.put("[哈欠]", R.mipmap.f089);
        map.put("[快哭了]", R.mipmap.f090);
        map.put("[吓]", R.mipmap.f091);
        map.put("[篮球]", R.mipmap.f092);
        map.put("[乒乓球]", R.mipmap.f093);
        map.put("[NO]", R.mipmap.f094);
        map.put("[跳跳]", R.mipmap.f095);
        map.put("[怄火]", R.mipmap.f096);
        map.put("[转圈]", R.mipmap.f097);
        map.put("[磕头]", R.mipmap.f098);
        map.put("[回头]", R.mipmap.f099);
        map.put("[跳绳]", R.mipmap.f100);
        map.put("[激动]", R.mipmap.f101);
        map.put("[街舞]", R.mipmap.f102);
        map.put("[献吻]", R.mipmap.f103);
        map.put("[左太极]", R.mipmap.f104);
        map.put("[右太极]", R.mipmap.f105);
        map.put("[闭嘴]", R.mipmap.f106);
        return map;
    }

    private List<GridView> getFaceViews() {
        Map<String, Integer> map = getFaceMap();
        final List<String> keys = new ArrayList<>();
        final List<Integer> values = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

        List<GridView> views = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            GridView gridView = new GridView(this);
            gridView.setNumColumns(7);
            gridView.setHorizontalSpacing(5);
            gridView.setVerticalSpacing(5);
            gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            gridView.setGravity(Gravity.CENTER);
            gridView.setBackgroundColor(Color.GREEN);

            final List<Integer> objects = new ArrayList<>();
            objects.addAll(values.subList(i * 20, (i + 1) * 20 < values.size() ? (i + 1) * 20 : values.size()));
            objects.add(R.mipmap.ic_launcher_round);
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
                        int rawHeigh = bitmap.getHeight();
                        int rawWidth = bitmap.getHeight();
                        int newHeight = 40;
                        int newWidth = 40;
                        // 计算缩放因子
                        float heightScale = ((float) newHeight) / rawHeigh;
                        float widthScale = ((float) newWidth) / rawWidth;
                        // 新建立矩阵
                        Matrix matrix = new Matrix();
                        matrix.postScale(heightScale, widthScale);
                        // 设置图片的旋转角度
                        // matrix.postRotate(-30);
                        // 设置图片的倾斜
                        // matrix.postSkew(0.1f, 0.1f);
                        // 将图片大小压缩
                        // 压缩后图片的宽和高以及kB大小均会变化
                        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, rawWidth, rawHeigh, matrix, true);
                        ImageSpan imageSpan = new ImageSpan(ChatActivity.this, newBitmap);
                        String emojiStr = keys.get(currentPage * 20 + position);
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
