package org.bouncycastle.asn1.x9;

import java.math.BigInteger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.math.ec.ECCurve;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x9/X9Curve.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x9/X9Curve.class */
public class X9Curve extends ASN1Encodable implements X9ObjectIdentifiers {
    private ECCurve curve;
    private byte[] seed;
    private DERObjectIdentifier fieldIdentifier;

    public X9Curve(ECCurve eCCurve) {
        this.fieldIdentifier = null;
        this.curve = eCCurve;
        this.seed = null;
        setFieldIdentifier();
    }

    public X9Curve(ECCurve eCCurve, byte[] bArr) {
        this.fieldIdentifier = null;
        this.curve = eCCurve;
        this.seed = bArr;
        setFieldIdentifier();
    }

    public X9Curve(X9FieldID x9FieldID, ASN1Sequence aSN1Sequence) {
        int iIntValue;
        this.fieldIdentifier = null;
        this.fieldIdentifier = x9FieldID.getIdentifier();
        if (this.fieldIdentifier.equals(prime_field)) {
            BigInteger value = ((DERInteger) x9FieldID.getParameters()).getValue();
            this.curve = new ECCurve.Fp(value, new X9FieldElement(value, (ASN1OctetString) aSN1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(value, (ASN1OctetString) aSN1Sequence.getObjectAt(1)).getValue().toBigInteger());
        } else if (this.fieldIdentifier.equals(characteristic_two_field)) {
            DERSequence dERSequence = (DERSequence) x9FieldID.getParameters();
            int iIntValue2 = ((DERInteger) dERSequence.getObjectAt(0)).getValue().intValue();
            int iIntValue3 = 0;
            int iIntValue4 = 0;
            if (((DERObjectIdentifier) dERSequence.getObjectAt(1)).equals(tpBasis)) {
                iIntValue = ((DERInteger) dERSequence.getObjectAt(2)).getValue().intValue();
            } else {
                DERSequence dERSequence2 = (DERSequence) dERSequence.getObjectAt(2);
                iIntValue = ((DERInteger) dERSequence2.getObjectAt(0)).getValue().intValue();
                iIntValue3 = ((DERInteger) dERSequence2.getObjectAt(1)).getValue().intValue();
                iIntValue4 = ((DERInteger) dERSequence2.getObjectAt(2)).getValue().intValue();
            }
            this.curve = new ECCurve.F2m(iIntValue2, iIntValue, iIntValue3, iIntValue4, new X9FieldElement(iIntValue2, iIntValue, iIntValue3, iIntValue4, (ASN1OctetString) aSN1Sequence.getObjectAt(0)).getValue().toBigInteger(), new X9FieldElement(iIntValue2, iIntValue, iIntValue3, iIntValue4, (ASN1OctetString) aSN1Sequence.getObjectAt(1)).getValue().toBigInteger());
        }
        if (aSN1Sequence.size() == 3) {
            this.seed = ((DERBitString) aSN1Sequence.getObjectAt(2)).getBytes();
        }
    }

    private void setFieldIdentifier() {
        if (this.curve instanceof ECCurve.Fp) {
            this.fieldIdentifier = prime_field;
        } else {
            if (!(this.curve instanceof ECCurve.F2m)) {
                throw new IllegalArgumentException("This type of ECCurve is not implemented");
            }
            this.fieldIdentifier = characteristic_two_field;
        }
    }

    public ECCurve getCurve() {
        return this.curve;
    }

    public byte[] getSeed() {
        return this.seed;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.fieldIdentifier.equals(prime_field) || this.fieldIdentifier.equals(characteristic_two_field)) {
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getA()).getDERObject());
            aSN1EncodableVector.add(new X9FieldElement(this.curve.getB()).getDERObject());
        }
        if (this.seed != null) {
            aSN1EncodableVector.add(new DERBitString(this.seed));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
