package org.bouncycastle.math.ec;

import java.math.BigInteger;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/WNafMultiplier.class */
class WNafMultiplier implements ECMultiplier {
    WNafMultiplier() {
    }

    public byte[] windowNaf(byte b, BigInteger bigInteger) {
        byte[] bArr = new byte[bigInteger.bitLength() + 1];
        short s = (short) (1 << b);
        BigInteger bigIntegerValueOf = BigInteger.valueOf(s);
        int i = 0;
        int i2 = 0;
        while (bigInteger.signum() > 0) {
            if (bigInteger.testBit(0)) {
                BigInteger bigIntegerMod = bigInteger.mod(bigIntegerValueOf);
                if (bigIntegerMod.testBit(b - 1)) {
                    bArr[i] = (byte) (bigIntegerMod.intValue() - s);
                } else {
                    bArr[i] = (byte) bigIntegerMod.intValue();
                }
                bigInteger = bigInteger.subtract(BigInteger.valueOf(bArr[i]));
                i2 = i;
            } else {
                bArr[i] = 0;
            }
            bigInteger = bigInteger.shiftRight(1);
            i++;
        }
        int i3 = i2 + 1;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, 0, bArr2, 0, i3);
        return bArr2;
    }

    @Override // org.bouncycastle.math.ec.ECMultiplier
    public ECPoint multiply(ECPoint eCPoint, BigInteger bigInteger, PreCompInfo preCompInfo) {
        byte b;
        int i;
        WNafPreCompInfo wNafPreCompInfo = (preCompInfo == null || !(preCompInfo instanceof WNafPreCompInfo)) ? new WNafPreCompInfo() : (WNafPreCompInfo) preCompInfo;
        int iBitLength = bigInteger.bitLength();
        if (iBitLength < 13) {
            b = 2;
            i = 1;
        } else if (iBitLength < 41) {
            b = 3;
            i = 2;
        } else if (iBitLength < 121) {
            b = 4;
            i = 4;
        } else if (iBitLength < 337) {
            b = 5;
            i = 8;
        } else if (iBitLength < 897) {
            b = 6;
            i = 16;
        } else if (iBitLength < 2305) {
            b = 7;
            i = 32;
        } else {
            b = 8;
            i = 127;
        }
        int length = 1;
        ECPoint[] preComp = wNafPreCompInfo.getPreComp();
        ECPoint twiceP = wNafPreCompInfo.getTwiceP();
        if (preComp == null) {
            preComp = new ECPoint[]{eCPoint};
        } else {
            length = preComp.length;
        }
        if (twiceP == null) {
            twiceP = eCPoint.twice();
        }
        if (length < i) {
            ECPoint[] eCPointArr = preComp;
            preComp = new ECPoint[i];
            System.arraycopy(eCPointArr, 0, preComp, 0, length);
            for (int i2 = length; i2 < i; i2++) {
                preComp[i2] = twiceP.add(preComp[i2 - 1]);
            }
        }
        byte[] bArrWindowNaf = windowNaf(b, bigInteger);
        int length2 = bArrWindowNaf.length;
        ECPoint infinity = eCPoint.getCurve().getInfinity();
        for (int i3 = length2 - 1; i3 >= 0; i3--) {
            infinity = infinity.twice();
            if (bArrWindowNaf[i3] != 0) {
                infinity = bArrWindowNaf[i3] > 0 ? infinity.add(preComp[(bArrWindowNaf[i3] - 1) / 2]) : infinity.subtract(preComp[((-bArrWindowNaf[i3]) - 1) / 2]);
            }
        }
        wNafPreCompInfo.setPreComp(preComp);
        wNafPreCompInfo.setTwiceP(twiceP);
        eCPoint.setPreCompInfo(wNafPreCompInfo);
        return infinity;
    }
}
