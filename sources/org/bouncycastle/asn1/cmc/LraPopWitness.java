package org.bouncycastle.asn1.cmc;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/cmc/LraPopWitness.class */
public class LraPopWitness extends ASN1Object {
    private final BodyPartID pkiDataBodyid;
    private final ASN1Sequence bodyIds;

    public LraPopWitness(BodyPartID bodyPartID, ASN1Sequence aSN1Sequence) {
        this.pkiDataBodyid = bodyPartID;
        this.bodyIds = aSN1Sequence;
    }

    private LraPopWitness(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("incorrect sequence size");
        }
        this.pkiDataBodyid = BodyPartID.getInstance(aSN1Sequence.getObjectAt(0));
        this.bodyIds = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public static LraPopWitness getInstance(Object obj) {
        if (obj instanceof LraPopWitness) {
            return (LraPopWitness) obj;
        }
        if (obj != null) {
            return new LraPopWitness(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public BodyPartID getPkiDataBodyid() {
        return this.pkiDataBodyid;
    }

    public BodyPartID[] getBodyIds() {
        BodyPartID[] bodyPartIDArr = new BodyPartID[this.bodyIds.size()];
        for (int i = 0; i != this.bodyIds.size(); i++) {
            bodyPartIDArr[i] = BodyPartID.getInstance(this.bodyIds.getObjectAt(i));
        }
        return bodyPartIDArr;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
        aSN1EncodableVector.add((ASN1Encodable) this.pkiDataBodyid);
        aSN1EncodableVector.add((ASN1Encodable) this.bodyIds);
        return new DERSequence(aSN1EncodableVector);
    }
}
