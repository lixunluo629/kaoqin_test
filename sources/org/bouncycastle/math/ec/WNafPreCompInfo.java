package org.bouncycastle.math.ec;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/WNafPreCompInfo.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/WNafPreCompInfo.class */
class WNafPreCompInfo implements PreCompInfo {
    private ECPoint[] preComp = null;
    private ECPoint twiceP = null;

    WNafPreCompInfo() {
    }

    protected ECPoint[] getPreComp() {
        return this.preComp;
    }

    protected void setPreComp(ECPoint[] eCPointArr) {
        this.preComp = eCPointArr;
    }

    protected ECPoint getTwiceP() {
        return this.twiceP;
    }

    protected void setTwiceP(ECPoint eCPoint) {
        this.twiceP = eCPoint;
    }
}
