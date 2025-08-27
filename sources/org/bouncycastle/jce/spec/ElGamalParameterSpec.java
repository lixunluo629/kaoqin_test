package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/spec/ElGamalParameterSpec.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/spec/ElGamalParameterSpec.class */
public class ElGamalParameterSpec implements AlgorithmParameterSpec {
    private BigInteger p;
    private BigInteger g;

    public ElGamalParameterSpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this.p = bigInteger;
        this.g = bigInteger2;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getG() {
        return this.g;
    }
}
