package org.bouncycastle.asn1.x509;

import java.math.BigInteger;
import org.bouncycastle.asn1.DERInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/CRLNumber.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/CRLNumber.class */
public class CRLNumber extends DERInteger {
    public CRLNumber(BigInteger bigInteger) {
        super(bigInteger);
    }

    public BigInteger getCRLNumber() {
        return getPositiveValue();
    }

    @Override // org.bouncycastle.asn1.DERInteger
    public String toString() {
        return "CRLNumber: " + getCRLNumber();
    }
}
