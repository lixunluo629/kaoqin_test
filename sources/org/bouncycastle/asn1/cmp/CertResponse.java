package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cmp/CertResponse.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/cmp/CertResponse.class */
public class CertResponse extends ASN1Encodable {
    private DERInteger certReqId;
    private PKIStatusInfo status;
    private CertifiedKeyPair certifiedKeyPair;
    private ASN1OctetString rspInfo;

    private CertResponse(ASN1Sequence aSN1Sequence) {
        this.certReqId = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        this.status = PKIStatusInfo.getInstance(aSN1Sequence.getObjectAt(1));
        if (aSN1Sequence.size() >= 3) {
            if (aSN1Sequence.size() != 3) {
                this.certifiedKeyPair = CertifiedKeyPair.getInstance(aSN1Sequence.getObjectAt(2));
                this.rspInfo = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(3));
                return;
            }
            DEREncodable objectAt = aSN1Sequence.getObjectAt(2);
            if (objectAt instanceof ASN1OctetString) {
                this.rspInfo = ASN1OctetString.getInstance(objectAt);
            } else {
                this.certifiedKeyPair = CertifiedKeyPair.getInstance(objectAt);
            }
        }
    }

    public static CertResponse getInstance(Object obj) {
        if (obj instanceof CertResponse) {
            return (CertResponse) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new CertResponse((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    public CertResponse(DERInteger dERInteger, PKIStatusInfo pKIStatusInfo) {
        this(dERInteger, pKIStatusInfo, null, null);
    }

    public CertResponse(DERInteger dERInteger, PKIStatusInfo pKIStatusInfo, CertifiedKeyPair certifiedKeyPair, ASN1OctetString aSN1OctetString) {
        if (dERInteger == null) {
            throw new IllegalArgumentException("'certReqId' cannot be null");
        }
        if (pKIStatusInfo == null) {
            throw new IllegalArgumentException("'status' cannot be null");
        }
        this.certReqId = dERInteger;
        this.status = pKIStatusInfo;
        this.certifiedKeyPair = certifiedKeyPair;
        this.rspInfo = aSN1OctetString;
    }

    public DERInteger getCertReqId() {
        return this.certReqId;
    }

    public PKIStatusInfo getStatus() {
        return this.status;
    }

    public CertifiedKeyPair getCertifiedKeyPair() {
        return this.certifiedKeyPair;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.certReqId);
        aSN1EncodableVector.add(this.status);
        if (this.certifiedKeyPair != null) {
            aSN1EncodableVector.add(this.certifiedKeyPair);
        }
        if (this.rspInfo != null) {
            aSN1EncodableVector.add(this.rspInfo);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
