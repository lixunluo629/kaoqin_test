package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ZSignedDigitR2LMultiplier.class */
public class ZSignedDigitR2LMultiplier extends AbstractECMultiplier {
    @Override // org.bouncycastle.math.ec.AbstractECMultiplier
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int iBitLength = bigInteger.bitLength();
        int lowestSetBit = bigInteger.getLowestSetBit();
        ECPoint eCPointTimesPow2 = eCPoint.timesPow2(lowestSetBit);
        int i = lowestSetBit;
        while (true) {
            i++;
            if (i >= iBitLength) {
                return infinity.add(eCPointTimesPow2);
            }
            infinity = infinity.add(bigInteger.testBit(i) ? eCPointTimesPow2 : eCPointTimesPow2.negate());
            eCPointTimesPow2 = eCPointTimesPow2.twice();
        }
    }
}
