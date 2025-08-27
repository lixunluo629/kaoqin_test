package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/ObjectDigestInfo.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/ObjectDigestInfo.class */
public class ObjectDigestInfo extends ASN1Encodable {
    public static final int publicKey = 0;
    public static final int publicKeyCert = 1;
    public static final int otherObjectDigest = 2;
    DEREnumerated digestedObjectType;
    DERObjectIdentifier otherObjectTypeID;
    AlgorithmIdentifier digestAlgorithm;
    DERBitString objectDigest;

    public static ObjectDigestInfo getInstance(Object obj) {
        if (obj == null || (obj instanceof ObjectDigestInfo)) {
            return (ObjectDigestInfo) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new ObjectDigestInfo((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static ObjectDigestInfo getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public ObjectDigestInfo(int i, String str, AlgorithmIdentifier algorithmIdentifier, byte[] bArr) {
        this.digestedObjectType = new DEREnumerated(i);
        if (i == 2) {
            this.otherObjectTypeID = new DERObjectIdentifier(str);
        }
        this.digestAlgorithm = algorithmIdentifier;
        this.objectDigest = new DERBitString(bArr);
    }

    private ObjectDigestInfo(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() > 4 || aSN1Sequence.size() < 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.digestedObjectType = DEREnumerated.getInstance(aSN1Sequence.getObjectAt(0));
        int i = 0;
        if (aSN1Sequence.size() == 4) {
            this.otherObjectTypeID = DERObjectIdentifier.getInstance(aSN1Sequence.getObjectAt(1));
            i = 0 + 1;
        }
        this.digestAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(1 + i));
        this.objectDigest = DERBitString.getInstance(aSN1Sequence.getObjectAt(2 + i));
    }

    public DEREnumerated getDigestedObjectType() {
        return this.digestedObjectType;
    }

    public DERObjectIdentifier getOtherObjectTypeID() {
        return this.otherObjectTypeID;
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    public DERBitString getObjectDigest() {
        return this.objectDigest;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.digestedObjectType);
        if (this.otherObjectTypeID != null) {
            aSN1EncodableVector.add(this.otherObjectTypeID);
        }
        aSN1EncodableVector.add(this.digestAlgorithm);
        aSN1EncodableVector.add(this.objectDigest);
        return new DERSequence(aSN1EncodableVector);
    }
}
