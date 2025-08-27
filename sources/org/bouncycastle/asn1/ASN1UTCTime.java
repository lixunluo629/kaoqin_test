package org.bouncycastle.asn1;

import java.util.Date;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1UTCTime.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1UTCTime.class */
public class ASN1UTCTime extends DERUTCTime {
    ASN1UTCTime(byte[] bArr) {
        super(bArr);
    }

    public ASN1UTCTime(Date date) {
        super(date);
    }

    public ASN1UTCTime(String str) {
        super(str);
    }
}
