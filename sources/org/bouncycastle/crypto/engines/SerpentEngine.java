package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/SerpentEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/SerpentEngine.class */
public class SerpentEngine implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    static final int ROUNDS = 32;
    static final int PHI = -1640531527;
    private boolean encrypting;
    private int[] wKey;
    private int X0;
    private int X1;
    private int X2;
    private int X3;

    @Override // org.bouncycastle.crypto.BlockCipher
    public void init(boolean z, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("invalid parameter passed to Serpent init - " + cipherParameters.getClass().getName());
        }
        this.encrypting = z;
        this.wKey = makeWorkingKey(((KeyParameter) cipherParameters).getKey());
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public String getAlgorithmName() {
        return "Serpent";
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int getBlockSize() {
        return 16;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public final int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.wKey == null) {
            throw new IllegalStateException("Serpent not initialised");
        }
        if (i + 16 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i2 + 16 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        }
        if (this.encrypting) {
            encryptBlock(bArr, i, bArr2, i2);
            return 16;
        }
        decryptBlock(bArr, i, bArr2, i2);
        return 16;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void reset() {
    }

    private int[] makeWorkingKey(byte[] bArr) throws IllegalArgumentException {
        int[] iArr = new int[16];
        int i = 0;
        int length = bArr.length - 4;
        while (length > 0) {
            int i2 = i;
            i++;
            iArr[i2] = bytesToWord(bArr, length);
            length -= 4;
        }
        if (length != 0) {
            throw new IllegalArgumentException("key must be a multiple of 4 bytes");
        }
        int i3 = i;
        int i4 = i + 1;
        iArr[i3] = bytesToWord(bArr, 0);
        if (i4 < 8) {
            iArr[i4] = 1;
        }
        int[] iArr2 = new int[132];
        for (int i5 = 8; i5 < 16; i5++) {
            iArr[i5] = rotateLeft(((((iArr[i5 - 8] ^ iArr[i5 - 5]) ^ iArr[i5 - 3]) ^ iArr[i5 - 1]) ^ PHI) ^ (i5 - 8), 11);
        }
        System.arraycopy(iArr, 8, iArr2, 0, 8);
        for (int i6 = 8; i6 < 132; i6++) {
            iArr2[i6] = rotateLeft(((((iArr2[i6 - 8] ^ iArr2[i6 - 5]) ^ iArr2[i6 - 3]) ^ iArr2[i6 - 1]) ^ PHI) ^ i6, 11);
        }
        sb3(iArr2[0], iArr2[1], iArr2[2], iArr2[3]);
        iArr2[0] = this.X0;
        iArr2[1] = this.X1;
        iArr2[2] = this.X2;
        iArr2[3] = this.X3;
        sb2(iArr2[4], iArr2[5], iArr2[6], iArr2[7]);
        iArr2[4] = this.X0;
        iArr2[5] = this.X1;
        iArr2[6] = this.X2;
        iArr2[7] = this.X3;
        sb1(iArr2[8], iArr2[9], iArr2[10], iArr2[11]);
        iArr2[8] = this.X0;
        iArr2[9] = this.X1;
        iArr2[10] = this.X2;
        iArr2[11] = this.X3;
        sb0(iArr2[12], iArr2[13], iArr2[14], iArr2[15]);
        iArr2[12] = this.X0;
        iArr2[13] = this.X1;
        iArr2[14] = this.X2;
        iArr2[15] = this.X3;
        sb7(iArr2[16], iArr2[17], iArr2[18], iArr2[19]);
        iArr2[16] = this.X0;
        iArr2[17] = this.X1;
        iArr2[18] = this.X2;
        iArr2[19] = this.X3;
        sb6(iArr2[20], iArr2[21], iArr2[22], iArr2[23]);
        iArr2[20] = this.X0;
        iArr2[21] = this.X1;
        iArr2[22] = this.X2;
        iArr2[23] = this.X3;
        sb5(iArr2[24], iArr2[25], iArr2[26], iArr2[27]);
        iArr2[24] = this.X0;
        iArr2[25] = this.X1;
        iArr2[26] = this.X2;
        iArr2[27] = this.X3;
        sb4(iArr2[28], iArr2[29], iArr2[30], iArr2[31]);
        iArr2[28] = this.X0;
        iArr2[29] = this.X1;
        iArr2[30] = this.X2;
        iArr2[31] = this.X3;
        sb3(iArr2[32], iArr2[33], iArr2[34], iArr2[35]);
        iArr2[32] = this.X0;
        iArr2[33] = this.X1;
        iArr2[34] = this.X2;
        iArr2[35] = this.X3;
        sb2(iArr2[36], iArr2[37], iArr2[38], iArr2[39]);
        iArr2[36] = this.X0;
        iArr2[37] = this.X1;
        iArr2[38] = this.X2;
        iArr2[39] = this.X3;
        sb1(iArr2[40], iArr2[41], iArr2[42], iArr2[43]);
        iArr2[40] = this.X0;
        iArr2[41] = this.X1;
        iArr2[42] = this.X2;
        iArr2[43] = this.X3;
        sb0(iArr2[44], iArr2[45], iArr2[46], iArr2[47]);
        iArr2[44] = this.X0;
        iArr2[45] = this.X1;
        iArr2[46] = this.X2;
        iArr2[47] = this.X3;
        sb7(iArr2[48], iArr2[49], iArr2[50], iArr2[51]);
        iArr2[48] = this.X0;
        iArr2[49] = this.X1;
        iArr2[50] = this.X2;
        iArr2[51] = this.X3;
        sb6(iArr2[52], iArr2[53], iArr2[54], iArr2[55]);
        iArr2[52] = this.X0;
        iArr2[53] = this.X1;
        iArr2[54] = this.X2;
        iArr2[55] = this.X3;
        sb5(iArr2[56], iArr2[57], iArr2[58], iArr2[59]);
        iArr2[56] = this.X0;
        iArr2[57] = this.X1;
        iArr2[58] = this.X2;
        iArr2[59] = this.X3;
        sb4(iArr2[60], iArr2[61], iArr2[62], iArr2[63]);
        iArr2[60] = this.X0;
        iArr2[61] = this.X1;
        iArr2[62] = this.X2;
        iArr2[63] = this.X3;
        sb3(iArr2[64], iArr2[65], iArr2[66], iArr2[67]);
        iArr2[64] = this.X0;
        iArr2[65] = this.X1;
        iArr2[66] = this.X2;
        iArr2[67] = this.X3;
        sb2(iArr2[68], iArr2[69], iArr2[70], iArr2[71]);
        iArr2[68] = this.X0;
        iArr2[69] = this.X1;
        iArr2[70] = this.X2;
        iArr2[71] = this.X3;
        sb1(iArr2[72], iArr2[73], iArr2[74], iArr2[75]);
        iArr2[72] = this.X0;
        iArr2[73] = this.X1;
        iArr2[74] = this.X2;
        iArr2[75] = this.X3;
        sb0(iArr2[76], iArr2[77], iArr2[78], iArr2[79]);
        iArr2[76] = this.X0;
        iArr2[77] = this.X1;
        iArr2[78] = this.X2;
        iArr2[79] = this.X3;
        sb7(iArr2[80], iArr2[81], iArr2[82], iArr2[83]);
        iArr2[80] = this.X0;
        iArr2[81] = this.X1;
        iArr2[82] = this.X2;
        iArr2[83] = this.X3;
        sb6(iArr2[84], iArr2[85], iArr2[86], iArr2[87]);
        iArr2[84] = this.X0;
        iArr2[85] = this.X1;
        iArr2[86] = this.X2;
        iArr2[87] = this.X3;
        sb5(iArr2[88], iArr2[89], iArr2[90], iArr2[91]);
        iArr2[88] = this.X0;
        iArr2[89] = this.X1;
        iArr2[90] = this.X2;
        iArr2[91] = this.X3;
        sb4(iArr2[92], iArr2[93], iArr2[94], iArr2[95]);
        iArr2[92] = this.X0;
        iArr2[93] = this.X1;
        iArr2[94] = this.X2;
        iArr2[95] = this.X3;
        sb3(iArr2[96], iArr2[97], iArr2[98], iArr2[99]);
        iArr2[96] = this.X0;
        iArr2[97] = this.X1;
        iArr2[98] = this.X2;
        iArr2[99] = this.X3;
        sb2(iArr2[100], iArr2[101], iArr2[102], iArr2[103]);
        iArr2[100] = this.X0;
        iArr2[101] = this.X1;
        iArr2[102] = this.X2;
        iArr2[103] = this.X3;
        sb1(iArr2[104], iArr2[105], iArr2[106], iArr2[107]);
        iArr2[104] = this.X0;
        iArr2[105] = this.X1;
        iArr2[106] = this.X2;
        iArr2[107] = this.X3;
        sb0(iArr2[108], iArr2[109], iArr2[110], iArr2[111]);
        iArr2[108] = this.X0;
        iArr2[109] = this.X1;
        iArr2[110] = this.X2;
        iArr2[111] = this.X3;
        sb7(iArr2[112], iArr2[113], iArr2[114], iArr2[115]);
        iArr2[112] = this.X0;
        iArr2[113] = this.X1;
        iArr2[114] = this.X2;
        iArr2[115] = this.X3;
        sb6(iArr2[116], iArr2[117], iArr2[118], iArr2[119]);
        iArr2[116] = this.X0;
        iArr2[117] = this.X1;
        iArr2[118] = this.X2;
        iArr2[119] = this.X3;
        sb5(iArr2[120], iArr2[121], iArr2[122], iArr2[123]);
        iArr2[120] = this.X0;
        iArr2[121] = this.X1;
        iArr2[122] = this.X2;
        iArr2[123] = this.X3;
        sb4(iArr2[124], iArr2[125], iArr2[126], iArr2[127]);
        iArr2[124] = this.X0;
        iArr2[125] = this.X1;
        iArr2[126] = this.X2;
        iArr2[127] = this.X3;
        sb3(iArr2[128], iArr2[129], iArr2[130], iArr2[131]);
        iArr2[128] = this.X0;
        iArr2[129] = this.X1;
        iArr2[130] = this.X2;
        iArr2[131] = this.X3;
        return iArr2;
    }

    private int rotateLeft(int i, int i2) {
        return (i << i2) | (i >>> (-i2));
    }

    private int rotateRight(int i, int i2) {
        return (i >>> i2) | (i << (-i2));
    }

    private int bytesToWord(byte[] bArr, int i) {
        return ((bArr[i] & 255) << 24) | ((bArr[i + 1] & 255) << 16) | ((bArr[i + 2] & 255) << 8) | (bArr[i + 3] & 255);
    }

    private void wordToBytes(int i, byte[] bArr, int i2) {
        bArr[i2 + 3] = (byte) i;
        bArr[i2 + 2] = (byte) (i >>> 8);
        bArr[i2 + 1] = (byte) (i >>> 16);
        bArr[i2] = (byte) (i >>> 24);
    }

    private void encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        this.X3 = bytesToWord(bArr, i);
        this.X2 = bytesToWord(bArr, i + 4);
        this.X1 = bytesToWord(bArr, i + 8);
        this.X0 = bytesToWord(bArr, i + 12);
        sb0(this.wKey[0] ^ this.X0, this.wKey[1] ^ this.X1, this.wKey[2] ^ this.X2, this.wKey[3] ^ this.X3);
        LT();
        sb1(this.wKey[4] ^ this.X0, this.wKey[5] ^ this.X1, this.wKey[6] ^ this.X2, this.wKey[7] ^ this.X3);
        LT();
        sb2(this.wKey[8] ^ this.X0, this.wKey[9] ^ this.X1, this.wKey[10] ^ this.X2, this.wKey[11] ^ this.X3);
        LT();
        sb3(this.wKey[12] ^ this.X0, this.wKey[13] ^ this.X1, this.wKey[14] ^ this.X2, this.wKey[15] ^ this.X3);
        LT();
        sb4(this.wKey[16] ^ this.X0, this.wKey[17] ^ this.X1, this.wKey[18] ^ this.X2, this.wKey[19] ^ this.X3);
        LT();
        sb5(this.wKey[20] ^ this.X0, this.wKey[21] ^ this.X1, this.wKey[22] ^ this.X2, this.wKey[23] ^ this.X3);
        LT();
        sb6(this.wKey[24] ^ this.X0, this.wKey[25] ^ this.X1, this.wKey[26] ^ this.X2, this.wKey[27] ^ this.X3);
        LT();
        sb7(this.wKey[28] ^ this.X0, this.wKey[29] ^ this.X1, this.wKey[30] ^ this.X2, this.wKey[31] ^ this.X3);
        LT();
        sb0(this.wKey[32] ^ this.X0, this.wKey[33] ^ this.X1, this.wKey[34] ^ this.X2, this.wKey[35] ^ this.X3);
        LT();
        sb1(this.wKey[36] ^ this.X0, this.wKey[37] ^ this.X1, this.wKey[38] ^ this.X2, this.wKey[39] ^ this.X3);
        LT();
        sb2(this.wKey[40] ^ this.X0, this.wKey[41] ^ this.X1, this.wKey[42] ^ this.X2, this.wKey[43] ^ this.X3);
        LT();
        sb3(this.wKey[44] ^ this.X0, this.wKey[45] ^ this.X1, this.wKey[46] ^ this.X2, this.wKey[47] ^ this.X3);
        LT();
        sb4(this.wKey[48] ^ this.X0, this.wKey[49] ^ this.X1, this.wKey[50] ^ this.X2, this.wKey[51] ^ this.X3);
        LT();
        sb5(this.wKey[52] ^ this.X0, this.wKey[53] ^ this.X1, this.wKey[54] ^ this.X2, this.wKey[55] ^ this.X3);
        LT();
        sb6(this.wKey[56] ^ this.X0, this.wKey[57] ^ this.X1, this.wKey[58] ^ this.X2, this.wKey[59] ^ this.X3);
        LT();
        sb7(this.wKey[60] ^ this.X0, this.wKey[61] ^ this.X1, this.wKey[62] ^ this.X2, this.wKey[63] ^ this.X3);
        LT();
        sb0(this.wKey[64] ^ this.X0, this.wKey[65] ^ this.X1, this.wKey[66] ^ this.X2, this.wKey[67] ^ this.X3);
        LT();
        sb1(this.wKey[68] ^ this.X0, this.wKey[69] ^ this.X1, this.wKey[70] ^ this.X2, this.wKey[71] ^ this.X3);
        LT();
        sb2(this.wKey[72] ^ this.X0, this.wKey[73] ^ this.X1, this.wKey[74] ^ this.X2, this.wKey[75] ^ this.X3);
        LT();
        sb3(this.wKey[76] ^ this.X0, this.wKey[77] ^ this.X1, this.wKey[78] ^ this.X2, this.wKey[79] ^ this.X3);
        LT();
        sb4(this.wKey[80] ^ this.X0, this.wKey[81] ^ this.X1, this.wKey[82] ^ this.X2, this.wKey[83] ^ this.X3);
        LT();
        sb5(this.wKey[84] ^ this.X0, this.wKey[85] ^ this.X1, this.wKey[86] ^ this.X2, this.wKey[87] ^ this.X3);
        LT();
        sb6(this.wKey[88] ^ this.X0, this.wKey[89] ^ this.X1, this.wKey[90] ^ this.X2, this.wKey[91] ^ this.X3);
        LT();
        sb7(this.wKey[92] ^ this.X0, this.wKey[93] ^ this.X1, this.wKey[94] ^ this.X2, this.wKey[95] ^ this.X3);
        LT();
        sb0(this.wKey[96] ^ this.X0, this.wKey[97] ^ this.X1, this.wKey[98] ^ this.X2, this.wKey[99] ^ this.X3);
        LT();
        sb1(this.wKey[100] ^ this.X0, this.wKey[101] ^ this.X1, this.wKey[102] ^ this.X2, this.wKey[103] ^ this.X3);
        LT();
        sb2(this.wKey[104] ^ this.X0, this.wKey[105] ^ this.X1, this.wKey[106] ^ this.X2, this.wKey[107] ^ this.X3);
        LT();
        sb3(this.wKey[108] ^ this.X0, this.wKey[109] ^ this.X1, this.wKey[110] ^ this.X2, this.wKey[111] ^ this.X3);
        LT();
        sb4(this.wKey[112] ^ this.X0, this.wKey[113] ^ this.X1, this.wKey[114] ^ this.X2, this.wKey[115] ^ this.X3);
        LT();
        sb5(this.wKey[116] ^ this.X0, this.wKey[117] ^ this.X1, this.wKey[118] ^ this.X2, this.wKey[119] ^ this.X3);
        LT();
        sb6(this.wKey[120] ^ this.X0, this.wKey[121] ^ this.X1, this.wKey[122] ^ this.X2, this.wKey[123] ^ this.X3);
        LT();
        sb7(this.wKey[124] ^ this.X0, this.wKey[125] ^ this.X1, this.wKey[126] ^ this.X2, this.wKey[127] ^ this.X3);
        wordToBytes(this.wKey[131] ^ this.X3, bArr2, i2);
        wordToBytes(this.wKey[130] ^ this.X2, bArr2, i2 + 4);
        wordToBytes(this.wKey[129] ^ this.X1, bArr2, i2 + 8);
        wordToBytes(this.wKey[128] ^ this.X0, bArr2, i2 + 12);
    }

    private void decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        this.X3 = this.wKey[131] ^ bytesToWord(bArr, i);
        this.X2 = this.wKey[130] ^ bytesToWord(bArr, i + 4);
        this.X1 = this.wKey[129] ^ bytesToWord(bArr, i + 8);
        this.X0 = this.wKey[128] ^ bytesToWord(bArr, i + 12);
        ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[124];
        this.X1 ^= this.wKey[125];
        this.X2 ^= this.wKey[126];
        this.X3 ^= this.wKey[127];
        inverseLT();
        ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[120];
        this.X1 ^= this.wKey[121];
        this.X2 ^= this.wKey[122];
        this.X3 ^= this.wKey[123];
        inverseLT();
        ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[116];
        this.X1 ^= this.wKey[117];
        this.X2 ^= this.wKey[118];
        this.X3 ^= this.wKey[119];
        inverseLT();
        ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[112];
        this.X1 ^= this.wKey[113];
        this.X2 ^= this.wKey[114];
        this.X3 ^= this.wKey[115];
        inverseLT();
        ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[108];
        this.X1 ^= this.wKey[109];
        this.X2 ^= this.wKey[110];
        this.X3 ^= this.wKey[111];
        inverseLT();
        ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[104];
        this.X1 ^= this.wKey[105];
        this.X2 ^= this.wKey[106];
        this.X3 ^= this.wKey[107];
        inverseLT();
        ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[100];
        this.X1 ^= this.wKey[101];
        this.X2 ^= this.wKey[102];
        this.X3 ^= this.wKey[103];
        inverseLT();
        ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[96];
        this.X1 ^= this.wKey[97];
        this.X2 ^= this.wKey[98];
        this.X3 ^= this.wKey[99];
        inverseLT();
        ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[92];
        this.X1 ^= this.wKey[93];
        this.X2 ^= this.wKey[94];
        this.X3 ^= this.wKey[95];
        inverseLT();
        ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[88];
        this.X1 ^= this.wKey[89];
        this.X2 ^= this.wKey[90];
        this.X3 ^= this.wKey[91];
        inverseLT();
        ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[84];
        this.X1 ^= this.wKey[85];
        this.X2 ^= this.wKey[86];
        this.X3 ^= this.wKey[87];
        inverseLT();
        ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[80];
        this.X1 ^= this.wKey[81];
        this.X2 ^= this.wKey[82];
        this.X3 ^= this.wKey[83];
        inverseLT();
        ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[76];
        this.X1 ^= this.wKey[77];
        this.X2 ^= this.wKey[78];
        this.X3 ^= this.wKey[79];
        inverseLT();
        ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[72];
        this.X1 ^= this.wKey[73];
        this.X2 ^= this.wKey[74];
        this.X3 ^= this.wKey[75];
        inverseLT();
        ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[68];
        this.X1 ^= this.wKey[69];
        this.X2 ^= this.wKey[70];
        this.X3 ^= this.wKey[71];
        inverseLT();
        ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[64];
        this.X1 ^= this.wKey[65];
        this.X2 ^= this.wKey[66];
        this.X3 ^= this.wKey[67];
        inverseLT();
        ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[60];
        this.X1 ^= this.wKey[61];
        this.X2 ^= this.wKey[62];
        this.X3 ^= this.wKey[63];
        inverseLT();
        ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[56];
        this.X1 ^= this.wKey[57];
        this.X2 ^= this.wKey[58];
        this.X3 ^= this.wKey[59];
        inverseLT();
        ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[52];
        this.X1 ^= this.wKey[53];
        this.X2 ^= this.wKey[54];
        this.X3 ^= this.wKey[55];
        inverseLT();
        ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[48];
        this.X1 ^= this.wKey[49];
        this.X2 ^= this.wKey[50];
        this.X3 ^= this.wKey[51];
        inverseLT();
        ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[44];
        this.X1 ^= this.wKey[45];
        this.X2 ^= this.wKey[46];
        this.X3 ^= this.wKey[47];
        inverseLT();
        ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[40];
        this.X1 ^= this.wKey[41];
        this.X2 ^= this.wKey[42];
        this.X3 ^= this.wKey[43];
        inverseLT();
        ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[36];
        this.X1 ^= this.wKey[37];
        this.X2 ^= this.wKey[38];
        this.X3 ^= this.wKey[39];
        inverseLT();
        ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[32];
        this.X1 ^= this.wKey[33];
        this.X2 ^= this.wKey[34];
        this.X3 ^= this.wKey[35];
        inverseLT();
        ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[28];
        this.X1 ^= this.wKey[29];
        this.X2 ^= this.wKey[30];
        this.X3 ^= this.wKey[31];
        inverseLT();
        ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[24];
        this.X1 ^= this.wKey[25];
        this.X2 ^= this.wKey[26];
        this.X3 ^= this.wKey[27];
        inverseLT();
        ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[20];
        this.X1 ^= this.wKey[21];
        this.X2 ^= this.wKey[22];
        this.X3 ^= this.wKey[23];
        inverseLT();
        ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[16];
        this.X1 ^= this.wKey[17];
        this.X2 ^= this.wKey[18];
        this.X3 ^= this.wKey[19];
        inverseLT();
        ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[12];
        this.X1 ^= this.wKey[13];
        this.X2 ^= this.wKey[14];
        this.X3 ^= this.wKey[15];
        inverseLT();
        ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[8];
        this.X1 ^= this.wKey[9];
        this.X2 ^= this.wKey[10];
        this.X3 ^= this.wKey[11];
        inverseLT();
        ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[4];
        this.X1 ^= this.wKey[5];
        this.X2 ^= this.wKey[6];
        this.X3 ^= this.wKey[7];
        inverseLT();
        ib0(this.X0, this.X1, this.X2, this.X3);
        wordToBytes(this.X3 ^ this.wKey[3], bArr2, i2);
        wordToBytes(this.X2 ^ this.wKey[2], bArr2, i2 + 4);
        wordToBytes(this.X1 ^ this.wKey[1], bArr2, i2 + 8);
        wordToBytes(this.X0 ^ this.wKey[0], bArr2, i2 + 12);
    }

    private void sb0(int i, int i2, int i3, int i4) {
        int i5 = i ^ i4;
        int i6 = i3 ^ i5;
        int i7 = i2 ^ i6;
        this.X3 = (i & i4) ^ i7;
        int i8 = i ^ (i2 & i5);
        this.X2 = i7 ^ (i3 | i8);
        int i9 = this.X3 & (i6 ^ i8);
        this.X1 = (i6 ^ (-1)) ^ i9;
        this.X0 = i9 ^ (i8 ^ (-1));
    }

    private void ib0(int i, int i2, int i3, int i4) {
        int i5 = i ^ (-1);
        int i6 = i ^ i2;
        int i7 = i4 ^ (i5 | i6);
        int i8 = i3 ^ i7;
        this.X2 = i6 ^ i8;
        int i9 = i5 ^ (i4 & i6);
        this.X1 = i7 ^ (this.X2 & i9);
        this.X3 = (i & i7) ^ (i8 | this.X1);
        this.X0 = this.X3 ^ (i8 ^ i9);
    }

    private void sb1(int i, int i2, int i3, int i4) {
        int i5 = i2 ^ (i ^ (-1));
        int i6 = i3 ^ (i | i5);
        this.X2 = i4 ^ i6;
        int i7 = i2 ^ (i4 | i5);
        int i8 = i5 ^ this.X2;
        this.X3 = i8 ^ (i6 & i7);
        int i9 = i6 ^ i7;
        this.X1 = this.X3 ^ i9;
        this.X0 = i6 ^ (i8 & i9);
    }

    private void ib1(int i, int i2, int i3, int i4) {
        int i5 = i2 ^ i4;
        int i6 = i ^ (i2 & i5);
        int i7 = i5 ^ i6;
        this.X3 = i3 ^ i7;
        int i8 = i2 ^ (i5 & i6);
        this.X1 = i6 ^ (this.X3 | i8);
        int i9 = this.X1 ^ (-1);
        int i10 = this.X3 ^ i8;
        this.X0 = i9 ^ i10;
        this.X2 = i7 ^ (i9 | i10);
    }

    private void sb2(int i, int i2, int i3, int i4) {
        int i5 = i ^ (-1);
        int i6 = i2 ^ i4;
        this.X0 = i6 ^ (i3 & i5);
        int i7 = i3 ^ i5;
        int i8 = i2 & (i3 ^ this.X0);
        this.X3 = i7 ^ i8;
        this.X2 = i ^ ((i4 | i8) & (this.X0 | i7));
        this.X1 = (i6 ^ this.X3) ^ (this.X2 ^ (i4 | i5));
    }

    private void ib2(int i, int i2, int i3, int i4) {
        int i5 = i2 ^ i4;
        int i6 = i5 ^ (-1);
        int i7 = i ^ i3;
        int i8 = i3 ^ i5;
        this.X0 = i7 ^ (i2 & i8);
        this.X3 = i5 ^ (i7 | (i4 ^ (i | i6)));
        int i9 = i8 ^ (-1);
        int i10 = this.X0 | this.X3;
        this.X1 = i9 ^ i10;
        this.X2 = (i4 & i9) ^ (i7 ^ i10);
    }

    private void sb3(int i, int i2, int i3, int i4) {
        int i5 = i ^ i2;
        int i6 = i | i4;
        int i7 = i3 ^ i4;
        int i8 = (i & i3) | (i5 & i6);
        this.X2 = i7 ^ i8;
        int i9 = i8 ^ (i2 ^ i6);
        this.X0 = i5 ^ (i7 & i9);
        int i10 = this.X2 & this.X0;
        this.X1 = i9 ^ i10;
        this.X3 = (i2 | i4) ^ (i7 ^ i10);
    }

    private void ib3(int i, int i2, int i3, int i4) {
        int i5 = i2 ^ i3;
        int i6 = i ^ (i2 & i5);
        int i7 = i3 ^ i6;
        int i8 = i4 | i6;
        this.X0 = i5 ^ i8;
        int i9 = i4 ^ (i5 | i8);
        this.X2 = i7 ^ i9;
        int i10 = (i | i2) ^ i9;
        this.X3 = i6 ^ (this.X0 & i10);
        this.X1 = this.X3 ^ (this.X0 ^ i10);
    }

    private void sb4(int i, int i2, int i3, int i4) {
        int i5 = i ^ i4;
        int i6 = i3 ^ (i4 & i5);
        int i7 = i2 | i6;
        this.X3 = i5 ^ i7;
        int i8 = i2 ^ (-1);
        this.X0 = i6 ^ (i5 | i8);
        int i9 = i5 ^ i8;
        this.X2 = (i & this.X0) ^ (i7 & i9);
        this.X1 = (i ^ i6) ^ (i9 & this.X2);
    }

    private void ib4(int i, int i2, int i3, int i4) {
        int i5 = i2 ^ (i & (i3 | i4));
        int i6 = i3 ^ (i & i5);
        this.X1 = i4 ^ i6;
        int i7 = i ^ (-1);
        this.X3 = i5 ^ (i6 & this.X1);
        int i8 = i4 ^ (this.X1 | i7);
        this.X0 = this.X3 ^ i8;
        this.X2 = (i5 & i8) ^ (this.X1 ^ i7);
    }

    private void sb5(int i, int i2, int i3, int i4) {
        int i5 = i ^ (-1);
        int i6 = i ^ i2;
        int i7 = i ^ i4;
        this.X0 = (i3 ^ i5) ^ (i6 | i7);
        int i8 = i4 & this.X0;
        this.X1 = i8 ^ (i6 ^ this.X0);
        int i9 = i5 | this.X0;
        int i10 = i6 | i8;
        int i11 = i7 ^ i9;
        this.X2 = i10 ^ i11;
        this.X3 = (i2 ^ i8) ^ (this.X1 & i11);
    }

    private void ib5(int i, int i2, int i3, int i4) {
        int i5 = i3 ^ (-1);
        int i6 = i4 ^ (i2 & i5);
        int i7 = i & i6;
        this.X3 = i7 ^ (i2 ^ i5);
        int i8 = i2 | this.X3;
        this.X1 = i6 ^ (i & i8);
        int i9 = i | i4;
        this.X0 = i9 ^ (i5 ^ i8);
        this.X2 = (i2 & i9) ^ (i7 | (i ^ i3));
    }

    private void sb6(int i, int i2, int i3, int i4) {
        int i5 = i ^ i4;
        int i6 = i2 ^ i5;
        int i7 = i3 ^ ((i ^ (-1)) | i5);
        this.X1 = i2 ^ i7;
        int i8 = i4 ^ (i5 | this.X1);
        this.X2 = i6 ^ (i7 & i8);
        int i9 = i7 ^ i8;
        this.X0 = this.X2 ^ i9;
        this.X3 = (i7 ^ (-1)) ^ (i6 & i9);
    }

    private void ib6(int i, int i2, int i3, int i4) {
        int i5 = i ^ (-1);
        int i6 = i ^ i2;
        int i7 = i3 ^ i6;
        int i8 = i4 ^ (i3 | i5);
        this.X1 = i7 ^ i8;
        int i9 = i6 ^ (i7 & i8);
        this.X3 = i8 ^ (i2 | i9);
        int i10 = i2 | this.X3;
        this.X0 = i9 ^ i10;
        this.X2 = (i4 & i5) ^ (i7 ^ i10);
    }

    private void sb7(int i, int i2, int i3, int i4) {
        int i5 = i2 ^ i3;
        int i6 = i4 ^ (i3 & i5);
        int i7 = i ^ i6;
        this.X1 = i2 ^ (i7 & (i4 | i5));
        int i8 = i6 | this.X1;
        this.X3 = i5 ^ (i & i7);
        int i9 = i7 ^ i8;
        this.X2 = i6 ^ (this.X3 & i9);
        this.X0 = (i9 ^ (-1)) ^ (this.X3 & this.X2);
    }

    private void ib7(int i, int i2, int i3, int i4) {
        int i5 = i3 | (i & i2);
        int i6 = i4 & (i | i2);
        this.X3 = i5 ^ i6;
        int i7 = i2 ^ i6;
        this.X1 = i ^ (i7 | (this.X3 ^ (i4 ^ (-1))));
        this.X0 = (i3 ^ i7) ^ (i4 | this.X1);
        this.X2 = (i5 ^ this.X1) ^ (this.X0 ^ (i & this.X3));
    }

    private void LT() {
        int iRotateLeft = rotateLeft(this.X0, 13);
        int iRotateLeft2 = rotateLeft(this.X2, 3);
        int i = (this.X1 ^ iRotateLeft) ^ iRotateLeft2;
        int i2 = (this.X3 ^ iRotateLeft2) ^ (iRotateLeft << 3);
        this.X1 = rotateLeft(i, 1);
        this.X3 = rotateLeft(i2, 7);
        this.X0 = rotateLeft((iRotateLeft ^ this.X1) ^ this.X3, 5);
        this.X2 = rotateLeft((iRotateLeft2 ^ this.X3) ^ (this.X1 << 7), 22);
    }

    private void inverseLT() {
        int iRotateRight = (rotateRight(this.X2, 22) ^ this.X3) ^ (this.X1 << 7);
        int iRotateRight2 = (rotateRight(this.X0, 5) ^ this.X1) ^ this.X3;
        int iRotateRight3 = rotateRight(this.X3, 7);
        int iRotateRight4 = rotateRight(this.X1, 1);
        this.X3 = (iRotateRight3 ^ iRotateRight) ^ (iRotateRight2 << 3);
        this.X1 = (iRotateRight4 ^ iRotateRight2) ^ iRotateRight;
        this.X2 = rotateRight(iRotateRight, 3);
        this.X0 = rotateRight(iRotateRight2, 13);
    }
}
