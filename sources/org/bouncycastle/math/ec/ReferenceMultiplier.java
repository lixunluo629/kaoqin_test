package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ReferenceMultiplier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ReferenceMultiplier.class */
class ReferenceMultiplier implements ECMultiplier {
    ReferenceMultiplier() {
    }

    @Override // org.bouncycastle.math.ec.ECMultiplier
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger, PreCompInfo preCompInfo) {
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        int iBitLength = bigInteger.bitLength();
        for (int i = 0; i < iBitLength; i++) {
            if (bigInteger.testBit(i)) {
                infinity = infinity.add(eCPoint);
            }
            eCPoint = eCPoint.twice();
        }
        return infinity;
    }
}
