package com.trendcatchadvertising.digitaldisplay;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AppRestartReceiver extends BroadcastReceiver {

    private static final String TAG = "AppRestartReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (!isAppVisibleOrForeground(context)) {
                Log.d(TAG, "App inactive. Relaunching...");
                restartApp(context);
            } else {
                Log.d(TAG, "App already visible or foreground.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error restarting app: " + e.getMessage());
            restartApp(context); // Sécurité maximale
        }
    }

    private void restartApp(Context context) {
        try {
            Intent launchIntent = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName());

            if (launchIntent != null) {
                launchIntent.addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                );
                context.startActivity(launchIntent);
            }
        } catch (Exception ex) {
            Log.e(TAG, "Launch intent failed: " + ex.getMessage());
        }
    }

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
