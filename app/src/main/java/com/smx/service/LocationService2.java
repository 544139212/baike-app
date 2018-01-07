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
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smx.Configuration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

public class LocationService2 extends Service {

    TelephonyManager telephonyManager;

    public LocationService2() {
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
        /**
         * 功能描述：通过手机信号获取基站信息
         * # 通过TelephonyManager 获取lac:mcc:mnc:cell-id
         * # MCC，Mobile Country Code，移动国家代码（中国的为460）；
         * # MNC，Mobile Network Code，移动网络号码（中国移动为0，中国联通为1，中国电信为2）；
         * # LAC，Location Area Code，位置区域码；
         * # CID，Cell Identity，基站编号；
         * # BSSS，Base station signal strength，基站信号强度。
         * @author android_ls
         */
        telephonyManager = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);

        // http://www.gpsspg.com/bs.htm
        String operator = telephonyManager.getNetworkOperator();
        int mcc = Integer.parseInt(operator.substring(0, 3)); // 移动设备国家代码 Mobile Country Code
        if (mcc == 460) {
            //中国
        }
        else {
            //其它国家/
        }
        int mnc = Integer.parseInt(operator.substring(3)); // 移动设备网络代码 Mobile Network Code
        if (mnc == 0) {
            //移动
        }
        else if (mnc == 1) {
            //联通
        }
        else if (mnc == 2 || mnc == 11) {
            //电信
        }

        // 中国移动和中国联通获取LAC、CID的方式
        CellLocation location = telephonyManager.getCellLocation();
//		saveLocation(location);



        // 获取邻区基站信息
//		List<NeighboringCellInfo> infos = telephonyManager.getNeighboringCellInfo();
//		StringBuffer sb = new StringBuffer("总数 : " + infos.size() + "\n");
//		for (NeighboringCellInfo info : infos) { // 根据邻区总数进行循环
//			sb.append(" LAC : " + info.getLac()); // 取出当前邻区的LAC
//			sb.append(" CID : " + info.getCid()); // 取出当前邻区的CID
//            sb.append(" BSSS : " + (-113 + 2 * info.getRssi()) + "\n"); // 获取邻区基站信号强度
//		}
//		System.out.println("获取邻区基站信息:" + sb.toString());


        // IMEI
//        String imei = telephonyManager.getDeviceId();

        // IMSI
        String imsi = telephonyManager.getSubscriberId();

        // 设备软件版本
        String deviceSoftwareVersion = telephonyManager.getDeviceSoftwareVersion();

        // 手机号
        String line1Number = telephonyManager.getLine1Number();

        // 网络国家码
        String networkCountryIso = telephonyManager.getNetworkCountryIso();

        // 网络运营商代码
        String networkOperator = telephonyManager.getNetworkOperator();

        // 网络运营商名称
        String networkOperatorName = telephonyManager.getNetworkOperatorName();

        // SIM卡国家码
        String simCountryIso = telephonyManager.getSimCountryIso();

        // SIM卡运营商代码
        String simOperator = telephonyManager.getSimOperator();

        // SIM卡运营商名称
        String simOperatorName = telephonyManager.getSimOperatorName();

        // SIM卡序列号
        String simSerialNumber = telephonyManager.getSimSerialNumber();

        // SIM卡状态
//        String simState = getSimState(telephonyManager.getSimState());

        // 语音类型
//        String phoneType = getPhoneType(telephonyManager.getPhoneType());

        // android.intent.action.PHONE_STATE
//        TelephonyManager.ACTION_PHONE_STATE_CHANGED;

        // android.intent.action.RESPOND_VIA_MESSAGE;
//        TelephonyManager.ACTION_RESPOND_VIA_MESSAGE;

        // incoming_number
//        TelephonyManager.EXTRA_INCOMING_NUMBER;

        // state
//        TelephonyManager.EXTRA_STATE;
//        TelephonyManager.EXTRA_STATE_IDLE;
//        TelephonyManager.EXTRA_STATE_OFFHOOK;
//        TelephonyManager.EXTRA_STATE_RINGING;

