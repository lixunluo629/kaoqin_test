package com.mysql.jdbc.util;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/Base64Decoder.class */
public class Base64Decoder {
    private static byte[] decoderMap = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/Base64Decoder$IntWrapper.class */
    public static class IntWrapper {
        public int value;

        public IntWrapper(int value) {
            this.value = value;
        }
    }

    private static byte getNextValidByte(byte[] in, IntWrapper pos, int maxPos) {
        while (pos.value <= maxPos) {
            if (in[pos.value] >= 0 && decoderMap[in[pos.value]] >= 0) {
                int i = pos.value;
                pos.value = i + 1;
                return in[i];
            }
            pos.value++;
        }
        return (byte) 61;
    }

    public static byte[] decode(byte[] in, int pos, int length) {
        IntWrapper offset = new IntWrapper(pos);
        byte[] sestet = new byte[4];
        int outLen = (length * 3) / 4;
        byte[] octet = new byte[outLen];
        int octetId = 0;
        int maxPos = (offset.value + length) - 1;
        while (offset.value <= maxPos) {
            sestet[0] = decoderMap[getNextValidByte(in, offset, maxPos)];
            sestet[1] = decoderMap[getNextValidByte(in, offset, maxPos)];
            sestet[2] = decoderMap[getNextValidByte(in, offset, maxPos)];
            sestet[3] = decoderMap[getNextValidByte(in, offset, maxPos)];
            if (sestet[1] != -2) {
                int i = octetId;
                octetId++;
                octet[i] = (byte) ((sestet[0] << 2) | (sestet[1] >>> 4));
            }
            if (sestet[2] != -2) {
                int i2 = octetId;
                octetId++;
                octet[i2] = (byte) (((sestet[1] & 15) << 4) | (sestet[2] >>> 2));
            }
            if (sestet[3] != -2) {
                int i3 = octetId;
                octetId++;
                octet[i3] = (byte) (((sestet[2] & 3) << 6) | sestet[3]);
            }
        }
        byte[] out = new byte[octetId];
        System.arraycopy(octet, 0, out, 0, octetId);
        return out;
    }
}
