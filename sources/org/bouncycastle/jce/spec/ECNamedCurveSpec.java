package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECFieldFp;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;
import org.bouncycastle.math.ec.ECCurve;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/spec/ECNamedCurveSpec.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/spec/ECNamedCurveSpec.class */
public class ECNamedCurveSpec extends java.security.spec.ECParameterSpec {
    private String name;

    private static EllipticCurve convertCurve(ECCurve eCCurve, byte[] bArr) {
        if (eCCurve instanceof ECCurve.Fp) {
            return new EllipticCurve(new ECFieldFp(((ECCurve.Fp) eCCurve).getQ()), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), bArr);
        }
        ECCurve.F2m f2m = (ECCurve.F2m) eCCurve;
        return f2m.isTrinomial() ? new EllipticCurve(new ECFieldF2m(f2m.getM(), new int[]{f2m.getK1()}), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), bArr) : new EllipticCurve(new ECFieldF2m(f2m.getM(), new int[]{f2m.getK3(), f2m.getK2(), f2m.getK1()}), eCCurve.getA().toBigInteger(), eCCurve.getB().toBigInteger(), bArr);
    }

    private static ECPoint convertPoint(org.bouncycastle.math.ec.ECPoint eCPoint) {
        return new ECPoint(eCPoint.getX().toBigInteger(), eCPoint.getY().toBigInteger());
    }

    public ECNamedCurveSpec(String str, ECCurve eCCurve, org.bouncycastle.math.ec.ECPoint eCPoint, BigInteger bigInteger) {
        super(convertCurve(eCCurve, null), convertPoint(eCPoint), bigInteger, 1);
        this.name = str;
    }

    public ECNamedCurveSpec(String str, EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger) {
        super(ellipticCurve, eCPoint, bigInteger, 1);
        this.name = str;
    }

    public ECNamedCurveSpec(String str, ECCurve eCCurve, org.bouncycastle.math.ec.ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        super(convertCurve(eCCurve, null), convertPoint(eCPoint), bigInteger, bigInteger2.intValue());
        this.name = str;
    }

    public ECNamedCurveSpec(String str, EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2) {
        super(ellipticCurve, eCPoint, bigInteger, bigInteger2.intValue());
        this.name = str;
    }

    public ECNamedCurveSpec(String str, ECCurve eCCurve, org.bouncycastle.math.ec.ECPoint eCPoint, BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr) {
        super(convertCurve(eCCurve, bArr), convertPoint(eCPoint), bigInteger, bigInteger2.intValue());
        this.name = str;
    }

    public String getName() {
        return this.name;
    }
}
