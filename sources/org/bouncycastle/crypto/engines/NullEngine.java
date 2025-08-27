package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/engines/NullEngine.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/engines/NullEngine.class */
public class NullEngine implements BlockCipher {
    private boolean initialised;
    protected static final int BLOCK_SIZE = 1;

    @Override // org.bouncycastle.crypto.BlockCipher
    public void init(boolean z, CipherParameters cipherParameters) throws IllegalArgumentException {
        this.initialised = true;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public String getAlgorithmName() {
        return "Null";
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int getBlockSize() {
        return 1;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) throws IllegalStateException, DataLengthException {
        if (!this.initialised) {
            throw new IllegalStateException("Null engine not initialised");
        }
        if (i + 1 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (i2 + 1 > bArr2.length) {
            throw new DataLengthException("output buffer too short");
        }
        for (int i3 = 0; i3 < 1; i3++) {
            bArr2[i2 + i3] = bArr[i + i3];
        }
        return 1;
    }

    @Override // org.bouncycastle.crypto.BlockCipher
    public void reset() {
    }
}
