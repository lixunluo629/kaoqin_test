package org.bouncycastle.asn1.cmp;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cmp/PKIStatusInfo.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/cmp/PKIStatusInfo.class */
public class PKIStatusInfo extends ASN1Encodable {
    DERInteger status;
    PKIFreeText statusString;
    DERBitString failInfo;

    public static PKIStatusInfo getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static PKIStatusInfo getInstance(Object obj) {
        if (obj instanceof PKIStatusInfo) {
            return (PKIStatusInfo) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new PKIStatusInfo((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public PKIStatusInfo(ASN1Sequence aSN1Sequence) {
        this.status = DERInteger.getInstance(aSN1Sequence.getObjectAt(0));
        this.statusString = null;
        this.failInfo = null;
        if (aSN1Sequence.size() > 2) {
            this.statusString = PKIFreeText.getInstance(aSN1Sequence.getObjectAt(1));
            this.failInfo = DERBitString.getInstance(aSN1Sequence.getObjectAt(2));
        } else if (aSN1Sequence.size() > 1) {
            DEREncodable objectAt = aSN1Sequence.getObjectAt(1);
            if (objectAt instanceof DERBitString) {
                this.failInfo = DERBitString.getInstance(objectAt);
            } else {
                this.statusString = PKIFreeText.getInstance(objectAt);
            }
        }
    }

    public PKIStatusInfo(int i) {
        this.status = new DERInteger(i);
    }

    public PKIStatusInfo(PKIStatus pKIStatus) {
        this.status = DERInteger.getInstance(pKIStatus.toASN1Object());
    }

    public PKIStatusInfo(int i, PKIFreeText pKIFreeText) {
        this.status = new DERInteger(i);
        this.statusString = pKIFreeText;
    }

    public PKIStatusInfo(PKIStatus pKIStatus, PKIFreeText pKIFreeText) {
        this.status = DERInteger.getInstance(pKIStatus.toASN1Object());
        this.statusString = pKIFreeText;
    }

    public PKIStatusInfo(int i, PKIFreeText pKIFreeText, PKIFailureInfo pKIFailureInfo) {
        this.status = new DERInteger(i);
        this.statusString = pKIFreeText;
        this.failInfo = pKIFailureInfo;
    }

    public BigInteger getStatus() {
        return this.status.getValue();
    }

    public PKIFreeText getStatusString() {
        return this.statusString;
    }

    public DERBitString getFailInfo() {
        return this.failInfo;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.status);
        if (this.statusString != null) {
            aSN1EncodableVector.add(this.statusString);
        }
        if (this.failInfo != null) {
            aSN1EncodableVector.add(this.failInfo);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
