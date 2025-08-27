package org.bouncycastle.pqc.crypto.qteslarnd1;

import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qteslarnd1/Polynomial.class */
class Polynomial {
    public static final int RANDOM = 32;
    public static final int SEED = 32;
    public static final int HASH = 32;
    public static final int MESSAGE = 64;
    public static final int SIGNATURE_I = 1376;
    public static final int SIGNATURE_III_SIZE = 2720;
    public static final int SIGNATURE_III_SPEED = 2848;
    public static final int SIGNATURE_I_P = 2848;
    public static final int SIGNATURE_III_P = 6176;
    public static final int PUBLIC_KEY_I = 1504;
    public static final int PUBLIC_KEY_III_SIZE = 2976;
    public static final int PUBLIC_KEY_III_SPEED = 3104;
    public static final int PUBLIC_KEY_I_P = 14880;
    public static final int PUBLIC_KEY_III_P = 39712;
    public static final int PRIVATE_KEY_I = 1344;
    public static final int PRIVATE_KEY_III_SIZE = 2112;
    public static final int PRIVATE_KEY_III_SPEED = 2368;
    public static final int PRIVATE_KEY_I_P = 5184;
    public static final int PRIVATE_KEY_III_P = 12352;

    Polynomial() {
    }

    private static int montgomery(long j, int i, long j2) {
        return (int) ((j + (((j * j2) & 4294967295L) * i)) >> 32);
    }

    private static long montgomeryP(long j, int i, long j2) {
        return (j + (((j * j2) & 4294967295L) * i)) >> 32;
    }

    public static int barrett(int i, int i2, int i3, int i4) {
        return i - (((int) ((i * i3) >> i4)) * i2);
    }

    public static long barrett(long j, int i, int i2, int i3) {
        return j - (((j * i2) >> i3) * i);
    }

