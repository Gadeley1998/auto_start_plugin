package com.trendcatchadvertising.digitaldisplay;

import android.content.Intent;
import android.os.Build;
import android.content.Context;
import android.os.PowerManager;
import android.net.Uri;
import android.provider.Settings;
import android.app.Activity;

public class BatteryOptimizationHelper {

    public static void requestDisableBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            String packageName = activity.getPackageName();

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        }
    }
}
