package org.bouncycastle.crypto.prng;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/prng/RandomGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/prng/RandomGenerator.class */
public interface RandomGenerator {
    void addSeedMaterial(byte[] bArr);

    void addSeedMaterial(long j);

    void nextBytes(byte[] bArr);

    void nextBytes(byte[] bArr, int i, int i2);
}
