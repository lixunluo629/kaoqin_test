package org.bouncycastle.asn1.cmp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cmp/PollReqContent.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/cmp/PollReqContent.class */
public class PollReqContent extends ASN1Encodable {
    private ASN1Sequence content;

    private PollReqContent(ASN1Sequence aSN1Sequence) {
        this.content = aSN1Sequence;
    }

    public static PollReqContent getInstance(Object obj) {
        if (obj instanceof PollReqContent) {
            return (PollReqContent) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new PollReqContent((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [org.bouncycastle.asn1.DERInteger[], org.bouncycastle.asn1.DERInteger[][]] */
    public DERInteger[][] getCertReqIds() {
        ?? r0 = new DERInteger[this.content.size()];
        for (int i = 0; i != r0.length; i++) {
            r0[i] = seqenceToDERIntegerArray((ASN1Sequence) this.content.getObjectAt(i));
        }
        return r0;
    }

    private DERInteger[] seqenceToDERIntegerArray(ASN1Sequence aSN1Sequence) {
        DERInteger[] dERIntegerArr = new DERInteger[aSN1Sequence.size()];
        for (int i = 0; i != dERIntegerArr.length; i++) {
            dERIntegerArr[i] = DERInteger.getInstance(aSN1Sequence.getObjectAt(i));
        }
        return dERIntegerArr;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.content;
    }
}
