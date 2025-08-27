package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/Mac.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/Mac.class */
public interface Mac {
    void init(CipherParameters cipherParameters) throws IllegalArgumentException;

    String getAlgorithmName();

    int getMacSize();

    void update(byte b) throws IllegalStateException;

    void update(byte[] bArr, int i, int i2) throws IllegalStateException, DataLengthException;

    int doFinal(byte[] bArr, int i) throws IllegalStateException, DataLengthException;

    void reset();
}
