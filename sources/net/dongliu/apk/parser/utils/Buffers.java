package net.dongliu.apk.parser.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: apk-parser-2.5.3.jar:net/dongliu/apk/parser/utils/Buffers.class */
public class Buffers {
    public static short readUByte(ByteBuffer buffer) {
        byte b = buffer.get();
        return (short) (b & 255);
    }

    public static int readUShort(ByteBuffer buffer) {
        short s = buffer.getShort();
        return s & 65535;
    }

    public static long readUInt(ByteBuffer buffer) {
        int i = buffer.getInt();
        return i & 4294967295L;
    }

    public static byte[] readBytes(ByteBuffer buffer, int size) {
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return bytes;
    }

    public static byte[] readBytes(ByteBuffer buffer) {
        return readBytes(buffer, buffer.remaining());
    }

    public static String readAsciiString(ByteBuffer buffer, int strLen) {
        byte[] bytes = new byte[strLen];
        buffer.get(bytes);
        return new String(bytes);
    }

    public static String readString(ByteBuffer buffer, int strLen) {
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            sb.append(buffer.getChar());
        }
        return sb.toString();
    }

    public static String readZeroTerminatedString(ByteBuffer buffer, int strLen) {
        StringBuilder sb = new StringBuilder(strLen);
        int i = 0;
        while (true) {
            if (i >= strLen) {
                break;
            }
            char c = buffer.getChar();
            if (c == 0) {
                skip(buffer, ((strLen - i) - 1) * 2);
                break;
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }

    public static void skip(ByteBuffer buffer, int count) {
        position(buffer, buffer.position() + count);
    }

    public static void position(ByteBuffer buffer, int position) {
        buffer.position(position);
    }

    public static void position(ByteBuffer buffer, long position) {
        position(buffer, Unsigned.ensureUInt(position));
    }

    public static ByteBuffer sliceAndSkip(ByteBuffer buffer, int size) {
        ByteBuffer buf = buffer.slice().order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer slice = (ByteBuffer) buf.limit(buf.position() + size);
        skip(buffer, size);
        return slice;
    }
}
