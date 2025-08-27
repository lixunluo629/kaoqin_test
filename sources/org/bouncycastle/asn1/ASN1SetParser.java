package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1SetParser.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1SetParser.class */
public interface ASN1SetParser extends DEREncodable, InMemoryRepresentable {
    DEREncodable readObject() throws IOException;
}
