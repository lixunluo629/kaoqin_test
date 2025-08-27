package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ZSignedDigitL2RMultiplier.class */
public class ZSignedDigitL2RMultiplier extends AbstractECMultiplier {
    @Override // org.bouncycastle.math.ec.AbstractECMultiplier
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint eCPointNormalize = eCPoint.normalize();
        ECPoint eCPointNegate = eCPointNormalize.negate();
        ECPoint eCPointTwicePlus = eCPointNormalize;
        int iBitLength = bigInteger.bitLength();
        int lowestSetBit = bigInteger.getLowestSetBit();
        int i = iBitLength;
        while (true) {
            i--;
            if (i <= lowestSetBit) {
                return eCPointTwicePlus.timesPow2(lowestSetBit);
            }
            eCPointTwicePlus = eCPointTwicePlus.twicePlus(bigInteger.testBit(i) ? eCPointNormalize : eCPointNegate);
        }
    }
}