        try {
            telephonyManager.listen(listener,
                    PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                            PhoneStateListener.LISTEN_CALL_STATE |
                            PhoneStateListener.LISTEN_CELL_LOCATION |
                            PhoneStateListener.LISTEN_DATA_ACTIVITY |
                            PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                            PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR |
                            PhoneStateListener.LISTEN_SERVICE_STATE);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private int[] getLacAndCid(CellLocation location) {
        int[] array = new int[2];

        GsmCellLocation gsmLocation = (GsmCellLocation)location;
        int lac = gsmLocation.getLac();
        int cid = gsmLocation.getCid();

        array[0] = lac;
        array[1] = cid;

        return array;

        // 中国电信获取LAC、CID的方式
//        CdmaCellLocation location = (CdmaCellLocation) telephonyManager.getCellLocation();
//        lac = location1.getNetworkId();
//        cid = location1.getBaseStationId();
    }

    /*private String getSimState(int state) {
    	String name = "";
    	switch (state) {
		case TelephonyManager.SIM_STATE_ABSENT:
			name = "ABSENT";
			break;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
			name = "NETWORK_LOCKED";
			break;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
			name = "PIN_REQUIRED";
			break;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			name = "PUK_REQUIRED";
			break;
		case TelephonyManager.SIM_STATE_READY:
			name = "READY";
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			name = "UNKNOWN";
			break;
		default:
			name = "其它";
			break;
		}
    	return name;
    }*/

    private String getNetworkType(int networkType) {
        String name = "";
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                name = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                name = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                name = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                name = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                name = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                name = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                name = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                name = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                name = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                name = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                name = "HSPAP";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                name = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                name = "IDEN";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                name = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                name = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                name = "UNKNOWN";
                break;
            default:
                name = "其它";
                break;
        }
        return name;
    }

    private String getCallState(int state) {
        String name = "";
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                name = "IDLE";
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                name = "OFFHOOK";
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                name = "RINGING";
                break;
            default:
                name = "其它";
                break;
        }
        return name;
    }

    /*private String getPhoneType(int type) {
    	String name = "";
    	switch (type) {
		case TelephonyManager.PHONE_TYPE_CDMA:
			name = "CDMA";
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			name = "GSM";
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			name = "NONE";
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			name = "SIP";
			break;
		default:
			name = "其它";
			break;
		}
    	return name;
    }*/

    private String getDataState(int state) {
        String name = "";
        switch (state) {
            case TelephonyManager.DATA_CONNECTED:
                name = "CONNECTED";
                break;
            case TelephonyManager.DATA_CONNECTING:
                name = "CONNECTING";
                break;
            case TelephonyManager.DATA_DISCONNECTED:
                name = "DISCONNECTED";
                break;
            case TelephonyManager.DATA_SUSPENDED:
                name = "SUSPENDED";
                break;
            default:
                name = "其它";
                break;
        }
        return name;
    }

    private String getDataActivity(int direction) {
        String name = "";
        switch (direction) {
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                name = "DORMANT";
                break;
            case TelephonyManager.DATA_ACTIVITY_IN:
                name = "IN";
                break;
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                name = "INOUT";
                break;
            case TelephonyManager.DATA_ACTIVITY_NONE:
                name = "NONE";
                break;
            case TelephonyManager.DATA_ACTIVITY_OUT:
                name = "OUT";
                break;
            default:
                name = "其它";
                break;
        }
        return name;
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
//			super.onCallStateChanged(state, incomingNumber);
//            Toast.makeText(LocationService2.this, getCallState(state) + " " + incomingNumber, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCellLocationChanged(CellLocation location) {
            // TODO Auto-generated method stub
//			super.onCellLocationChanged(location);
            saveLocation(location);
        }

        @Override
        public void onDataActivity(int direction) {
            // TODO Auto-generated method stub
//			super.onDataActivity(direction);
//            Toast.makeText(LocationService2.this, getDataActivity(direction), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDataConnectionStateChanged(int state, int networkType) {
            // TODO Auto-generated method stub
//			super.onDataConnectionStateChanged(state, networkType);
//            Toast.makeText(LocationService2.this, getDataState(state) + " " + getNetworkType(networkType), Toast.LENGTH_SHORT).show();
        }
    };

    private void saveLocation(CellLocation location) {
        int[] array = getLacAndCid(location);
        int lac = array[0]; // 位置区识别码 Location Area Code
        int cid = array[1]; // 基站编号 Cell Identity

        final Context context = this;
        OkHttpUtils.post().url(Configuration.ws_url + "/celllocation/add")
                .addParams("lac", String.valueOf(lac))
                .addParams("cid", String.valueOf(cid))
                .addParams("createTime", "2010-07-09 01:56:21")
                .addParams("createBy", "wh@aishk.com")
                .build().execute(new Callback<String>() {
            @Override
            public String parseNetworkResponse(Response response, int i) throws Exception {
                return new Gson().fromJson(response.body().string(), String.class);
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String o, int i) {

            }
        });
    }

}
