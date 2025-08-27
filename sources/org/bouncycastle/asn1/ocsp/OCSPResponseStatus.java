package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.DEREnumerated;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ocsp/OCSPResponseStatus.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ocsp/OCSPResponseStatus.class */
public class OCSPResponseStatus extends DEREnumerated {
    public static final int SUCCESSFUL = 0;
    public static final int MALFORMED_REQUEST = 1;
    public static final int INTERNAL_ERROR = 2;
    public static final int TRY_LATER = 3;
    public static final int SIG_REQUIRED = 5;
    public static final int UNAUTHORIZED = 6;

    public OCSPResponseStatus(int i) {
        super(i);
    }

    public OCSPResponseStatus(DEREnumerated dEREnumerated) {
        super(dEREnumerated.getValue().intValue());
    }
}
