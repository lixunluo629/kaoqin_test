package org.bouncycastle.crypto;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/RawAgreement.class */
public interface RawAgreement {
    void init(CipherParameters cipherParameters);

    int getAgreementSize();

    void calculateAgreement(CipherParameters cipherParameters, byte[] bArr, int i);
}
