package com.smx.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.Configuration;
import com.smx.dto.ResultDTO;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

public class LocationService extends Service {

    LocationManager locationManager;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //注册位置监听
        registerLocationListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    private void registerLocationListener() {
        //获取定位服务
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        String provider = this.getBestProvider();
        if (provider == null) {
            provider = this.getProvider();
        }
        if (provider == null) {
            //定位服务不支持或未开启
            return;
        }

//		Location location = locationManager.getLastKnownLocation(provider);
//		saveLocation(location);
        try {
            locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private String getBestProvider() {
        Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        return locationManager.getBestProvider(criteria, true);
    }

    private String getProvider() {
        //位置資訊來源，如GPS衛星定位，NETWORK基站定位
        List<String> allProviderList = locationManager.getAllProviders();

        if (allProviderList != null && !allProviderList.isEmpty()) {
            for (String provider : allProviderList) {
                //检测是否启用位置资讯
                if (locationManager.isProviderEnabled(provider)) {
                    return provider;
                } else {
                    //位置资讯未启用，则转至启用页
//                    Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            }
        } else {
            //如果沒有資訊來源，則定為服務不支持
//            Toast.makeText(this, "定位服務不支持", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            saveLocation(location);
        }
    };

    private void saveLocation(Location location) {
        //所有資訊來源都不能提供位置，提示無法定位
        if (location == null) {
//            Toast.makeText(this, "无法定位坐标", Toast.LENGTH_LONG).show();
            return;
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String[] addressLines = new String[10];
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            int max = 0;
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                max = address.getMaxAddressLineIndex();
                for (int i = 0; i < max; i++) {
                    addressLines[i] = URLEncoder.encode(address.getAddressLine(i), "UTF-8");
                }
            }
            if (max < 10) {
                for (int i = max; i < 10; i++) {
                    addressLines[i] = "";
                }
            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        OkHttpUtils.post().url(Configuration.ws_url + "/location/add")
                .addParams("longitude", String.valueOf(location.getLongitude()))
                .addParams("latitude", String.valueOf(location.getLatitude()))
                .addParams("addressLine1", addressLines[0])
                .addParams("addressLine2", addressLines[1])
                .addParams("addressLine3", addressLines[2])
                .addParams("addressLine4", addressLines[3])
                .addParams("addressLine5", addressLines[4])
                .addParams("addressLine6", addressLines[5])
                .addParams("addressLine7", addressLines[6])
                .addParams("addressLine8", addressLines[7])
                .addParams("addressLine9", addressLines[8])
                .addParams("addressLine10", addressLines[9])
                .addParams("createTime", "2010-07-09 01:56:21")
                .addParams("createBy", "wh@aishk.com")
                .build().execute(new Callback<ResultDTO>() {
            @Override
            public ResultDTO parseNetworkResponse(Response response, int i) throws Exception {
                return new Gson().fromJson(response.body().string(), ResultDTO.class);
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ResultDTO o, int i) {
                if (o.getCode() == 200) {

                }
            }
        });
    }

}
