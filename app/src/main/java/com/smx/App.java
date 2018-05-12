package com.smx;

import android.app.Application;
import android.app.Notification;
import android.media.MediaPlayer;

import com.smx.util.SharePreferenceUtil;

import cn.jpush.android.api.JPushInterface;

public class App extends Application {

	public static final String SP_FILE_NAME = "push_msg_sp";
	

	private static App _instance;


	private SharePreferenceUtil mSpUtil;
	private MediaPlayer mMediaPlayer;
	private Notification mNotification;
	


	@Override
	public void onCreate() {
		super.onCreate();
		_instance = this;
		CrashHandler.getInstance().init(this);

		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush

		//未见其作用，暂时屏蔽
        /*OkHttpClient okHttpClient = new OkHttpClient.Builder()
//      .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);*/
	}


	
	public synchronized static App getInstance() {
		return _instance;
	}
	
	
	




	public synchronized MediaPlayer getMediaPlayer() {
		if (mMediaPlayer == null) {
			mMediaPlayer = MediaPlayer.create(this, R.raw.office);
		}
		return mMediaPlayer;
	}
	
	public synchronized SharePreferenceUtil getSpUtil() {
		if (mSpUtil == null) {
			mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
		}
		return mSpUtil;
	}

	









	
	
	

	


}








// 如果用户开启播放声音
//				if (App.getInstance().getSpUtil().getMsgSound()) {
//					App.getInstance().getMediaPlayer().start();
//				}