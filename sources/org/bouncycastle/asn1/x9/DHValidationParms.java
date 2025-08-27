package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x9/DHValidationParms.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x9/DHValidationParms.class */
public class DHValidationParms extends ASN1Encodable {
    private DERBitString seed;
    private DERInteger pgenCounter;

    public static DHValidationParms getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static DHValidationParms getInstance(Object obj) {
        if (obj == null || (obj instanceof DHDomainParameters)) {
            return (DHValidationParms) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new DHValidationParms((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid DHValidationParms: " + obj.getClass().getName());
    }

    public DHValidationParms(DERBitString dERBitString, DERInteger dERInteger) {
        if (dERBitString == null) {
            throw new IllegalArgumentException("'seed' cannot be null");
        }
        if (dERInteger == null) {
            throw new IllegalArgumentException("'pgenCounter' cannot be null");
        }
        this.seed = dERBitString;
        this.pgenCounter = dERInteger;
    }

    private DHValidationParms(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.seed = DERBitString.getInstance(aSN1Sequence.getObjectAt(0));
        this.pgenCounter = DERInteger.getInstance(aSN1Sequence.getObjectAt(1));
    }

    public DERBitString getSeed() {
        return this.seed;
    }

    public DERInteger getPgenCounter() {
        return this.pgenCounter;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.seed);
        aSN1EncodableVector.add(this.pgenCounter);
        return new DERSequence(aSN1EncodableVector);
    }
}
