package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.Attributes;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/tsp/ArchiveTimeStamp.class */
public class ArchiveTimeStamp extends ASN1Object {
    private AlgorithmIdentifier digestAlgorithm;
    private Attributes attributes;
    private ASN1Sequence reducedHashTree;
    private ContentInfo timeStamp;

    public static ArchiveTimeStamp getInstance(Object obj) {
        if (obj instanceof ArchiveTimeStamp) {
            return (ArchiveTimeStamp) obj;
        }
        if (obj != null) {
            return new ArchiveTimeStamp(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public ArchiveTimeStamp(AlgorithmIdentifier algorithmIdentifier, PartialHashtree[] partialHashtreeArr, ContentInfo contentInfo) {
        this.digestAlgorithm = algorithmIdentifier;
        this.reducedHashTree = new DERSequence(partialHashtreeArr);
        this.timeStamp = contentInfo;
    }

    public ArchiveTimeStamp(AlgorithmIdentifier algorithmIdentifier, Attributes attributes, PartialHashtree[] partialHashtreeArr, ContentInfo contentInfo) {
        this.digestAlgorithm = algorithmIdentifier;
        this.attributes = attributes;
        this.reducedHashTree = new DERSequence(partialHashtreeArr);
        this.timeStamp = contentInfo;
    }

    public ArchiveTimeStamp(ContentInfo contentInfo) {
        this.timeStamp = contentInfo;
    }

    private ArchiveTimeStamp(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 1 || aSN1Sequence.size() > 4) {
            throw new IllegalArgumentException("wrong sequence size in constructor: " + aSN1Sequence.size());
        }
        for (int i = 0; i < aSN1Sequence.size() - 1; i++) {
            ASN1Encodable objectAt = aSN1Sequence.getObjectAt(i);
            if (objectAt instanceof ASN1TaggedObject) {
                ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(objectAt);
                switch (aSN1TaggedObject.getTagNo()) {
                    case 0:
                        this.digestAlgorithm = AlgorithmIdentifier.getInstance(aSN1TaggedObject, false);
                        break;
                    case 1:
                        this.attributes = Attributes.getInstance(aSN1TaggedObject, false);
                        break;
                    case 2:
                        this.reducedHashTree = ASN1Sequence.getInstance(aSN1TaggedObject, false);
                        break;
                    default:
                        throw new IllegalArgumentException("invalid tag no in constructor: " + aSN1TaggedObject.getTagNo());
                }
            }
        }
        this.timeStamp = ContentInfo.getInstance(aSN1Sequence.getObjectAt(aSN1Sequence.size() - 1));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1ObjectIdentifier, org.bouncycastle.asn1.ASN1Primitive] */
    public AlgorithmIdentifier getDigestAlgorithmIdentifier() {
        if (this.digestAlgorithm != null) {
            return this.digestAlgorithm;
        }
        if (!this.timeStamp.getContentType().equals((ASN1Primitive) CMSObjectIdentifiers.signedData)) {
            throw new IllegalStateException("cannot identify algorithm identifier for digest");
        }
        SignedData signedData = SignedData.getInstance(this.timeStamp.getContent());
        if (signedData.getEncapContentInfo().getContentType().equals((ASN1Primitive) PKCSObjectIdentifiers.id_ct_TSTInfo)) {
            return TSTInfo.getInstance(signedData.getEncapContentInfo()).getMessageImprint().getHashAlgorithm();
        }
        throw new IllegalStateException("cannot parse time stamp");
    }

    public AlgorithmIdentifier getDigestAlgorithm() {
        return this.digestAlgorithm;
    }

    public PartialHashtree[] getReducedHashTree() {
        if (this.reducedHashTree == null) {
            return null;
        }
        PartialHashtree[] partialHashtreeArr = new PartialHashtree[this.reducedHashTree.size()];
        for (int i = 0; i != partialHashtreeArr.length; i++) {
            partialHashtreeArr[i] = PartialHashtree.getInstance(this.reducedHashTree.getObjectAt(i));
        }
        return partialHashtreeArr;
    }

    public ContentInfo getTimeStamp() {
        return this.timeStamp;
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(4);
        if (this.digestAlgorithm != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 0, (ASN1Encodable) this.digestAlgorithm));
        }
        if (this.attributes != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 1, (ASN1Encodable) this.attributes));
        }
        if (this.reducedHashTree != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(false, 2, (ASN1Encodable) this.reducedHashTree));
        }
        aSN1EncodableVector.add((ASN1Encodable) this.timeStamp);
        return new DERSequence(aSN1EncodableVector);
    }
}
