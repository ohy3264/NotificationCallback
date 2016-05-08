package com.example.ohyowan.notificationcallback.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.example.ohyowan.notificationcallback.Constants;
import com.example.ohyowan.notificationcallback.ForegroundService;

/**
 * simple network conneted / disconneted
 *
 * @author hwoh
 * @since 1.0
 */
public class WifiMonitorReceiver_hwoh extends BroadcastReceiver {
    private static final String TAG = "WifiMonitorReceiver_hwoh";

    private WifiManager wifiManager = null;
    private WifiInfo wifiInfo;
    private NetworkInfo networkInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ForegroundService.class);
        String action = intent.getAction();

        switch (action) {
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    wifiInfo = wifiManager.getConnectionInfo();
                    if (wifiInfo != null && wifiInfo.getSSID() != null) {
                        Log.i(TAG, "wifi 연결됨");
                        if ((wifiInfo.getSSID()).toUpperCase().contains("SUNGSU")) {
                            Log.i(TAG, "접속한 Wifi 는 GameKOK Wifi");
                            if (!ForegroundService.IS_SERVICE_RUNNING) {
                                Log.i(TAG, "서비스 작동중이 아니면 작동");
                                service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                                context.startService(service);
                            }
                        } else {
                            Log.i(TAG, "접속한 Wifi 는 GameKOK Wifi가 아닙니다.");
                            if (ForegroundService.IS_SERVICE_RUNNING) {
                                Log.i(TAG, "서비스 작동중이면 종료");
                                service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                                context.startService(service);
                            }
                        }
                    }
                }
                break;

            case ConnectivityManager.CONNECTIVITY_ACTION:
                networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo.getType() ==
                        ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
                    Log.i(TAG, "wifi 연결끊김");
                    if (ForegroundService.IS_SERVICE_RUNNING) {
                        Log.i(TAG, "서비스 작동중이면 종료");
                        service.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
                        context.startService(service);
                    }
                }
                break;
        }
    }







}