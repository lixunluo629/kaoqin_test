package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/AbstractECMultiplier.class */
public abstract class AbstractECMultiplier implements ECMultiplier {
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger) {
        int iSignum = bigInteger.signum();
        if (iSignum == 0 || eCPoint.isInfinity()) {
            return eCPoint.getCurve().getInfinity();
        }
        ECPoint eCPointMultiplyPositive = multiplyPositive(eCPoint, bigInteger.abs());
        return checkResult(iSignum > 0 ? eCPointMultiplyPositive : eCPointMultiplyPositive.negate());
    }

    protected abstract ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger);

    protected ECPoint checkResult(ECPoint eCPoint) {
        return ECAlgorithms.implCheckResult(eCPoint);
    }
}
