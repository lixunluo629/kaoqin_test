package org.bouncycastle.asn1;

import java.io.IOException;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DERObject.class */
public abstract class DERObject extends ASN1Encodable implements DERTags {
    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public abstract int hashCode();

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public abstract boolean equals(Object obj);

    abstract void encode(DEROutputStream dEROutputStream) throws IOException;
}
