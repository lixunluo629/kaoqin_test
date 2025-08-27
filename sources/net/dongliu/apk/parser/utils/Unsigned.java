package net.dongliu.apk.parser.utils;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/Unsigned.class */
public class Unsigned {
    public static long toLong(int value) {
        return value & 4294967295L;
    }

    public static int toUInt(long value) {
        return (int) value;
    }

    public static int toInt(short value) {
        return value & 65535;
    }

    public static short toUShort(int value) {
        return (short) value;
    }

    public static int ensureUInt(long value) {
        if (value < 0 || value > 2147483647L) {
            throw new ArithmeticException("unsigned integer overflow");
        }
        return (int) value;
    }

    public static long ensureULong(long value) {
        if (value < 0) {
            throw new ArithmeticException("unsigned long overflow");
        }
        return value;
    }

    public static short toShort(byte value) {
        return (short) (value & 255);
    }

    public static byte toUByte(short value) {
        return (byte) value;
    }
}
