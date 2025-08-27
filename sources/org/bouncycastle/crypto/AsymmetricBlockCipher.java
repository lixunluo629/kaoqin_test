package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/AsymmetricBlockCipher.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/AsymmetricBlockCipher.class */
public interface AsymmetricBlockCipher {
    void init(boolean z, CipherParameters cipherParameters);

    int getInputBlockSize();

    int getOutputBlockSize();

    byte[] processBlock(byte[] bArr, int i, int i2) throws InvalidCipherTextException;
}
