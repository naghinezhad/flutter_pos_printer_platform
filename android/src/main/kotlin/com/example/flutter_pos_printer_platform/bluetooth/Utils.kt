package com.example.flutter_pos_printer_platform.bluetooth

import android.graphics.Bitmap
import android.util.Log
import java.util.*

class Utils {


    companion object {
        // UNICODE 0x23 = #
        @Suppress("unused")
        val UNICODE_TEXT = byteArrayOf(
            0x23, 0x23, 0x23,
            0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
            0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
            0x23, 0x23, 0x23
        )
        private const val hexStr = "0123456789ABCDEF"
        private val binaryArray = arrayOf(
            "0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111"
        )

        fun decodeBitmap(bmp: Bitmap): ByteArray? {
            val bmpWidth: Int = bmp.width
            val bmpHeight: Int = bmp.height
            val list: MutableList<String> = ArrayList() //binaryString list
            var sb: StringBuffer
//            var bitLen = bmpWidth / 8
            val zeroCount = bmpWidth % 8
            var zeroStr = ""
            if (zeroCount > 0) {
//                bitLen = bmpWidth / 8 + 1
                for (i in 0 until 8 - zeroCount) {
                    zeroStr += "0"
                }
            }
            for (i in 0 until bmpHeight) {
                sb = StringBuffer()
                for (j in 0 until bmpWidth) {
                    val color: Int = bmp.getPixel(j, i)
                    val r = color shr 16 and 0xff
                    val g = color shr 8 and 0xff
                    val b = color and 0xff

                    // if color close to white，bit='0', else bit='1'
                    if (r > 160 && g > 160 && b > 160) sb.append("0") else sb.append("1")
                }
                if (zeroCount > 0) {
                    sb.append(zeroStr)
                }
                list.add(sb.toString())
            }
            val bmpHexList = binaryListToHexStringList(list)
            val commandHexString = "1D763000"
            var widthHexString = Integer
                .toHexString(if (bmpWidth % 8 == 0) bmpWidth / 8 else bmpWidth / 8 + 1)
            if (widthHexString.length > 2) {
                Log.e("decodeBitmap error", " width is too large")
                return null
            } else if (widthHexString.length == 1) {
                widthHexString = "0$widthHexString"
            }
            widthHexString += "00"
            var heightHexString = Integer.toHexString(bmpHeight)
            if (heightHexString.length > 2) {
                Log.e("decodeBitmap error", " height is too large")
                return null
            } else if (heightHexString.length == 1) {
                heightHexString = "0$heightHexString"
            }
            heightHexString += "00"
            val commandList: MutableList<String> = ArrayList()
            commandList.add(commandHexString + widthHexString + heightHexString)
            commandList.addAll(bmpHexList)
            return hexList2Byte(commandList)
        }

        private fun binaryListToHexStringList(list: List<String>): List<String> {
            val hexList: MutableList<String> = ArrayList()
            for (binaryStr in list) {
                val sb = StringBuffer()
                var i = 0
                while (i < binaryStr.length) {
                    val str = binaryStr.substring(i, i + 8)
                    val hexString = myBinaryStrToHexString(str)
                    sb.append(hexString)
                    i += 8
                }
                hexList.add(sb.toString())
            }
            return hexList
        }

        private fun myBinaryStrToHexString(binaryStr: String): String {
            var hex = ""
            val f4 = binaryStr.substring(0, 4)
            val b4 = binaryStr.substring(4, 8)
            for (i in binaryArray.indices) {
                if (f4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
            }
            for (i in binaryArray.indices) {
                if (b4 == binaryArray[i]) hex += hexStr.substring(i, i + 1)
            }
            return hex
        }

        private fun hexList2Byte(list: List<String>): ByteArray {
            val commandList: MutableList<ByteArray?> = ArrayList()
            for (hexStr in list) {
                commandList.add(hexStringToBytes(hexStr))
            }
            return sysCopy(commandList)
        }

        private fun hexStringToBytes(hexString: String?): ByteArray? {
            var hexString2 = hexString
            if (hexString2 == null || hexString2 == "") {
                return null
            }
            hexString2 = hexString2.uppercase(Locale.getDefault())
            val length = hexString2.length / 2
            val hexChars = hexString2.toCharArray()
            val d = ByteArray(length)
            for (i in 0 until length) {
                val pos = i * 2
                // d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
                d[i] = ((charToByte(hexChars[pos]).toInt() shl 4) or charToByte(hexChars[pos + 1]).toInt()).toByte()
            }
            return d
        }

        private fun sysCopy(srcArrays: List<ByteArray?>): ByteArray {
            var len = 0
            for (srcArray in srcArrays) {
                len += srcArray!!.size
            }
            val destArray = ByteArray(len)
            var destLen = 0
            for (srcArray in srcArrays) {
                if (srcArray != null) {
                    System.arraycopy(srcArray, 0, destArray, destLen, srcArray.size)
                    destLen += srcArray.size
                }

            }
            return destArray
        }

        private fun charToByte(c: Char): Byte {
            return "0123456789ABCDEF".indexOf(c).toByte()
        }
    }
}