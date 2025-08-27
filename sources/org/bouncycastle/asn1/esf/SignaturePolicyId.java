package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/SignaturePolicyId.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/SignaturePolicyId.class */
public class SignaturePolicyId extends ASN1Encodable {
    private DERObjectIdentifier sigPolicyId;
    private OtherHashAlgAndValue sigPolicyHash;
    private SigPolicyQualifiers sigPolicyQualifiers;

    public static SignaturePolicyId getInstance(Object obj) {
        if (obj == null || (obj instanceof SignaturePolicyId)) {
            return (SignaturePolicyId) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new SignaturePolicyId((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Unknown object in 'SignaturePolicyId' factory : " + obj.getClass().getName() + ".");
    }

    public SignaturePolicyId(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2 && aSN1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.sigPolicyId = DERObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        this.sigPolicyHash = OtherHashAlgAndValue.getInstance(aSN1Sequence.getObjectAt(1));
        if (aSN1Sequence.size() == 3) {
            this.sigPolicyQualifiers = SigPolicyQualifiers.getInstance(aSN1Sequence.getObjectAt(2));
        }
    }

    public SignaturePolicyId(DERObjectIdentifier dERObjectIdentifier, OtherHashAlgAndValue otherHashAlgAndValue) {
        this(dERObjectIdentifier, otherHashAlgAndValue, null);
    }

    public SignaturePolicyId(DERObjectIdentifier dERObjectIdentifier, OtherHashAlgAndValue otherHashAlgAndValue, SigPolicyQualifiers sigPolicyQualifiers) {
        this.sigPolicyId = dERObjectIdentifier;
        this.sigPolicyHash = otherHashAlgAndValue;
        this.sigPolicyQualifiers = sigPolicyQualifiers;
    }

    public ASN1ObjectIdentifier getSigPolicyId() {
        return new ASN1ObjectIdentifier(this.sigPolicyId.getId());
    }

    public OtherHashAlgAndValue getSigPolicyHash() {
        return this.sigPolicyHash;
    }

    public SigPolicyQualifiers getSigPolicyQualifiers() {
        return this.sigPolicyQualifiers;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.sigPolicyId);
        aSN1EncodableVector.add(this.sigPolicyHash);
        if (this.sigPolicyQualifiers != null) {
            aSN1EncodableVector.add(this.sigPolicyQualifiers);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
