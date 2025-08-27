package org.bouncycastle.asn1.iana;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/iana/IANAObjectIdentifiers.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/iana/IANAObjectIdentifiers.class */
public interface IANAObjectIdentifiers {
    public static final ASN1ObjectIdentifier isakmpOakley = new ASN1ObjectIdentifier("1.3.6.1.5.5.8.1");
    public static final ASN1ObjectIdentifier hmacMD5 = new ASN1ObjectIdentifier(isakmpOakley + ".1");
    public static final ASN1ObjectIdentifier hmacSHA1 = new ASN1ObjectIdentifier(isakmpOakley + ".2");
    public static final ASN1ObjectIdentifier hmacTIGER = new ASN1ObjectIdentifier(isakmpOakley + ".3");
    public static final ASN1ObjectIdentifier hmacRIPEMD160 = new ASN1ObjectIdentifier(isakmpOakley + ".4");
}
