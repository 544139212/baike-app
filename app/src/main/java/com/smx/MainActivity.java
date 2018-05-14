package com.smx;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smx.fragment.IndexFragment;
import com.smx.fragment.MessageFragment;
import com.smx.fragment.ProfileFragment;
import com.smx.receiver.ScreenOffReceiver;
import com.smx.receiver.ScreenOnReceiver;
import com.smx.receiver.TimeTickBroadcaseReceiver;
import com.smx.service.LocationService;
import com.smx.service.LocationService2;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

public class MainActivity extends BasicActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.iv_left)
    ImageView ivLeft;

    @BindView(R.id.tv_center)
    TextView tvCenter;

    @BindView(R.id.iv_right)
    ImageView ivRight;

    IndexFragment indexFragment;
    MessageFragment messageFragment;
    ProfileFragment profileFragment;

    @BindView(R.id.rb_index)
    TextView tvIndex;

    @BindView(R.id.rb_message)
    TextView tvMessage;

    @BindView(R.id.rb_profile)
    TextView tvProfile;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    int currentId = R.id.rb_index;

    long firstTime = System.currentTimeMillis();

    TimeTickBroadcaseReceiver timeClickBroadcaseReceiver = new TimeTickBroadcaseReceiver();
    ScreenOnReceiver screenOnReceiver = new ScreenOnReceiver();
    ScreenOffReceiver screenOffReceiver = new ScreenOffReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        JPushInterface.init(getApplicationContext());

        registerReceivers();
//        startServices();

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        tvIndex.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvProfile.setOnClickListener(this);

        changeTitle(currentId);
        changeFragment(currentId);
        changeSelect(currentId);
        changeIvRight(currentId);

        //屏蔽默认toolbar(以下2行)
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //屏蔽浮动按钮
//        Snackbar.make(view, "你尚未登录，请先登录", Snackbar.LENGTH_SHORT)
//                .setAction("Action", null).show();

        //屏蔽屏蔽默认toolbar(以下5行)
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //需要删除 R.id.nav_view 这个view中的 app:headerLayout="@layout/activity_main_drawer_header" 属性，否则会出现两个header
        LinearLayout linearLayout = (LinearLayout)navigationView.inflateHeaderView(R.layout.activity_main_drawer_header);
        CircleImageView civAvator = (CircleImageView)linearLayout.findViewById(R.id.civ_avator);
        TextView tvName = (TextView)linearLayout.findViewById(R.id.tv_name);

        SharedPreferences preferences = getSharedPreferences("LOGIN", MODE_PRIVATE);
        String indicator = preferences.getString("INDICATOR", "N");
        if ("Y".equals(indicator)) {
            civAvator.setImageResource(R.mipmap.splash);
            tvName.setText("Android Studio");
        } else {
//            civAvator.setImageResource(R.mipmap.news_placeholder);
//            tvName.setText("登录 / 注册");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.resumePush(getApplicationContext());
        getNotificationManager().cancel(0x000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        //替换下面3行改为使用BindView，同时加入退出提示
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            if (System.currentTimeMillis() - firstTime < 3000) {
                if (JCVideoPlayer.backPress()) {
                    return;
                }

                JPushInterface.stopPush(getApplicationContext());
                finish();
            } else {
                firstTime = System.currentTimeMillis();
                if (App.getInstance().getSpUtil().getMsgNotify()) {
                    Toast.makeText(this, "再按一次应用程序转入后台运行", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "再按一次应用程序退出不再运行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceivers();
    }

    //屏蔽屏蔽默认toolbar
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_html_menu, menu);
        return true;
    }*/

    //屏蔽屏蔽默认toolbar
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up shape_button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_photo) {

        } else if (id == R.id.nav_share) {
            Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
            View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null);
            bottomDialog.setContentView(contentView);
            ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            contentView.setLayoutParams(layoutParams);
            //下面4行，对话框和屏幕之间有间距
//            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
//            params.width = getResources().getDisplayMetrics().widthPixels - DpPxUtils.dp2px(this, 16f);
//            params.bottomMargin = DpPxUtils.dp2px(this, 8f);
//            contentView.setLayoutParams(params);
            bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
            bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
            bottomDialog.show();
        } else if (id == R.id.nav_settings) {
            go(MainActivity.this, SettingsActivity.class);
        }

        //替换下面2行改为使用BindView
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() != currentId) {
            currentId = v.getId();
            //改变标题
            changeTitle(currentId);
            //改变Fragment
            changeFragment(currentId);
            //改变选中
            changeSelect(currentId);
            //改变顶部右侧按钮显示及行为
            changeIvRight(currentId);
        }
    }

    private void changeTitle(int resId) {
        switch (resId) {
            case R.id.rb_index:
                tvCenter.setText(tvIndex.getText());
                break;
            case R.id.rb_message:
                tvCenter.setText(tvMessage.getText());
                break;
            case R.id.rb_profile:
                tvCenter.setText(tvProfile.getText());
                break;
        }
    }

    private void changeFragment(int resId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (profileFragment != null) {
            transaction.hide(profileFragment);
        }

        switch (resId) {
            case R.id.rb_index:
                if (indexFragment == null){
                    indexFragment = IndexFragment.newInstance();
                    transaction.add(R.id.main_container, indexFragment);
                } else {
                    transaction.show(indexFragment);
                }
                break;
            case R.id.rb_message:
                if (messageFragment == null){
                    messageFragment = MessageFragment.newInstance();
                    transaction.add(R.id.main_container, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                break;
            case R.id.rb_profile:
                if (profileFragment == null){
                    profileFragment = ProfileFragment.newInstance();
                    transaction.add(R.id.main_container, profileFragment);
                } else {
                    transaction.show(profileFragment);
                }
                break;
        }

        transaction.commit();
    }

    private void changeSelect(int resId) {
        tvIndex.setSelected(false);
        tvMessage.setSelected(false);
        tvProfile.setSelected(false);

        switch (resId) {
            case R.id.rb_index:
                tvIndex.setSelected(true);
                break;
            case R.id.rb_message:
                tvMessage.setSelected(true);
                break;
            case R.id.rb_profile:
                tvProfile.setSelected(true);
                break;
        }
    }

    private void changeIvRight(int resId) {
        ivRight.setVisibility(View.GONE);

        switch (resId) {
            case R.id.rb_index:
                break;
            case R.id.rb_message:
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        go(MainActivity.this, RequestActivity.class);
                    }
                });
                break;
            case R.id.rb_profile:
                break;
        }
    }

    private void registerReceivers() {
        //注册时间变化监听服务
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeClickBroadcaseReceiver, intentFilter);

        //注册屏幕亮起监听服务
        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenOnReceiver, intentFilter);

        //注册屏幕关闭监听服务
        intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, intentFilter);
    }

    private void unRegisterReceivers() {
        unregisterReceiver(timeClickBroadcaseReceiver);
        unregisterReceiver(screenOnReceiver);
        unregisterReceiver(screenOffReceiver);
    }

    private void startServices() {
        //开启定位服务
        Intent service = new Intent(this, LocationService.class);
        startService(service);
        service = new Intent(this, LocationService2.class);
        startService(service);
    }

}
