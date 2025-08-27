package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/Wrapper.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/Wrapper.class */
public interface Wrapper {
    void init(boolean z, CipherParameters cipherParameters);

    String getAlgorithmName();

    byte[] wrap(byte[] bArr, int i, int i2);

    byte[] unwrap(byte[] bArr, int i, int i2) throws InvalidCipherTextException;
}
