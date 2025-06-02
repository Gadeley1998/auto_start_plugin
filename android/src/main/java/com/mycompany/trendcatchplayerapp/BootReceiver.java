package com.mycompany.trendcatchplayerapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("BootReceiver", "[auto_start] Boot completed - launching MainActivity");

            // Délai optionnel de 5 secondes pour laisser le système respirer
            new Handler().postDelayed(() -> {
                Intent i = new Intent();
                i.setClassName("com.mycompany.trendcatchplayerapp", "com.mycompany.trendcatchplayerapp.MainActivity");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }, 5000); // délai en ms
        }
    }
}
