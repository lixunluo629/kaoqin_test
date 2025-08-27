package org.bouncycastle.asn1.pkcs;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/pkcs/SafeBag.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/pkcs/SafeBag.class */
public class SafeBag extends ASN1Encodable {
    DERObjectIdentifier bagId;
    DERObject bagValue;
    ASN1Set bagAttributes;

    public SafeBag(DERObjectIdentifier dERObjectIdentifier, DERObject dERObject) {
        this.bagId = dERObjectIdentifier;
        this.bagValue = dERObject;
        this.bagAttributes = null;
    }

    public SafeBag(DERObjectIdentifier dERObjectIdentifier, DERObject dERObject, ASN1Set aSN1Set) {
        this.bagId = dERObjectIdentifier;
        this.bagValue = dERObject;
        this.bagAttributes = aSN1Set;
    }

    public SafeBag(ASN1Sequence aSN1Sequence) {
        this.bagId = (DERObjectIdentifier) aSN1Sequence.getObjectAt(0);
        this.bagValue = ((DERTaggedObject) aSN1Sequence.getObjectAt(1)).getObject();
        if (aSN1Sequence.size() == 3) {
            this.bagAttributes = (ASN1Set) aSN1Sequence.getObjectAt(2);
        }
    }

    public DERObjectIdentifier getBagId() {
        return this.bagId;
    }

    public DERObject getBagValue() {
        return this.bagValue;
    }

    public ASN1Set getBagAttributes() {
        return this.bagAttributes;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.bagId);
        aSN1EncodableVector.add(new DERTaggedObject(0, this.bagValue));
        if (this.bagAttributes != null) {
            aSN1EncodableVector.add(this.bagAttributes);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
