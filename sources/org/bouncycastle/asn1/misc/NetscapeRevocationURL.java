package org.bouncycastle.asn1.misc;

import org.bouncycastle.asn1.DERIA5String;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/misc/NetscapeRevocationURL.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/misc/NetscapeRevocationURL.class */
public class NetscapeRevocationURL extends DERIA5String {
    public NetscapeRevocationURL(DERIA5String dERIA5String) {
        super(dERIA5String.getString());
    }

    @Override // org.bouncycastle.asn1.DERIA5String
    public String toString() {
        return "NetscapeRevocationURL: " + getString();
    }
}
