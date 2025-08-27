package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x9/X9ECPoint.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x9/X9ECPoint.class */
public class X9ECPoint extends ASN1Encodable {
    ECPoint p;

    public X9ECPoint(ECPoint eCPoint) {
        this.p = eCPoint;
    }

    public X9ECPoint(ECCurve eCCurve, ASN1OctetString aSN1OctetString) {
        this.p = eCCurve.decodePoint(aSN1OctetString.getOctets());
    }

    public ECPoint getPoint() {
        return this.p;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return new DEROctetString(this.p.getEncoded());
    }
}
