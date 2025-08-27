package org.bouncycastle.asn1;

import java.util.Vector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ASN1EncodableVector.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ASN1EncodableVector.class */
public class ASN1EncodableVector extends DEREncodableVector {
    Vector v = new Vector();

    @Override // org.bouncycastle.asn1.DEREncodableVector
    public void add(DEREncodable dEREncodable) {
        this.v.addElement(dEREncodable);
    }

    @Override // org.bouncycastle.asn1.DEREncodableVector
    public DEREncodable get(int i) {
        return (DEREncodable) this.v.elementAt(i);
    }

    @Override // org.bouncycastle.asn1.DEREncodableVector
    public int size() {
        return this.v.size();
    }
}
