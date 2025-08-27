package org.bouncycastle.asn1;

import ch.qos.logback.core.joran.action.ActionConst;
import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1Null.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1Null.class */
public abstract class ASN1Null extends ASN1Object {
    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject, org.bouncycastle.asn1.ASN1Encodable
    public int hashCode() {
        return -1;
    }

    @Override // org.bouncycastle.asn1.ASN1Object
    boolean asn1Equals(DERObject dERObject) {
        return dERObject instanceof ASN1Null;
    }

    @Override // org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    abstract void encode(DEROutputStream dEROutputStream) throws IOException;

    public String toString() {
        return ActionConst.NULL;
    }
}
