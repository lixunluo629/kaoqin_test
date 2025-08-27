package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cms/OtherKeyAttribute.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/cms/OtherKeyAttribute.class */
public class OtherKeyAttribute extends ASN1Encodable {
    private DERObjectIdentifier keyAttrId;
    private DEREncodable keyAttr;

    public static OtherKeyAttribute getInstance(Object obj) {
        if (obj == null || (obj instanceof OtherKeyAttribute)) {
            return (OtherKeyAttribute) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new OtherKeyAttribute((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public OtherKeyAttribute(ASN1Sequence aSN1Sequence) {
        this.keyAttrId = (DERObjectIdentifier) aSN1Sequence.getObjectAt(0);
        this.keyAttr = aSN1Sequence.getObjectAt(1);
    }

    public OtherKeyAttribute(DERObjectIdentifier dERObjectIdentifier, DEREncodable dEREncodable) {
        this.keyAttrId = dERObjectIdentifier;
        this.keyAttr = dEREncodable;
    }

    public DERObjectIdentifier getKeyAttrId() {
        return this.keyAttrId;
    }

    public DEREncodable getKeyAttr() {
        return this.keyAttr;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.keyAttrId);
        aSN1EncodableVector.add(this.keyAttr);
        return new DERSequence(aSN1EncodableVector);
    }
}
