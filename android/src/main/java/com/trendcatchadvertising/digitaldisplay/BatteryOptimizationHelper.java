package com.trendcatchadvertising.digitaldisplay;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.content.Intent;
import android.provider.Settings;
import android.net.Uri;
import android.content.Context;
import android.util.Log;

/**
 * ✅ BatteryOptimizationHelper
 * Vérifie si l'app est optimisée pour la batterie et,
 * si oui, demande à l'utilisateur de l'exclure.
 */
public class BatteryOptimizationHelper implements Application.ActivityLifecycleCallbacks {

    private static final String TAG = "BatteryOptimizationHelper";
    private final Application app;
    private static boolean requestShown = false;

    public BatteryOptimizationHelper(Application application) {
        this.app = application;
        application.registerActivityLifecycleCallbacks(this);
        Log.d(TAG, "BatteryOptimizationHelper registered.");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !requestShown) {
            try {
                PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
                String pkg = activity.getPackageName();

                if (pm != null && !pm.isIgnoringBatteryOptimizations(pkg)) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            Uri.parse("package:" + pkg));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (intent.resolveActivity(activity.getPackageManager()) != null) {
                        activity.startActivity(intent);
                        requestShown = true;
                        Log.d(TAG, "Demande de désactivation de l'optimisation batterie affichée.");
                    } else {
                        Log.w(TAG, "ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS non supportée sur cet appareil.");
                    }
                } else {
                    Log.d(TAG, "Optimisation batterie déjà ignorée ou PowerManager indisponible.");
                }
            } catch (Exception e) {
                Log.e(TAG, "Erreur BatteryOptimizationHelper: " + e.getMessage());
            }
        }
    }

    @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}
    @Override public void onActivityStarted(Activity activity) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivityStopped(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        // 🔄 Bonne pratique : si l'activité principale est détruite, on se désenregistre.
        if (activity.isFinishing()) {
            app.unregisterActivityLifecycleCallbacks(this);
            Log.d(TAG, "BatteryOptimizationHelper unregistered.");
        }
    }
}
