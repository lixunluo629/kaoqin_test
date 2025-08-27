package org.bouncycastle.crypto;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/KeyEncapsulation.class */
public interface KeyEncapsulation {
    void init(CipherParameters cipherParameters);

    CipherParameters encrypt(byte[] bArr, int i, int i2);

    CipherParameters decrypt(byte[] bArr, int i, int i2, int i3);
}
