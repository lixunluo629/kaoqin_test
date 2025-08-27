package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/Woff2Converter.class */
public class Woff2Converter {
    public static boolean isWoff2Font(byte[] woff2Bytes) {
        if (woff2Bytes.length < 4) {
            return false;
        }
        Buffer file = new Buffer(woff2Bytes, 0, 4);
        try {
            return file.readInt() == 2001684018;
        } catch (Exception e) {
            return false;
        }
    }

    public static byte[] convert(byte[] woff2Bytes) {
        byte[] inner_byte_buffer = new byte[Woff2Dec.computeWoff2FinalSize(woff2Bytes, woff2Bytes.length)];
        Woff2Out out = new Woff2MemoryOut(inner_byte_buffer, inner_byte_buffer.length);
        Woff2Dec.convertWoff2ToTtf(woff2Bytes, woff2Bytes.length, out);
        return inner_byte_buffer;
    }
}
