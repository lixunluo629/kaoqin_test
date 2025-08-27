package org.bouncycastle.asn1.bc;

import java.math.BigInteger;
import java.util.Date;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/bc/ObjectStoreData.class */
public class ObjectStoreData extends ASN1Object {
    private final BigInteger version;
    private final AlgorithmIdentifier integrityAlgorithm;
    private final ASN1GeneralizedTime creationDate;
    private final ASN1GeneralizedTime lastModifiedDate;
    private final ObjectDataSequence objectDataSequence;
    private final String comment;

    /* JADX WARN: Type inference failed for: r1v3, types: [org.bouncycastle.asn1.ASN1GeneralizedTime, org.bouncycastle.asn1.DERGeneralizedTime] */
    /* JADX WARN: Type inference failed for: r1v4, types: [org.bouncycastle.asn1.ASN1GeneralizedTime, org.bouncycastle.asn1.DERGeneralizedTime] */
    public ObjectStoreData(AlgorithmIdentifier algorithmIdentifier, Date date, Date date2, ObjectDataSequence objectDataSequence, String str) {
        this.version = BigInteger.valueOf(1L);
        this.integrityAlgorithm = algorithmIdentifier;
        this.creationDate = new DERGeneralizedTime(date);
        this.lastModifiedDate = new DERGeneralizedTime(date2);
        this.objectDataSequence = objectDataSequence;
        this.comment = str;
    }

    private ObjectStoreData(ASN1Sequence aSN1Sequence) {
        this.version = ASN1Integer.getInstance((Object) aSN1Sequence.getObjectAt(0)).getValue();
        this.integrityAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(1));
        this.creationDate = ASN1GeneralizedTime.getInstance((Object) aSN1Sequence.getObjectAt(2));
        this.lastModifiedDate = ASN1GeneralizedTime.getInstance((Object) aSN1Sequence.getObjectAt(3));
        this.objectDataSequence = ObjectDataSequence.getInstance(aSN1Sequence.getObjectAt(4));
        this.comment = aSN1Sequence.size() == 6 ? DERUTF8String.getInstance(aSN1Sequence.getObjectAt(5)).getString() : null;
    }

    public static ObjectStoreData getInstance(Object obj) {
        if (obj instanceof ObjectStoreData) {
            return (ObjectStoreData) obj;
        }
        if (obj != null) {
            return new ObjectStoreData(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public String getComment() {
        return this.comment;
    }

    public ASN1GeneralizedTime getCreationDate() {
        return this.creationDate;
    }

    public AlgorithmIdentifier getIntegrityAlgorithm() {
        return this.integrityAlgorithm;
    }

    public ASN1GeneralizedTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public ObjectDataSequence getObjectDataSequence() {
        return this.objectDataSequence;
    }

    public BigInteger getVersion() {
        return this.version;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(6);
        aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.version));
        aSN1EncodableVector.add((ASN1Encodable) this.integrityAlgorithm);
        aSN1EncodableVector.add((ASN1Encodable) this.creationDate);
        aSN1EncodableVector.add((ASN1Encodable) this.lastModifiedDate);
        aSN1EncodableVector.add((ASN1Encodable) this.objectDataSequence);
        if (this.comment != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERUTF8String(this.comment));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
