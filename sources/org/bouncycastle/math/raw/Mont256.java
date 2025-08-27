package org.bouncycastle.math.raw;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/raw/Mont256.class */
public abstract class Mont256 {
    private static final long M = 4294967295L;

    public static int inverse32(int i) {
        int i2 = i * (2 - (i * i));
        int i3 = i2 * (2 - (i * i2));
        int i4 = i3 * (2 - (i * i3));
        return i4 * (2 - (i * i4));
    }

    public static void multAdd(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i) {
        int i2 = 0;
        long j = iArr2[0] & 4294967295L;
        for (int i3 = 0; i3 < 8; i3++) {
            long j2 = iArr[i3] & 4294967295L;
            long j3 = j2 * j;
            long j4 = (j3 & 4294967295L) + (iArr3[0] & 4294967295L);
            long j5 = (((int) j4) * i) & 4294967295L;
            long j6 = j5 * (iArr4[0] & 4294967295L);
            long j7 = ((j4 + (j6 & 4294967295L)) >>> 32) + (j3 >>> 32) + (j6 >>> 32);
            for (int i4 = 1; i4 < 8; i4++) {
                long j8 = j2 * (iArr2[i4] & 4294967295L);
                long j9 = j5 * (iArr4[i4] & 4294967295L);
                long j10 = j7 + (j8 & 4294967295L) + (j9 & 4294967295L) + (iArr3[i4] & 4294967295L);
                iArr3[i4 - 1] = (int) j10;
                j7 = (j10 >>> 32) + (j8 >>> 32) + (j9 >>> 32);
            }
            long j11 = j7 + (i2 & 4294967295L);
            iArr3[7] = (int) j11;
            i2 = (int) (j11 >>> 32);
        }
        if (i2 != 0 || Nat256.gte(iArr3, iArr4)) {
            Nat256.sub(iArr3, iArr4, iArr3);
        }
    }

    public static void multAddXF(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4) {
        int i = 0;
        long j = iArr2[0] & 4294967295L;
        for (int i2 = 0; i2 < 8; i2++) {
            long j2 = iArr[i2] & 4294967295L;
            long j3 = (j2 * j) + (iArr3[0] & 4294967295L);
            long j4 = j3 & 4294967295L;
            long j5 = (j3 >>> 32) + j4;
            for (int i3 = 1; i3 < 8; i3++) {
                long j6 = j2 * (iArr2[i3] & 4294967295L);
                long j7 = j4 * (iArr4[i3] & 4294967295L);
                long j8 = j5 + (j6 & 4294967295L) + (j7 & 4294967295L) + (iArr3[i3] & 4294967295L);
                iArr3[i3 - 1] = (int) j8;
                j5 = (j8 >>> 32) + (j6 >>> 32) + (j7 >>> 32);
            }
            long j9 = j5 + (i & 4294967295L);
            iArr3[7] = (int) j9;
            i = (int) (j9 >>> 32);
        }
        if (i != 0 || Nat256.gte(iArr3, iArr4)) {
            Nat256.sub(iArr3, iArr4, iArr3);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2, int i) {
        for (int i2 = 0; i2 < 8; i2++) {
            long j = (r0 * i) & 4294967295L;
            long j2 = ((j * (iArr2[0] & 4294967295L)) + (iArr[0] & 4294967295L)) >>> 32;
            for (int i3 = 1; i3 < 8; i3++) {
                long j3 = j2 + (j * (iArr2[i3] & 4294967295L)) + (iArr[i3] & 4294967295L);
                iArr[i3 - 1] = (int) j3;
                j2 = j3 >>> 32;
            }
            iArr[7] = (int) j2;
        }
        if (Nat256.gte(iArr, iArr2)) {
            Nat256.sub(iArr, iArr2, iArr);
        }
    }

    public static void reduceXF(int[] iArr, int[] iArr2) {
        for (int i = 0; i < 8; i++) {
            long j = iArr[0] & 4294967295L;
            long j2 = j;
            for (int i2 = 1; i2 < 8; i2++) {
                long j3 = j2 + (j * (iArr2[i2] & 4294967295L)) + (iArr[i2] & 4294967295L);
                iArr[i2 - 1] = (int) j3;
                j2 = j3 >>> 32;
            }
            iArr[7] = (int) j2;
        }
        if (Nat256.gte(iArr, iArr2)) {
            Nat256.sub(iArr, iArr2, iArr);
        }
    }
}
