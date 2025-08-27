package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/InMemoryRepresentable.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/InMemoryRepresentable.class */
public interface InMemoryRepresentable {
    DERObject getLoadedObject() throws IOException;
}
