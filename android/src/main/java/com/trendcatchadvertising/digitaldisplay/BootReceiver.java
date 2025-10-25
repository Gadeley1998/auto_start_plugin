package com.trendcatchadvertising.digitaldisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Boot completed - Starting AutoStartService + MainActivity");

            startAutoStartService(context);

            // Lancement sécurisé du launcher en HOME après délai
            new Handler().postDelayed(() -> launchMainActivity(context), 5000);
        }
    }

    private void startAutoStartService(Context context) {
        try {
            Intent serviceIntent = new Intent(context, AutoStartService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }
            Log.d(TAG, "AutoStartService lancé correctement.");
        } catch (Exception e) {
            Log.e(TAG, "Erreur démarrage AutoStartService", e);
        }
    }

    private void launchMainActivity(Context context) {
        try {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
            Log.d(TAG, "MainActivity lancé avec succès.");
        } catch (Exception e) {
            Log.e(TAG, "Erreur lancement MainActivity", e);
        }
    }
}
