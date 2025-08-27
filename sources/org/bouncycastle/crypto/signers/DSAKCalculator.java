package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/signers/DSAKCalculator.class */
public interface DSAKCalculator {
    boolean isDeterministic();

    void init(BigInteger bigInteger, SecureRandom secureRandom);

    void init(BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr);

    BigInteger nextK();
}
