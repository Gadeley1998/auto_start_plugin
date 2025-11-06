library auto_start;

import 'package:flutter/services.dart';

/// {@template auto_start}
/// A Flutter plugin that allows your app to:
/// - Start automatically when the device boots (Android only)
/// - Stay alive in foreground services (for Android TV / Kiosk apps)
///
/// This plugin communicates with native Android code using a [MethodChannel].
/// {@endtemplate}
class AutoStart {
  /// The single [MethodChannel] used to communicate with native Android.
  static const MethodChannel _channel = MethodChannel('auto_start');

  /// Returns the Android system version as a string (for testing communication).
  static Future<String?> getPlatformVersion() async {
    try {
      return await _channel.invokeMethod<String>('getPlatformVersion');
    } catch (e) {
      // ignore: avoid_print
      print('AutoStart.getPlatformVersion error: $e');
      return null;
    }
  }

  /// Enables automatic startup behavior on Android.
  /// 
  /// You’ll need to implement this method natively in
  /// `AutoStartPlugin.java` under the method name 'enableAutoStart'.
  static Future<void> enableAutoStart() async {
    try {
      await _channel.invokeMethod('enableAutoStart');
    } catch (e) {
      // ignore: avoid_print
      print('AutoStart.enableAutoStart error: $e');
    }
  }

  /// Checks if the auto-start feature is currently active.
  static Future<bool> isAutoStartEnabled() async {
    try {
      final result = await _channel.invokeMethod<bool>('isAutoStartEnabled');
      return result ?? false;
    } catch (e) {
      // ignore: avoid_print
      print('AutoStart.isAutoStartEnabled error: $e');
      return false;
    }
  }
}
