package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.asn1.x509.NoticeReference;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/SPUserNotice.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/SPUserNotice.class */
public class SPUserNotice {
    private NoticeReference noticeRef;
    private DisplayText explicitText;

    public static SPUserNotice getInstance(Object obj) {
        if (obj == null || (obj instanceof SPUserNotice)) {
            return (SPUserNotice) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new SPUserNotice((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in 'SPUserNotice' factory : " + obj.getClass().getName() + ".");
    }

    public SPUserNotice(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            DEREncodable dEREncodable = (DEREncodable) objects.nextElement();
            if (dEREncodable instanceof NoticeReference) {
                this.noticeRef = NoticeReference.getInstance(dEREncodable);
            } else {
                if (!(dEREncodable instanceof DisplayText)) {
                    throw new IllegalArgumentException("Invalid element in 'SPUserNotice'.");
                }
                this.explicitText = DisplayText.getInstance(dEREncodable);
            }
        }
    }

    public SPUserNotice(NoticeReference noticeReference, DisplayText displayText) {
        this.noticeRef = noticeReference;
        this.explicitText = displayText;
    }

    public NoticeReference getNoticeRef() {
        return this.noticeRef;
    }

    public DisplayText getExplicitText() {
        return this.explicitText;
    }

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
