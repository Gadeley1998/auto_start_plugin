package com.trendcatchadvertising.digitaldisplay;

import android.app.Activity;
import android.os.Build;
import android.os.PowerManager;
import android.content.Intent;
import android.content.Context;
import android.provider.Settings;
import android.net.Uri;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

public class BatteryOptimizationChannel implements MethodChannel.MethodCallHandler {

    private final Activity activity;

    public BatteryOptimizationChannel(Activity activity) {
        this.activity = activity;
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "battery_optimization");
        BatteryOptimizationChannel instance = new BatteryOptimizationChannel(registrar.activity());
        channel.setMethodCallHandler(instance);
    }

    @Override
    public void onMethodCall(MethodChannel.MethodCall call, MethodChannel.Result result) {
        if (call.method.equals("requestExclusion")) {
            requestDisableBatteryOptimization(activity);
            result.success(null);
        } else {
            result.notImplemented();
        }
    }

    private void requestDisableBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);
            String packageName = activity.getPackageName();

            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                        Uri.parse("package:" + packageName));
                activity.startActivity(intent);
            }
        }
    }
}
