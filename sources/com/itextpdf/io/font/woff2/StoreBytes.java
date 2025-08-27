package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/StoreBytes.class */
class StoreBytes {
    StoreBytes() {
    }

    public static int storeU32(byte[] dst, int offset, int x) {
        dst[offset] = JavaUnsignedUtil.toU8(x >> 24);
        dst[offset + 1] = JavaUnsignedUtil.toU8(x >> 16);
        dst[offset + 2] = JavaUnsignedUtil.toU8(x >> 8);
        dst[offset + 3] = JavaUnsignedUtil.toU8(x);
        return offset + 4;
    }

    public static int storeU16(byte[] dst, int offset, int x) {
        dst[offset] = JavaUnsignedUtil.toU8(x >> 8);
        dst[offset + 1] = JavaUnsignedUtil.toU8(x);
        return offset + 2;
    }
}
