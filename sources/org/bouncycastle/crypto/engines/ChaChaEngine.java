package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Pack;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/ChaChaEngine.class */
public class ChaChaEngine extends Salsa20Engine {
    public ChaChaEngine() {
    }

    public ChaChaEngine(int i) {
        super(i);
    }

    @Override // org.bouncycastle.crypto.engines.Salsa20Engine, org.bouncycastle.crypto.StreamCipher
    public String getAlgorithmName() {
        return "ChaCha" + this.rounds;
    }

    protected void advanceCounter(long j) {
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        if (i > 0) {
            int[] iArr = this.engineState;
            iArr[13] = iArr[13] + i;
        }
        int i3 = this.engineState[12];
        int[] iArr2 = this.engineState;
        iArr2[12] = iArr2[12] + i2;
        if (i3 == 0 || this.engineState[12] >= i3) {
            return;
        }
        int[] iArr3 = this.engineState;
        iArr3[13] = iArr3[13] + 1;
    }

    protected void advanceCounter() {
        int[] iArr = this.engineState;
        int i = iArr[12] + 1;
        iArr[12] = i;
        if (i == 0) {
            int[] iArr2 = this.engineState;
            iArr2[13] = iArr2[13] + 1;
        }
    }

    protected void retreatCounter(long j) {
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        if (i != 0) {
            if ((this.engineState[13] & 4294967295L) < (i & 4294967295L)) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
            int[] iArr = this.engineState;
            iArr[13] = iArr[13] - i;
        }
        if ((this.engineState[12] & 4294967295L) >= (i2 & 4294967295L)) {
            int[] iArr2 = this.engineState;
            iArr2[12] = iArr2[12] - i2;
        } else {
            if (this.engineState[13] == 0) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
            int[] iArr3 = this.engineState;
            iArr3[13] = iArr3[13] - 1;
            int[] iArr4 = this.engineState;
            iArr4[12] = iArr4[12] - i2;
        }
    }

    protected void retreatCounter() {
        if (this.engineState[12] == 0 && this.engineState[13] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] iArr = this.engineState;
        int i = iArr[12] - 1;
        iArr[12] = i;
        if (i == -1) {
            int[] iArr2 = this.engineState;
            iArr2[13] = iArr2[13] - 1;
        }
    }

    protected long getCounter() {
        return (this.engineState[13] << 32) | (this.engineState[12] & 4294967295L);
    }

    protected void resetCounter() {
        int[] iArr = this.engineState;
        this.engineState[13] = 0;
        iArr[12] = 0;
    }

    protected void setKey(byte[] bArr, byte[] bArr2) {
        if (bArr != null) {
            if (bArr.length != 16 && bArr.length != 32) {
                throw new IllegalArgumentException(getAlgorithmName() + " requires 128 bit or 256 bit key");
            }
            packTauOrSigma(bArr.length, this.engineState, 0);
            Pack.littleEndianToInt(bArr, 0, this.engineState, 4, 4);
            Pack.littleEndianToInt(bArr, bArr.length - 16, this.engineState, 8, 4);
        }
        Pack.littleEndianToInt(bArr2, 0, this.engineState, 14, 2);
    }

