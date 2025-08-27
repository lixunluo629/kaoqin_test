package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/NafR2LMultiplier.class */
public class NafR2LMultiplier extends AbstractECMultiplier {
    @Override // org.bouncycastle.math.ec.AbstractECMultiplier
    protected ECPoint multiplyPositive(ECPoint eCPoint, BigInteger bigInteger) {
        int[] iArrGenerateCompactNaf = WNafUtil.generateCompactNaf(bigInteger);
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        ECPoint eCPointTimesPow2 = eCPoint;
        int i = 0;
        for (int i2 : iArrGenerateCompactNaf) {
            int i3 = i2 >> 16;
            eCPointTimesPow2 = eCPointTimesPow2.timesPow2(i + (i2 & 65535));
            infinity = infinity.add(i3 < 0 ? eCPointTimesPow2.negate() : eCPointTimesPow2);
            i = 1;
        }
        return infinity;
    }
}
