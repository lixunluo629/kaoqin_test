package org.bouncycastle.asn1.cms;

import org.bouncycastle.asn1.ASN1Choice;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERTaggedObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cms/RecipientInfo.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/cms/RecipientInfo.class */
public class RecipientInfo extends ASN1Encodable implements ASN1Choice {
    DEREncodable info;

    public RecipientInfo(KeyTransRecipientInfo keyTransRecipientInfo) {
        this.info = keyTransRecipientInfo;
    }

    public RecipientInfo(KeyAgreeRecipientInfo keyAgreeRecipientInfo) {
        this.info = new DERTaggedObject(false, 1, keyAgreeRecipientInfo);
    }

    public RecipientInfo(KEKRecipientInfo kEKRecipientInfo) {
        this.info = new DERTaggedObject(false, 2, kEKRecipientInfo);
    }

    public RecipientInfo(PasswordRecipientInfo passwordRecipientInfo) {
        this.info = new DERTaggedObject(false, 3, passwordRecipientInfo);
    }

    public RecipientInfo(OtherRecipientInfo otherRecipientInfo) {
        this.info = new DERTaggedObject(false, 4, otherRecipientInfo);
    }

    public RecipientInfo(DERObject dERObject) {
        this.info = dERObject;
    }

    public static RecipientInfo getInstance(Object obj) {
        if (obj == null || (obj instanceof RecipientInfo)) {
            return (RecipientInfo) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new RecipientInfo((ASN1Sequence) obj);
        }
        if (obj instanceof ASN1TaggedObject) {
            return new RecipientInfo((ASN1TaggedObject) obj);
        }
        throw new IllegalArgumentException("unknown object in factory: " + obj.getClass().getName());
    }

    public DERInteger getVersion() {
        if (!(this.info instanceof ASN1TaggedObject)) {
            return KeyTransRecipientInfo.getInstance(this.info).getVersion();
        }
        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) this.info;
        switch (aSN1TaggedObject.getTagNo()) {
            case 1:
                return KeyAgreeRecipientInfo.getInstance(aSN1TaggedObject, false).getVersion();
            case 2:
                return getKEKInfo(aSN1TaggedObject).getVersion();
            case 3:
                return PasswordRecipientInfo.getInstance(aSN1TaggedObject, false).getVersion();
            case 4:
                return new DERInteger(0);
            default:
                throw new IllegalStateException("unknown tag");
        }
    }

    public boolean isTagged() {
        return this.info instanceof ASN1TaggedObject;
    }

    public DEREncodable getInfo() {
        if (!(this.info instanceof ASN1TaggedObject)) {
            return KeyTransRecipientInfo.getInstance(this.info);
        }
        ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) this.info;
        switch (aSN1TaggedObject.getTagNo()) {
            case 1:
                return KeyAgreeRecipientInfo.getInstance(aSN1TaggedObject, false);
            case 2:
                return getKEKInfo(aSN1TaggedObject);
            case 3:
                return PasswordRecipientInfo.getInstance(aSN1TaggedObject, false);
            case 4:
                return OtherRecipientInfo.getInstance(aSN1TaggedObject, false);
            default:
                throw new IllegalStateException("unknown tag");
        }
    }

    private KEKRecipientInfo getKEKInfo(ASN1TaggedObject aSN1TaggedObject) {
        return aSN1TaggedObject.isExplicit() ? KEKRecipientInfo.getInstance(aSN1TaggedObject, true) : KEKRecipientInfo.getInstance(aSN1TaggedObject, false);
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        return this.info.getDERObject();
    }
}
