package com.smx.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class SplashService extends Service {

    SplashBinder splashBinder = new SplashBinder();

    public SplashService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return splashBinder;
    }

    class SplashBinder extends Binder {

        public void load() {
            String board = android.os.Build.BOARD;
            String boorloader = android.os.Build.BOOTLOADER;
            String brand = android.os.Build.BRAND;
            String cpuAbi = android.os.Build.CPU_ABI;
            String cpuAbi2 = android.os.Build.CPU_ABI2;
            String device = android.os.Build.DEVICE;
            String display = android.os.Build.DISPLAY; //系统版本号
            String fingerprint = android.os.Build.FINGERPRINT;
            String hardware = android.os.Build.HARDWARE;
            String host = android.os.Build.HOST;
            String id = android.os.Build.ID;
            String manufacturer = android.os.Build.MANUFACTURER; //制造商
            String model = android.os.Build.MODEL; //型号
            String product = android.os.Build.PRODUCT;
            String radio = android.os.Build.RADIO;
            String serial = android.os.Build.SERIAL; //序列号
            String tags = android.os.Build.TAGS;
            String type = android.os.Build.TYPE;
            String unknown = android.os.Build.UNKNOWN;
            String user = android.os.Build.USER;
            long time = android.os.Build.TIME;
            String codeName = android.os.Build.VERSION.CODENAME;
            String release = android.os.Build.VERSION.RELEASE; //Android 版本
            String incremental = android.os.Build.VERSION.INCREMENTAL;
            String sdk = android.os.Build.VERSION.SDK;
            int sdkInt = android.os.Build.VERSION.SDK_INT; //Android SDK 版本

            System.out.println(board + "\n" + boorloader + "\n" + brand + "\n" + cpuAbi + "\n" + cpuAbi2 + "\n" + device + "\n" + display
                    + "\n" + fingerprint + "\n" + hardware + "\n" + host + "\n" + id + "\n" + manufacturer + "\n" + model + "\n" + product
                    + "\n" + radio + "\n" + serial + "\n" + tags + "\n" + type + "\n" + unknown + "\n" + user + "\n" + time
                    + "\n" + codeName + "\n" + release + "\n" + incremental + "\n" + sdk + "\n" + sdkInt);


//            DisplayMetrics dm = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(dm);
            // 屏幕分辨率
//            dm.widthPixels + "X" + dm.heightPixels


            // 像素密度(每英寸像素数)
//            dm.densityDpi

            //"密度(每平方英寸像素数)
//            dm.density



            /*OkHttpUtils
                .get()//
                .url("https://unsplash.it/400/800/?random")//
                .build()//
                .execute(new BitmapCallback() {
                    @Override
                    public void onError(Call arg0, Exception exception, int arg2) {
                        // TODO Auto-generated method stub
                        exception.printStackTrace();
                    }

                    @Override
                    public void onResponse(Bitmap bitmap, int arg1) {
                        // TODO Auto-generated method stub
                        String path111 = Environment.getDataDirectory().getAbsolutePath() + "=" + Environment.getDataDirectory().getPath() + "=" + Environment.getDataDirectory().canRead() + "=" + Environment.getDataDirectory().canWrite();
                        System.out.println(path111);
                        File p = new File(Environment.getDataDirectory().getAbsolutePath() + File.separator + "baike");
                        if (!p.exists()) {
                            p.mkdir();
                        }
                        System.out.println(p.canRead() + " " + p.canWrite());
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "baike");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        FileOutputStream out;
                        try{
                            out = new FileOutputStream(new File(file, "splash.jpg"));
                            if (out != null) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                out.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });*/
        }
    }
}
