package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/crmf/CertReqMessages.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/crmf/CertReqMessages.class */
public class CertReqMessages extends ASN1Encodable {
    private ASN1Sequence content;

    private CertReqMessages(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static CertReqMessages getInstance(Object obj) {
        if (obj instanceof CertReqMessages) {
            return (CertReqMessages) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new CertReqMessages((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    public CertReqMessages(CertReqMsg certReqMsg) {
        this.content = new DERSequence(certReqMsg);
    }

    public CertReqMessages(CertReqMsg[] certReqMsgArr) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        for (CertReqMsg certReqMsg : certReqMsgArr) {
            aSN1EncodableVector.add(certReqMsg);
        }
        this.content = new DERSequence(aSN1EncodableVector);
    }

    public CertReqMsg[] toCertReqMsgArray() {
        CertReqMsg[] certReqMsgArr = new CertReqMsg[this.content.size()];
        for (int i = 0; i != certReqMsgArr.length; i++) {
            certReqMsgArr[i] = CertReqMsg.getInstance(this.content.getObjectAt(i));
        }
        return certReqMsgArr;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.content;
    }
}
