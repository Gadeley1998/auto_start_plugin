package com.trendcatchadvertising.digitaldisplay;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Build;

public class AppRestartReceiver extends BroadcastReceiver {

    private static final String TAG = "AppRestartReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (!isAppVisibleOrForeground(context)) {
                Log.d(TAG, "App inactive. Relaunching safely...");
                bringAppToForegroundSafely(context);
            } else {
                Log.d(TAG, "App already visible or in foreground.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error restarting app: " + e.getMessage());
            bringAppToForegroundSafely(context); // Sécurité maximale
        }
    }

    /**
     * ✅ Relance l’application de façon sûre via PendingIntent
     * (évite IllegalStateException sur Android 10+)
     */
    private void bringAppToForegroundSafely(Context context) {
        try {
            Intent launchIntent = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());

            if (launchIntent != null) {
                launchIntent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                );

                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        launchIntent,
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                ? PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                                : PendingIntent.FLAG_UPDATE_CURRENT
                );

                pendingIntent.send(); // ✅ plus sûr que startActivity()
                Log.d(TAG, "App relaunched via PendingIntent successfully.");
            } else {
                Log.e(TAG, "Launch intent not found for package: " + context.getPackageName());
            }
        } catch (PendingIntent.CanceledException e) {
            Log.e(TAG, "PendingIntent cancelled: " + e.getMessage());
        } catch (Exception ex) {
            Log.e(TAG, "Launch intent failed: " + ex.getMessage());
        }
    }

    /**
     * Vérifie si l’app est déjà visible ou au premier plan
     */
    private boolean isAppVisibleOrForeground(Context context) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String pkg = context.getPackageName();

        if (activityManager != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess :
                    activityManager.getRunningAppProcesses()) {

                if (appProcess.processName.equals(pkg)) {
                    int importance = appProcess.importance;
                    return importance ==
                            ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                            || importance ==
                            ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                }
            }
        }
        return false;
    }
}
