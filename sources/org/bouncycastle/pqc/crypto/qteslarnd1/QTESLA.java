package org.bouncycastle.pqc.crypto.qteslarnd1;

import java.security.SecureRandom;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/qteslarnd1/QTESLA.class */
public class QTESLA {
    private static void hashFunction(byte[] bArr, int i, int[] iArr, byte[] bArr2, int i2, int i3, int i4, int i5) {
        byte[] bArr3 = new byte[i3 + 64];
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = ((i5 / 2) - iArr[i6]) >> 31;
            iArr[i6] = ((iArr[i6] - i5) & i7) | (iArr[i6] & (i7 ^ (-1)));
            int i8 = iArr[i6] & ((1 << i4) - 1);
            int i9 = ((1 << (i4 - 1)) - i8) >> 31;
            bArr3[i6] = (byte) ((iArr[i6] - (((i8 - (1 << i4)) & i9) | (i8 & (i9 ^ (-1))))) >> i4);
        }
        System.arraycopy(bArr2, i2, bArr3, i3, 64);
        if (i5 == 4205569) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr, i, 32, bArr3, 0, i3 + 64);
        }
        if (i5 == 4206593 || i5 == 8404993) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr, i, 32, bArr3, 0, i3 + 64);
        }
    }

    private static void hashFunction(byte[] bArr, int i, long[] jArr, byte[] bArr2, int i2, int i3, int i4, int i5, int i6) {
        byte[] bArr3 = new byte[(i3 * i4) + 64];
        for (int i7 = 0; i7 < i4; i7++) {
            int i8 = i3 * i7;
            for (int i9 = 0; i9 < i3; i9++) {
                long j = jArr[i8];
                long j2 = ((i6 / 2) - j) >> 63;
                long j3 = ((1 << (i5 - 1)) - ((((j - i6) & j2) | (j & (j2 ^ (-1)))) & ((1 << i5) - 1))) >> 63;
                int i10 = i8;
                i8++;
                bArr3[i10] = (byte) ((r0 - (((r0 - (1 << i5)) & j3) | (r0 & (j3 ^ (-1))))) >> i5);
            }
        }
        System.arraycopy(bArr2, i2, bArr3, i3 * i4, 64);
        if (i6 == 485978113) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr, i, 32, bArr3, 0, (i3 * i4) + 64);
        }
        if (i6 == 1129725953) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr, i, 32, bArr3, 0, (i3 * i4) + 64);
        }
    }

    private static int absolute(int i) {
        return ((i >> 31) ^ i) - (i >> 31);
    }

    private static long absolute(long j) {
        return ((j >> 63) ^ j) - (j >> 63);
    }

    private static boolean testRejection(int[] iArr, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i; i4++) {
            if (absolute(iArr[i4]) > i2 - i3) {
                return true;
            }
        }
        return false;
    }

    private static boolean testRejection(long[] jArr, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i; i4++) {
            if (absolute(jArr[i4]) > i2 - i3) {
                return true;
            }
        }
        return false;
    }

    private static boolean testZ(int[] iArr, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i; i4++) {
            if (iArr[i4] < (-(i2 - i3)) || iArr[i4] > i2 - i3) {
                return true;
            }
        }
        return false;
    }

    private static boolean testZ(long[] jArr, int i, int i2, int i3) {
        for (int i4 = 0; i4 < i; i4++) {
            if (jArr[i4] < (-(i2 - i3)) || jArr[i4] > i2 - i3) {
                return true;
            }
        }
        return false;
    }

    private static boolean testV(int[] iArr, int i, int i2, int i3, int i4) {
        for (int i5 = 0; i5 < i; i5++) {
            int i6 = ((i3 / 2) - iArr[i5]) >> 31;
            int i7 = ((iArr[i5] - i3) & i6) | (iArr[i5] & (i6 ^ (-1)));
            if (((((absolute(i7) - ((i3 / 2) - i4)) ^ (-1)) >>> 31) | (((absolute(i7 - ((((i7 + (1 << (i2 - 1))) - 1) >> i2) << i2)) - ((1 << (i2 - 1)) - i4)) ^ (-1)) >>> 31)) == 1) {
                return true;
            }
        }
        return false;
    }

    private static boolean testV(long[] jArr, int i, int i2, int i3, int i4, int i5) {
        for (int i6 = 0; i6 < i2; i6++) {
            long j = ((i4 / 2) - jArr[i + i6]) >> 63;
            long j2 = ((jArr[i + i6] - i4) & j) | (jArr[i + i6] & (j ^ (-1)));
            if (((((absolute(j2) - ((i4 / 2) - i5)) ^ (-1)) >>> 63) | (((absolute(j2 - (((int) (((j2 + (1 << (i3 - 1))) - 1) >> i3)) << i3)) - ((1 << (i3 - 1)) - i5)) ^ (-1)) >>> 63)) == 1) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkPolynomial(int[] iArr, int i, int i2, int i3) {
        int i4 = 0;
        int i5 = i2;
        int[] iArr2 = new int[i2];
        for (int i6 = 0; i6 < i2; i6++) {
            iArr2[i6] = absolute(iArr[i6]);
        }
        for (int i7 = 0; i7 < i3; i7++) {
            for (int i8 = 0; i8 < i5 - 1; i8++) {
                int i9 = (iArr2[i8 + 1] - iArr2[i8]) >> 31;
                int i10 = (iArr2[i8 + 1] & i9) | (iArr2[i8] & (i9 ^ (-1)));
                iArr2[i8 + 1] = (iArr2[i8] & i9) | (iArr2[i8 + 1] & (i9 ^ (-1)));
                iArr2[i8] = i10;
            }
            i4 += iArr2[i5 - 1];
            i5--;
        }
        return i4 > i;
    }

    private static boolean checkPolynomial(long[] jArr, int i, int i2, int i3, int i4) {
        int i5 = 0;
        int i6 = i3;
        short[] sArr = new short[i3];
        for (int i7 = 0; i7 < i3; i7++) {
            sArr[i7] = (short) absolute(jArr[i + i7]);
        }
        for (int i8 = 0; i8 < i4; i8++) {
            for (int i9 = 0; i9 < i6 - 1; i9++) {
                short s = (short) ((sArr[i9 + 1] - sArr[i9]) >> 15);
                short s2 = (short) ((sArr[i9 + 1] & s) | (sArr[i9] & (s ^ (-1))));
                sArr[i9 + 1] = (short) ((sArr[i9] & s) | (sArr[i9 + 1] & (s ^ (-1))));
                sArr[i9] = s2;
            }
            i5 += sArr[i6 - 1];
            i6--;
        }
        return i5 > i2;
    }

    private static int generateKeyPair(byte[] bArr, byte[] bArr2, SecureRandom secureRandom, int i, int i2, int i3, long j, int i4, int i5, int i6, double d, int[] iArr, int i7, int i8) {
        int i9 = 0;
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[128];
        int[] iArr2 = new int[i];
        int[] iArr3 = new int[i];
        int[] iArr4 = new int[i];
        int[] iArr5 = new int[i];
        secureRandom.nextBytes(bArr3);
        if (i3 == 4205569) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr4, 0, 128, bArr3, 0, 32);
        }
        if (i3 == 4206593 || i3 == 8404993) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr4, 0, 128, bArr3, 0, 32);
        }
        do {
            if (i3 == 4205569) {
                i9++;
                Sample.polynomialGaussSamplerI(iArr3, 0, bArr4, 0, i9);
            }
            if (i3 == 4206593) {
                i9++;
                Sample.polynomialGaussSamplerIII(iArr3, 0, bArr4, 0, i9, i, d, Sample.EXPONENTIAL_DISTRIBUTION_III_SIZE);
            }
            if (i3 == 8404993) {
                i9++;
                Sample.polynomialGaussSamplerIII(iArr3, 0, bArr4, 0, i9, i, d, Sample.EXPONENTIAL_DISTRIBUTION_III_SPEED);
            }
        } while (checkPolynomial(iArr3, i7, i, i2));
        do {
            if (i3 == 4205569) {
                i9++;
                Sample.polynomialGaussSamplerI(iArr2, 0, bArr4, 32, i9);
            }
            if (i3 == 4206593) {
                i9++;
                Sample.polynomialGaussSamplerIII(iArr2, 0, bArr4, 32, i9, i, d, Sample.EXPONENTIAL_DISTRIBUTION_III_SIZE);
            }
            if (i3 == 8404993) {
                i9++;
                Sample.polynomialGaussSamplerIII(iArr2, 0, bArr4, 32, i9, i, d, Sample.EXPONENTIAL_DISTRIBUTION_III_SPEED);
            }
        } while (checkPolynomial(iArr2, i8, i, i2));
        Polynomial.polynomialUniform(iArr4, bArr4, 64, i, i3, j, i4, i5, i6);
        Polynomial.polynomialMultiplication(iArr5, iArr4, iArr2, i, i3, j, iArr);
        Polynomial.polynomialAdditionCorrection(iArr5, iArr5, iArr3, i, i3);
        if (i3 == 4205569) {
            Pack.encodePrivateKeyI(bArr2, iArr2, iArr3, bArr4, 64);
            Pack.encodePublicKey(bArr, iArr5, bArr4, 64, 512, 23);
        }
        if (i3 == 4206593) {
            Pack.encodePrivateKeyIIISize(bArr2, iArr2, iArr3, bArr4, 64);
            Pack.encodePublicKey(bArr, iArr5, bArr4, 64, 1024, 23);
        }
        if (i3 != 8404993) {
            return 0;
        }
        Pack.encodePrivateKeyIIISpeed(bArr2, iArr2, iArr3, bArr4, 64);
        Pack.encodePublicKeyIIISpeed(bArr, iArr5, bArr4, 64);
        return 0;
    }

    public static int generateKeyPairI(byte[] bArr, byte[] bArr2, SecureRandom secureRandom) {
        return generateKeyPair(bArr, bArr2, secureRandom, 512, 30, Parameter.Q_I, Parameter.Q_INVERSE_I, 23, 19, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_I, 27.0d, PolynomialHeuristic.ZETA_I, 1586, 1586);
    }

    public static int generateKeyPairIIISize(byte[] bArr, byte[] bArr2, SecureRandom secureRandom) {
        return generateKeyPair(bArr, bArr2, secureRandom, 1024, 48, Parameter.Q_III_SIZE, Parameter.Q_INVERSE_III_SIZE, 23, 38, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_SIZE, 9.0d, PolynomialHeuristic.ZETA_III_SIZE, 910, 910);
    }

    public static int generateKeyPairIIISpeed(byte[] bArr, byte[] bArr2, SecureRandom secureRandom) {
        return generateKeyPair(bArr, bArr2, secureRandom, 1024, 48, Parameter.Q_III_SPEED, Parameter.Q_INVERSE_III_SPEED, 24, 38, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_SPEED, 12.0d, PolynomialHeuristic.ZETA_III_SPEED, 1147, 1233);
    }

    private static int generateKeyPair(byte[] bArr, byte[] bArr2, SecureRandom secureRandom, int i, int i2, int i3, int i4, long j, int i5, int i6, int i7, double d, long[] jArr, int i8, int i9) {
        int i10 = 0;
        byte[] bArr3 = new byte[32];
        byte[] bArr4 = new byte[32 * (i2 + 3)];
        long[] jArr2 = new long[i];
        long[] jArr3 = new long[i];
        long[] jArr4 = new long[i * i2];
        long[] jArr5 = new long[i * i2];
        long[] jArr6 = new long[i * i2];
        secureRandom.nextBytes(bArr3);
        if (i4 == 485978113) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr4, 0, 32 * (i2 + 3), bArr3, 0, 32);
        }
        if (i4 == 1129725953) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr4, 0, 32 * (i2 + 3), bArr3, 0, 32);
        }
        for (int i11 = 0; i11 < i2; i11++) {
            do {
                if (i4 == 485978113) {
                    i10++;
                    Sample.polynomialGaussSamplerIP(jArr4, i * i11, bArr4, 32 * i11, i10);
                }
                if (i4 == 1129725953) {
                    i10++;
                    Sample.polynomialGaussSamplerIIIP(jArr4, i * i11, bArr4, 32 * i11, i10);
                }
            } while (checkPolynomial(jArr4, i * i11, i8, i, i3));
        }
        do {
            if (i4 == 485978113) {
                i10++;
                Sample.polynomialGaussSamplerIP(jArr2, 0, bArr4, 32 * i2, i10);
            }
            if (i4 == 1129725953) {
                i10++;
                Sample.polynomialGaussSamplerIIIP(jArr2, 0, bArr4, 32 * i2, i10);
            }
        } while (checkPolynomial(jArr2, 0, i9, i, i3));
        Polynomial.polynomialUniform(jArr5, bArr4, 32 * (i2 + 1), i, i2, i4, j, i5, i6, i7);
        Polynomial.polynomialNumberTheoreticTransform(jArr3, jArr2, i);
        for (int i12 = 0; i12 < i2; i12++) {
            Polynomial.polynomialMultiplication(jArr6, i * i12, jArr5, i * i12, jArr3, 0, i, i4, j);
            Polynomial.polynomialAddition(jArr6, i * i12, jArr6, i * i12, jArr4, i * i12, i);
            for (int i13 = 0; i13 < i; i13++) {
                int i14 = (i * i12) + i13;
                jArr6[i14] = jArr6[i14] - (i4 & ((i4 - jArr6[(i * i12) + i13]) >> 63));
            }
        }
        Pack.packPrivateKey(bArr2, jArr2, jArr4, bArr4, 32 * (i2 + 1), i, i2);
        if (i4 == 485978113) {
            Pack.encodePublicKeyIP(bArr, jArr6, bArr4, 32 * (i2 + 1));
        }
        if (i4 != 1129725953) {
            return 0;
        }
        Pack.encodePublicKeyIIIP(bArr, jArr6, bArr4, 32 * (i2 + 1));
        return 0;
    }

    public static int generateKeyPairIP(byte[] bArr, byte[] bArr2, SecureRandom secureRandom) {
        return generateKeyPair(bArr, bArr2, secureRandom, 1024, 4, 25, Parameter.Q_I_P, Parameter.Q_INVERSE_I_P, 29, 108, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_I_P, 10.0d, PolynomialProvablySecure.ZETA_I_P, 554, 554);
    }

    public static int generateKeyPairIIIP(byte[] bArr, byte[] bArr2, SecureRandom secureRandom) {
        return generateKeyPair(bArr, bArr2, secureRandom, 2048, 5, 40, Parameter.Q_III_P, Parameter.Q_INVERSE_III_P, 31, 180, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_P, 10.0d, PolynomialProvablySecure.ZETA_III_P, 901, 901);
    }

    private static int signing(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom, int i3, int i4, int i5, long j, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int[] iArr) {
        byte[] bArr4 = new byte[32];
        byte[] bArr5 = new byte[32];
        byte[] bArr6 = new byte[128];
        byte[] bArr7 = new byte[64];
        byte[] bArr8 = new byte[32];
        int[] iArr2 = new int[i4];
        short[] sArr = new short[i4];
        short[] sArr2 = new short[i3];
        short[] sArr3 = new short[i3];
        int[] iArr3 = new int[i3];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[i3];
        int[] iArr6 = new int[i3];
        int[] iArr7 = new int[i3];
        int[] iArr8 = new int[i3];
        int i16 = 0;
        if (i5 == 4205569) {
            Pack.decodePrivateKeyI(bArr7, sArr2, sArr3, bArr3);
        }
        if (i5 == 4206593) {
            Pack.decodePrivateKeyIIISize(bArr7, sArr2, sArr3, bArr3);
        }
        if (i5 == 8404993) {
            Pack.decodePrivateKeyIIISpeed(bArr7, sArr2, sArr3, bArr3);
        }
        secureRandom.nextBytes(bArr8);
        System.arraycopy(bArr8, 0, bArr6, 32, 32);
        System.arraycopy(bArr7, 32, bArr6, 0, 32);
        if (i5 == 4205569) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr6, 64, 64, bArr2, 0, i2);
            HashUtils.secureHashAlgorithmKECCAK128(bArr5, 0, 32, bArr6, 0, 128);
        }
        if (i5 == 4206593 || i5 == 8404993) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr6, 64, 64, bArr2, 0, i2);
            HashUtils.secureHashAlgorithmKECCAK256(bArr5, 0, 32, bArr6, 0, 128);
        }
        Polynomial.polynomialUniform(iArr3, bArr7, 0, i3, i5, j, i6, i12, i13);
        while (true) {
            i16++;
            Sample.sampleY(iArr5, bArr5, 0, i16, i3, i5, i7, i8);
            Polynomial.polynomialMultiplication(iArr4, iArr3, iArr5, i3, i5, j, iArr);
            hashFunction(bArr4, 0, iArr4, bArr6, 64, i3, i9, i5);
            Sample.encodeC(iArr2, sArr, bArr4, 0, i3, i4);
            Polynomial.sparsePolynomialMultiplication16(iArr7, sArr2, iArr2, sArr, i3, i4);
            Polynomial.polynomialAddition(iArr6, iArr5, iArr7, i3);
            if (!testRejection(iArr6, i3, i7, i10)) {
                Polynomial.sparsePolynomialMultiplication16(iArr8, sArr3, iArr2, sArr, i3, i4);
                Polynomial.polynomialSubtractionCorrection(iArr4, iArr4, iArr8, i3, i5);
                if (!testV(iArr4, i3, i9, i5, i11)) {
                    break;
                }
            }
        }
        if (i5 == 4205569) {
            Pack.encodeSignature(bArr, 0, bArr4, 0, iArr6, i3, i9);
        }
        if (i5 == 4206593) {
            Pack.encodeSignature(bArr, 0, bArr4, 0, iArr6, i3, i9);
        }
        if (i5 != 8404993) {
            return 0;
        }
        Pack.encodeSignatureIIISpeed(bArr, 0, bArr4, 0, iArr6);
        return 0;
    }

    static int signingI(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom) {
        return signing(bArr, bArr2, i, i2, bArr3, secureRandom, 512, 30, Parameter.Q_I, Parameter.Q_INVERSE_I, 23, 1048575, 20, 21, 1586, 1586, 19, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_I, 1021, 32, PolynomialHeuristic.ZETA_I);
    }

    static int signingIIISize(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom) {
        return signing(bArr, bArr2, i, i2, bArr3, secureRandom, 1024, 48, Parameter.Q_III_SIZE, Parameter.Q_INVERSE_III_SIZE, 23, 1048575, 20, 21, 910, 910, 38, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_SIZE, 1021, 32, PolynomialHeuristic.ZETA_III_SIZE);
    }

    static int signingIIISpeed(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom) {
        return signing(bArr, bArr2, i, i2, bArr3, secureRandom, 1024, 48, Parameter.Q_III_SPEED, Parameter.Q_INVERSE_III_SPEED, 24, 2097151, 21, 22, 1233, 1147, 38, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_SPEED, 511, 32, PolynomialHeuristic.ZETA_III_SPEED);
    }

    private static int signing(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom, int i3, int i4, int i5, int i6, long j, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17) {
        byte[] bArr4 = new byte[32];
        byte[] bArr5 = new byte[32];
        byte[] bArr6 = new byte[128];
        byte[] bArr7 = new byte[32];
        int[] iArr = new int[i5];
        short[] sArr = new short[i5];
        long[] jArr = new long[i3 * i4];
        long[] jArr2 = new long[i3 * i4];
        long[] jArr3 = new long[i3];
        long[] jArr4 = new long[i3];
        long[] jArr5 = new long[i3];
        long[] jArr6 = new long[i3];
        long[] jArr7 = new long[i3 * i4];
        boolean zTestV = false;
        int i18 = 0;
        secureRandom.nextBytes(bArr7);
        System.arraycopy(bArr7, 0, bArr6, 32, 32);
        System.arraycopy(bArr3, i15 - 32, bArr6, 0, 32);
        if (i6 == 485978113) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr6, 64, 64, bArr2, 0, i2);
            HashUtils.secureHashAlgorithmKECCAK128(bArr5, 0, 32, bArr6, 0, 128);
        }
        if (i6 == 1129725953) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr6, 64, 64, bArr2, 0, i2);
            HashUtils.secureHashAlgorithmKECCAK256(bArr5, 0, 32, bArr6, 0, 128);
        }
        Polynomial.polynomialUniform(jArr, bArr3, i15 - 64, i3, i4, i6, j, i7, i13, i14);
        while (true) {
            i18++;
            Sample.sampleY(jArr3, bArr5, 0, i18, i3, i6, i8, i9);
            Polynomial.polynomialNumberTheoreticTransform(jArr4, jArr3, i3);
            for (int i19 = 0; i19 < i4; i19++) {
                Polynomial.polynomialMultiplication(jArr2, i3 * i19, jArr, i3 * i19, jArr4, 0, i3, i6, j);
            }
            hashFunction(bArr4, 0, jArr2, bArr6, 64, i3, i4, i10, i6);
            Sample.encodeC(iArr, sArr, bArr4, 0, i3, i5);
            Polynomial.sparsePolynomialMultiplication8(jArr6, 0, bArr3, 0, iArr, sArr, i3, i5);
            Polynomial.polynomialAddition(jArr5, 0, jArr3, 0, jArr6, 0, i3);
            if (!testRejection(jArr5, i3, i8, i11)) {
                for (int i20 = 0; i20 < i4; i20++) {
                    Polynomial.sparsePolynomialMultiplication8(jArr7, i3 * i20, bArr3, i3 * (i20 + 1), iArr, sArr, i3, i5);
                    Polynomial.polynomialSubtraction(jArr2, i3 * i20, jArr2, i3 * i20, jArr7, i3 * i20, i3, i6, i16, i17);
                    zTestV = testV(jArr2, i3 * i20, i3, i10, i6, i12);
                    if (zTestV) {
                        break;
                    }
                }
                if (!zTestV) {
                    break;
                }
            }
        }
        if (i6 == 485978113) {
            Pack.encodeSignatureIP(bArr, 0, bArr4, 0, jArr5);
        }
        if (i6 != 1129725953) {
            return 0;
        }
        Pack.encodeSignatureIIIP(bArr, 0, bArr4, 0, jArr5);
        return 0;
    }

    public static int signingIP(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom) {
        return signing(bArr, bArr2, i, i2, bArr3, secureRandom, 1024, 4, 25, Parameter.Q_I_P, Parameter.Q_INVERSE_I_P, 29, 2097151, 21, 22, 554, 554, 108, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_I_P, Polynomial.PRIVATE_KEY_I_P, 1, 29);
    }

    public static int signingIIIP(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, SecureRandom secureRandom) {
        return signing(bArr, bArr2, i, i2, bArr3, secureRandom, 2048, 5, 40, Parameter.Q_III_P, Parameter.Q_INVERSE_III_P, 31, Parameter.B_III_P, 23, 24, 901, 901, 180, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_P, Polynomial.PRIVATE_KEY_III_P, 15, 34);
    }

    private static int verifying(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, int i3, int i4, int i5, long j, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int[] iArr) {
        byte[] bArr4 = new byte[32];
        byte[] bArr5 = new byte[32];
        byte[] bArr6 = new byte[32];
        byte[] bArr7 = new byte[64];
        int[] iArr2 = new int[i3];
        int[] iArr3 = new int[i4];
        short[] sArr = new short[i4];
        int[] iArr4 = new int[i3];
        int[] iArr5 = new int[i3];
        int[] iArr6 = new int[i3];
        int[] iArr7 = new int[i3];
        if (i2 < i11) {
            return -1;
        }
        if (i5 == 4205569 || i5 == 4206593) {
            Pack.decodeSignature(bArr4, iArr5, bArr2, i, i3, i8);
        }
        if (i5 == 8404993) {
            Pack.decodeSignatureIIISpeed(bArr4, iArr5, bArr2, i);
        }
        if (testZ(iArr5, i3, i7, i9)) {
            return -2;
        }
        if (i5 == 4205569 || i5 == 4206593) {
            Pack.decodePublicKey(iArr2, bArr6, 0, bArr3, i3, i6);
        }
        if (i5 == 8404993) {
            Pack.decodePublicKeyIIISpeed(iArr2, bArr6, 0, bArr3);
        }
        Polynomial.polynomialUniform(iArr7, bArr6, 0, i3, i5, j, i6, i12, i13);
        Sample.encodeC(iArr3, sArr, bArr4, 0, i3, i4);
        Polynomial.sparsePolynomialMultiplication32(iArr6, iArr2, iArr3, sArr, i3, i4);
        Polynomial.polynomialMultiplication(iArr4, iArr7, iArr5, i3, i5, j, iArr);
        Polynomial.polynomialSubtractionMontgomery(iArr4, iArr4, iArr6, i3, i5, j, i10);
        if (i5 == 4205569) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr7, 0, 64, bArr, 0, bArr.length);
        }
        if (i5 == 4206593 || i5 == 8404993) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr7, 0, 64, bArr, 0, bArr.length);
        }
        hashFunction(bArr5, 0, iArr4, bArr7, 0, i3, i8, i5);
        return !CommonFunction.memoryEqual(bArr4, 0, bArr5, 0, 32) ? -3 : 0;
    }

    static int verifyingI(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3) {
        return verifying(bArr, bArr2, i, i2, bArr3, 512, 30, Parameter.Q_I, Parameter.Q_INVERSE_I, 23, 1048575, 21, 1586, Parameter.R_I, 1376, 19, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_I, 1021, 32, PolynomialHeuristic.ZETA_I);
    }

    static int verifyingIIISize(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3) {
        return verifying(bArr, bArr2, i, i2, bArr3, 1024, 48, Parameter.Q_III_SIZE, Parameter.Q_INVERSE_III_SIZE, 23, 1048575, 21, 910, Parameter.R_III_SIZE, Polynomial.SIGNATURE_III_SIZE, 38, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_SIZE, 1021, 32, PolynomialHeuristic.ZETA_III_SIZE);
    }

    static int verifyingIIISpeed(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3) {
        return verifying(bArr, bArr2, i, i2, bArr3, 1024, 48, Parameter.Q_III_SPEED, Parameter.Q_INVERSE_III_SPEED, 24, 2097151, 22, 1233, Parameter.R_III_SPEED, 2848, 38, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_SPEED, 511, 32, PolynomialHeuristic.ZETA_III_SPEED);
    }

    private static int verifying(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3, int i3, int i4, int i5, int i6, long j, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, long[] jArr) {
        byte[] bArr4 = new byte[32];
        byte[] bArr5 = new byte[32];
        byte[] bArr6 = new byte[32];
        byte[] bArr7 = new byte[64];
        int[] iArr = new int[i3 * i4];
        int[] iArr2 = new int[i5];
        short[] sArr = new short[i5];
        long[] jArr2 = new long[i3 * i4];
        long[] jArr3 = new long[i3];
        long[] jArr4 = new long[i3];
        long[] jArr5 = new long[i3 * i4];
        long[] jArr6 = new long[i3 * i4];
        if (i2 < i11) {
            return -1;
        }
        if (i6 == 485978113) {
            Pack.decodeSignatureIP(bArr4, jArr3, bArr2, i);
        }
        if (i6 == 1129725953) {
            Pack.decodeSignatureIIIP(bArr4, jArr3, bArr2, i);
        }
        if (testZ(jArr3, i3, i8, i10)) {
            return -2;
        }
        if (i6 == 485978113) {
            Pack.decodePublicKeyIP(iArr, bArr6, 0, bArr3);
        }
        if (i6 == 1129725953) {
            Pack.decodePublicKeyIIIP(iArr, bArr6, 0, bArr3);
        }
        Polynomial.polynomialUniform(jArr6, bArr6, 0, i3, i4, i6, j, i7, i12, i13);
        Sample.encodeC(iArr2, sArr, bArr4, 0, i3, i5);
        Polynomial.polynomialNumberTheoreticTransform(jArr4, jArr3, i3);
        for (int i16 = 0; i16 < i4; i16++) {
            Polynomial.polynomialMultiplication(jArr2, i3 * i16, jArr6, i3 * i16, jArr4, 0, i3, i6, j);
            Polynomial.sparsePolynomialMultiplication32(jArr5, i3 * i16, iArr, i3 * i16, iArr2, sArr, i3, i5, i6, i14, i15);
            Polynomial.polynomialSubtraction(jArr2, i3 * i16, jArr2, i3 * i16, jArr5, i3 * i16, i3, i6, i14, i15);
        }
        if (i6 == 485978113) {
            HashUtils.secureHashAlgorithmKECCAK128(bArr7, 0, 64, bArr, 0, bArr.length);
        }
        if (i6 == 1129725953) {
            HashUtils.secureHashAlgorithmKECCAK256(bArr7, 0, 64, bArr, 0, bArr.length);
        }
        hashFunction(bArr5, 0, jArr2, bArr7, 0, i3, i4, i9, i6);
        return !CommonFunction.memoryEqual(bArr4, 0, bArr5, 0, 32) ? -3 : 0;
    }

    static int verifyingPI(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3) {
        return verifying(bArr, bArr2, i, i2, bArr3, 1024, 4, 25, Parameter.Q_I_P, Parameter.Q_INVERSE_I_P, 29, 2097151, 22, 554, 2848, 108, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_I_P, 1, 29, PolynomialProvablySecure.ZETA_I_P);
    }

    static int verifyingPIII(byte[] bArr, byte[] bArr2, int i, int i2, byte[] bArr3) {
        return verifying(bArr, bArr2, i, i2, bArr3, 2048, 5, 40, Parameter.Q_III_P, Parameter.Q_INVERSE_III_P, 31, Parameter.B_III_P, 24, 901, Polynomial.SIGNATURE_III_P, 180, Parameter.INVERSE_NUMBER_THEORETIC_TRANSFORM_III_P, 15, 34, PolynomialProvablySecure.ZETA_III_P);
    }
}
