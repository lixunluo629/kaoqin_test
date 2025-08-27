package org.bouncycastle.crypto.agreement.kdf;

import org.bouncycastle.crypto.DerivationParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/agreement/kdf/GSKKDFParameters.class */
public class GSKKDFParameters implements DerivationParameters {
    private final byte[] z;
    private final int startCounter;
    private final byte[] nonce;

    public GSKKDFParameters(byte[] bArr, int i) {
        this(bArr, i, null);
    }

    public GSKKDFParameters(byte[] bArr, int i, byte[] bArr2) {
        this.z = bArr;
        this.startCounter = i;
        this.nonce = bArr2;
    }

    public byte[] getZ() {
        return this.z;
    }

    public int getStartCounter() {
        return this.startCounter;
    }

    public byte[] getNonce() {
        return this.nonce;
    }
}
