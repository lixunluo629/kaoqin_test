package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/CrlOcspRef.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/CrlOcspRef.class */
public class CrlOcspRef extends ASN1Encodable {
    private CrlListID crlids;
    private OcspListID ocspids;
    private OtherRevRefs otherRev;

    public static CrlOcspRef getInstance(Object obj) {
        if (obj instanceof CrlOcspRef) {
            return (CrlOcspRef) obj;
        }
        if (obj != null) {
            return new CrlOcspRef(ASN1Sequence.getInstance(obj));
        }
        throw new IllegalArgumentException("null value in getInstance");
    }

    private CrlOcspRef(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject) objects.nextElement();
            switch (dERTaggedObject.getTagNo()) {
                case 0:
                    this.crlids = CrlListID.getInstance(dERTaggedObject.getObject());
                    break;
                case 1:
                    this.ocspids = OcspListID.getInstance(dERTaggedObject.getObject());
                    break;
                case 2:
                    this.otherRev = OtherRevRefs.getInstance(dERTaggedObject.getObject());
                    break;
                default:
                    throw new IllegalArgumentException("illegal tag");
            }
        }
    }

    public CrlOcspRef(CrlListID crlListID, OcspListID ocspListID, OtherRevRefs otherRevRefs) {
        this.crlids = crlListID;
        this.ocspids = ocspListID;
        this.otherRev = otherRevRefs;
    }

    public CrlListID getCrlids() {
        return this.crlids;
    }

    public OcspListID getOcspids() {
        return this.ocspids;
    }

    public OtherRevRefs getOtherRev() {
        return this.otherRev;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (null != this.crlids) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.crlids.toASN1Object()));
        }
        if (null != this.ocspids) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.ocspids.toASN1Object()));
        }
        if (null != this.otherRev) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.otherRev.toASN1Object()));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
