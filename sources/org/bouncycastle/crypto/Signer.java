package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/Signer.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/Signer.class */
public interface Signer {
    void init(boolean z, CipherParameters cipherParameters);

    void update(byte b);

    void update(byte[] bArr, int i, int i2);

    byte[] generateSignature() throws DataLengthException, CryptoException;

    boolean verifySignature(byte[] bArr);

    void reset();
}
