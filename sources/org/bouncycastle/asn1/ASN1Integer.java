package org.bouncycastle.asn1;

import java.math.BigInteger;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Integer.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1Integer.class */
public class ASN1Integer extends DERInteger {
    /* JADX INFO: Access modifiers changed from: package-private */
    public ASN1Integer(byte[] bArr) {
        super(bArr);
    }

    public ASN1Integer(BigInteger bigInteger) {
        super(bigInteger);
    }

    public ASN1Integer(int i) {
        super(i);
    }
}
