package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/CommitmentTypeIndication.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/CommitmentTypeIndication.class */
public class CommitmentTypeIndication extends ASN1Encodable {
    private DERObjectIdentifier commitmentTypeId;
    private ASN1Sequence commitmentTypeQualifier;

    public CommitmentTypeIndication(ASN1Sequence aSN1Sequence) {
        this.commitmentTypeId = (DERObjectIdentifier) aSN1Sequence.getObjectAt(0);
        if (aSN1Sequence.size() > 1) {
            this.commitmentTypeQualifier = (ASN1Sequence) aSN1Sequence.getObjectAt(1);
        }
    }

    public CommitmentTypeIndication(DERObjectIdentifier dERObjectIdentifier) {
        this.commitmentTypeId = dERObjectIdentifier;
    }

    public CommitmentTypeIndication(DERObjectIdentifier dERObjectIdentifier, ASN1Sequence aSN1Sequence) {
        this.commitmentTypeId = dERObjectIdentifier;
        this.commitmentTypeQualifier = aSN1Sequence;
    }

    public static CommitmentTypeIndication getInstance(Object obj) {
        return (obj == null || (obj instanceof CommitmentTypeIndication)) ? (CommitmentTypeIndication) obj : new CommitmentTypeIndication(ASN1Sequence.getInstance(obj));
    }

    public DERObjectIdentifier getCommitmentTypeId() {
        return this.commitmentTypeId;
    }

    public ASN1Sequence getCommitmentTypeQualifier() {
        return this.commitmentTypeQualifier;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.commitmentTypeId);
        if (this.commitmentTypeQualifier != null) {
            aSN1EncodableVector.add(this.commitmentTypeQualifier);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
