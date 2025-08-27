package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/OtherName.class */
public class OtherName extends ASN1Object {
    private final ASN1ObjectIdentifier typeID;
    private final ASN1Encodable value;

    public static OtherName getInstance(Object obj) {
        if (obj instanceof OtherName) {
            return (OtherName) obj;
        }
        if (obj != null) {
            return new OtherName(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public OtherName(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.typeID = aSN1ObjectIdentifier;
        this.value = aSN1Encodable;
    }

    private OtherName(ASN1Sequence aSN1Sequence) {
        this.typeID = ASN1ObjectIdentifier.getInstance((Object) aSN1Sequence.getObjectAt(0));
        this.value = ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(1)).getObject();
    }

    public ASN1ObjectIdentifier getTypeID() {
        return this.typeID;
    }

    public ASN1Encodable getValue() {
        return this.value;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
        aSN1EncodableVector.add((ASN1Encodable) this.typeID);
        aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(true, 0, this.value));
        return new DERSequence(aSN1EncodableVector);
    }
}
