package org.bouncycastle.asn1.esf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/SignaturePolicyIdentifier.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/SignaturePolicyIdentifier.class */
public class SignaturePolicyIdentifier extends ASN1Encodable {
    private SignaturePolicyId signaturePolicyId;
    private boolean isSignaturePolicyImplied = true;

    public static SignaturePolicyIdentifier getInstance(Object obj) {
        if (obj == null || (obj instanceof SignaturePolicyIdentifier)) {
            return (SignaturePolicyIdentifier) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new SignaturePolicyIdentifier(SignaturePolicyId.getInstance(obj));
        }
        if (obj instanceof ASN1Null) {
            return new SignaturePolicyIdentifier();
        }
        throw new IllegalArgumentException("unknown object in 'SignaturePolicyIdentifier' factory: " + obj.getClass().getName() + ".");
    }

    public SignaturePolicyIdentifier() {
    }

    public SignaturePolicyIdentifier(SignaturePolicyId signaturePolicyId) {
        this.signaturePolicyId = signaturePolicyId;
    }

    public SignaturePolicyId getSignaturePolicyId() {
        return this.signaturePolicyId;
    }

    public boolean isSignaturePolicyImplied() {
        return this.isSignaturePolicyImplied;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.isSignaturePolicyImplied ? new DERNull() : this.signaturePolicyId.getDERObject();
    }
}
