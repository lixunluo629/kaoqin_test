package org.bouncycastle.crypto.engines;

import com.moredian.onpremise.core.utils.RSAUtils;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.hssf.record.UnknownRecord;
import org.aspectj.apache.bcel.Constants;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/AESLightEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/AESLightEngine.class */
public class AESLightEngine implements BlockCipher {
    private static final byte[] S = {99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, Byte.MIN_VALUE, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, Byte.MAX_VALUE, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final byte[] Si = {82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, Byte.MIN_VALUE, -20, 95, 96, 81, Byte.MAX_VALUE, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
    private static final int[] rcon = {1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, Constants.INVOKESUPER_QUICK, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, Constants.GETSTATIC2_QUICK, 179, 125, EscherProperties.GEOTEXT__BOLDFONT, UnknownRecord.PHONETICPR_00EF, 197, 145};
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private int ROUNDS;
    private int[][] WorkingKey = (int[][]) null;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private boolean forEncryption;
    private static final int BLOCK_SIZE = 16;

    private int shift(int i, int i2) {
        return (i >>> i2) | (i << (-i2));
    }

    private int FFmulX(int i) {
        return ((i & m2) << 1) ^ (((i & m1) >>> 7) * 27);
    }

    private int mcol(int i) {
        int iFFmulX = FFmulX(i);
        return ((iFFmulX ^ shift(i ^ iFFmulX, 8)) ^ shift(i, 16)) ^ shift(i, 24);
    }

    private int inv_mcol(int i) {
        int iFFmulX = FFmulX(i);
        int iFFmulX2 = FFmulX(iFFmulX);
        int iFFmulX3 = FFmulX(iFFmulX2);
        int i2 = i ^ iFFmulX3;
        return ((((iFFmulX ^ iFFmulX2) ^ iFFmulX3) ^ shift(iFFmulX ^ i2, 8)) ^ shift(iFFmulX2 ^ i2, 16)) ^ shift(i2, 24);
    }

    private int subWord(int i) {
        return (S[i & 255] & 255) | ((S[(i >> 8) & 255] & 255) << 8) | ((S[(i >> 16) & 255] & 255) << 16) | (S[(i >> 24) & 255] << 24);
    }

    private int[][] generateWorkingKey(byte[] bArr, boolean z) {
        int length = bArr.length / 4;
        if ((length != 4 && length != 6 && length != 8) || length * 4 != bArr.length) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        this.ROUNDS = length + 6;
        int[][] iArr = new int[this.ROUNDS + 1][4];
        int i = 0;
        int i2 = 0;
        while (i2 < bArr.length) {
            iArr[i >> 2][i & 3] = (bArr[i2] & 255) | ((bArr[i2 + 1] & 255) << 8) | ((bArr[i2 + 2] & 255) << 16) | (bArr[i2 + 3] << 24);
            i2 += 4;
            i++;
        }
        int i3 = (this.ROUNDS + 1) << 2;
        for (int i4 = length; i4 < i3; i4++) {
            int iSubWord = iArr[(i4 - 1) >> 2][(i4 - 1) & 3];
            if (i4 % length == 0) {
                iSubWord = subWord(shift(iSubWord, 8)) ^ rcon[(i4 / length) - 1];
            } else if (length > 6 && i4 % length == 4) {
                iSubWord = subWord(iSubWord);
            }
            iArr[i4 >> 2][i4 & 3] = iArr[(i4 - length) >> 2][(i4 - length) & 3] ^ iSubWord;
        }
        if (!z) {
            for (int i5 = 1; i5 < this.ROUNDS; i5++) {
                for (int i6 = 0; i6 < 4; i6++) {
                    iArr[i5][i6] = inv_mcol(iArr[i5][i6]);
                }
            }
        }
        return iArr;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void init(boolean z, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
        }
        this.WorkingKey = generateWorkingKey(((KeyParameter) cipherParameters).getKey(), z);
        this.forEncryption = z;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public String getAlgorithmName() {
        return RSAUtils.AES_KEY_ALGORITHM;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int getBlockSize() {
        return 16;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (i + 16 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i2 + 16 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            unpackBlock(bArr, i);
            encryptBlock(this.WorkingKey);
            packBlock(bArr2, i2);
            return 16;
        }
        unpackBlock(bArr, i);
        decryptBlock(this.WorkingKey);
        packBlock(bArr2, i2);
        return 16;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void reset() {
    }

    private void unpackBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        this.C0 = bArr[i] & 255;
        int i3 = i2 + 1;
        this.C0 |= (bArr[i2] & 255) << 8;
        int i4 = i3 + 1;
        this.C0 |= (bArr[i3] & 255) << 16;
        int i5 = i4 + 1;
        this.C0 |= bArr[i4] << 24;
        int i6 = i5 + 1;
        this.C1 = bArr[i5] & 255;
        int i7 = i6 + 1;
        this.C1 |= (bArr[i6] & 255) << 8;
        int i8 = i7 + 1;
        this.C1 |= (bArr[i7] & 255) << 16;
        int i9 = i8 + 1;
        this.C1 |= bArr[i8] << 24;
        int i10 = i9 + 1;
        this.C2 = bArr[i9] & 255;
        int i11 = i10 + 1;
        this.C2 |= (bArr[i10] & 255) << 8;
        int i12 = i11 + 1;
        this.C2 |= (bArr[i11] & 255) << 16;
        int i13 = i12 + 1;
        this.C2 |= bArr[i12] << 24;
        int i14 = i13 + 1;
        this.C3 = bArr[i13] & 255;
        int i15 = i14 + 1;
        this.C3 |= (bArr[i14] & 255) << 8;
        int i16 = i15 + 1;
        this.C3 |= (bArr[i15] & 255) << 16;
        int i17 = i16 + 1;
        this.C3 |= bArr[i16] << 24;
    }

    private void packBlock(byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) this.C0;
        int i3 = i2 + 1;
        bArr[i2] = (byte) (this.C0 >> 8);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (this.C0 >> 16);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (this.C0 >> 24);
        int i6 = i5 + 1;
        bArr[i5] = (byte) this.C1;
        int i7 = i6 + 1;
        bArr[i6] = (byte) (this.C1 >> 8);
        int i8 = i7 + 1;
        bArr[i7] = (byte) (this.C1 >> 16);
        int i9 = i8 + 1;
        bArr[i8] = (byte) (this.C1 >> 24);
        int i10 = i9 + 1;
        bArr[i9] = (byte) this.C2;
        int i11 = i10 + 1;
        bArr[i10] = (byte) (this.C2 >> 8);
        int i12 = i11 + 1;
        bArr[i11] = (byte) (this.C2 >> 16);
        int i13 = i12 + 1;
        bArr[i12] = (byte) (this.C2 >> 24);
        int i14 = i13 + 1;
        bArr[i13] = (byte) this.C3;
        int i15 = i14 + 1;
        bArr[i14] = (byte) (this.C3 >> 8);
        int i16 = i15 + 1;
        bArr[i15] = (byte) (this.C3 >> 16);
        int i17 = i16 + 1;
        bArr[i16] = (byte) (this.C3 >> 24);
    }

    private void encryptBlock(int[][] iArr) {
        this.C0 ^= iArr[0][0];
        this.C1 ^= iArr[0][1];
        this.C2 ^= iArr[0][2];
        this.C3 ^= iArr[0][3];
        int i = 1;
        while (i < this.ROUNDS - 1) {
            int iMcol = mcol((((S[this.C0 & 255] & 255) ^ ((S[(this.C1 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C2 >> 16) & 255] & 255) << 16)) ^ (S[(this.C3 >> 24) & 255] << 24)) ^ iArr[i][0];
            int iMcol2 = mcol((((S[this.C1 & 255] & 255) ^ ((S[(this.C2 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C3 >> 16) & 255] & 255) << 16)) ^ (S[(this.C0 >> 24) & 255] << 24)) ^ iArr[i][1];
            int iMcol3 = mcol((((S[this.C2 & 255] & 255) ^ ((S[(this.C3 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C0 >> 16) & 255] & 255) << 16)) ^ (S[(this.C1 >> 24) & 255] << 24)) ^ iArr[i][2];
            int i2 = i;
            int i3 = i + 1;
            int iMcol4 = mcol((((S[this.C3 & 255] & 255) ^ ((S[(this.C0 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C1 >> 16) & 255] & 255) << 16)) ^ (S[(this.C2 >> 24) & 255] << 24)) ^ iArr[i2][3];
            this.C0 = mcol((((S[iMcol & 255] & 255) ^ ((S[(iMcol2 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol3 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol4 >> 24) & 255] << 24)) ^ iArr[i3][0];
            this.C1 = mcol((((S[iMcol2 & 255] & 255) ^ ((S[(iMcol3 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol4 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol >> 24) & 255] << 24)) ^ iArr[i3][1];
            this.C2 = mcol((((S[iMcol3 & 255] & 255) ^ ((S[(iMcol4 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol >> 16) & 255] & 255) << 16)) ^ (S[(iMcol2 >> 24) & 255] << 24)) ^ iArr[i3][2];
            i = i3 + 1;
            this.C3 = mcol((((S[iMcol4 & 255] & 255) ^ ((S[(iMcol >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol2 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol3 >> 24) & 255] << 24)) ^ iArr[i3][3];
        }
        int iMcol5 = mcol((((S[this.C0 & 255] & 255) ^ ((S[(this.C1 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C2 >> 16) & 255] & 255) << 16)) ^ (S[(this.C3 >> 24) & 255] << 24)) ^ iArr[i][0];
        int iMcol6 = mcol((((S[this.C1 & 255] & 255) ^ ((S[(this.C2 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C3 >> 16) & 255] & 255) << 16)) ^ (S[(this.C0 >> 24) & 255] << 24)) ^ iArr[i][1];
        int iMcol7 = mcol((((S[this.C2 & 255] & 255) ^ ((S[(this.C3 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C0 >> 16) & 255] & 255) << 16)) ^ (S[(this.C1 >> 24) & 255] << 24)) ^ iArr[i][2];
        int i4 = i;
        int i5 = i + 1;
        int iMcol8 = mcol((((S[this.C3 & 255] & 255) ^ ((S[(this.C0 >> 8) & 255] & 255) << 8)) ^ ((S[(this.C1 >> 16) & 255] & 255) << 16)) ^ (S[(this.C2 >> 24) & 255] << 24)) ^ iArr[i4][3];
        this.C0 = ((((S[iMcol5 & 255] & 255) ^ ((S[(iMcol6 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol7 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol8 >> 24) & 255] << 24)) ^ iArr[i5][0];
        this.C1 = ((((S[iMcol6 & 255] & 255) ^ ((S[(iMcol7 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol8 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol5 >> 24) & 255] << 24)) ^ iArr[i5][1];
        this.C2 = ((((S[iMcol7 & 255] & 255) ^ ((S[(iMcol8 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol5 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol6 >> 24) & 255] << 24)) ^ iArr[i5][2];
        this.C3 = ((((S[iMcol8 & 255] & 255) ^ ((S[(iMcol5 >> 8) & 255] & 255) << 8)) ^ ((S[(iMcol6 >> 16) & 255] & 255) << 16)) ^ (S[(iMcol7 >> 24) & 255] << 24)) ^ iArr[i5][3];
    }

    private void decryptBlock(int[][] iArr) {
        this.C0 ^= iArr[this.ROUNDS][0];
        this.C1 ^= iArr[this.ROUNDS][1];
        this.C2 ^= iArr[this.ROUNDS][2];
        this.C3 ^= iArr[this.ROUNDS][3];
        int i = this.ROUNDS - 1;
        while (i > 1) {
            int iInv_mcol = inv_mcol((((Si[this.C0 & 255] & 255) ^ ((Si[(this.C3 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C2 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C1 >> 24) & 255] << 24)) ^ iArr[i][0];
            int iInv_mcol2 = inv_mcol((((Si[this.C1 & 255] & 255) ^ ((Si[(this.C0 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C3 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C2 >> 24) & 255] << 24)) ^ iArr[i][1];
            int iInv_mcol3 = inv_mcol((((Si[this.C2 & 255] & 255) ^ ((Si[(this.C1 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C0 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C3 >> 24) & 255] << 24)) ^ iArr[i][2];
            int i2 = i;
            int i3 = i - 1;
            int iInv_mcol4 = inv_mcol((((Si[this.C3 & 255] & 255) ^ ((Si[(this.C2 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C1 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C0 >> 24) & 255] << 24)) ^ iArr[i2][3];
            this.C0 = inv_mcol((((Si[iInv_mcol & 255] & 255) ^ ((Si[(iInv_mcol4 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol3 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol2 >> 24) & 255] << 24)) ^ iArr[i3][0];
            this.C1 = inv_mcol((((Si[iInv_mcol2 & 255] & 255) ^ ((Si[(iInv_mcol >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol4 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol3 >> 24) & 255] << 24)) ^ iArr[i3][1];
            this.C2 = inv_mcol((((Si[iInv_mcol3 & 255] & 255) ^ ((Si[(iInv_mcol2 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol4 >> 24) & 255] << 24)) ^ iArr[i3][2];
            i = i3 - 1;
            this.C3 = inv_mcol((((Si[iInv_mcol4 & 255] & 255) ^ ((Si[(iInv_mcol3 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol2 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol >> 24) & 255] << 24)) ^ iArr[i3][3];
        }
        int iInv_mcol5 = inv_mcol((((Si[this.C0 & 255] & 255) ^ ((Si[(this.C3 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C2 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C1 >> 24) & 255] << 24)) ^ iArr[i][0];
        int iInv_mcol6 = inv_mcol((((Si[this.C1 & 255] & 255) ^ ((Si[(this.C0 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C3 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C2 >> 24) & 255] << 24)) ^ iArr[i][1];
        int iInv_mcol7 = inv_mcol((((Si[this.C2 & 255] & 255) ^ ((Si[(this.C1 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C0 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C3 >> 24) & 255] << 24)) ^ iArr[i][2];
        int iInv_mcol8 = inv_mcol((((Si[this.C3 & 255] & 255) ^ ((Si[(this.C2 >> 8) & 255] & 255) << 8)) ^ ((Si[(this.C1 >> 16) & 255] & 255) << 16)) ^ (Si[(this.C0 >> 24) & 255] << 24)) ^ iArr[i][3];
        this.C0 = ((((Si[iInv_mcol5 & 255] & 255) ^ ((Si[(iInv_mcol8 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol7 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol6 >> 24) & 255] << 24)) ^ iArr[0][0];
        this.C1 = ((((Si[iInv_mcol6 & 255] & 255) ^ ((Si[(iInv_mcol5 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol8 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol7 >> 24) & 255] << 24)) ^ iArr[0][1];
        this.C2 = ((((Si[iInv_mcol7 & 255] & 255) ^ ((Si[(iInv_mcol6 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol5 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol8 >> 24) & 255] << 24)) ^ iArr[0][2];
        this.C3 = ((((Si[iInv_mcol8 & 255] & 255) ^ ((Si[(iInv_mcol7 >> 8) & 255] & 255) << 8)) ^ ((Si[(iInv_mcol6 >> 16) & 255] & 255) << 16)) ^ (Si[(iInv_mcol5 >> 24) & 255] << 24)) ^ iArr[0][3];
    }
}
