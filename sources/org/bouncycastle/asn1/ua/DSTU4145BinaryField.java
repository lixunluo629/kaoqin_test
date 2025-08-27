package org.bouncycastle.asn1.ua;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/ua/DSTU4145BinaryField.class */
public class DSTU4145BinaryField extends ASN1Object {
    private int m;
    private int k;
    private int j;
    private int l;

    private DSTU4145BinaryField(ASN1Sequence aSN1Sequence) {
        this.m = ASN1Integer.getInstance((Object) aSN1Sequence.getObjectAt(0)).intPositiveValueExact();
        if (aSN1Sequence.getObjectAt(1) instanceof ASN1Integer) {
            this.k = ((ASN1Integer) aSN1Sequence.getObjectAt(1)).intPositiveValueExact();
        } else {
            if (!(aSN1Sequence.getObjectAt(1) instanceof ASN1Sequence)) {
                throw new IllegalArgumentException("object parse error");
            }
            ASN1Sequence aSN1Sequence2 = ASN1Sequence.getInstance(aSN1Sequence.getObjectAt(1));
            this.k = ASN1Integer.getInstance((Object) aSN1Sequence2.getObjectAt(0)).intPositiveValueExact();
            this.j = ASN1Integer.getInstance((Object) aSN1Sequence2.getObjectAt(1)).intPositiveValueExact();
            this.l = ASN1Integer.getInstance((Object) aSN1Sequence2.getObjectAt(2)).intPositiveValueExact();
        }
    }

    public static DSTU4145BinaryField getInstance(Object obj) {
        if (obj instanceof DSTU4145BinaryField) {
            return (DSTU4145BinaryField) obj;
        }
        if (obj != null) {
            return new DSTU4145BinaryField(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    public DSTU4145BinaryField(int i, int i2, int i3, int i4) {
        this.m = i;
        this.k = i2;
        this.j = i3;
        this.l = i4;
    }

    public int getM() {
        return this.m;
    }

    public int getK1() {
        return this.k;
    }

    public int getK2() {
        return this.j;
    }

    public int getK3() {
        return this.l;
    }

    public DSTU4145BinaryField(int i, int i2) {
        this(i, i2, 0, 0);
    }

    /* JADX WARN: Type inference failed for: r0v9, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(2);
        aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.m));
        if (this.j == 0) {
            aSN1EncodableVector.add((ASN1Encodable) new ASN1Integer(this.k));
        } else {
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector(3);
            aSN1EncodableVector2.add((ASN1Encodable) new ASN1Integer(this.k));
            aSN1EncodableVector2.add((ASN1Encodable) new ASN1Integer(this.j));
            aSN1EncodableVector2.add((ASN1Encodable) new ASN1Integer(this.l));
            aSN1EncodableVector.add((ASN1Encodable) new DERSequence(aSN1EncodableVector2));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
