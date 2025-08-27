package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAValidationParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/generators/DSAParametersGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/generators/DSAParametersGenerator.class */
public class DSAParametersGenerator {
    private int L;
    private int N;
    private int certainty;
    private SecureRandom random;
    private static final BigInteger ZERO = BigInteger.valueOf(0);
    private static final BigInteger ONE = BigInteger.valueOf(1);
    private static final BigInteger TWO = BigInteger.valueOf(2);

    public void init(int i, int i2, SecureRandom secureRandom) {
        init(i, getDefaultN(i), i2, secureRandom);
    }

    private void init(int i, int i2, int i3, SecureRandom secureRandom) {
        this.L = i;
        this.N = i2;
        this.certainty = i3;
        this.random = secureRandom;
    }

    public DSAParameters generateParameters() {
        return this.L > 1024 ? generateParameters_FIPS186_3() : generateParameters_FIPS186_2();
    }

    private DSAParameters generateParameters_FIPS186_2() {
        byte[] bArr = new byte[20];
        byte[] bArr2 = new byte[20];
        byte[] bArr3 = new byte[20];
        byte[] bArr4 = new byte[20];
        SHA1Digest sHA1Digest = new SHA1Digest();
        int i = (this.L - 1) / 160;
        byte[] bArr5 = new byte[this.L / 8];
        while (true) {
            this.random.nextBytes(bArr);
            hash(sHA1Digest, bArr, bArr2);
            System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
            inc(bArr3);
            hash(sHA1Digest, bArr3, bArr3);
            for (int i2 = 0; i2 != bArr4.length; i2++) {
                bArr4[i2] = (byte) (bArr2[i2] ^ bArr3[i2]);
            }
            bArr4[0] = (byte) (bArr4[0] | Byte.MIN_VALUE);
            bArr4[19] = (byte) (bArr4[19] | 1);
            BigInteger bigInteger = new BigInteger(1, bArr4);
            if (bigInteger.isProbablePrime(this.certainty)) {
                byte[] bArrClone = Arrays.clone(bArr);
                inc(bArrClone);
                for (int i3 = 0; i3 < 4096; i3++) {
                    for (int i4 = 0; i4 < i; i4++) {
                        inc(bArrClone);
                        hash(sHA1Digest, bArrClone, bArr2);
                        System.arraycopy(bArr2, 0, bArr5, bArr5.length - ((i4 + 1) * bArr2.length), bArr2.length);
                    }
                    inc(bArrClone);
                    hash(sHA1Digest, bArrClone, bArr2);
                    System.arraycopy(bArr2, bArr2.length - (bArr5.length - (i * bArr2.length)), bArr5, 0, bArr5.length - (i * bArr2.length));
                    bArr5[0] = (byte) (bArr5[0] | Byte.MIN_VALUE);
                    BigInteger bigInteger2 = new BigInteger(1, bArr5);
                    BigInteger bigIntegerSubtract = bigInteger2.subtract(bigInteger2.mod(bigInteger.shiftLeft(1)).subtract(ONE));
                    if (bigIntegerSubtract.bitLength() == this.L && bigIntegerSubtract.isProbablePrime(this.certainty)) {
                        return new DSAParameters(bigIntegerSubtract, bigInteger, calculateGenerator_FIPS186_2(bigIntegerSubtract, bigInteger, this.random), new DSAValidationParameters(bArr, i3));
                    }
                }
            }
        }
    }

    private static BigInteger calculateGenerator_FIPS186_2(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger bigIntegerModPow;
        BigInteger bigIntegerDivide = bigInteger.subtract(ONE).divide(bigInteger2);
        BigInteger bigIntegerSubtract = bigInteger.subtract(TWO);
        do {
            bigIntegerModPow = BigIntegers.createRandomInRange(TWO, bigIntegerSubtract, secureRandom).modPow(bigIntegerDivide, bigInteger);
        } while (bigIntegerModPow.bitLength() <= 1);
        return bigIntegerModPow;
    }

    private DSAParameters generateParameters_FIPS186_3() {
        SHA256Digest sHA256Digest = new SHA256Digest();
        int digestSize = sHA256Digest.getDigestSize() * 8;
        byte[] bArr = new byte[this.N / 8];
        int i = (this.L - 1) / digestSize;
        int i2 = (this.L - 1) % digestSize;
        byte[] bArr2 = new byte[sHA256Digest.getDigestSize()];
        while (true) {
            this.random.nextBytes(bArr);
            hash(sHA256Digest, bArr, bArr2);
            BigInteger bigIntegerMod = new BigInteger(1, bArr2).mod(ONE.shiftLeft(this.N - 1));
            BigInteger bigIntegerSubtract = ONE.shiftLeft(this.N - 1).add(bigIntegerMod).add(ONE).subtract(bigIntegerMod.mod(TWO));
            if (bigIntegerSubtract.isProbablePrime(this.certainty)) {
                byte[] bArrClone = Arrays.clone(bArr);
                int i3 = 4 * this.L;
                for (int i4 = 0; i4 < i3; i4++) {
                    BigInteger bigIntegerAdd = ZERO;
                    int i5 = 0;
                    int i6 = 0;
                    while (true) {
                        int i7 = i6;
                        if (i5 > i) {
                            break;
                        }
                        inc(bArrClone);
                        hash(sHA256Digest, bArrClone, bArr2);
                        BigInteger bigInteger = new BigInteger(1, bArr2);
                        if (i5 == i) {
                            bigInteger = bigInteger.mod(ONE.shiftLeft(i2));
                        }
                        bigIntegerAdd = bigIntegerAdd.add(bigInteger.shiftLeft(i7));
                        i5++;
                        i6 = i7 + digestSize;
                    }
                    BigInteger bigIntegerAdd2 = bigIntegerAdd.add(ONE.shiftLeft(this.L - 1));
                    BigInteger bigIntegerSubtract2 = bigIntegerAdd2.subtract(bigIntegerAdd2.mod(bigIntegerSubtract.shiftLeft(1)).subtract(ONE));
                    if (bigIntegerSubtract2.bitLength() == this.L && bigIntegerSubtract2.isProbablePrime(this.certainty)) {
                        return new DSAParameters(bigIntegerSubtract2, bigIntegerSubtract, calculateGenerator_FIPS186_3_Unverifiable(bigIntegerSubtract2, bigIntegerSubtract, this.random), new DSAValidationParameters(bArr, i4));
                    }
                }
            }
        }
    }

    private static BigInteger calculateGenerator_FIPS186_3_Unverifiable(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        return calculateGenerator_FIPS186_2(bigInteger, bigInteger2, secureRandom);
    }

    private static void hash(Digest digest, byte[] bArr, byte[] bArr2) {
        digest.update(bArr, 0, bArr.length);
        digest.doFinal(bArr2, 0);
    }

    private static int getDefaultN(int i) {
        return i > 1024 ? 256 : 160;
    }

    private static void inc(byte[] bArr) {
        for (int length = bArr.length - 1; length >= 0; length--) {
            byte b = (byte) ((bArr[length] + 1) & 255);
            bArr[length] = b;
            if (b != 0) {
                return;
            }
        }
    }
}
