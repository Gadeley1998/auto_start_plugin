package com.trendcatchadvertising.digitaldisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.content.pm.PackageManager;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "Boot completed - Starting AutoStartService + launching app");

            // Démarrer le service en foreground
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

            // Lancer l’application via le Launch Intent après un petit délai
            new Handler().postDelayed(() -> {
                try {
                    PackageManager pm = context.getPackageManager();
                    Intent launch = pm.getLaunchIntentForPackage(context.getPackageName());
                    if (launch != null) {
                        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(launch);
                        Log.d(TAG, "Application lancée via Launch Intent.");
                    } else {
                        Log.e(TAG, "Launch Intent introuvable.");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erreur lancement application", e);
                }
            }, 5000);
        }
    }
}
