package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/ParametersWithID.class */
public class ParametersWithID implements CipherParameters {
    private CipherParameters parameters;
    private byte[] id;

    public ParametersWithID(CipherParameters cipherParameters, byte[] bArr) {
        this.parameters = cipherParameters;
        this.id = bArr;
    }

    public byte[] getID() {
        return this.id;
    }

    public CipherParameters getParameters() {
        return this.parameters;
    }
}
