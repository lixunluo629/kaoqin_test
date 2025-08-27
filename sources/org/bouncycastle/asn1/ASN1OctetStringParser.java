package org.bouncycastle.asn1;

import java.io.InputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1OctetStringParser.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1OctetStringParser.class */
public interface ASN1OctetStringParser extends DEREncodable, InMemoryRepresentable {
    InputStream getOctetStream();
}
