package org.bouncycastle.crypto;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/Digest.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/Digest.class */
public interface Digest {
    String getAlgorithmName();

    int getDigestSize();

    void update(byte b);

    void update(byte[] bArr, int i, int i2);

    int doFinal(byte[] bArr, int i);

    void reset();
}
