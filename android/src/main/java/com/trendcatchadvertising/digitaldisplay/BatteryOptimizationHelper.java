package com.trendcatchadvertising.digitaldisplay;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.PowerManager;
import android.content.Intent;
import android.provider.Settings;
import android.net.Uri;
import android.content.Context;

public class BatteryOptimizationHelper implements Application.ActivityLifecycleCallbacks {

    private final Application app;

    public BatteryOptimizationHelper(Application application) {
        this.app = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            String pkg = activity.getPackageName();

            if (!pm.isIgnoringBatteryOptimizations(pkg)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                        Uri.parse("package:" + pkg));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        }
    }

    // Unused but required methods
    @Override public void onActivityCreated(Activity activity, android.os.Bundle savedInstanceState) {}
    @Override public void onActivityStarted(Activity activity) {}
    @Override public void onActivityPaused(Activity activity) {}
    @Override public void onActivityStopped(Activity activity) {}
    @Override public void onActivitySaveInstanceState(Activity activity, android.os.Bundle outState) {}
    @Override public void onActivityDestroyed(Activity activity) {}
}
