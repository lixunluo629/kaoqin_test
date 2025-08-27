package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/pkcs/Attribute.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/pkcs/Attribute.class */
public class Attribute extends ASN1Encodable {
    private DERObjectIdentifier attrType;
    private ASN1Set attrValues;

    public static Attribute getInstance(Object obj) {
        if (obj == null || (obj instanceof Attribute)) {
            return (Attribute) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new Attribute((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public Attribute(ASN1Sequence aSN1Sequence) {
        this.attrType = (DERObjectIdentifier) aSN1Sequence.getObjectAt(0);
        this.attrValues = (ASN1Set) aSN1Sequence.getObjectAt(1);
    }

    public Attribute(DERObjectIdentifier dERObjectIdentifier, ASN1Set aSN1Set) {
        this.attrType = dERObjectIdentifier;
        this.attrValues = aSN1Set;
    }

    public DERObjectIdentifier getAttrType() {
        return this.attrType;
    }

    public ASN1Set getAttrValues() {
        return this.attrValues;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.attrType);
        aSN1EncodableVector.add(this.attrValues);
        return new DERSequence(aSN1EncodableVector);
    }
}
