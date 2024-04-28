package com.project.financemanager.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class BroadcastWifi extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
        if (action != null && action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
        int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
        switch (wifiState) {
            case WifiManager.WIFI_STATE_ENABLED:
                // Xử lý khi WiFi được bật
                Toast.makeText(context, "Đã kết nối Wi-Fi", Toast.LENGTH_SHORT).show();
                break;
            case WifiManager.WIFI_STATE_DISABLED:
                // Xử lý khi WiFi bị tắt
                Toast.makeText(context, "Đã ngắt kết nối Wi-Fi", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    }


}
