package org.bouncycastle.asn1;

import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Enumerated.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1Enumerated.class */
public class ASN1Enumerated extends DEREnumerated {
    ASN1Enumerated(byte[] bArr) {
        super(bArr);
    }

    public ASN1Enumerated(BigInteger bigInteger) {
        super(bigInteger);
    }

    public ASN1Enumerated(int i) {
        super(i);
    }
}
