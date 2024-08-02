import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_pos_printer_platform_method_channel.dart';

abstract class FlutterPosPrinterPlatformPlatform extends PlatformInterface {
  /// Constructs a FlutterPosPrinterPlatformPlatform.
  FlutterPosPrinterPlatformPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterPosPrinterPlatformPlatform _instance = MethodChannelFlutterPosPrinterPlatform();

  /// The default instance of [FlutterPosPrinterPlatformPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterPosPrinterPlatform].
  static FlutterPosPrinterPlatformPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterPosPrinterPlatformPlatform] when
  /// they register themselves.
  static set instance(FlutterPosPrinterPlatformPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
