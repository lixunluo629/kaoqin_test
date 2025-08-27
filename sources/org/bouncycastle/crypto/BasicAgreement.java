package org.bouncycastle.crypto;

import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/BasicAgreement.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/BasicAgreement.class */
public interface BasicAgreement {
    void init(CipherParameters cipherParameters);

    BigInteger calculateAgreement(CipherParameters cipherParameters);
}
