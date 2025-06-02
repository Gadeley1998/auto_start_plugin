package com.mycompany.trendcatchplayerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("BootReceiver", "[auto_start] Boot completed - launching MainActivity");

            try {
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                Log.d("BootReceiver", "MainActivity launched successfully.");
            } catch (Exception e) {
                Log.e("BootReceiver", "Error launching MainActivity", e);
            }
        }
    }
}
