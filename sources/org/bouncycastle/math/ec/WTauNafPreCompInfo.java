package org.bouncycastle.math.ec;

import org.bouncycastle.math.ec.ECPoint;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/WTauNafPreCompInfo.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/WTauNafPreCompInfo.class */
class WTauNafPreCompInfo implements PreCompInfo {
    private ECPoint.F2m[] preComp;

    WTauNafPreCompInfo(ECPoint.F2m[] f2mArr) {
        this.preComp = null;
        this.preComp = f2mArr;
    }

    protected ECPoint.F2m[] getPreComp() {
        return this.preComp;
    }
}
