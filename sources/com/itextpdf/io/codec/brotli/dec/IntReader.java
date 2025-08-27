package com.itextpdf.io.codec.brotli.dec;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/codec/brotli/dec/IntReader.class */
final class IntReader {
    private byte[] byteBuffer;
    private int[] intBuffer;

    IntReader() {
    }

    static void init(IntReader ir, byte[] byteBuffer, int[] intBuffer) {
        ir.byteBuffer = byteBuffer;
        ir.intBuffer = intBuffer;
    }

    static void convert(IntReader ir, int intLen) {
        for (int i = 0; i < intLen; i++) {
            ir.intBuffer[i] = (ir.byteBuffer[i * 4] & 255) | ((ir.byteBuffer[(i * 4) + 1] & 255) << 8) | ((ir.byteBuffer[(i * 4) + 2] & 255) << 16) | ((ir.byteBuffer[(i * 4) + 3] & 255) << 24);
        }
    }
}
