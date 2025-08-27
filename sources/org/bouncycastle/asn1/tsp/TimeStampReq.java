package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBoolean;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.X509Extensions;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/tsp/TimeStampReq.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/tsp/TimeStampReq.class */
public class TimeStampReq extends ASN1Encodable {
    DERInteger version;
    MessageImprint messageImprint;
    DERObjectIdentifier tsaPolicy;
    DERInteger nonce;
    DERBoolean certReq;
    X509Extensions extensions;

    public static TimeStampReq getInstance(Object obj) {
        if (obj == null || (obj instanceof TimeStampReq)) {
            return (TimeStampReq) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new TimeStampReq((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Unknown object in 'TimeStampReq' factory : " + obj.getClass().getName() + ".");
    }

    public TimeStampReq(ASN1Sequence aSN1Sequence) {
        int size = aSN1Sequence.size();
        this.version = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        int i = 0 + 1;
        this.messageImprint = MessageImprint.getInstance(aSN1Sequence.getObjectAt(i));
        for (int i2 = i + 1; i2 < size; i2++) {
            if (aSN1Sequence.getObjectAt(i2) instanceof DERObjectIdentifier) {
                this.tsaPolicy = DERObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(i2));
            } else if (aSN1Sequence.getObjectAt(i2) instanceof DERInteger) {
                this.nonce = DERInteger.getInstance(aSN1Sequence.getObjectAt(i2));
            } else if (aSN1Sequence.getObjectAt(i2) instanceof DERBoolean) {
                this.certReq = DERBoolean.getInstance(aSN1Sequence.getObjectAt(i2));
            } else if (aSN1Sequence.getObjectAt(i2) instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Sequence.getObjectAt(i2);
                if (aSN1TaggedObject.getTagNo() == 0) {
                    this.extensions = X509Extensions.getInstance(aSN1TaggedObject, false);
                }
            }
        }
    }

    public TimeStampReq(MessageImprint messageImprint, DERObjectIdentifier dERObjectIdentifier, DERInteger dERInteger, DERBoolean dERBoolean, X509Extensions x509Extensions) {
        this.version = new DERInteger(1);
        this.messageImprint = messageImprint;
        this.tsaPolicy = dERObjectIdentifier;
        this.nonce = dERInteger;
        this.certReq = dERBoolean;
        this.extensions = x509Extensions;
    }

    public DERInteger getVersion() {
        return this.version;
    }

    public MessageImprint getMessageImprint() {
        return this.messageImprint;
    }

    public DERObjectIdentifier getReqPolicy() {
        return this.tsaPolicy;
    }

    public DERInteger getNonce() {
        return this.nonce;
    }

    public DERBoolean getCertReq() {
        return this.certReq;
    }

    public X509Extensions getExtensions() {
        return this.extensions;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.version);
        aSN1EncodableVector.add(this.messageImprint);
        if (this.tsaPolicy != null) {
            aSN1EncodableVector.add(this.tsaPolicy);
        }
        if (this.nonce != null) {
            aSN1EncodableVector.add(this.nonce);
        }
        if (this.certReq != null && this.certReq.isTrue()) {
            aSN1EncodableVector.add(this.certReq);
        }
        if (this.extensions != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.extensions));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
