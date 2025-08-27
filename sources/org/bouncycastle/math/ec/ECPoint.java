package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECPoint.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ECPoint.class */
public abstract class ECPoint {
    ECCurve curve;
    ECFieldElement x;
    ECFieldElement y;
    protected boolean withCompression;
    protected ECMultiplier multiplier = null;
    protected PreCompInfo preCompInfo = null;
    private static X9IntegerConverter converter = new X9IntegerConverter();

    /* renamed from: org.bouncycastle.math.ec.ECPoint$1, reason: invalid class name */
    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECPoint$1.class */
    class AnonymousClass1 implements PreCompCallback {
        final /* synthetic */ boolean val$decompressed;
        final /* synthetic */ boolean val$checkOrder;

        AnonymousClass1(boolean z, boolean z2) {
            this.val$decompressed = z;
            this.val$checkOrder = z2;
        }

        @Override // org.bouncycastle.math.ec.PreCompCallback
        public PreCompInfo precompute(PreCompInfo preCompInfo) {
            ValidityPrecompInfo validityPrecompInfo = preCompInfo instanceof ValidityPrecompInfo ? (ValidityPrecompInfo) preCompInfo : null;
            if (validityPrecompInfo == null) {
                validityPrecompInfo = new ValidityPrecompInfo();
            }
            if (validityPrecompInfo.hasFailed()) {
                return validityPrecompInfo;
            }
            if (!validityPrecompInfo.hasCurveEquationPassed()) {
                if (!this.val$decompressed && !ECPoint.this.satisfiesCurveEquation()) {
                    validityPrecompInfo.reportFailed();
                    return validityPrecompInfo;
                }
                validityPrecompInfo.reportCurveEquationPassed();
            }
            if (this.val$checkOrder && !validityPrecompInfo.hasOrderPassed()) {
                if (!ECPoint.this.satisfiesOrder()) {
                    validityPrecompInfo.reportFailed();
                    return validityPrecompInfo;
                }
                validityPrecompInfo.reportOrderPassed();
            }
            return validityPrecompInfo;
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECPoint$AbstractF2m.class */
    public static abstract class AbstractF2m extends ECPoint {
        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractF2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        }

        protected boolean satisfiesCurveEquation() {
            ECFieldElement eCFieldElementMultiplyPlusProduct;
            ECFieldElement eCFieldElementSquarePlusProduct;
            ECCurve curve = getCurve();
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement a = curve.getA();
            ECFieldElement b = curve.getB();
            int coordinateSystem = curve.getCoordinateSystem();
            if (coordinateSystem != 6) {
                ECFieldElement eCFieldElement2 = this.y;
                ECFieldElement eCFieldElementMultiply = eCFieldElement2.add(eCFieldElement).multiply(eCFieldElement2);
                switch (coordinateSystem) {
                    case 0:
                        break;
                    case 1:
                        ECFieldElement eCFieldElement3 = this.zs[0];
                        if (!eCFieldElement3.isOne()) {
                            ECFieldElement eCFieldElementMultiply2 = eCFieldElement3.multiply(eCFieldElement3.square());
                            eCFieldElementMultiply = eCFieldElementMultiply.multiply(eCFieldElement3);
                            a = a.multiply(eCFieldElement3);
                            b = b.multiply(eCFieldElementMultiply2);
                            break;
                        }
                        break;
                    default:
                        throw new IllegalStateException("unsupported coordinate system");
                }
                return eCFieldElementMultiply.equals(eCFieldElement.add(a).multiply(eCFieldElement.square()).add(b));
            }
            ECFieldElement eCFieldElement4 = this.zs[0];
            boolean zIsOne = eCFieldElement4.isOne();
            if (eCFieldElement.isZero()) {
                ECFieldElement eCFieldElementSquare = this.y.square();
                ECFieldElement eCFieldElementMultiply3 = b;
                if (!zIsOne) {
                    eCFieldElementMultiply3 = eCFieldElementMultiply3.multiply(eCFieldElement4.square());
                }
                return eCFieldElementSquare.equals(eCFieldElementMultiply3);
            }
            ECFieldElement eCFieldElement5 = this.y;
            ECFieldElement eCFieldElementSquare2 = eCFieldElement.square();
            if (zIsOne) {
                eCFieldElementMultiplyPlusProduct = eCFieldElement5.square().add(eCFieldElement5).add(a);
                eCFieldElementSquarePlusProduct = eCFieldElementSquare2.square().add(b);
            } else {
                ECFieldElement eCFieldElementSquare3 = eCFieldElement4.square();
                ECFieldElement eCFieldElementSquare4 = eCFieldElementSquare3.square();
                eCFieldElementMultiplyPlusProduct = eCFieldElement5.add(eCFieldElement4).multiplyPlusProduct(eCFieldElement5, a, eCFieldElementSquare3);
                eCFieldElementSquarePlusProduct = eCFieldElementSquare2.squarePlusProduct(b, eCFieldElementSquare4);
            }
            return eCFieldElementMultiplyPlusProduct.multiply(eCFieldElementSquare2).equals(eCFieldElementSquarePlusProduct);
        }

        protected boolean satisfiesOrder() {
            BigInteger cofactor = this.curve.getCofactor();
            if (ECConstants.TWO.equals(cofactor)) {
                return 0 != ((ECFieldElement.AbstractF2m) normalize().getAffineXCoord()).trace();
            }
            if (!ECConstants.FOUR.equals(cofactor)) {
                return super.satisfiesOrder();
            }
            ECPoint eCPointNormalize = normalize();
            ECFieldElement affineXCoord = eCPointNormalize.getAffineXCoord();
            ECFieldElement eCFieldElementSolveQuadraticEquation = ((ECCurve.AbstractF2m) this.curve).solveQuadraticEquation(affineXCoord.add(this.curve.getA()));
            if (null == eCFieldElementSolveQuadraticEquation) {
                return false;
            }
            return 0 == ((ECFieldElement.AbstractF2m) affineXCoord.multiply(eCFieldElementSolveQuadraticEquation).add(eCPointNormalize.getAffineYCoord())).trace();
        }

        public ECPoint scaleX(ECFieldElement eCFieldElement) {
            if (isInfinity()) {
                return this;
            }
            switch (getCurveCoordinateSystem()) {
                case 5:
                    ECFieldElement rawXCoord = getRawXCoord();
                    return getCurve().createRawPoint(rawXCoord, getRawYCoord().add(rawXCoord).divide(eCFieldElement).add(rawXCoord.multiply(eCFieldElement)), getRawZCoords());
                case 6:
                    ECFieldElement rawXCoord2 = getRawXCoord();
                    ECFieldElement rawYCoord = getRawYCoord();
                    ECFieldElement eCFieldElement2 = getRawZCoords()[0];
                    ECFieldElement eCFieldElementMultiply = rawXCoord2.multiply(eCFieldElement.square());
                    return getCurve().createRawPoint(eCFieldElementMultiply, rawYCoord.add(rawXCoord2).add(eCFieldElementMultiply), new ECFieldElement[]{eCFieldElement2.multiply(eCFieldElement)});
                default:
                    return super.scaleX(eCFieldElement);
            }
        }

        public ECPoint scaleXNegateY(ECFieldElement eCFieldElement) {
            return scaleX(eCFieldElement);
        }

        public ECPoint scaleY(ECFieldElement eCFieldElement) {
            if (isInfinity()) {
                return this;
            }
            switch (getCurveCoordinateSystem()) {
                case 5:
                case 6:
                    ECFieldElement rawXCoord = getRawXCoord();
                    return getCurve().createRawPoint(rawXCoord, getRawYCoord().add(rawXCoord).multiply(eCFieldElement).add(rawXCoord), getRawZCoords());
                default:
                    return super.scaleY(eCFieldElement);
            }
        }

        public ECPoint scaleYNegateX(ECFieldElement eCFieldElement) {
            return scaleY(eCFieldElement);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }

        public AbstractF2m tau() {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            switch (coordinateSystem) {
                case 0:
                case 5:
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.square(), this.y.square());
                case 1:
                case 6:
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.square(), this.y.square(), new ECFieldElement[]{this.zs[0].square()});
                case 2:
                case 3:
                case 4:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }

