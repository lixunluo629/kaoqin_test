package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Pack;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/ChaCha7539Engine.class */
public class ChaCha7539Engine extends Salsa20Engine {
    @Override // org.bouncycastle.crypto.engines.Salsa20Engine, org.bouncycastle.crypto.StreamCipher
    public String getAlgorithmName() {
        return "ChaCha7539";
    }

    protected int getNonceSize() {
        return 12;
    }

    protected void advanceCounter(long j) {
        int i = (int) j;
        if (((int) (j >>> 32)) > 0) {
            throw new IllegalStateException("attempt to increase counter past 2^32.");
        }
        int i2 = this.engineState[12];
        int[] iArr = this.engineState;
        iArr[12] = iArr[12] + i;
        if (i2 != 0 && this.engineState[12] < i2) {
            throw new IllegalStateException("attempt to increase counter past 2^32.");
        }
    }

    protected void advanceCounter() {
        int[] iArr = this.engineState;
        int i = iArr[12] + 1;
        iArr[12] = i;
        if (i == 0) {
            throw new IllegalStateException("attempt to increase counter past 2^32.");
        }
    }

    protected void retreatCounter(long j) {
        int i = (int) j;
        if (((int) (j >>> 32)) != 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        if ((this.engineState[12] & 4294967295L) < (i & 4294967295L)) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] iArr = this.engineState;
        iArr[12] = iArr[12] - i;
    }

    protected void retreatCounter() {
        if (this.engineState[12] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] iArr = this.engineState;
        iArr[12] = iArr[12] - 1;
    }

    protected long getCounter() {
        return this.engineState[12] & 4294967295L;
    }

    protected void resetCounter() {
        this.engineState[12] = 0;
    }

    protected void setKey(byte[] bArr, byte[] bArr2) {
        if (bArr != null) {
            if (bArr.length != 32) {
                throw new IllegalArgumentException(getAlgorithmName() + " requires 256 bit key");
            }
            packTauOrSigma(bArr.length, this.engineState, 0);
            Pack.littleEndianToInt(bArr, 0, this.engineState, 4, 8);
        }
        Pack.littleEndianToInt(bArr2, 0, this.engineState, 13, 3);
    }

    protected void generateKeyStream(byte[] bArr) {
        ChaChaEngine.chachaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, bArr, 0);
    }
}
