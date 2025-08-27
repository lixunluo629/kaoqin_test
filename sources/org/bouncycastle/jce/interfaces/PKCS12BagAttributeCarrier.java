package org.bouncycastle.jce.interfaces;

import java.util.Enumeration;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/jce/interfaces/PKCS12BagAttributeCarrier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/jce/interfaces/PKCS12BagAttributeCarrier.class */
public interface PKCS12BagAttributeCarrier {
    void setBagAttribute(DERObjectIdentifier dERObjectIdentifier, DEREncodable dEREncodable);

    DEREncodable getBagAttribute(DERObjectIdentifier dERObjectIdentifier);

    Enumeration getBagAttributeKeys();
}
