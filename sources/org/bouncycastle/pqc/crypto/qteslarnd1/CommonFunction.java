package org.bouncycastle.pqc.crypto.qteslarnd1;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qteslarnd1/CommonFunction.class */
class CommonFunction {
    CommonFunction() {
    }

    public static boolean memoryEqual(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        if (i + i3 > bArr.length || i2 + i3 > bArr2.length) {
            return false;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            if (bArr[i + i4] != bArr2[i2 + i4]) {
                return false;
            }
        }
        return true;
    }

    public static short load16(byte[] bArr, int i) {
        short s = 0;
        if (bArr.length - i >= 2) {
            for (int i2 = 0; i2 < 2; i2++) {
                s = (short) (s ^ (((short) (bArr[i + i2] & 255)) << (8 * i2)));
            }
        } else {
            for (int i3 = 0; i3 < bArr.length - i; i3++) {
                s = (short) (s ^ (((short) (bArr[i + i3] & 255)) << (8 * i3)));
            }
        }
        return s;
    }

    public static int load32(byte[] bArr, int i) {
        int i2 = 0;
        if (bArr.length - i >= 4) {
            for (int i3 = 0; i3 < 4; i3++) {
                i2 ^= (bArr[i + i3] & 255) << (8 * i3);
            }
        } else {
            for (int i4 = 0; i4 < bArr.length - i; i4++) {
                i2 ^= (bArr[i + i4] & 255) << (8 * i4);
            }
        }
        return i2;
    }

    public static long load64(byte[] bArr, int i) {
        long j = 0;
        if (bArr.length - i >= 8) {
            for (int i2 = 0; i2 < 8; i2++) {
                j ^= (bArr[i + i2] & 255) << (8 * i2);
            }
        } else {
            for (int i3 = 0; i3 < bArr.length - i; i3++) {
                j ^= (bArr[i + i3] & 255) << (8 * i3);
            }
        }
        return j;
    }

    public static void store16(byte[] bArr, int i, short s) {
        if (bArr.length - i >= 2) {
            for (int i2 = 0; i2 < 2; i2++) {
                bArr[i + i2] = (byte) ((s >> (8 * i2)) & 255);
            }
            return;
        }
        for (int i3 = 0; i3 < bArr.length - i; i3++) {
            bArr[i + i3] = (byte) ((s >> (8 * i3)) & 255);
        }
    }

    public static void store32(byte[] bArr, int i, int i2) {
        if (bArr.length - i >= 4) {
            for (int i3 = 0; i3 < 4; i3++) {
                bArr[i + i3] = (byte) ((i2 >> (8 * i3)) & 255);
            }
            return;
        }
        for (int i4 = 0; i4 < bArr.length - i; i4++) {
            bArr[i + i4] = (byte) ((i2 >> (8 * i4)) & 255);
        }
    }

    public static void store64(byte[] bArr, int i, long j) {
        if (bArr.length - i >= 8) {
            for (int i2 = 0; i2 < 8; i2++) {
                bArr[i + i2] = (byte) ((j >> (8 * i2)) & 255);
            }
            return;
        }
        for (int i3 = 0; i3 < bArr.length - i; i3++) {
            bArr[i + i3] = (byte) ((j >> (8 * i3)) & 255);
        }
    }
}
