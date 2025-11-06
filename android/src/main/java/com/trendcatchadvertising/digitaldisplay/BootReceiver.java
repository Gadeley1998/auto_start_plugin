package com.trendcatchadvertising.digitaldisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.content.pm.PackageManager;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d(TAG, "✅ Boot completed - Starting AutoStartService and launching app");

            try {
                // 🟢 Démarrage du service foreground (obligatoire Android 8+)
                Intent serviceIntent = new Intent(context, AutoStartService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent);
                } else {
                    context.startService(serviceIntent);
                }
                Log.d(TAG, "AutoStartService lancé avec succès");
            } catch (Exception e) {
                Log.e(TAG, "❌ Erreur lors du démarrage d'AutoStartService", e);
            }

            // 🕓 Lancer l’application avec un léger délai sur le thread principal sûr
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    PackageManager pm = context.getPackageManager();
                    Intent launchIntent = pm.getLaunchIntentForPackage(context.getPackageName());

                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(launchIntent);
                        Log.d(TAG, "Application relancée après boot avec succès");
                    } else {
                        Log.e(TAG, "Launch Intent introuvable pour le package " + context.getPackageName());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "❌ Erreur lors du lancement de l'application", e);
                }
            }, 5000);
        }
    }
}
