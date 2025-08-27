package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/JavaUnsignedUtil.class */
class JavaUnsignedUtil {
    JavaUnsignedUtil() {
    }

    public static int asU16(short number) {
        return number & 65535;
    }

    public static int asU8(byte number) {
        return number & 255;
    }

    public static byte toU8(int number) {
        return (byte) (number & 255);
    }

    public static short toU16(int number) {
        return (short) (number & 65535);
    }

    public static int compareAsUnsigned(int left, int right) {
        return Long.valueOf(left & 4294967295L).compareTo(Long.valueOf(right & 4294967295L));
    }
}
