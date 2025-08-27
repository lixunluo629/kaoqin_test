package org.bouncycastle.asn1;

import java.util.Vector;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DEREncodableVector.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DEREncodableVector.class */
public class DEREncodableVector {
    Vector v = new Vector();

    public void add(DEREncodable dEREncodable) {
        this.v.addElement(dEREncodable);
    }

    public DEREncodable get(int i) {
        return (DEREncodable) this.v.elementAt(i);
    }

    public int size() {
        return this.v.size();
    }
}
