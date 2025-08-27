package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ocsp/ResponseBytes.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ocsp/ResponseBytes.class */
public class ResponseBytes extends ASN1Encodable {
    DERObjectIdentifier responseType;
    ASN1OctetString response;

    public ResponseBytes(DERObjectIdentifier dERObjectIdentifier, ASN1OctetString aSN1OctetString) {
        this.responseType = dERObjectIdentifier;
        this.response = aSN1OctetString;
    }

    public ResponseBytes(ASN1Sequence aSN1Sequence) {
        this.responseType = (DERObjectIdentifier) aSN1Sequence.getObjectAt(0);
        this.response = (ASN1OctetString) aSN1Sequence.getObjectAt(1);
    }

    public static ResponseBytes getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static ResponseBytes getInstance(Object obj) {
        if (obj == null || (obj instanceof ResponseBytes)) {
            return (ResponseBytes) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new ResponseBytes((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public DERObjectIdentifier getResponseType() {
        return this.responseType;
    }

    public ASN1OctetString getResponse() {
        return this.response;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.responseType);
        aSN1EncodableVector.add(this.response);
        return new DERSequence(aSN1EncodableVector);
    }
}
