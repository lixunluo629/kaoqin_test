package org.bouncycastle.asn1.ocsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.X509Extensions;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ocsp/ResponseData.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ocsp/ResponseData.class */
public class ResponseData extends ASN1Encodable {
    private static final DERInteger V1 = new DERInteger(0);
    private boolean versionPresent;
    private DERInteger version;
    private ResponderID responderID;
    private DERGeneralizedTime producedAt;
    private ASN1Sequence responses;
    private X509Extensions responseExtensions;

    public ResponseData(DERInteger dERInteger, ResponderID responderID, DERGeneralizedTime dERGeneralizedTime, ASN1Sequence aSN1Sequence, X509Extensions x509Extensions) {
        this.version = dERInteger;
        this.responderID = responderID;
        this.producedAt = dERGeneralizedTime;
        this.responses = aSN1Sequence;
        this.responseExtensions = x509Extensions;
    }

    public ResponseData(ResponderID responderID, DERGeneralizedTime dERGeneralizedTime, ASN1Sequence aSN1Sequence, X509Extensions x509Extensions) {
        this(V1, responderID, dERGeneralizedTime, aSN1Sequence, x509Extensions);
    }

    public ResponseData(ASN1Sequence aSN1Sequence) {
        int i = 0;
        if ((aSN1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) && ((ASN1TaggedObject) aSN1Sequence.getObjectAt(0)).getTagNo() == 0) {
            this.versionPresent = true;
            this.version = DERInteger.getInstance((ASN1TaggedObject) aSN1Sequence.getObjectAt(0), true);
            i = 0 + 1;
        } else {
            this.version = V1;
        }
        int i2 = i;
        int i3 = i + 1;
        this.responderID = ResponderID.getInstance(aSN1Sequence.getObjectAt(i2));
        int i4 = i3 + 1;
        this.producedAt = (DERGeneralizedTime) aSN1Sequence.getObjectAt(i3);
        int i5 = i4 + 1;
        this.responses = (ASN1Sequence) aSN1Sequence.getObjectAt(i4);
        if (aSN1Sequence.size() > i5) {
            this.responseExtensions = X509Extensions.getInstance((ASN1TaggedObject) aSN1Sequence.getObjectAt(i5), true);
        }
    }

    public static ResponseData getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static ResponseData getInstance(Object obj) {
        if (obj == null || (obj instanceof ResponseData)) {
            return (ResponseData) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new ResponseData((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public DERInteger getVersion() {
        return this.version;
    }

    public ResponderID getResponderID() {
        return this.responderID;
    }

    public DERGeneralizedTime getProducedAt() {
        return this.producedAt;
    }

    public ASN1Sequence getResponses() {
        return this.responses;
    }

    public X509Extensions getResponseExtensions() {
        return this.responseExtensions;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.versionPresent || !this.version.equals(V1)) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.version));
        }
        aSN1EncodableVector.add(this.responderID);
        aSN1EncodableVector.add(this.producedAt);
        aSN1EncodableVector.add(this.responses);
        if (this.responseExtensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.responseExtensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
