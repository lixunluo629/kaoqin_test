package org.bouncycastle.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DERSet.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DERSet.class */
public class DERSet extends ASN1Set {
    public DERSet() {
    }

    public DERSet(DEREncodable dEREncodable) {
        addObject(dEREncodable);
    }

    public DERSet(ASN1EncodableVector aSN1EncodableVector) {
        this(aSN1EncodableVector, true);
    }

    public DERSet(ASN1Encodable[] aSN1EncodableArr) {
        for (int i = 0; i != aSN1EncodableArr.length; i++) {
            addObject(aSN1EncodableArr[i]);
        }
        sort();
    }

    DERSet(ASN1EncodableVector aSN1EncodableVector, boolean z) {
        for (int i = 0; i != aSN1EncodableVector.size(); i++) {
            addObject(aSN1EncodableVector.get(i));
        }
        if (z) {
            sort();
        }
    }

    @Override // org.bouncycastle.asn1.ASN1Set, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DEROutputStream dEROutputStream2 = new DEROutputStream(byteArrayOutputStream);
        Enumeration objects = getObjects();
        while (objects.hasMoreElements()) {
            dEROutputStream2.writeObject(objects.nextElement());
        }
        dEROutputStream2.close();
        dEROutputStream.writeEncoded(49, byteArrayOutputStream.toByteArray());
    }
}
