package org.bouncycastle.asn1.ua;

import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.math.ec.ECPoint;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ua/DSTU4145PublicKey.class */
public class DSTU4145PublicKey extends ASN1Object {
    private ASN1OctetString pubKey;

    public DSTU4145PublicKey(ECPoint eCPoint) {
        this.pubKey = new DEROctetString(DSTU4145PointEncoder.encodePoint(eCPoint));
    }

    private DSTU4145PublicKey(ASN1OctetString aSN1OctetString) {
        this.pubKey = aSN1OctetString;
    }

    public static DSTU4145PublicKey getInstance(Object obj) {
        if (obj instanceof DSTU4145PublicKey) {
            return (DSTU4145PublicKey) obj;
        }
        if (obj != null) {
            return new DSTU4145PublicKey(ASN1OctetString.getInstance(obj));
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1OctetString, org.bouncycastle.asn1.ASN1Primitive] */
    public ASN1Primitive toASN1Primitive() {
        return this.pubKey;
    }
}
