package com.trendcatchadvertising.digitaldisplay;

import androidx.annotation.NonNull;
import android.content.Context;
import android.util.Log;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * AutoStartPlugin
 * ✅ Communication Flutter ↔ Android
 * ✅ Compatible FlutterPlugin + ActivityAware
 * ✅ Compatible Android 12+
 */
public final class AutoStartPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {

  private static final String TAG = "AutoStartPlugin";
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();

    // Enregistrement du canal Flutter ↔ Android
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "auto_start");
    channel.setMethodCallHandler(this);

    Log.i(TAG, "✅ AutoStartPlugin attached to engine.");
    // Optionnel : helper de batterie (désactivé si non présent)
    // new BatteryOptimizationHelper((Application) context);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    switch (call.method) {
      case "getPlatformVersion":
        String version = "Android " + android.os.Build.VERSION.RELEASE;
        result.success(version);
        Log.d(TAG, "getPlatformVersion exécuté → " + version);
        break;

      case "enableAutoStart":
        Log.d(TAG, "enableAutoStart (placeholder)");
        // Ici tu pourras implémenter plus tard la logique native
        result.success(true);
        break;

      case "isAutoStartEnabled":
        Log.d(TAG, "isAutoStartEnabled (placeholder)");
        result.success(true);
        break;

      default:
        result.notImplemented();
        break;
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (channel != null) {
      channel.setMethodCallHandler(null);
      channel = null;
    }
    context = null;
    Log.i(TAG, "🧹 AutoStartPlugin detached from engine.");
  }

  // ============================================================================================
  // ActivityAware (pour compatibilité Android TV / multi-activité)
  // ============================================================================================
  @Override public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) { }
  @Override public void onDetachedFromActivityForConfigChanges() { }
  @Override public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) { }
  @Override public void onDetachedFromActivity() { }
}
