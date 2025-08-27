package org.apache.coyote.http2;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/coyote/http2/ByteUtil.class */
public class ByteUtil {
    private ByteUtil() {
    }

    public static boolean isBit7Set(byte input) {
        return (input & 128) != 0;
    }

    public static int get31Bits(byte[] input, int firstByte) {
        return ((input[firstByte] & Byte.MAX_VALUE) << 24) + ((input[firstByte + 1] & 255) << 16) + ((input[firstByte + 2] & 255) << 8) + (input[firstByte + 3] & 255);
    }

    public static void set31Bits(byte[] output, int firstByte, int value) {
        output[firstByte] = (byte) ((value & 2130706432) >> 24);
        output[firstByte + 1] = (byte) ((value & 16711680) >> 16);
        output[firstByte + 2] = (byte) ((value & 65280) >> 8);
        output[firstByte + 3] = (byte) (value & 255);
    }

    public static int getOneByte(byte[] input, int pos) {
        return input[pos] & 255;
    }

    public static int getTwoBytes(byte[] input, int firstByte) {
        return ((input[firstByte] & 255) << 8) + (input[firstByte + 1] & 255);
    }

    public static int getThreeBytes(byte[] input, int firstByte) {
        return ((input[firstByte] & 255) << 16) + ((input[firstByte + 1] & 255) << 8) + (input[firstByte + 2] & 255);
    }

    public static void setOneBytes(byte[] output, int firstByte, int value) {
        output[firstByte] = (byte) (value & 255);
    }

    public static void setTwoBytes(byte[] output, int firstByte, int value) {
        output[firstByte] = (byte) ((value & 65280) >> 8);
        output[firstByte + 1] = (byte) (value & 255);
    }

    public static void setThreeBytes(byte[] output, int firstByte, int value) {
        output[firstByte] = (byte) ((value & 16711680) >> 16);
        output[firstByte + 1] = (byte) ((value & 65280) >> 8);
        output[firstByte + 2] = (byte) (value & 255);
    }

    public static long getFourBytes(byte[] input, int firstByte) {
        return ((input[firstByte] & 255) << 24) + ((input[firstByte + 1] & 255) << 16) + ((input[firstByte + 2] & 255) << 8) + (input[firstByte + 3] & 255);
    }

    public static void setFourBytes(byte[] output, int firstByte, long value) {
        output[firstByte] = (byte) ((value & (-16777216)) >> 24);
        output[firstByte + 1] = (byte) ((value & 16711680) >> 16);
        output[firstByte + 2] = (byte) ((value & 65280) >> 8);
        output[firstByte + 3] = (byte) (value & 255);
    }
}
