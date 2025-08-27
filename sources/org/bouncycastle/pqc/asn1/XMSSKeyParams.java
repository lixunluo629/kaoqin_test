package org.bouncycastle.pqc.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/pqc/asn1/XMSSKeyParams.class */
public class XMSSKeyParams extends ASN1Object {
    private final ASN1Integer version;
    private final int height;
    private final AlgorithmIdentifier treeDigest;

    public XMSSKeyParams(int i, AlgorithmIdentifier algorithmIdentifier) {
        this.version = new ASN1Integer(0L);
        this.height = i;
        this.treeDigest = algorithmIdentifier;
    }

    private XMSSKeyParams(ASN1Sequence aSN1Sequence) {
        this.version = ASN1Integer.getInstance((Object) aSN1Sequence.getObjectAt(0));
        this.height = ASN1Integer.getInstance((Object) aSN1Sequence.getObjectAt(1)).intValueExact();
        this.treeDigest = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(2));
    }

    public static XMSSKeyParams getInstance(Object obj) {
        if (obj instanceof XMSSKeyParams) {
            return (XMSSKeyParams) obj;
        }
        if (obj != null) {
            return new XMSSKeyParams(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public int getHeight() {
        return this.height;
    }

    public AlgorithmIdentifier getTreeDigest() {
        return this.treeDigest;
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add((ASN1Encodable) this.version);
        aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.height));
        aSN1EncodableVector.add((ASN1Encodable) this.treeDigest);
        return new DERSequence(aSN1EncodableVector);
    }
}
