package org.bouncycastle.asn1.x509;

import java.util.Enumeration;
import java.util.Vector;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/NameConstraints.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/NameConstraints.class */
public class NameConstraints extends ASN1Encodable {
    private ASN1Sequence permitted;
    private ASN1Sequence excluded;

    public NameConstraints(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(objects.nextElement());
            switch (aSN1TaggedObject.getTagNo()) {
                case 0:
                    this.permitted = ASN1Sequence.getInstance(aSN1TaggedObject, false);
                    break;
                case 1:
                    this.excluded = ASN1Sequence.getInstance(aSN1TaggedObject, false);
                    break;
            }
        }
    }

    public NameConstraints(Vector vector, Vector vector2) {
        if (vector != null) {
            this.permitted = createSequence(vector);
        }
        if (vector2 != null) {
            this.excluded = createSequence(vector2);
        }
    }

    private DERSequence createSequence(Vector vector) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        Enumeration enumerationElements = vector.elements();
        while (enumerationElements.hasMoreElements()) {
            aSN1EncodableVector.add((GeneralSubtree) enumerationElements.nextElement());
        }
        return new DERSequence(aSN1EncodableVector);
    }

    public ASN1Sequence getPermittedSubtrees() {
        return this.permitted;
    }

    public ASN1Sequence getExcludedSubtrees() {
        return this.excluded;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.permitted != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.permitted));
        }
        if (this.excluded != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.excluded));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
