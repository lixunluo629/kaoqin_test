package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/tsp/PartialHashtree.class */
public class PartialHashtree extends ASN1Object {
    private ASN1Sequence values;

    public static PartialHashtree getInstance(Object obj) {
        if (obj instanceof PartialHashtree) {
            return (PartialHashtree) obj;
        }
        if (obj != null) {
            return new PartialHashtree(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    private PartialHashtree(ASN1Sequence aSN1Sequence) {
        for (int i = 0; i != aSN1Sequence.size(); i++) {
            if (!(aSN1Sequence.getObjectAt(i) instanceof DEROctetString)) {
                throw new IllegalArgumentException("unknown object in constructor: " + aSN1Sequence.getObjectAt(i).getClass().getName());
            }
        }
        this.values = aSN1Sequence;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    public PartialHashtree(byte[] bArr) {
        this((byte[][]) new byte[]{bArr});
    }

    public PartialHashtree(byte[][] bArr) {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(bArr.length);
        for (int i = 0; i != bArr.length; i++) {
            aSN1EncodableVector.add((ASN1Encodable) new DEROctetString(Arrays.clone(bArr[i])));
        }
        this.values = new DERSequence(aSN1EncodableVector);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    public byte[][] getValues() {
        ?? r0 = new byte[this.values.size()];
        for (int i = 0; i != r0.length; i++) {
            r0[i] = Arrays.clone(ASN1OctetString.getInstance(this.values.getObjectAt(i)).getOctets());
        }
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.ASN1Sequence] */
    public ASN1Primitive toASN1Primitive() {
        return this.values;
    }
}