        public AbstractF2m tauPow(int i) {
            if (isInfinity()) {
                return this;
            }
            ECCurve curve = getCurve();
            int coordinateSystem = curve.getCoordinateSystem();
            ECFieldElement eCFieldElement = this.x;
            switch (coordinateSystem) {
                case 0:
                case 5:
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.squarePow(i), this.y.squarePow(i));
                case 1:
                case 6:
                    return (AbstractF2m) curve.createRawPoint(eCFieldElement.squarePow(i), this.y.squarePow(i), new ECFieldElement[]{this.zs[0].squarePow(i)});
                case 2:
                case 3:
                case 4:
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
        }
    }

    /* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECPoint$AbstractFp.class */
    public static abstract class AbstractFp extends ECPoint {
        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
        }

        protected AbstractFp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, ECFieldElement[] eCFieldElementArr) {
            super(eCCurve, eCFieldElement, eCFieldElement2, eCFieldElementArr);
        }

        protected boolean getCompressionYTilde() {
            return getAffineYCoord().testBitZero();
        }

        protected boolean satisfiesCurveEquation() {
            ECFieldElement eCFieldElement = this.x;
            ECFieldElement eCFieldElement2 = this.y;
            ECFieldElement a = this.curve.getA();
            ECFieldElement b = this.curve.getB();
            ECFieldElement eCFieldElementSquare = eCFieldElement2.square();
            switch (getCurveCoordinateSystem()) {
                case 0:
                    break;
                case 1:
                    ECFieldElement eCFieldElement3 = this.zs[0];
                    if (!eCFieldElement3.isOne()) {
                        ECFieldElement eCFieldElementSquare2 = eCFieldElement3.square();
                        ECFieldElement eCFieldElementMultiply = eCFieldElement3.multiply(eCFieldElementSquare2);
                        eCFieldElementSquare = eCFieldElementSquare.multiply(eCFieldElement3);
                        a = a.multiply(eCFieldElementSquare2);
                        b = b.multiply(eCFieldElementMultiply);
                        break;
                    }
                    break;
                case 2:
                case 3:
                case 4:
                    ECFieldElement eCFieldElement4 = this.zs[0];
                    if (!eCFieldElement4.isOne()) {
                        ECFieldElement eCFieldElementSquare3 = eCFieldElement4.square();
                        ECFieldElement eCFieldElementSquare4 = eCFieldElementSquare3.square();
                        ECFieldElement eCFieldElementMultiply2 = eCFieldElementSquare3.multiply(eCFieldElementSquare4);
                        a = a.multiply(eCFieldElementSquare4);
                        b = b.multiply(eCFieldElementMultiply2);
                        break;
                    }
                    break;
                default:
                    throw new IllegalStateException("unsupported coordinate system");
            }
            return eCFieldElementSquare.equals(eCFieldElement.square().add(a).multiply(eCFieldElement).add(b));
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECPoint$F2m.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ECPoint$F2m.class */
    public static class F2m extends ECPoint {
        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        public F2m(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
            if ((eCFieldElement != null && eCFieldElement2 == null) || (eCFieldElement == null && eCFieldElement2 != null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            if (eCFieldElement != null) {
                ECFieldElement.F2m.checkFieldElements(this.x, this.y);
                if (eCCurve != null) {
                    ECFieldElement.F2m.checkFieldElements(this.x, this.curve.getA());
                }
            }
            this.withCompression = z;
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public byte[] getEncoded() {
            byte[] bArr;
            if (isInfinity()) {
                return new byte[1];
            }
            int byteLength = ECPoint.converter.getByteLength(this.x);
            byte[] bArrIntegerToBytes = ECPoint.converter.integerToBytes(getX().toBigInteger(), byteLength);
            if (this.withCompression) {
                bArr = new byte[byteLength + 1];
                bArr[0] = 2;
                if (!getX().toBigInteger().equals(ECConstants.ZERO) && getY().multiply(getX().invert()).toBigInteger().testBit(0)) {
                    bArr[0] = 3;
                }
                System.arraycopy(bArrIntegerToBytes, 0, bArr, 1, byteLength);
            } else {
                byte[] bArrIntegerToBytes2 = ECPoint.converter.integerToBytes(getY().toBigInteger(), byteLength);
                bArr = new byte[byteLength + byteLength + 1];
                bArr[0] = 4;
                System.arraycopy(bArrIntegerToBytes, 0, bArr, 1, byteLength);
                System.arraycopy(bArrIntegerToBytes2, 0, bArr, byteLength + 1, byteLength);
            }
            return bArr;
        }

        private static void checkPoints(ECPoint eCPoint, ECPoint eCPoint2) {
            if (!eCPoint.curve.equals(eCPoint2.curve)) {
                throw new IllegalArgumentException("Only points on the same curve can be added or subtracted");
            }
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint add(ECPoint eCPoint) {
            checkPoints(this, eCPoint);
            return addSimple((F2m) eCPoint);
        }

        public F2m addSimple(F2m f2m) {
            if (isInfinity()) {
                return f2m;
            }
            if (f2m.isInfinity()) {
                return this;
            }
            ECFieldElement.F2m f2m2 = (ECFieldElement.F2m) f2m.getX();
            ECFieldElement.F2m f2m3 = (ECFieldElement.F2m) f2m.getY();
            if (this.x.equals(f2m2)) {
                return this.y.equals(f2m3) ? (F2m) twice() : (F2m) this.curve.getInfinity();
            }
            ECFieldElement eCFieldElement = (ECFieldElement.F2m) this.y.add(f2m3).divide(this.x.add(f2m2));
            ECFieldElement.F2m f2m4 = (ECFieldElement.F2m) eCFieldElement.square().add(eCFieldElement).add(this.x).add(f2m2).add(this.curve.getA());
            return new F2m(this.curve, f2m4, (ECFieldElement.F2m) eCFieldElement.multiply(this.x.add(f2m4)).add(f2m4).add(this.y), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint subtract(ECPoint eCPoint) {
            checkPoints(this, eCPoint);
            return subtractSimple((F2m) eCPoint);
        }

        public F2m subtractSimple(F2m f2m) {
            return f2m.isInfinity() ? this : addSimple((F2m) f2m.negate());
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint twice() {
            if (isInfinity()) {
                return this;
            }
            if (this.x.toBigInteger().signum() == 0) {
                return this.curve.getInfinity();
            }
            ECFieldElement eCFieldElement = (ECFieldElement.F2m) this.x.add(this.y.divide(this.x));
            ECFieldElement.F2m f2m = (ECFieldElement.F2m) eCFieldElement.square().add(eCFieldElement).add(this.curve.getA());
            return new F2m(this.curve, f2m, (ECFieldElement.F2m) this.x.square().add(f2m.multiply(eCFieldElement.add(this.curve.fromBigInteger(ECConstants.ONE)))), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint negate() {
            return new F2m(this.curve, getX(), getY().add(getX()), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        synchronized void assertECMultiplier() {
            if (this.multiplier == null) {
                if (((ECCurve.F2m) this.curve).isKoblitz()) {
                    this.multiplier = new WTauNafMultiplier();
                } else {
                    this.multiplier = new WNafMultiplier();
                }
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/math/ec/ECPoint$Fp.class
 */
    /* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/math/ec/ECPoint$Fp.class */
    public static class Fp extends ECPoint {
        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
            this(eCCurve, eCFieldElement, eCFieldElement2, false);
        }

        public Fp(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2, boolean z) {
            super(eCCurve, eCFieldElement, eCFieldElement2);
            if ((eCFieldElement != null && eCFieldElement2 == null) || (eCFieldElement == null && eCFieldElement2 != null)) {
                throw new IllegalArgumentException("Exactly one of the field elements is null");
            }
            this.withCompression = z;
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public byte[] getEncoded() {
            if (isInfinity()) {
                return new byte[1];
            }
            int byteLength = ECPoint.converter.getByteLength(this.x);
            if (this.withCompression) {
                byte b = getY().toBigInteger().testBit(0) ? (byte) 3 : (byte) 2;
                byte[] bArrIntegerToBytes = ECPoint.converter.integerToBytes(getX().toBigInteger(), byteLength);
                byte[] bArr = new byte[bArrIntegerToBytes.length + 1];
                bArr[0] = b;
                System.arraycopy(bArrIntegerToBytes, 0, bArr, 1, bArrIntegerToBytes.length);
                return bArr;
            }
            byte[] bArrIntegerToBytes2 = ECPoint.converter.integerToBytes(getX().toBigInteger(), byteLength);
            byte[] bArrIntegerToBytes3 = ECPoint.converter.integerToBytes(getY().toBigInteger(), byteLength);
            byte[] bArr2 = new byte[bArrIntegerToBytes2.length + bArrIntegerToBytes3.length + 1];
            bArr2[0] = 4;
            System.arraycopy(bArrIntegerToBytes2, 0, bArr2, 1, bArrIntegerToBytes2.length);
            System.arraycopy(bArrIntegerToBytes3, 0, bArr2, bArrIntegerToBytes2.length + 1, bArrIntegerToBytes3.length);
            return bArr2;
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint add(ECPoint eCPoint) {
            if (isInfinity()) {
                return eCPoint;
            }
            if (eCPoint.isInfinity()) {
                return this;
            }
            if (this.x.equals(eCPoint.x)) {
                return this.y.equals(eCPoint.y) ? twice() : this.curve.getInfinity();
            }
            ECFieldElement eCFieldElementDivide = eCPoint.y.subtract(this.y).divide(eCPoint.x.subtract(this.x));
            ECFieldElement eCFieldElementSubtract = eCFieldElementDivide.square().subtract(this.x).subtract(eCPoint.x);
            return new Fp(this.curve, eCFieldElementSubtract, eCFieldElementDivide.multiply(this.x.subtract(eCFieldElementSubtract)).subtract(this.y));
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint twice() {
            if (isInfinity()) {
                return this;
            }
            if (this.y.toBigInteger().signum() == 0) {
                return this.curve.getInfinity();
            }
            ECFieldElement eCFieldElementFromBigInteger = this.curve.fromBigInteger(BigInteger.valueOf(2L));
            ECFieldElement eCFieldElementDivide = this.x.square().multiply(this.curve.fromBigInteger(BigInteger.valueOf(3L))).add(this.curve.a).divide(this.y.multiply(eCFieldElementFromBigInteger));
            ECFieldElement eCFieldElementSubtract = eCFieldElementDivide.square().subtract(this.x.multiply(eCFieldElementFromBigInteger));
            return new Fp(this.curve, eCFieldElementSubtract, eCFieldElementDivide.multiply(this.x.subtract(eCFieldElementSubtract)).subtract(this.y), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint subtract(ECPoint eCPoint) {
            return eCPoint.isInfinity() ? this : add(eCPoint.negate());
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        public ECPoint negate() {
            return new Fp(this.curve, this.x, this.y.negate(), this.withCompression);
        }

        @Override // org.bouncycastle.math.ec.ECPoint
        synchronized void assertECMultiplier() {
            if (this.multiplier == null) {
                this.multiplier = new WNafMultiplier();
            }
        }
    }

    protected ECPoint(ECCurve eCCurve, ECFieldElement eCFieldElement, ECFieldElement eCFieldElement2) {
        this.curve = eCCurve;
        this.x = eCFieldElement;
        this.y = eCFieldElement2;
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public ECFieldElement getX() {
        return this.x;
    }

    public ECFieldElement getY() {
        return this.y;
    }

    public boolean isInfinity() {
        return this.x == null && this.y == null;
    }

    public boolean isCompressed() {
        return this.withCompression;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ECPoint)) {
            return false;
        }
        ECPoint eCPoint = (ECPoint) obj;
        return isInfinity() ? eCPoint.isInfinity() : this.x.equals(eCPoint.x) && this.y.equals(eCPoint.y);
    }

    public int hashCode() {
        if (isInfinity()) {
            return 0;
        }
        return this.x.hashCode() ^ this.y.hashCode();
    }

    void setPreCompInfo(PreCompInfo preCompInfo) {
        this.preCompInfo = preCompInfo;
    }

    public abstract byte[] getEncoded();

    public abstract ECPoint add(ECPoint eCPoint);

    public abstract ECPoint subtract(ECPoint eCPoint);

    public abstract ECPoint negate();

    public abstract ECPoint twice();

    synchronized void assertECMultiplier() {
        if (this.multiplier == null) {
            this.multiplier = new FpNafMultiplier();
        }
    }

    public ECPoint multiply(BigInteger bigInteger) {
        if (bigInteger.signum() < 0) {
            throw new IllegalArgumentException("The multiplicator cannot be negative");
        }
        if (isInfinity()) {
            return this;
        }
        if (bigInteger.signum() == 0) {
            return this.curve.getInfinity();
        }
        assertECMultiplier();
        return this.multiplier.multiply(this, bigInteger, this.preCompInfo);
    }
}
