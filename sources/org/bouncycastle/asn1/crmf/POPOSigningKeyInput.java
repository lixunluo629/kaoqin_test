package org.bouncycastle.asn1.crmf;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/crmf/POPOSigningKeyInput.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/crmf/POPOSigningKeyInput.class */
public class POPOSigningKeyInput extends ASN1Encodable {
    private GeneralName sender;
    private PKMACValue publicKeyMAC;
    private SubjectPublicKeyInfo publicKey;

    private POPOSigningKeyInput(ASN1Sequence aSN1Sequence) {
        ASN1Encodable aSN1Encodable = (ASN1Encodable) aSN1Sequence.getObjectAt(0);
        if (aSN1Encodable instanceof ASN1TaggedObject) {
            ASN1TaggedObject aSN1TaggedObject = (ASN1TaggedObject) aSN1Encodable;
            if (aSN1TaggedObject.getTagNo() != 0) {
                throw new IllegalArgumentException("Unknown authInfo tag: " + aSN1TaggedObject.getTagNo());
            }
            this.sender = GeneralName.getInstance(aSN1TaggedObject.getObject());
        } else {
            this.publicKeyMAC = PKMACValue.getInstance(aSN1Encodable);
        }
        this.publicKey = SubjectPublicKeyInfo.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public static POPOSigningKeyInput getInstance(Object obj) {
        if (obj instanceof POPOSigningKeyInput) {
            return (POPOSigningKeyInput) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new POPOSigningKeyInput((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid object: " + obj.getClass().getName());
    }

    public POPOSigningKeyInput(GeneralName generalName, SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.sender = generalName;
        this.publicKey = subjectPublicKeyInfo;
    }

    public POPOSigningKeyInput(PKMACValue pKMACValue, SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.publicKeyMAC = pKMACValue;
        this.publicKey = subjectPublicKeyInfo;
    }

    public GeneralName getSender() {
        return this.sender;
    }

    public PKMACValue getPublicKeyMAC() {
        return this.publicKeyMAC;
    }

    public SubjectPublicKeyInfo getPublicKey() {
        return this.publicKey;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.sender != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.sender));
        } else {
            aSN1EncodableVector.add(this.publicKeyMAC);
        }
        aSN1EncodableVector.add(this.publicKey);
        return new DERSequence(aSN1EncodableVector);
    }
}
