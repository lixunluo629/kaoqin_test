package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/MontgomeryLadderMultiplier.class */
public class MontgomeryLadderMultiplier extends AbstractECMultiplier {
    @Override // org.bouncycastle.math.ec.AbstractECMultiplier
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        ECPoint[] eCPointArr = new ECPoint[2];
        eCPointArr[0] = eCPoint.getCurve().getInfinity();
        eCPointArr[1] = eCPoint;
        int iBitLength = bigInteger.bitLength();
        while (true) {
            iBitLength--;
            if (iBitLength < 0) {
                return eCPointArr[0];
            }
            int i = bigInteger.testBit(iBitLength) ? 1 : 0;
            int i2 = 1 - i;
            eCPointArr[i2] = eCPointArr[i2].add(eCPointArr[i]);
            eCPointArr[i] = eCPointArr[i].twice();
        }
    }
}
