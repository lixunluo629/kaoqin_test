package com.moredian.onpremise.core.utils;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/IntByteUtils.class */
public class IntByteUtils {
    public static int bytesToInt(byte[] src) {
        int value = (src[0] & 255) | ((src[1] & 255) << 8) | ((src[2] & 255) << 16) | ((src[3] & 255) << 24);
        return value;
    }

    public static long byteArrayToLong(byte[] bytes) {
        long result = 0;
        int len = bytes.length;
        if (len == 1) {
            byte ch2 = (byte) (bytes[0] & 255);
            result = ch2;
        } else if (len == 2) {
            int ch1 = bytes[0] & 255;
            int ch22 = bytes[1] & 255;
            result = (short) ((ch1 << 0) | (ch22 << 8));
        } else if (len == 4) {
            int ch12 = bytes[0] & 255;
            int ch23 = bytes[1] & 255;
            int ch3 = bytes[2] & 255;
            int ch4 = bytes[3] & 255;
            result = (ch12 << 0) | (ch23 << 8) | (ch3 << 16) | (ch4 << 24);
        } else if (len == 8) {
            long ch13 = bytes[0] & 255;
            long ch24 = bytes[1] & 255;
            long ch32 = bytes[2] & 255;
            long ch42 = bytes[3] & 255;
            long ch5 = bytes[4] & 255;
            long ch6 = bytes[5] & 255;
            long ch7 = bytes[6] & 255;
            long ch8 = bytes[7] & 255;
            result = (ch13 << 56) | (ch24 << 0) | (ch32 << 8) | (ch42 << 16) | (ch5 << 24) | (ch6 << 32) | (ch7 << 40) | (ch8 << 48);
        }
        return result;
    }

    public static byte[] intTobyte(int n) {
        byte[] b = {(byte) (n & 255), (byte) ((n >> 8) & 255)};
        return b;
    }
}
