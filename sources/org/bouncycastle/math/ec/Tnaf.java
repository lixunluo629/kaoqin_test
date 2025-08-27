package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/Tnaf.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/Tnaf.class */
class Tnaf {
    public static final byte WIDTH = 4;
    public static final byte POW_2_WIDTH = 16;
    private static final BigInteger MINUS_ONE = ECConstants.ONE.negate();
    private static final BigInteger MINUS_TWO = ECConstants.TWO.negate();
    private static final BigInteger MINUS_THREE = ECConstants.THREE.negate();
    public static final ZTauElement[] alpha0 = {null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, MINUS_ONE), null, new ZTauElement(MINUS_ONE, MINUS_ONE), null, new ZTauElement(ECConstants.ONE, MINUS_ONE), null};
    public static final byte[][] alpha0Tnaf = {0, new byte[]{1}, 0, new byte[]{-1, 0, 1}, 0, new byte[]{1, 0, 1}, 0, new byte[]{-1, 0, 0, 1}};
    public static final ZTauElement[] alpha1 = {null, new ZTauElement(ECConstants.ONE, ECConstants.ZERO), null, new ZTauElement(MINUS_THREE, ECConstants.ONE), null, new ZTauElement(MINUS_ONE, ECConstants.ONE), null, new ZTauElement(ECConstants.ONE, ECConstants.ONE), null};
    public static final byte[][] alpha1Tnaf = {0, new byte[]{1}, 0, new byte[]{-1, 0, 1}, 0, new byte[]{1, 0, 1}, 0, new byte[]{-1, 0, 0, -1}};

    Tnaf() {
    }

    public static BigInteger norm(byte b, ZTauElement zTauElement) {
        BigInteger bigIntegerAdd;
        BigInteger bigIntegerMultiply = zTauElement.u.multiply(zTauElement.u);
        BigInteger bigIntegerMultiply2 = zTauElement.u.multiply(zTauElement.v);
        BigInteger bigIntegerShiftLeft = zTauElement.v.multiply(zTauElement.v).shiftLeft(1);
        if (b == 1) {
            bigIntegerAdd = bigIntegerMultiply.add(bigIntegerMultiply2).add(bigIntegerShiftLeft);
        } else {
            if (b != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            bigIntegerAdd = bigIntegerMultiply.subtract(bigIntegerMultiply2).add(bigIntegerShiftLeft);
        }
        return bigIntegerAdd;
    }

    public static SimpleBigDecimal norm(byte b, SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal simpleBigDecimal2) {
        SimpleBigDecimal simpleBigDecimalAdd;
        SimpleBigDecimal simpleBigDecimalMultiply = simpleBigDecimal.multiply(simpleBigDecimal);
        SimpleBigDecimal simpleBigDecimalMultiply2 = simpleBigDecimal.multiply(simpleBigDecimal2);
        SimpleBigDecimal simpleBigDecimalShiftLeft = simpleBigDecimal2.multiply(simpleBigDecimal2).shiftLeft(1);
        if (b == 1) {
            simpleBigDecimalAdd = simpleBigDecimalMultiply.add(simpleBigDecimalMultiply2).add(simpleBigDecimalShiftLeft);
        } else {
            if (b != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            simpleBigDecimalAdd = simpleBigDecimalMultiply.subtract(simpleBigDecimalMultiply2).add(simpleBigDecimalShiftLeft);
        }
        return simpleBigDecimalAdd;
    }

    public static ZTauElement round(SimpleBigDecimal simpleBigDecimal, SimpleBigDecimal simpleBigDecimal2, byte b) {
        SimpleBigDecimal simpleBigDecimalAdd;
        SimpleBigDecimal simpleBigDecimalSubtract;
        if (simpleBigDecimal2.getScale() != simpleBigDecimal.getScale()) {
            throw new IllegalArgumentException("lambda0 and lambda1 do not have same scale");
        }
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        BigInteger bigIntegerRound = simpleBigDecimal.round();
        BigInteger bigIntegerRound2 = simpleBigDecimal2.round();
        SimpleBigDecimal simpleBigDecimalSubtract2 = simpleBigDecimal.subtract(bigIntegerRound);
        SimpleBigDecimal simpleBigDecimalSubtract3 = simpleBigDecimal2.subtract(bigIntegerRound2);
        SimpleBigDecimal simpleBigDecimalAdd2 = simpleBigDecimalSubtract2.add(simpleBigDecimalSubtract2);
        SimpleBigDecimal simpleBigDecimalAdd3 = b == 1 ? simpleBigDecimalAdd2.add(simpleBigDecimalSubtract3) : simpleBigDecimalAdd2.subtract(simpleBigDecimalSubtract3);
        SimpleBigDecimal simpleBigDecimalAdd4 = simpleBigDecimalSubtract3.add(simpleBigDecimalSubtract3).add(simpleBigDecimalSubtract3);
        SimpleBigDecimal simpleBigDecimalAdd5 = simpleBigDecimalAdd4.add(simpleBigDecimalSubtract3);
        if (b == 1) {
            simpleBigDecimalAdd = simpleBigDecimalSubtract2.subtract(simpleBigDecimalAdd4);
            simpleBigDecimalSubtract = simpleBigDecimalSubtract2.add(simpleBigDecimalAdd5);
        } else {
            simpleBigDecimalAdd = simpleBigDecimalSubtract2.add(simpleBigDecimalAdd4);
            simpleBigDecimalSubtract = simpleBigDecimalSubtract2.subtract(simpleBigDecimalAdd5);
        }
        int i = 0;
        byte b2 = 0;
        if (simpleBigDecimalAdd3.compareTo(ECConstants.ONE) >= 0) {
            if (simpleBigDecimalAdd.compareTo(MINUS_ONE) < 0) {
                b2 = b;
            } else {
                i = 1;
            }
        } else if (simpleBigDecimalSubtract.compareTo(ECConstants.TWO) >= 0) {
            b2 = b;
        }
        if (simpleBigDecimalAdd3.compareTo(MINUS_ONE) < 0) {
            if (simpleBigDecimalAdd.compareTo(ECConstants.ONE) >= 0) {
                b2 = (byte) (-b);
            } else {
                i = -1;
            }
        } else if (simpleBigDecimalSubtract.compareTo(MINUS_TWO) < 0) {
            b2 = (byte) (-b);
        }
        return new ZTauElement(bigIntegerRound.add(BigInteger.valueOf(i)), bigIntegerRound2.add(BigInteger.valueOf(b2)));
    }

    public static SimpleBigDecimal approximateDivisionByN(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, byte b, int i, int i2) {
        int i3 = ((i + 5) / 2) + i2;
        BigInteger bigIntegerMultiply = bigInteger2.multiply(bigInteger.shiftRight(((i - i3) - 2) + b));
        BigInteger bigIntegerAdd = bigIntegerMultiply.add(bigInteger3.multiply(bigIntegerMultiply.shiftRight(i)));
        BigInteger bigIntegerShiftRight = bigIntegerAdd.shiftRight(i3 - i2);
        if (bigIntegerAdd.testBit((i3 - i2) - 1)) {
            bigIntegerShiftRight = bigIntegerShiftRight.add(ECConstants.ONE);
        }
        return new SimpleBigDecimal(bigIntegerShiftRight, i2);
    }

    public static byte[] tauAdicNaf(byte b, ZTauElement zTauElement) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        int iBitLength = norm(b, zTauElement).bitLength();
        byte[] bArr = new byte[iBitLength > 30 ? iBitLength + 4 : 34];
        int i = 0;
        int i2 = 0;
        BigInteger bigIntegerAdd = zTauElement.u;
        BigInteger bigIntegerNegate = zTauElement.v;
        while (true) {
            if (bigIntegerAdd.equals(ECConstants.ZERO) && bigIntegerNegate.equals(ECConstants.ZERO)) {
                int i3 = i2 + 1;
                byte[] bArr2 = new byte[i3];
                System.arraycopy(bArr, 0, bArr2, 0, i3);
                return bArr2;
            }
            if (bigIntegerAdd.testBit(0)) {
                bArr[i] = (byte) ECConstants.TWO.subtract(bigIntegerAdd.subtract(bigIntegerNegate.shiftLeft(1)).mod(ECConstants.FOUR)).intValue();
                bigIntegerAdd = bArr[i] == 1 ? bigIntegerAdd.clearBit(0) : bigIntegerAdd.add(ECConstants.ONE);
                i2 = i;
            } else {
                bArr[i] = 0;
            }
            BigInteger bigInteger = bigIntegerAdd;
            BigInteger bigIntegerShiftRight = bigIntegerAdd.shiftRight(1);
            bigIntegerAdd = b == 1 ? bigIntegerNegate.add(bigIntegerShiftRight) : bigIntegerNegate.subtract(bigIntegerShiftRight);
            bigIntegerNegate = bigInteger.shiftRight(1).negate();
            i++;
        }
    }

    public static ECPoint.F2m tau(ECPoint.F2m f2m) {
        if (f2m.isInfinity()) {
            return f2m;
        }
        return new ECPoint.F2m(f2m.getCurve(), f2m.getX().square(), f2m.getY().square(), f2m.isCompressed());
    }

    public static byte getMu(ECCurve.F2m f2m) {
        byte b;
        BigInteger bigInteger = f2m.getA().toBigInteger();
        if (bigInteger.equals(ECConstants.ZERO)) {
            b = -1;
        } else {
            if (!bigInteger.equals(ECConstants.ONE)) {
                throw new IllegalArgumentException("No Koblitz curve (ABC), TNAF multiplication not possible");
            }
            b = 1;
        }
        return b;
    }

    public static BigInteger[] getLucas(byte b, int i, boolean z) {
        BigInteger bigInteger;
        BigInteger bigIntegerValueOf;
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        if (z) {
            bigInteger = ECConstants.TWO;
            bigIntegerValueOf = BigInteger.valueOf(b);
        } else {
            bigInteger = ECConstants.ZERO;
            bigIntegerValueOf = ECConstants.ONE;
        }
        for (int i2 = 1; i2 < i; i2++) {
            BigInteger bigIntegerSubtract = (b == 1 ? bigIntegerValueOf : bigIntegerValueOf.negate()).subtract(bigInteger.shiftLeft(1));
            bigInteger = bigIntegerValueOf;
            bigIntegerValueOf = bigIntegerSubtract;
        }
        return new BigInteger[]{bigInteger, bigIntegerValueOf};
    }

    public static BigInteger getTw(byte b, int i) {
        if (i == 4) {
            return b == 1 ? BigInteger.valueOf(6L) : BigInteger.valueOf(10L);
        }
        BigInteger[] lucas = getLucas(b, i, false);
        BigInteger bit = ECConstants.ZERO.setBit(i);
        return ECConstants.TWO.multiply(lucas[0]).multiply(lucas[1].modInverse(bit)).mod(bit);
    }

    public static BigInteger[] getSi(ECCurve.F2m f2m) {
        BigInteger bigIntegerAdd;
        BigInteger bigIntegerAdd2;
        if (!f2m.isKoblitz()) {
            throw new IllegalArgumentException("si is defined for Koblitz curves only");
        }
        int m = f2m.getM();
        int iIntValue = f2m.getA().toBigInteger().intValue();
        byte mu = f2m.getMu();
        int iIntValue2 = f2m.getH().intValue();
        BigInteger[] lucas = getLucas(mu, (m + 3) - iIntValue, false);
        if (mu == 1) {
            bigIntegerAdd = ECConstants.ONE.subtract(lucas[1]);
            bigIntegerAdd2 = ECConstants.ONE.subtract(lucas[0]);
        } else {
            if (mu != -1) {
                throw new IllegalArgumentException("mu must be 1 or -1");
            }
            bigIntegerAdd = ECConstants.ONE.add(lucas[1]);
            bigIntegerAdd2 = ECConstants.ONE.add(lucas[0]);
        }
        BigInteger[] bigIntegerArr = new BigInteger[2];
        if (iIntValue2 == 2) {
            bigIntegerArr[0] = bigIntegerAdd.shiftRight(1);
            bigIntegerArr[1] = bigIntegerAdd2.shiftRight(1).negate();
        } else {
            if (iIntValue2 != 4) {
                throw new IllegalArgumentException("h (Cofactor) must be 2 or 4");
            }
            bigIntegerArr[0] = bigIntegerAdd.shiftRight(2);
            bigIntegerArr[1] = bigIntegerAdd2.shiftRight(2).negate();
        }
        return bigIntegerArr;
    }

    public static ZTauElement partModReduction(BigInteger bigInteger, int i, byte b, BigInteger[] bigIntegerArr, byte b2, byte b3) {
        BigInteger bigIntegerAdd = b2 == 1 ? bigIntegerArr[0].add(bigIntegerArr[1]) : bigIntegerArr[0].subtract(bigIntegerArr[1]);
        BigInteger bigInteger2 = getLucas(b2, i, true)[1];
        ZTauElement zTauElementRound = round(approximateDivisionByN(bigInteger, bigIntegerArr[0], bigInteger2, b, i, b3), approximateDivisionByN(bigInteger, bigIntegerArr[1], bigInteger2, b, i, b3), b2);
        return new ZTauElement(bigInteger.subtract(bigIntegerAdd.multiply(zTauElementRound.u)).subtract(BigInteger.valueOf(2L).multiply(bigIntegerArr[1]).multiply(zTauElementRound.v)), bigIntegerArr[1].multiply(zTauElementRound.u).subtract(bigIntegerArr[0].multiply(zTauElementRound.v)));
    }

    public static ECPoint.F2m multiplyRTnaf(ECPoint.F2m f2m, BigInteger bigInteger) {
        ECCurve.F2m f2m2 = (ECCurve.F2m) f2m.getCurve();
        return multiplyTnaf(f2m, partModReduction(bigInteger, f2m2.getM(), (byte) f2m2.getA().toBigInteger().intValue(), f2m2.getSi(), f2m2.getMu(), (byte) 10));
    }

    public static ECPoint.F2m multiplyTnaf(ECPoint.F2m f2m, ZTauElement zTauElement) {
        return multiplyFromTnaf(f2m, tauAdicNaf(((ECCurve.F2m) f2m.getCurve()).getMu(), zTauElement));
    }

    public static ECPoint.F2m multiplyFromTnaf(ECPoint.F2m f2m, byte[] bArr) {
        ECPoint.F2m f2mTau = (ECPoint.F2m) ((ECCurve.F2m) f2m.getCurve()).getInfinity();
        for (int length = bArr.length - 1; length >= 0; length--) {
            f2mTau = tau(f2mTau);
            if (bArr[length] == 1) {
                f2mTau = f2mTau.addSimple(f2m);
            } else if (bArr[length] == -1) {
                f2mTau = f2mTau.subtractSimple(f2m);
            }
        }
        return f2mTau;
    }

    public static byte[] tauAdicWNaf(byte b, ZTauElement zTauElement, byte b2, BigInteger bigInteger, BigInteger bigInteger2, ZTauElement[] zTauElementArr) {
        if (b != 1 && b != -1) {
            throw new IllegalArgumentException("mu must be 1 or -1");
        }
        int iBitLength = norm(b, zTauElement).bitLength();
        byte[] bArr = new byte[iBitLength > 30 ? iBitLength + 4 + b2 : 34 + b2];
        BigInteger bigIntegerShiftRight = bigInteger.shiftRight(1);
        BigInteger bigIntegerAdd = zTauElement.u;
        BigInteger bigIntegerNegate = zTauElement.v;
        int i = 0;
        while (true) {
            if (bigIntegerAdd.equals(ECConstants.ZERO) && bigIntegerNegate.equals(ECConstants.ZERO)) {
                return bArr;
            }
            if (bigIntegerAdd.testBit(0)) {
                BigInteger bigIntegerMod = bigIntegerAdd.add(bigIntegerNegate.multiply(bigInteger2)).mod(bigInteger);
                byte bIntValue = bigIntegerMod.compareTo(bigIntegerShiftRight) >= 0 ? (byte) bigIntegerMod.subtract(bigInteger).intValue() : (byte) bigIntegerMod.intValue();
                bArr[i] = bIntValue;
                boolean z = true;
                if (bIntValue < 0) {
                    z = false;
                    bIntValue = (byte) (-bIntValue);
                }
                if (z) {
                    bigIntegerAdd = bigIntegerAdd.subtract(zTauElementArr[bIntValue].u);
                    bigIntegerNegate = bigIntegerNegate.subtract(zTauElementArr[bIntValue].v);
                } else {
                    bigIntegerAdd = bigIntegerAdd.add(zTauElementArr[bIntValue].u);
                    bigIntegerNegate = bigIntegerNegate.add(zTauElementArr[bIntValue].v);
                }
            } else {
                bArr[i] = 0;
            }
            BigInteger bigInteger3 = bigIntegerAdd;
            bigIntegerAdd = b == 1 ? bigIntegerNegate.add(bigIntegerAdd.shiftRight(1)) : bigIntegerNegate.subtract(bigIntegerAdd.shiftRight(1));
            bigIntegerNegate = bigInteger3.shiftRight(1).negate();
            i++;
        }
    }

    public static ECPoint.F2m[] getPreComp(ECPoint.F2m f2m, byte b) {
        ECPoint.F2m[] f2mArr = new ECPoint.F2m[16];
        f2mArr[1] = f2m;
        byte[][] bArr = b == 0 ? alpha0Tnaf : alpha1Tnaf;
        int length = bArr.length;
        int i = 3;
        while (true) {
            int i2 = i;
            if (i2 >= length) {
                return f2mArr;
            }
            f2mArr[i2] = multiplyFromTnaf(f2m, bArr[i2]);
            i = i2 + 2;
        }
    }
}
