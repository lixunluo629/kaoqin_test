package org.bouncycastle.asn1;

import java.util.Date;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1GeneralizedTime.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1GeneralizedTime.class */
public class ASN1GeneralizedTime extends DERGeneralizedTime {
    ASN1GeneralizedTime(byte[] bArr) {
        super(bArr);
    }

    public ASN1GeneralizedTime(Date date) {
        super(date);
    }

    public ASN1GeneralizedTime(String str) {
        super(str);
    }
}
