package com.trendcatchadvertising.digitaldisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("BootReceiver", "[auto_start] Boot completed - launching MainActivity and AutoStartService");

            // ✅ Démarre le service pour relancer l'app toutes les 60 secondes si fermée
            try {
                Intent serviceIntent = new Intent(context, AutoStartService.class);
                context.startService(serviceIntent);
                Log.d("BootReceiver", "AutoStartService lancé avec succès.");
            } catch (Exception e) {
                Log.e("BootReceiver", "Erreur lors du démarrage de AutoStartService", e);
            }

            // ✅ Lancer l'application après un petit délai (facultatif mais utile)
            new Handler().postDelayed(() -> {
                try {
                    Intent i = new Intent();
                    i.setClassName("com.trendcatchadvertising.digitaldisplay", "com.trendcatchadvertising.digitaldisplay.MainActivity");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    Log.d("BootReceiver", "MainActivity lancé avec succès.");
                } catch (Exception e) {
                    Log.e("BootReceiver", "Erreur lors du lancement de MainActivity", e);
                }
            }, 5000); // 5 secondes de délai
        }
    }
}
