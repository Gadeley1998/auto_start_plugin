library auto_start;

import 'package:flutter/services.dart';

/// Plugin AutoStart
/// Connecte Flutter à la couche native Android si nécessaire
class AutoStart {
  static const MethodChannel _channel = MethodChannel('auto_start');

  /// Test de communication Android (facultatif)
  static Future<String?> getPlatformVersion() async {
    try {
      return await _channel.invokeMethod<String>('getPlatformVersion');
    } catch (_) {
      return null;
    }
  }
}
