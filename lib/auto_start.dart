library auto_start;

import 'dart:async';
import 'package:flutter/services.dart';

class AutoStart {
  static const MethodChannel _channel = MethodChannel('auto_start');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