    private static void numberTheoreticTransform(int[] iArr, int[] iArr2, int i, int i2, long j) {
        int i3 = 0;
        int i4 = i;
        while (true) {
            int i5 = i4 >> 1;
            if (i5 <= 0) {
                return;
            }
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 < i) {
                    int i8 = i3;
                    i3++;
                    long j2 = iArr2[i8];
                    int i9 = i7;
                    while (i9 < i7 + i5) {
                        int iMontgomery = montgomery(j2 * iArr[i9 + i5], i2, j);
                        iArr[i9 + i5] = iArr[i9] - iMontgomery;
                        iArr[i9] = iArr[i9] + iMontgomery;
                        i9++;
                    }
                    i6 = i9 + i5;
                }
            }
            i4 = i5;
        }
    }

    private static void numberTheoreticTransformIP(long[] jArr, long[] jArr2) {
        int i = 0;
        for (int i2 = 512; i2 > 0; i2 >>= 1) {
            int i3 = 0;
            while (true) {
                int i4 = i3;
                if (i4 < 1024) {
                    int i5 = i;
                    i++;
                    long j = jArr2[i5];
                    int i6 = i4;
                    while (i6 < i4 + i2) {
                        long jMontgomeryP = montgomeryP(j * jArr[i6 + i2], Parameter.Q_I_P, Parameter.Q_INVERSE_I_P);
                        jArr[i6 + i2] = jArr[i6] + (485978113 - jMontgomeryP);
                        jArr[i6] = jArr[i6] + jMontgomeryP;
                        i6++;
                    }
                    i3 = i6 + i2;
                }
            }
        }
    }

    private static void numberTheoreticTransformIIIP(long[] jArr, long[] jArr2) {
        int i = 0;
        int i2 = 1024;
        while (true) {
            int i3 = i2;
            if (i3 <= 0) {
                return;
            }
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < 2048) {
                    int i6 = i;
                    i++;
                    int i7 = (int) jArr2[i6];
                    int i8 = i5;
                    while (i8 < i5 + i3) {
                        long jBarrett = barrett(montgomeryP(i7 * jArr[i8 + i3], Parameter.Q_III_P, Parameter.Q_INVERSE_III_P), Parameter.Q_III_P, 15, 34);
                        jArr[i8 + i3] = barrett(jArr[i8] + (2259451906L - jBarrett), Parameter.Q_III_P, 15, 34);
                        jArr[i8] = barrett(jArr[i8] + jBarrett, Parameter.Q_III_P, 15, 34);
                        i8++;
                    }
                    i4 = i8 + i3;
                }
            }
            i2 = i3 >> 1;
        }
    }

    private static void inverseNumberTheoreticTransformI(int[] iArr, int[] iArr2) {
        int i = 0;
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 >= 512) {
                break;
            }
            int i4 = 0;
            while (true) {
                int i5 = i4;
                if (i5 < 512) {
                    int i6 = i;
                    i++;
                    long j = iArr2[i6];
                    int i7 = i5;
                    while (i7 < i5 + i3) {
                        iArr[i7] = iArr[i7] + iArr[i7 + i3];
                        iArr[i7 + i3] = montgomery(j * (r0 - iArr[i7 + i3]), Parameter.Q_I, Parameter.Q_INVERSE_I);
                        i7++;
                    }
                    i4 = i7 + i3;
                }
            }
            i2 = i3 * 2;
        }
        for (int i8 = 0; i8 < 256; i8++) {
            iArr[i8] = montgomery(1081347 * iArr[i8], Parameter.Q_I, Parameter.Q_INVERSE_I);
        }
    }

    private static void inverseNumberTheoreticTransform(int[] iArr, int[] iArr2, int i, int i2, long j, int i3, int i4, int i5) {
        int i6 = 0;
        int i7 = 1;
        while (true) {
            int i8 = i7;
            if (i8 >= i) {
                break;
            }
            int i9 = 0;
            while (true) {
                int i10 = i9;
                if (i10 < i) {
                    int i11 = i6;
                    i6++;
                    long j2 = iArr2[i11];
                    int i12 = i10;
                    while (i12 < i10 + i8) {
                        int i13 = iArr[i12];
                        if (i8 == 16) {
                            iArr[i12] = barrett(i13 + iArr[i12 + i8], i2, i4, i5);
                        } else {
                            iArr[i12] = i13 + iArr[i12 + i8];
                        }
                        iArr[i12 + i8] = montgomery(j2 * (i13 - iArr[i12 + i8]), i2, j);
                        i12++;
                    }
                    i9 = i12 + i8;
                }
            }
            i7 = i8 * 2;
        }
        for (int i14 = 0; i14 < i / 2; i14++) {
            iArr[i14] = montgomery(i3 * iArr[i14], i2, j);
        }
    }

    private static void inverseNumberTheoreticTransformIP(long[] jArr, int i, long[] jArr2, int i2) {
        int i3 = 0;
        int i4 = 1;
        while (true) {
            int i5 = i4;
            if (i5 >= 1024) {
                return;
            }
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 >= 1024) {
                    break;
                }
                int i8 = i3;
                i3++;
                long j = jArr2[i2 + i8];
                int i9 = i7;
                while (i9 < i7 + i5) {
                    long j2 = jArr[i + i9];
                    jArr[i + i9] = j2 + jArr[i + i9 + i5];
                    jArr[i + i9 + i5] = montgomeryP(j * (j2 + (971956226 - jArr[(i + i9) + i5])), Parameter.Q_I_P, Parameter.Q_INVERSE_I_P);
                    i9++;
                }
                i6 = i9 + i5;
            }
            int i10 = i5 * 2;
            int i11 = 0;
            while (true) {
                int i12 = i11;
                if (i12 < 1024) {
                    int i13 = i3;
                    i3++;
                    long j3 = jArr2[i2 + i13];
                    int i14 = i12;
                    while (i14 < i12 + i10) {
                        long j4 = jArr[i + i14];
                        jArr[i + i14] = barrett(j4 + jArr[i + i14 + i10], Parameter.Q_I_P, 1, 29);
                        jArr[i + i14 + i10] = montgomeryP(j3 * (j4 + (971956226 - jArr[(i + i14) + i10])), Parameter.Q_I_P, Parameter.Q_INVERSE_I_P);
                        i14++;
                    }
                    i11 = i14 + i10;
                }
            }
            i4 = i10 * 2;
        }
    }

    private static void inverseNumberTheoreticTransformIIIP(long[] jArr, int i, long[] jArr2, int i2) {
        int i3 = 0;
        int i4 = 1;
        while (true) {
            int i5 = i4;
            if (i5 >= 2048) {
                return;
            }
            int i6 = 0;
            while (true) {
                int i7 = i6;
                if (i7 < 2048) {
                    int i8 = i3;
                    i3++;
                    long j = jArr2[i2 + i8];
                    int i9 = i7;
                    while (i9 < i7 + i5) {
                        long j2 = jArr[i + i9];
                        jArr[i + i9] = barrett(j2 + jArr[i + i9 + i5], Parameter.Q_III_P, 15, 34);
                        jArr[i + i9 + i5] = barrett(montgomeryP(j * (j2 + (2259451906L - jArr[(i + i9) + i5])), Parameter.Q_III_P, Parameter.Q_INVERSE_III_P), Parameter.Q_III_P, 15, 34);
                        i9++;
                    }
                    i6 = i9 + i5;
                }
            }
            i4 = i5 * 2;
        }
    }

    private static void componentWisePolynomialMultiplication(int[] iArr, int[] iArr2, int[] iArr3, int i, int i2, long j) {
        for (int i3 = 0; i3 < i; i3++) {
            iArr[i3] = montgomery(iArr2[i3] * iArr3[i3], i2, j);
        }
    }

    private static void componentWisePolynomialMultiplication(long[] jArr, int i, long[] jArr2, int i2, long[] jArr3, int i3, int i4, int i5, long j) {
        for (int i6 = 0; i6 < i4; i6++) {
            jArr[i + i6] = montgomeryP(jArr2[i2 + i6] * jArr3[i3 + i6], i5, j);
        }
    }

    public static void polynomialNumberTheoreticTransform(long[] jArr, long[] jArr2, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            jArr[i2] = jArr2[i2];
        }
        if (i == 1024) {
            numberTheoreticTransformIP(jArr, PolynomialProvablySecure.ZETA_I_P);
        }
        if (i == 2048) {
            numberTheoreticTransformIIIP(jArr, PolynomialProvablySecure.ZETA_III_P);
        }
    }

    public static void polynomialMultiplication(int[] iArr, int[] iArr2, int[] iArr3, int i, int i2, long j, int[] iArr4) {
        int[] iArr5 = new int[i];
        for (int i3 = 0; i3 < i; i3++) {
            iArr5[i3] = iArr3[i3];
        }
        numberTheoreticTransform(iArr5, iArr4, i, i2, j);
        componentWisePolynomialMultiplication(iArr, iArr2, iArr5, i, i2, j);
        if (i2 == 4205569) {
            inverseNumberTheoreticTransformI(iArr, PolynomialHeuristic.ZETA_INVERSE_I);
        }
        if (i2 == 4206593) {
            inverseNumberTheoreticTransform(iArr, PolynomialHeuristic.ZETA_INVERSE_III_SIZE, 1024, Parameter.Q_III_SIZE, Parameter.Q_INVERSE_III_SIZE, Parameter.R_III_SIZE, 1021, 32);
        }
        if (i2 == 8404993) {
            inverseNumberTheoreticTransform(iArr, PolynomialHeuristic.ZETA_INVERSE_III_SPEED, 1024, Parameter.Q_III_SPEED, Parameter.Q_INVERSE_III_SPEED, Parameter.R_III_SPEED, 511, 32);
        }
    }

    public static void polynomialMultiplication(long[] jArr, int i, long[] jArr2, int i2, long[] jArr3, int i3, int i4, int i5, long j) {
        componentWisePolynomialMultiplication(jArr, i, jArr2, i2, jArr3, i3, i4, i5, j);
        if (i5 == 485978113) {
            inverseNumberTheoreticTransformIP(jArr, i, PolynomialProvablySecure.ZETA_INVERSE_I_P, 0);
        }
        if (i5 == 1129725953) {
            inverseNumberTheoreticTransformIIIP(jArr, i, PolynomialProvablySecure.ZETA_INVERSE_III_P, 0);
        }
    }

    public static void polynomialAddition(int[] iArr, int[] iArr2, int[] iArr3, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            iArr[i2] = iArr2[i2] + iArr3[i2];
        }
    }

    public static void polynomialAddition(long[] jArr, int i, long[] jArr2, int i2, long[] jArr3, int i3, int i4) {
        for (int i5 = 0; i5 < i4; i5++) {
            jArr[i + i5] = jArr2[i2 + i5] + jArr3[i3 + i5];
        }
    }

    public static void polynomialAdditionCorrection(int[] iArr, int[] iArr2, int[] iArr3, int i, int i2) {
        for (int i3 = 0; i3 < i; i3++) {
            iArr[i3] = iArr2[i3] + iArr3[i3];
            int i4 = i3;
            iArr[i4] = iArr[i4] + ((iArr[i3] >> 31) & i2);
            int i5 = i3;
            iArr[i5] = iArr[i5] - i2;
            int i6 = i3;
            iArr[i6] = iArr[i6] + ((iArr[i3] >> 31) & i2);
        }
    }

    public static void polynomialSubtractionCorrection(int[] iArr, int[] iArr2, int[] iArr3, int i, int i2) {
        for (int i3 = 0; i3 < i; i3++) {
            iArr[i3] = iArr2[i3] - iArr3[i3];
            int i4 = i3;
            iArr[i4] = iArr[i4] + ((iArr[i3] >> 31) & i2);
        }
    }

    public static void polynomialSubtractionMontgomery(int[] iArr, int[] iArr2, int[] iArr3, int i, int i2, long j, int i3) {
        for (int i4 = 0; i4 < i; i4++) {
            iArr[i4] = montgomery(i3 * (iArr2[i4] - iArr3[i4]), i2, j);
        }
    }

    public static void polynomialSubtraction(long[] jArr, int i, long[] jArr2, int i2, long[] jArr3, int i3, int i4, int i5, int i6, int i7) {
        for (int i8 = 0; i8 < i4; i8++) {
            jArr[i + i8] = barrett(jArr2[i2 + i8] - jArr3[i3 + i8], i5, i6, i7);
        }
    }

    public static void polynomialUniform(int[] iArr, byte[] bArr, int i, int i2, int i3, long j, int i4, int i5, int i6) {
        int i7 = 0;
        int i8 = 0;
        int i9 = (i4 + 7) / 8;
        int i10 = i5;
        int i11 = (1 << i4) - 1;
        byte[] bArr2 = new byte[168 * i5];
        short s = (short) (0 + 1);
        HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(bArr2, 0, 168 * i5, (short) 0, bArr, i, 32);
        while (i8 < i2) {
            if (i7 > (168 * i10) - (4 * i9)) {
                i10 = 1;
                short s2 = s;
                s = (short) (s + 1);
                HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(bArr2, 0, 168 * 1, s2, bArr, i, 32);
                i7 = 0;
            }
            int iLoad32 = CommonFunction.load32(bArr2, i7) & i11;
            int i12 = i7 + i9;
            int iLoad322 = CommonFunction.load32(bArr2, i12) & i11;
            int i13 = i12 + i9;
            int iLoad323 = CommonFunction.load32(bArr2, i13) & i11;
            int i14 = i13 + i9;
            int iLoad324 = CommonFunction.load32(bArr2, i14) & i11;
            i7 = i14 + i9;
            if (iLoad32 < i3 && i8 < i2) {
                int i15 = i8;
                i8++;
                iArr[i15] = montgomery(iLoad32 * i6, i3, j);
            }
            if (iLoad322 < i3 && i8 < i2) {
                int i16 = i8;
                i8++;
                iArr[i16] = montgomery(iLoad322 * i6, i3, j);
            }
            if (iLoad323 < i3 && i8 < i2) {
                int i17 = i8;
                i8++;
                iArr[i17] = montgomery(iLoad323 * i6, i3, j);
            }
            if (iLoad324 < i3 && i8 < i2) {
                int i18 = i8;
                i8++;
                iArr[i18] = montgomery(iLoad324 * i6, i3, j);
            }
        }
    }

    public static void polynomialUniform(long[] jArr, byte[] bArr, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7) {
        int i8 = 0;
        int i9 = 0;
        int i10 = (i5 + 7) / 8;
        int i11 = i6;
        int i12 = (1 << i5) - 1;
        byte[] bArr2 = new byte[168 * i11];
        short s = (short) (0 + 1);
        HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(bArr2, 0, 168 * i11, (short) 0, bArr, i, 32);
        while (i9 < i2 * i3) {
            if (i8 > (168 * i11) - (4 * i10)) {
                i11 = 1;
                short s2 = s;
                s = (short) (s + 1);
                HashUtils.customizableSecureHashAlgorithmKECCAK128Simple(bArr2, 0, 168 * 1, s2, bArr, i, 32);
                i8 = 0;
            }
            int iLoad32 = CommonFunction.load32(bArr2, i8) & i12;
            int i13 = i8 + i10;
            int iLoad322 = CommonFunction.load32(bArr2, i13) & i12;
            int i14 = i13 + i10;
            int iLoad323 = CommonFunction.load32(bArr2, i14) & i12;
            int i15 = i14 + i10;
            int iLoad324 = CommonFunction.load32(bArr2, i15) & i12;
            i8 = i15 + i10;
            if (iLoad32 < i4 && i9 < i2 * i3) {
                int i16 = i9;
                i9++;
                jArr[i16] = montgomeryP(iLoad32 * i7, i4, j);
            }
            if (iLoad322 < i4 && i9 < i2 * i3) {
                int i17 = i9;
                i9++;
                jArr[i17] = montgomeryP(iLoad322 * i7, i4, j);
            }
            if (iLoad323 < i4 && i9 < i2 * i3) {
                int i18 = i9;
                i9++;
                jArr[i18] = montgomeryP(iLoad323 * i7, i4, j);
            }
            if (iLoad324 < i4 && i9 < i2 * i3) {
                int i19 = i9;
                i9++;
                jArr[i19] = montgomeryP(iLoad324 * i7, i4, j);
            }
        }
    }

    public static void sparsePolynomialMultiplication16(int[] iArr, short[] sArr, int[] iArr2, short[] sArr2, int i, int i2) {
        Arrays.fill(iArr, 0);
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = iArr2[i3];
            for (int i5 = 0; i5 < i4; i5++) {
                int i6 = i5;
                iArr[i6] = iArr[i6] - (sArr2[i3] * sArr[(i + i5) - i4]);
            }
            for (int i7 = i4; i7 < i; i7++) {
                int i8 = i7;
                iArr[i8] = iArr[i8] + (sArr2[i3] * sArr[i7 - i4]);
            }
        }
    }

    public static void sparsePolynomialMultiplication8(long[] jArr, int i, byte[] bArr, int i2, int[] iArr, short[] sArr, int i3, int i4) {
        Arrays.fill(jArr, 0L);
        for (int i5 = 0; i5 < i4; i5++) {
            int i6 = iArr[i5];
            for (int i7 = 0; i7 < i6; i7++) {
                int i8 = i + i7;
                jArr[i8] = jArr[i8] - (sArr[i5] * bArr[((i2 + i3) + i7) - i6]);
            }
            for (int i9 = i6; i9 < i3; i9++) {
                int i10 = i + i9;
                jArr[i10] = jArr[i10] + (sArr[i5] * bArr[(i2 + i9) - i6]);
            }
        }
    }

    public static void sparsePolynomialMultiplication32(int[] iArr, int[] iArr2, int[] iArr3, short[] sArr, int i, int i2) {
        Arrays.fill(iArr, 0);
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = iArr3[i3];
            for (int i5 = 0; i5 < i4; i5++) {
                int i6 = i5;
                iArr[i6] = iArr[i6] - (sArr[i3] * iArr2[(i + i5) - i4]);
            }
            for (int i7 = i4; i7 < i; i7++) {
                int i8 = i7;
                iArr[i8] = iArr[i8] + (sArr[i3] * iArr2[i7 - i4]);
            }
        }
    }

    public static void sparsePolynomialMultiplication32(long[] jArr, int i, int[] iArr, int i2, int[] iArr2, short[] sArr, int i3, int i4, int i5, int i6, int i7) {
        Arrays.fill(jArr, 0L);
        for (int i8 = 0; i8 < i4; i8++) {
            int i9 = iArr2[i8];
            for (int i10 = 0; i10 < i9; i10++) {
                int i11 = i + i10;
                jArr[i11] = jArr[i11] - (sArr[i8] * iArr[((i2 + i3) + i10) - i9]);
            }
            for (int i12 = i9; i12 < i3; i12++) {
                int i13 = i + i12;
                jArr[i13] = jArr[i13] + (sArr[i8] * iArr[(i2 + i12) - i9]);
            }
        }
        for (int i14 = 0; i14 < i3; i14++) {
            jArr[i + i14] = barrett(jArr[i + i14], i5, i6, i7);
        }
    }
}
