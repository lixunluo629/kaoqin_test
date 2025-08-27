package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/WTauNafMultiplier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/WTauNafMultiplier.class */
public class WTauNafMultiplier implements ECMultiplier {

    /* renamed from: org.bouncycastle.math.ec.WTauNafMultiplier$1, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/WTauNafMultiplier$1.class */
    static class AnonymousClass1 implements PreCompCallback {
        final /* synthetic */ ECPoint.AbstractF2m val$p;
        final /* synthetic */ byte val$a;

        AnonymousClass1(ECPoint.AbstractF2m abstractF2m, byte b) {
            this.val$p = abstractF2m;
            this.val$a = b;
        }

        @Override // org.bouncycastle.math.ec.PreCompCallback
        public PreCompInfo precompute(PreCompInfo preCompInfo) {
            if (preCompInfo instanceof WTauNafPreCompInfo) {
                return preCompInfo;
            }
            WTauNafPreCompInfo wTauNafPreCompInfo = new WTauNafPreCompInfo();
            wTauNafPreCompInfo.setPreComp(Tnaf.getPreComp(this.val$p, this.val$a));
            return wTauNafPreCompInfo;
        }
    }

    @Override // org.bouncycastle.math.ec.ECMultiplier
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger, PreCompInfo preCompInfo) {
        if (!(eCPoint instanceof ECPoint.F2m)) {
            throw new IllegalArgumentException("Only ECPoint.F2m can be used in WTauNafMultiplier");
        }
        ECPoint.F2m f2m = (ECPoint.F2m) eCPoint;
        ECCurve.F2m f2m2 = (ECCurve.F2m) f2m.getCurve();
        int m = f2m2.getM();
        byte bByteValue = f2m2.getA().toBigInteger().byteValue();
        byte mu = f2m2.getMu();
        return multiplyWTnaf(f2m, Tnaf.partModReduction(bigInteger, m, bByteValue, f2m2.getSi(), mu, (byte) 10), preCompInfo, bByteValue, mu);
    }

    private ECPoint.F2m multiplyWTnaf(ECPoint.F2m f2m, ZTauElement zTauElement, PreCompInfo preCompInfo, byte b, byte b2) {
        return multiplyFromWTnaf(f2m, Tnaf.tauAdicWNaf(b2, zTauElement, (byte) 4, BigInteger.valueOf(16L), Tnaf.getTw(b2, 4), b == 0 ? Tnaf.alpha0 : Tnaf.alpha1), preCompInfo);
    }

    private static ECPoint.F2m multiplyFromWTnaf(ECPoint.F2m f2m, byte[] bArr, PreCompInfo preCompInfo) {
        ECPoint.F2m[] preComp;
        byte bByteValue = ((ECCurve.F2m) f2m.getCurve()).getA().toBigInteger().byteValue();
        if (preCompInfo == null || !(preCompInfo instanceof WTauNafPreCompInfo)) {
            preComp = Tnaf.getPreComp(f2m, bByteValue);
            f2m.setPreCompInfo(new WTauNafPreCompInfo(preComp));
        } else {
            preComp = ((WTauNafPreCompInfo) preCompInfo).getPreComp();
        }
        ECPoint.F2m f2mTau = (ECPoint.F2m) f2m.getCurve().getInfinity();
        for (int length = bArr.length - 1; length >= 0; length--) {
            f2mTau = Tnaf.tau(f2mTau);
            if (bArr[length] != 0) {
                f2mTau = bArr[length] > 0 ? f2mTau.addSimple(preComp[bArr[length]]) : f2mTau.subtractSimple(preComp[-bArr[length]]);
            }
        }
        return f2mTau;
    }
}
