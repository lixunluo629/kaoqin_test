package org.bouncycastle.crypto.params;

import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/params/DSAPublicKeyParameters.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/params/DSAPublicKeyParameters.class */
public class DSAPublicKeyParameters extends DSAKeyParameters {
    private BigInteger y;

    public DSAPublicKeyParameters(BigInteger bigInteger, DSAParameters dSAParameters) {
        super(false, dSAParameters);
        this.y = bigInteger;
    }

    public BigInteger getY() {
        return this.y;
    }
}
