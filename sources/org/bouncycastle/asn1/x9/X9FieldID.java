package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x9/X9FieldID.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x9/X9FieldID.class */
public class X9FieldID extends ASN1Encodable implements X9ObjectIdentifiers {
    private DERObjectIdentifier id;
    private DERObject parameters;

    public X9FieldID(BigInteger bigInteger) {
        this.id = prime_field;
        this.parameters = new DERInteger(bigInteger);
    }

    public X9FieldID(int i, int i2, int i3, int i4) {
        this.id = characteristic_two_field;
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(new DERInteger(i));
        if (i3 == 0) {
            aSN1EncodableVector.add(tpBasis);
            aSN1EncodableVector.add(new DERInteger(i2));
        } else {
            aSN1EncodableVector.add(ppBasis);
            ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
            aSN1EncodableVector2.add(new DERInteger(i2));
            aSN1EncodableVector2.add(new DERInteger(i3));
            aSN1EncodableVector2.add(new DERInteger(i4));
            aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        }
        this.parameters = new DERSequence(aSN1EncodableVector);
    }

    public X9FieldID(ASN1Sequence aSN1Sequence) {
        this.id = (DERObjectIdentifier) aSN1Sequence.getObjectAt(0);
        this.parameters = (DERObject) aSN1Sequence.getObjectAt(1);
    }

    public DERObjectIdentifier getIdentifier() {
        return this.id;
    }

    public DERObject getParameters() {
        return this.parameters;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.id);
        aSN1EncodableVector.add(this.parameters);
        return new DERSequence(aSN1EncodableVector);
    }
}
