package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1TaggedObjectParser.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1TaggedObjectParser.class */
public interface ASN1TaggedObjectParser extends DEREncodable, InMemoryRepresentable {
    int getTagNo();

    DEREncodable getObjectParser(int i, boolean z) throws IOException;
}
