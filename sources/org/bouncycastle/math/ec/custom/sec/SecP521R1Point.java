package org.bouncycastle.math.ec.custom.sec;

import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.raw.Nat;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/custom/sec/SecP521R1Point.class */
public class SecP521R1Point extends ECPoint.AbstractFp {
    SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        super(eCCurve, eCFieldElement, eCFieldElement2);
    }

    SecP521R1Point(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
        super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
    }

    protected ECPoint detach() {
        return new SecP521R1Point(null, getAffineXCoord(), getAffineYCoord());
    }

    @Override // org.bouncycastle.math.ec.ECPoint
    public ECPoint add(ECPoint eCPoint) {
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        int[] iArr4;
        if (isInfinity()) {
            return eCPoint;
        }
        if (eCPoint.isInfinity()) {
            return this;
        }
        if (this == eCPoint) {
            return twice();
        }
        ECCurve curve = getCurve();
        SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement) this.x;
        SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement) this.y;
        SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement) eCPoint.getXCoord();
        SecP521R1FieldElement secP521R1FieldElement4 = (SecP521R1FieldElement) eCPoint.getYCoord();
        SecP521R1FieldElement secP521R1FieldElement5 = (SecP521R1FieldElement) this.zs[0];
        SecP521R1FieldElement secP521R1FieldElement6 = (SecP521R1FieldElement) eCPoint.getZCoord(0);
        int[] iArrCreate = Nat.create(17);
        int[] iArrCreate2 = Nat.create(17);
        int[] iArrCreate3 = Nat.create(17);
        int[] iArrCreate4 = Nat.create(17);
        boolean zIsOne = secP521R1FieldElement5.isOne();
        if (zIsOne) {
            iArr2 = secP521R1FieldElement3.x;
            iArr = secP521R1FieldElement4.x;
        } else {
            iArr = iArrCreate3;
            SecP521R1Field.square(secP521R1FieldElement5.x, iArr);
            iArr2 = iArrCreate2;
            SecP521R1Field.multiply(iArr, secP521R1FieldElement3.x, iArr2);
            SecP521R1Field.multiply(iArr, secP521R1FieldElement5.x, iArr);
            SecP521R1Field.multiply(iArr, secP521R1FieldElement4.x, iArr);
        }
        boolean zIsOne2 = secP521R1FieldElement6.isOne();
        if (zIsOne2) {
            iArr4 = secP521R1FieldElement.x;
            iArr3 = secP521R1FieldElement2.x;
        } else {
            iArr3 = iArrCreate4;
            SecP521R1Field.square(secP521R1FieldElement6.x, iArr3);
            iArr4 = iArrCreate;
            SecP521R1Field.multiply(iArr3, secP521R1FieldElement.x, iArr4);
            SecP521R1Field.multiply(iArr3, secP521R1FieldElement6.x, iArr3);
            SecP521R1Field.multiply(iArr3, secP521R1FieldElement2.x, iArr3);
        }
        int[] iArrCreate5 = Nat.create(17);
        SecP521R1Field.subtract(iArr4, iArr2, iArrCreate5);
        SecP521R1Field.subtract(iArr3, iArr, iArrCreate2);
        if (Nat.isZero(17, iArrCreate5)) {
            return Nat.isZero(17, iArrCreate2) ? twice() : curve.getInfinity();
        }
        SecP521R1Field.square(iArrCreate5, iArrCreate3);
        int[] iArrCreate6 = Nat.create(17);
        SecP521R1Field.multiply(iArrCreate3, iArrCreate5, iArrCreate6);
        SecP521R1Field.multiply(iArrCreate3, iArr4, iArrCreate3);
        SecP521R1Field.multiply(iArr3, iArrCreate6, iArrCreate);
        SecP521R1FieldElement secP521R1FieldElement7 = new SecP521R1FieldElement(iArrCreate4);
        SecP521R1Field.square(iArrCreate2, secP521R1FieldElement7.x);
        SecP521R1Field.add(secP521R1FieldElement7.x, iArrCreate6, secP521R1FieldElement7.x);
        SecP521R1Field.subtract(secP521R1FieldElement7.x, iArrCreate3, secP521R1FieldElement7.x);
        SecP521R1Field.subtract(secP521R1FieldElement7.x, iArrCreate3, secP521R1FieldElement7.x);
        SecP521R1FieldElement secP521R1FieldElement8 = new SecP521R1FieldElement(iArrCreate6);
        SecP521R1Field.subtract(iArrCreate3, secP521R1FieldElement7.x, secP521R1FieldElement8.x);
        SecP521R1Field.multiply(secP521R1FieldElement8.x, iArrCreate2, iArrCreate2);
        SecP521R1Field.subtract(iArrCreate2, iArrCreate, secP521R1FieldElement8.x);
        SecP521R1FieldElement secP521R1FieldElement9 = new SecP521R1FieldElement(iArrCreate5);
        if (!zIsOne) {
            SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement5.x, secP521R1FieldElement9.x);
        }
        if (!zIsOne2) {
            SecP521R1Field.multiply(secP521R1FieldElement9.x, secP521R1FieldElement6.x, secP521R1FieldElement9.x);
        }
        return new SecP521R1Point(curve, secP521R1FieldElement7, secP521R1FieldElement8, new ECFieldElement[]{secP521R1FieldElement9});
    }

    @Override // org.bouncycastle.math.ec.ECPoint
    public ECPoint twice() {
        if (isInfinity()) {
            return this;
        }
        ECCurve curve = getCurve();
        SecP521R1FieldElement secP521R1FieldElement = (SecP521R1FieldElement) this.y;
        if (secP521R1FieldElement.isZero()) {
            return curve.getInfinity();
        }
        SecP521R1FieldElement secP521R1FieldElement2 = (SecP521R1FieldElement) this.x;
        SecP521R1FieldElement secP521R1FieldElement3 = (SecP521R1FieldElement) this.zs[0];
        int[] iArrCreate = Nat.create(17);
        int[] iArrCreate2 = Nat.create(17);
        int[] iArrCreate3 = Nat.create(17);
        SecP521R1Field.square(secP521R1FieldElement.x, iArrCreate3);
        int[] iArrCreate4 = Nat.create(17);
        SecP521R1Field.square(iArrCreate3, iArrCreate4);
        boolean zIsOne = secP521R1FieldElement3.isOne();
        int[] iArr = secP521R1FieldElement3.x;
        if (!zIsOne) {
            iArr = iArrCreate2;
            SecP521R1Field.square(secP521R1FieldElement3.x, iArr);
        }
        SecP521R1Field.subtract(secP521R1FieldElement2.x, iArr, iArrCreate);
        SecP521R1Field.add(secP521R1FieldElement2.x, iArr, iArrCreate2);
        SecP521R1Field.multiply(iArrCreate2, iArrCreate, iArrCreate2);
        Nat.addBothTo(17, iArrCreate2, iArrCreate2, iArrCreate2);
        SecP521R1Field.reduce23(iArrCreate2);
        SecP521R1Field.multiply(iArrCreate3, secP521R1FieldElement2.x, iArrCreate3);
        Nat.shiftUpBits(17, iArrCreate3, 2, 0);
        SecP521R1Field.reduce23(iArrCreate3);
        Nat.shiftUpBits(17, iArrCreate4, 3, 0, iArrCreate);
        SecP521R1Field.reduce23(iArrCreate);
        SecP521R1FieldElement secP521R1FieldElement4 = new SecP521R1FieldElement(iArrCreate4);
        SecP521R1Field.square(iArrCreate2, secP521R1FieldElement4.x);
        SecP521R1Field.subtract(secP521R1FieldElement4.x, iArrCreate3, secP521R1FieldElement4.x);
        SecP521R1Field.subtract(secP521R1FieldElement4.x, iArrCreate3, secP521R1FieldElement4.x);
        SecP521R1FieldElement secP521R1FieldElement5 = new SecP521R1FieldElement(iArrCreate3);
        SecP521R1Field.subtract(iArrCreate3, secP521R1FieldElement4.x, secP521R1FieldElement5.x);
        SecP521R1Field.multiply(secP521R1FieldElement5.x, iArrCreate2, secP521R1FieldElement5.x);
        SecP521R1Field.subtract(secP521R1FieldElement5.x, iArrCreate, secP521R1FieldElement5.x);
        SecP521R1FieldElement secP521R1FieldElement6 = new SecP521R1FieldElement(iArrCreate2);
        SecP521R1Field.twice(secP521R1FieldElement.x, secP521R1FieldElement6.x);
        if (!zIsOne) {
            SecP521R1Field.multiply(secP521R1FieldElement6.x, secP521R1FieldElement3.x, secP521R1FieldElement6.x);
        }
        return new SecP521R1Point(curve, secP521R1FieldElement4, secP521R1FieldElement5, new ECFieldElement[]{secP521R1FieldElement6});
    }

    public ECPoint twicePlus(ECPoint eCPoint) {
        return this == eCPoint ? threeTimes() : isInfinity() ? eCPoint : eCPoint.isInfinity() ? twice() : this.y.isZero() ? eCPoint : twice().add(eCPoint);
    }

    public ECPoint threeTimes() {
        return (isInfinity() || this.y.isZero()) ? this : twice().add(this);
    }

    protected ECFieldElement two(ECFieldElement eCFieldElement) {
        return eCFieldElement.add(eCFieldElement);
    }

    protected ECFieldElement three(ECFieldElement eCFieldElement) {
        return two(eCFieldElement).add(eCFieldElement);
    }

    protected ECFieldElement four(ECFieldElement eCFieldElement) {
        return two(two(eCFieldElement));
    }

    protected ECFieldElement eight(ECFieldElement eCFieldElement) {
        return four(two(eCFieldElement));
    }

    protected ECFieldElement doubleProductFromSquares(ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement eCFieldElement3, ECFieldElement eCFieldElement4) {
        return eCFieldElement.add(eCFieldElement2).square().subtract(eCFieldElement3).subtract(eCFieldElement4);
    }

    @Override // org.bouncycastle.math.ec.ECPoint
    public ECPoint negate() {
        return isInfinity() ? this : new SecP521R1Point(this.curve, this.x, this.y.negate(), this.zs);
    }
}
