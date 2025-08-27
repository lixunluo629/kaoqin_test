package org.bouncycastle.crypto.util;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/util/Pack.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/util/Pack.class */
public abstract class Pack {
    public static int bigEndianToInt(byte[] bArr, int i) {
        int i2 = bArr[i] << 24;
        int i3 = i + 1;
        int i4 = i2 | ((bArr[i3] & 255) << 16);
        int i5 = i3 + 1;
        return i4 | ((bArr[i5] & 255) << 8) | (bArr[i5 + 1] & 255);
    }

    public static void intToBigEndian(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) (i >>> 24);
        int i3 = i2 + 1;
        bArr[i3] = (byte) (i >>> 16);
        int i4 = i3 + 1;
        bArr[i4] = (byte) (i >>> 8);
        bArr[i4 + 1] = (byte) i;
    }

    public static long bigEndianToLong(byte[] bArr, int i) {
        return ((bigEndianToInt(bArr, i) & 4294967295L) << 32) | (bigEndianToInt(bArr, i + 4) & 4294967295L);
    }

    public static void longToBigEndian(long j, byte[] bArr, int i) {
        intToBigEndian((int) (j >>> 32), bArr, i);
        intToBigEndian((int) (j & 4294967295L), bArr, i + 4);
    }

    public static int littleEndianToInt(byte[] bArr, int i) {
        byte b = bArr[i];
        int i2 = i + 1;
        int i3 = b | ((bArr[i2] & 255) << 8);
        int i4 = i2 + 1;
        return i3 | ((bArr[i4] & 255) << 16) | ((bArr[i4 + 1] & 255) << 24);
    }

    public static void intToLittleEndian(int i, byte[] bArr, int i2) {
        bArr[i2] = (byte) i;
        int i3 = i2 + 1;
        bArr[i3] = (byte) (i >>> 8);
        int i4 = i3 + 1;
        bArr[i4] = (byte) (i >>> 16);
        bArr[i4 + 1] = (byte) (i >>> 24);
    }

    public static long littleEndianToLong(byte[] bArr, int i) {
        return ((littleEndianToInt(bArr, i + 4) & 4294967295L) << 32) | (littleEndianToInt(bArr, i) & 4294967295L);
    }

    public static void longToLittleEndian(long j, byte[] bArr, int i) {
        intToLittleEndian((int) (j & 4294967295L), bArr, i);
        intToLittleEndian((int) (j >>> 32), bArr, i + 4);
    }
}
