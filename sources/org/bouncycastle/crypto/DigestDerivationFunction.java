package org.bouncycastle.crypto;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/DigestDerivationFunction.class */
public interface DigestDerivationFunction extends DerivationFunction {
    @Override // org.bouncycastle.crypto.DerivationFunction
    Digest getDigest();
}
