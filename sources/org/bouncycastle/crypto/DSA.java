package org.bouncycastle.crypto;

import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/DSA.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/DSA.class */
public interface DSA {
    void init(boolean z, CipherParameters cipherParameters);

    BigInteger[] generateSignature(byte[] bArr);

    boolean verifySignature(byte[] bArr, BigInteger bigInteger, BigInteger bigInteger2);
}
