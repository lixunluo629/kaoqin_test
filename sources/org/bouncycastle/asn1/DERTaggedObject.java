package org.bouncycastle.asn1;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/DERTaggedObject.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/DERTaggedObject.class */
public class DERTaggedObject extends ASN1TaggedObject {
    private static final byte[] ZERO_BYTES = new byte[0];

    public DERTaggedObject(int i, DEREncodable dEREncodable) {
        super(i, dEREncodable);
    }

    public DERTaggedObject(boolean z, int i, DEREncodable dEREncodable) {
        super(z, i, dEREncodable);
    }

    public DERTaggedObject(int i) {
        super(false, i, new DERSequence());
    }

    @Override // org.bouncycastle.asn1.ASN1TaggedObject, org.bouncycastle.asn1.ASN1Object, org.bouncycastle.asn1.DERObject
    void encode(DEROutputStream dEROutputStream) throws IOException {
        if (this.empty) {
            dEROutputStream.writeEncoded(160, this.tagNo, ZERO_BYTES);
            return;
        }
        byte[] encoded = this.obj.getDERObject().getEncoded("DER");
        if (this.explicit) {
            dEROutputStream.writeEncoded(160, this.tagNo, encoded);
        } else {
            dEROutputStream.writeTag((encoded[0] & 32) != 0 ? 160 : 128, this.tagNo);
            dEROutputStream.write(encoded, 1, encoded.length - 1);
        }
    }
}
