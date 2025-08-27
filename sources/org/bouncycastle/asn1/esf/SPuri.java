package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.DERIA5String;
import org.bouncycastle.asn1.DERObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/SPuri.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/SPuri.class */
public class SPuri {
    private DERIA5String uri;

    public static SPuri getInstance(Object obj) {
        if (obj instanceof SPuri) {
            return (SPuri) obj;
        }
        if (obj instanceof DERIA5String) {
            return new SPuri((DERIA5String) obj);
        }
        throw new IllegalArgumentException("unknown object in 'SPuri' factory: " + obj.getClass().getName() + ".");
    }

    public SPuri(DERIA5String dERIA5String) {
        this.uri = dERIA5String;
    }

    public DERIA5String getUri() {
        return this.uri;
    }

    public DERObject toASN1Object() {
        return this.uri.getDERObject();
    }
}
