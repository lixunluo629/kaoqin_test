package org.bouncycastle.asn1.bc;

import java.math.BigInteger;
import java.util.Date;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERGeneralizedTime;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/bc/ObjectData.class */
public class ObjectData extends ASN1Object {
    private final BigInteger type;
    private final String identifier;
    private final ASN1GeneralizedTime creationDate;
    private final ASN1GeneralizedTime lastModifiedDate;
    private final ASN1OctetString data;
    private final String comment;

    private ObjectData(ASN1Sequence aSN1Sequence) {
        this.type = ASN1Integer.getInstance((Object) aSN1Sequence.getObjectAt(0)).getValue();
        this.identifier = DERUTF8String.getInstance(aSN1Sequence.getObjectAt(1)).getString();
        this.creationDate = ASN1GeneralizedTime.getInstance((Object) aSN1Sequence.getObjectAt(2));
        this.lastModifiedDate = ASN1GeneralizedTime.getInstance((Object) aSN1Sequence.getObjectAt(3));
        this.data = ASN1OctetString.getInstance(aSN1Sequence.getObjectAt(4));
        this.comment = aSN1Sequence.size() == 6 ? DERUTF8String.getInstance(aSN1Sequence.getObjectAt(5)).getString() : null;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1GeneralizedTime, org.bouncycastle.asn1.DERGeneralizedTime] */
    /* JADX WARN: Type inference failed for: r1v3, types: [org.bouncycastle.asn1.ASN1GeneralizedTime, org.bouncycastle.asn1.DERGeneralizedTime] */
    public ObjectData(BigInteger bigInteger, String str, Date date, Date date2, byte[] bArr, String str2) {
        this.type = bigInteger;
        this.identifier = str;
        this.creationDate = new DERGeneralizedTime(date);
        this.lastModifiedDate = new DERGeneralizedTime(date2);
        this.data = new DEROctetString(Arrays.clone(bArr));
        this.comment = str2;
    }

    public static ObjectData getInstance(Object obj) {
        if (obj instanceof ObjectData) {
            return (ObjectData) obj;
        }
        if (obj != null) {
            return new ObjectData(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public String getComment() {
        return this.comment;
    }

    public ASN1GeneralizedTime getCreationDate() {
        return this.creationDate;
    }

    public byte[] getData() {
        return Arrays.clone(this.data.getOctets());
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public ASN1GeneralizedTime getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public BigInteger getType() {
        return this.type;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(6);
        aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.type));
        aSN1EncodableVector.add((ASN1Encodable) new DERUTF8String(this.identifier));
        aSN1EncodableVector.add((ASN1Encodable) this.creationDate);
        aSN1EncodableVector.add((ASN1Encodable) this.lastModifiedDate);
        aSN1EncodableVector.add((ASN1Encodable) this.data);
        if (this.comment != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERUTF8String(this.comment));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
