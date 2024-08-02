import 'dart:typed_data';

import 'package:flutter_pos_printer_platform/printer.dart';
import 'package:flutter_pos_printer_platform/src/utils.dart';
import 'package:image/image.dart' as img;

class EscPosPrinter<T> extends GenericPrinter<T> {
  EscPosPrinter(
    super.connector,
    super.model, {
    this.dpi = 200,
    required this.width,
    this.beepCount = 4,
  });

  final int width;
  final int dpi;
  final int beepCount;

  @override
  Future<bool> beep() async {
    return true;
  }

  @override
  Future<bool> image(Uint8List image, {int threshold = 150}) async {
    final decodedImage = img.decodeImage(image)!;

    var imgData =
        ImageData(width: decodedImage.width, height: decodedImage.height);
    final converted =
        toPixel(imgData, paperWidth: width, dpi: dpi, isTspl: false);

    final ms = 1000 + (converted.height * 0.5).toInt();

    return await sendToConnector(() {
      List<int> bytes = [];
      return bytes;
    }, delayMs: ms);
  }

  @override
  Future<bool> pulseDrawer() async {
    return await sendToConnector(() => [0x1b, 0x70, 0x00, 0x1e, 0xff, 0x00]);
  }

  @override
  Future<bool> selfTest() async {
    return true;
  }

  @override
  Future<bool> setIp(String ipAddress) async {
    return await sendToConnector(() => encodeSetIP(ipAddress));
  }
}
