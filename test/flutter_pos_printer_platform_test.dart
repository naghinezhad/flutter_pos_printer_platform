import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_pos_printer_platform/flutter_pos_printer_platform.dart';
import 'package:flutter_pos_printer_platform/flutter_pos_printer_platform_platform_interface.dart';
import 'package:flutter_pos_printer_platform/flutter_pos_printer_platform_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterPosPrinterPlatformPlatform
    with MockPlatformInterfaceMixin
    implements FlutterPosPrinterPlatformPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterPosPrinterPlatformPlatform initialPlatform = FlutterPosPrinterPlatformPlatform.instance;

  test('$MethodChannelFlutterPosPrinterPlatform is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterPosPrinterPlatform>());
  });

  test('getPlatformVersion', () async {
    FlutterPosPrinterPlatform flutterPosPrinterPlatformPlugin = FlutterPosPrinterPlatform();
    MockFlutterPosPrinterPlatformPlatform fakePlatform = MockFlutterPosPrinterPlatformPlatform();
    FlutterPosPrinterPlatformPlatform.instance = fakePlatform;

    expect(await flutterPosPrinterPlatformPlugin.getPlatformVersion(), '42');
  });
}
