import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'flutter_pos_printer_platform_platform_interface.dart';

/// An implementation of [FlutterPosPrinterPlatformPlatform] that uses method channels.
class MethodChannelFlutterPosPrinterPlatform extends FlutterPosPrinterPlatformPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_pos_printer_platform');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
