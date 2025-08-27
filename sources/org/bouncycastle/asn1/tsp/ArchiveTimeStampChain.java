package org.bouncycastle.asn1.tsp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/tsp/ArchiveTimeStampChain.class */
public class ArchiveTimeStampChain extends ASN1Object {
    private ASN1Sequence archiveTimestamps;

    public static ArchiveTimeStampChain getInstance(Object obj) {
        if (obj instanceof ArchiveTimeStampChain) {
            return (ArchiveTimeStampChain) obj;
        }
        if (obj != null) {
            return new ArchiveTimeStampChain(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public ArchiveTimeStampChain(ArchiveTimeStamp archiveTimeStamp) {
        this.archiveTimestamps = new DERSequence((ASN1Encodable) archiveTimeStamp);
    }

    public ArchiveTimeStampChain(ArchiveTimeStamp[] archiveTimeStampArr) {
        this.archiveTimestamps = new DERSequence(archiveTimeStampArr);
    }

    private ArchiveTimeStampChain(ASN1Sequence aSN1Sequence) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(aSN1Sequence.size());
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            aSN1EncodableVector.add((ASN1Encodable) ArchiveTimeStamp.getInstance(objects.nextElement()));
        }
        this.archiveTimestamps = new DERSequence(aSN1EncodableVector);
    }

    public ArchiveTimeStamp[] getArchiveTimestamps() {
        ArchiveTimeStamp[] archiveTimeStampArr = new ArchiveTimeStamp[this.archiveTimestamps.size()];
        for (int i = 0; i != archiveTimeStampArr.length; i++) {
            archiveTimeStampArr[i] = ArchiveTimeStamp.getInstance(this.archiveTimestamps.getObjectAt(i));
        }
        return archiveTimeStampArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ArchiveTimeStampChain append(ArchiveTimeStamp archiveTimeStamp) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(this.archiveTimestamps.size() + 1);
        for (int i = 0; i != this.archiveTimestamps.size(); i++) {
            aSN1EncodableVector.add(this.archiveTimestamps.getObjectAt(i));
        }
        aSN1EncodableVector.add((ASN1Encodable) archiveTimeStamp);
        return new ArchiveTimeStampChain(new DERSequence(aSN1EncodableVector));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Sequence] */
    public ASN1Primitive toASN1Primitive() {
        return this.archiveTimestamps;
    }
}
