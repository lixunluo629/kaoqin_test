package org.bouncycastle.pqc.crypto.gmss;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/crypto/gmss/GMSSPublicKeyParameters.class */
public class GMSSPublicKeyParameters extends GMSSKeyParameters {
    private byte[] gmssPublicKey;

    public GMSSPublicKeyParameters(byte[] bArr, GMSSParameters gMSSParameters) {
        super(false, gMSSParameters);
        this.gmssPublicKey = bArr;
    }

    public byte[] getPublicKey() {
        return this.gmssPublicKey;
    }
}