    protected void generateKeyStream(byte[] bArr) {
        chachaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, bArr, 0);
    }

    public static void chachaCore(int i, int[] iArr, int[] iArr2) {
        if (iArr.length != 16) {
            throw new IllegalArgumentException();
        }
        if (iArr2.length != 16) {
            throw new IllegalArgumentException();
        }
        if (i % 2 != 0) {
            throw new IllegalArgumentException("Number of rounds must be even");
        }
        int i2 = iArr[0];
        int i3 = iArr[1];
        int i4 = iArr[2];
        int i5 = iArr[3];
        int iRotl = iArr[4];
        int iRotl2 = iArr[5];
        int iRotl3 = iArr[6];
        int iRotl4 = iArr[7];
        int i6 = iArr[8];
        int i7 = iArr[9];
        int i8 = iArr[10];
        int i9 = iArr[11];
        int iRotl5 = iArr[12];
        int iRotl6 = iArr[13];
        int iRotl7 = iArr[14];
        int iRotl8 = iArr[15];
        for (int i10 = i; i10 > 0; i10 -= 2) {
            int i11 = i2 + iRotl;
            int iRotl9 = rotl(iRotl5 ^ i11, 16);
            int i12 = i6 + iRotl9;
            int iRotl10 = rotl(iRotl ^ i12, 12);
            int i13 = i11 + iRotl10;
            int iRotl11 = rotl(iRotl9 ^ i13, 8);
            int i14 = i12 + iRotl11;
            int iRotl12 = rotl(iRotl10 ^ i14, 7);
            int i15 = i3 + iRotl2;
            int iRotl13 = rotl(iRotl6 ^ i15, 16);
            int i16 = i7 + iRotl13;
            int iRotl14 = rotl(iRotl2 ^ i16, 12);
            int i17 = i15 + iRotl14;
            int iRotl15 = rotl(iRotl13 ^ i17, 8);
            int i18 = i16 + iRotl15;
            int iRotl16 = rotl(iRotl14 ^ i18, 7);
            int i19 = i4 + iRotl3;
            int iRotl17 = rotl(iRotl7 ^ i19, 16);
            int i20 = i8 + iRotl17;
            int iRotl18 = rotl(iRotl3 ^ i20, 12);
            int i21 = i19 + iRotl18;
            int iRotl19 = rotl(iRotl17 ^ i21, 8);
            int i22 = i20 + iRotl19;
            int iRotl20 = rotl(iRotl18 ^ i22, 7);
            int i23 = i5 + iRotl4;
            int iRotl21 = rotl(iRotl8 ^ i23, 16);
            int i24 = i9 + iRotl21;
            int iRotl22 = rotl(iRotl4 ^ i24, 12);
            int i25 = i23 + iRotl22;
            int iRotl23 = rotl(iRotl21 ^ i25, 8);
            int i26 = i24 + iRotl23;
            int iRotl24 = rotl(iRotl22 ^ i26, 7);
            int i27 = i13 + iRotl16;
            int iRotl25 = rotl(iRotl23 ^ i27, 16);
            int i28 = i22 + iRotl25;
            int iRotl26 = rotl(iRotl16 ^ i28, 12);
            i2 = i27 + iRotl26;
            iRotl8 = rotl(iRotl25 ^ i2, 8);
            i8 = i28 + iRotl8;
            iRotl2 = rotl(iRotl26 ^ i8, 7);
            int i29 = i17 + iRotl20;
            int iRotl27 = rotl(iRotl11 ^ i29, 16);
            int i30 = i26 + iRotl27;
            int iRotl28 = rotl(iRotl20 ^ i30, 12);
            i3 = i29 + iRotl28;
            iRotl5 = rotl(iRotl27 ^ i3, 8);
            i9 = i30 + iRotl5;
            iRotl3 = rotl(iRotl28 ^ i9, 7);
            int i31 = i21 + iRotl24;
            int iRotl29 = rotl(iRotl15 ^ i31, 16);
            int i32 = i14 + iRotl29;
            int iRotl30 = rotl(iRotl24 ^ i32, 12);
            i4 = i31 + iRotl30;
            iRotl6 = rotl(iRotl29 ^ i4, 8);
            i6 = i32 + iRotl6;
            iRotl4 = rotl(iRotl30 ^ i6, 7);
            int i33 = i25 + iRotl12;
            int iRotl31 = rotl(iRotl19 ^ i33, 16);
            int i34 = i18 + iRotl31;
            int iRotl32 = rotl(iRotl12 ^ i34, 12);
            i5 = i33 + iRotl32;
            iRotl7 = rotl(iRotl31 ^ i5, 8);
            i7 = i34 + iRotl7;
            iRotl = rotl(iRotl32 ^ i7, 7);
        }
        iArr2[0] = i2 + iArr[0];
        iArr2[1] = i3 + iArr[1];
        iArr2[2] = i4 + iArr[2];
        iArr2[3] = i5 + iArr[3];
        iArr2[4] = iRotl + iArr[4];
        iArr2[5] = iRotl2 + iArr[5];
        iArr2[6] = iRotl3 + iArr[6];
        iArr2[7] = iRotl4 + iArr[7];
        iArr2[8] = i6 + iArr[8];
        iArr2[9] = i7 + iArr[9];
        iArr2[10] = i8 + iArr[10];
        iArr2[11] = i9 + iArr[11];
        iArr2[12] = iRotl5 + iArr[12];
        iArr2[13] = iRotl6 + iArr[13];
        iArr2[14] = iRotl7 + iArr[14];
        iArr2[15] = iRotl8 + iArr[15];
    }
}
