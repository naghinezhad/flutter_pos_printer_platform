
import 'flutter_pos_printer_platform_platform_interface.dart';

class FlutterPosPrinterPlatform {
  Future<String?> getPlatformVersion() {
    return FlutterPosPrinterPlatformPlatform.instance.getPlatformVersion();
  }
}
