package com.trendcatchadvertising.digitaldisplay;

import androidx.annotation.NonNull;
import android.app.Application;
import android.util.Log;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * AutoStartPlugin
 * Amélioré avec :
 * ✅ Méthode de communication Flutter ↔ Android
 * ✅ Enregistrement automatique du helper batterie
 * ✅ Compatible FlutterPlugin + ActivityAware
 */
public class AutoStartPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {

  private MethodChannel channel;
  private Application application;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    application = (Application) flutterPluginBinding.getApplicationContext();

    // ✅ Enregistrer le canal
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "auto_start");
    channel.setMethodCallHandler(this);

    // ✅ Enregistrer BatteryOptimizationHelper AUTO au démarrage
    Log.d("AutoStartPlugin", "Enregistrement BatteryOptimizationHelper");
    new BatteryOptimizationHelper(application);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
      Log.d("AutoStartPlugin", "[auto_start] getPlatformVersion exécuté");
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  // ============================================================================================
  // ✅ Implémentations ActivityAware pour éviter erreurs sur Android TV
  // ============================================================================================

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {}

  @Override
  public void onDetachedFromActivityForConfigChanges() {}

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {}

  @Override
  public void onDetachedFromActivity() {}
}
