package com.itextpdf.io.codec.brotli.dec;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/Utils.class */
final class Utils {
    private static final byte[] BYTE_ZEROES = new byte[1024];
    private static final int[] INT_ZEROES = new int[1024];

    Utils() {
    }

    static void fillWithZeroes(byte[] dest, int offset, int length) {
        int i = 0;
        while (true) {
            int cursor = i;
            if (cursor < length) {
                int step = Math.min(cursor + 1024, length) - cursor;
                System.arraycopy(BYTE_ZEROES, 0, dest, offset + cursor, step);
                i = cursor + step;
            } else {
                return;
            }
        }
    }

    static void fillWithZeroes(int[] dest, int offset, int length) {
        int i = 0;
        while (true) {
            int cursor = i;
            if (cursor < length) {
                int step = Math.min(cursor + 1024, length) - cursor;
                System.arraycopy(INT_ZEROES, 0, dest, offset + cursor, step);
                i = cursor + step;
            } else {
                return;
            }
        }
    }
}
