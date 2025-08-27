package com.itextpdf.io.font.woff2;

/* loaded from: io-7.1.10.jar:com/itextpdf/io/font/woff2/VariableLength.class */
class VariableLength {
    VariableLength() {
    }

    public static int read255UShort(Buffer buf) {
        byte code = buf.readByte();
        if (JavaUnsignedUtil.asU8(code) == 253) {
            short result = buf.readShort();
            return JavaUnsignedUtil.asU16(result);
        }
        if (JavaUnsignedUtil.asU8(code) == 255) {
            byte result2 = buf.readByte();
            return JavaUnsignedUtil.asU8(result2) + 253;
        }
        if (JavaUnsignedUtil.asU8(code) == 254) {
            byte result3 = buf.readByte();
            return JavaUnsignedUtil.asU8(result3) + 506;
        }
        return JavaUnsignedUtil.asU8(code);
    }

    public static int readBase128(Buffer buf) {
        int result = 0;
        for (int i = 0; i < 5; i++) {
            byte code = buf.readByte();
            if (i == 0 && JavaUnsignedUtil.asU8(code) == 128) {
                throw new FontCompressionException(FontCompressionException.READ_BASE_128_FAILED);
            }
            if ((result & (-33554432)) != 0) {
                throw new FontCompressionException(FontCompressionException.READ_BASE_128_FAILED);
            }
            result = (result << 7) | (code & Byte.MAX_VALUE);
            if ((code & 128) == 0) {
                return result;
            }
        }
        throw new FontCompressionException(FontCompressionException.READ_BASE_128_FAILED);
    }
}
