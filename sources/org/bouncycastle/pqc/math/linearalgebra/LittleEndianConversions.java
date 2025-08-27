package org.bouncycastle.pqc.math.linearalgebra;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/math/linearalgebra/LittleEndianConversions.class */
public final class LittleEndianConversions {
    private LittleEndianConversions() {
    }

    public static int OS2IP(byte[] bArr) {
        return (bArr[0] & 255) | ((bArr[1] & 255) << 8) | ((bArr[2] & 255) << 16) | ((bArr[3] & 255) << 24);
    }

    public static int OS2IP(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = bArr[i] & 255;
        int i4 = i2 + 1;
        return i3 | ((bArr[i2] & 255) << 8) | ((bArr[i4] & 255) << 16) | ((bArr[i4 + 1] & 255) << 24);
    }

    public static int OS2IP(byte[] bArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = i2 - 1; i4 >= 0; i4--) {
            i3 |= (bArr[i + i4] & 255) << (8 * i4);
        }
        return i3;
    }

    public static long OS2LIP(byte[] bArr, int i) {
        long j = bArr[i] & 255;
        long j2 = j | ((bArr[r8] & 255) << 8);
        long j3 = j2 | ((bArr[r8] & 255) << 16);
        long j4 = j3 | ((bArr[r8] & 255) << 24);
        long j5 = j4 | ((bArr[r8] & 255) << 32);
        long j6 = j5 | ((bArr[r8] & 255) << 40);
        long j7 = j6 | ((bArr[r8] & 255) << 48);
        int i2 = i + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1;
        return j7 | ((bArr[r8] & 255) << 56);
    }

    public static byte[] I2OSP(int i) {
        return new byte[]{(byte) i, (byte) (i >>> 8), (byte) (i >>> 16), (byte) (i >>> 24)};
    }

    public static void I2OSP(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) i;
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 8);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (i >>> 16);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (i >>> 24);
    }

    public static void I2OSP(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = i3 - 1; i4 >= 0; i4--) {
            bArr[i2 + i4] = (byte) (i >>> (8 * i4));
        }
    }

    public static byte[] I2OSP(long j) {
        return new byte[]{(byte) j, (byte) (j >>> 8), (byte) (j >>> 16), (byte) (j >>> 24), (byte) (j >>> 32), (byte) (j >>> 40), (byte) (j >>> 48), (byte) (j >>> 56)};
    }

    public static void I2OSP(long j, byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) j;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (j >>> 8);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (j >>> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (j >>> 24);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (j >>> 32);
        int i7 = i6 + 1;
        bArr[i6] = (byte) (j >>> 40);
        bArr[i7] = (byte) (j >>> 48);
        bArr[i7 + 1] = (byte) (j >>> 56);
    }

    public static byte[] toByteArray(int[] iArr, int i) {
        int length = iArr.length;
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        while (i3 <= length - 2) {
            I2OSP(iArr[i3], bArr, i2);
            i3++;
            i2 += 4;
        }
        I2OSP(iArr[length - 1], bArr, i2, i - i2);
        return bArr;
    }

    public static int[] toIntArray(byte[] bArr) {
        int length = (bArr.length + 3) / 4;
        int length2 = bArr.length & 3;
        int[] iArr = new int[length];
        int i = 0;
        int i2 = 0;
        while (i2 <= length - 2) {
            iArr[i2] = OS2IP(bArr, i);
            i2++;
            i += 4;
        }
        if (length2 != 0) {
            iArr[length - 1] = OS2IP(bArr, i, length2);
        } else {
            iArr[length - 1] = OS2IP(bArr, i);
        }
        return iArr;
    }
}
