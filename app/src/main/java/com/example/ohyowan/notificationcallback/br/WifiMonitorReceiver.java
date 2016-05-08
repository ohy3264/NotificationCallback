package com.example.ohyowan.notificationcallback.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Platform Development Center - NMP Corp.
 *
 * @author David KIM
 * @since 1.0
 */
public class WifiMonitorReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiMonitorReceiver";
    private WifiManager wifiManager = null;
    private ConnectivityManager connectivityManager = null;

    @Override
    public void onReceive(Context context, Intent intent){
        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String strAction = intent.getAction();

        if (strAction.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){
            switch(wifiManager.getWifiState()) {


                case WifiManager.WIFI_STATE_DISABLED:
                    Log.i(TAG, "WIFI_STATE_DISABLED");

                break;

                case WifiManager.WIFI_STATE_DISABLING:
                    Log.i(TAG, "WIFI_STATE_DISABLING");

                break;

                case WifiManager.WIFI_STATE_ENABLED:
                    Log.i(TAG, "WIFI_STATE_ENABLED");

                break;

                case WifiManager.WIFI_STATE_ENABLING:
                    Log.i(TAG, "WIFI_STATE_ENABLING");

                break;

                case WifiManager.WIFI_STATE_UNKNOWN:
                    Log.i(TAG, "WIFI_STATE_UNKNOWN");

                break;
            }
        }else if (strAction.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {

            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if((networkInfo != null) && (networkInfo.isAvailable() == true)) {

                if(networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    Log.i(TAG, "NETWORK CONNECTED");


                }else if (networkInfo.getState() == NetworkInfo.State.CONNECTING) {
                    Log.i(TAG, "NETWORK CONNECTING");


                }else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTED) {
                    Log.i(TAG, "NETWORK DISCONNECTED");


                }else if (networkInfo.getState() == NetworkInfo.State.DISCONNECTING) {
                    Log.i(TAG, "NETWORK DISCONNECTING");


                }else if (networkInfo.getState() == NetworkInfo.State.SUSPENDED) {
                    Log.i(TAG, "NETWORK SUSPENDED");


                } else if (networkInfo.getState() == NetworkInfo.State.UNKNOWN) {
                    Log.i(TAG, "NETWORK UNKNOWN");


                }
            }
        }
    }
}