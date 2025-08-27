package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/DoubleAddMultiplier.class */
public class DoubleAddMultiplier extends AbstractECMultiplier {
    @Override // org.bouncycastle.math.ec.AbstractECMultiplier
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint[] eCPointArr = new ECPoint[2];
        eCPointArr[0] = eCPoint.getCurve().getInfinity();
        eCPointArr[1] = eCPoint;
        int iBitLength = bigInteger.bitLength();
        for (int i = 0; i < iBitLength; i++) {
            int i2 = bigInteger.testBit(i) ? 1 : 0;
            int i3 = 1 - i2;
            eCPointArr[i3] = eCPointArr[i3].twicePlus(eCPointArr[i2]);
        }
        return eCPointArr[0];
    }
}
