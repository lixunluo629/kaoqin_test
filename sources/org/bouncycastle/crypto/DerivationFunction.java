package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/DerivationFunction.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/DerivationFunction.class */
public interface DerivationFunction {
    void init(DerivationParameters derivationParameters);

    Digest getDigest();

    int generateBytes(byte[] bArr, int i, int i2) throws DataLengthException, IllegalArgumentException;
}
