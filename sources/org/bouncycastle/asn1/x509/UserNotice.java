package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/UserNotice.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/UserNotice.class */
public class UserNotice extends ASN1Encodable {
    private NoticeReference noticeRef;
    private DisplayText explicitText;

    public UserNotice(NoticeReference noticeReference, DisplayText displayText) {
        this.noticeRef = noticeReference;
        this.explicitText = displayText;
    }

    public UserNotice(NoticeReference noticeReference, String str) {
        this.noticeRef = noticeReference;
        this.explicitText = new DisplayText(str);
    }

    public UserNotice(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() == 2) {
            this.noticeRef = NoticeReference.getInstance(aSN1Sequence.getObjectAt(0));
            this.explicitText = DisplayText.getInstance(aSN1Sequence.getObjectAt(1));
        } else {
            if (aSN1Sequence.size() != 1) {
                throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
            }
            if (aSN1Sequence.getObjectAt(0).getDERObject() instanceof ASN1Sequence) {
                this.noticeRef = NoticeReference.getInstance(aSN1Sequence.getObjectAt(0));
            } else {
                this.explicitText = DisplayText.getInstance(aSN1Sequence.getObjectAt(0));
            }
        }
    }

    public NoticeReference getNoticeRef() {
        return this.noticeRef;
    }

    public DisplayText getExplicitText() {
        return this.explicitText;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.noticeRef != null) {
            aSN1EncodableVector.add(this.noticeRef);
        }
        if (this.explicitText != null) {
            aSN1EncodableVector.add(this.explicitText);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
