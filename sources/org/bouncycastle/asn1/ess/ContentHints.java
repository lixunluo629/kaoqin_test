package org.bouncycastle.asn1.ess;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ess/ContentHints.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/ess/ContentHints.class */
public class ContentHints extends ASN1Encodable {
    private DERUTF8String contentDescription;
    private DERObjectIdentifier contentType;

    public static ContentHints getInstance(Object obj) {
        if (obj == null || (obj instanceof ContentHints)) {
            return (ContentHints) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new ContentHints((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in 'ContentHints' factory : " + obj.getClass().getName() + ".");
    }

    private ContentHints(ASN1Sequence aSN1Sequence) {
        DEREncodable objectAt = aSN1Sequence.getObjectAt(0);
        if (!(objectAt.getDERObject() instanceof DERUTF8String)) {
            this.contentType = DERObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        } else {
            this.contentDescription = DERUTF8String.getInstance(objectAt);
            this.contentType = DERObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(1));
        }
    }

    public ContentHints(DERObjectIdentifier dERObjectIdentifier) {
        this.contentType = dERObjectIdentifier;
        this.contentDescription = null;
    }

    public ContentHints(DERObjectIdentifier dERObjectIdentifier, DERUTF8String dERUTF8String) {
        this.contentType = dERObjectIdentifier;
        this.contentDescription = dERUTF8String;
    }

    public DERObjectIdentifier getContentType() {
        return this.contentType;
    }

    public DERUTF8String getContentDescription() {
        return this.contentDescription;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.contentDescription != null) {
            aSN1EncodableVector.add(this.contentDescription);
        }
        aSN1EncodableVector.add(this.contentType);
        return new DERSequence(aSN1EncodableVector);
    }
}
