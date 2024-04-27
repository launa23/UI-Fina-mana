package com.project.financemanager.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastAirplaneMode extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED)) {
            boolean isAirplaneModeEnabled = intent.getBooleanExtra("state", false);
            if (isAirplaneModeEnabled) {
                // Chế độ máy bay đã được bật
                Toast.makeText(context, "Bật chế độ máy bay", Toast.LENGTH_SHORT).show();
            } else {
                // Chế độ máy bay đã được tắt
                Toast.makeText(context, "Tắt chế độ máy bay", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
