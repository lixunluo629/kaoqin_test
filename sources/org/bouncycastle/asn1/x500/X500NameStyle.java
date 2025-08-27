package org.bouncycastle.asn1.x500;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x500/X500NameStyle.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x500/X500NameStyle.class */
public interface X500NameStyle {
    ASN1Encodable stringToValue(ASN1ObjectIdentifier aSN1ObjectIdentifier, String str);

    ASN1ObjectIdentifier attrNameToOID(String str);

    boolean areEqual(X500Name x500Name, X500Name x500Name2);

    RDN[] fromString(String str);

    int calculateHashCode(X500Name x500Name);

    String toString(X500Name x500Name);
}
