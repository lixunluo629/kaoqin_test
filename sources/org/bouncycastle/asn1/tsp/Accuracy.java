package org.bouncycastle.asn1.tsp;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/tsp/Accuracy.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/tsp/Accuracy.class */
public class Accuracy extends ASN1Encodable {
    DERInteger seconds;
    DERInteger millis;
    DERInteger micros;
    protected static final int MIN_MILLIS = 1;
    protected static final int MAX_MILLIS = 999;
    protected static final int MIN_MICROS = 1;
    protected static final int MAX_MICROS = 999;

    protected Accuracy() {
    }

    public Accuracy(DERInteger dERInteger, DERInteger dERInteger2, DERInteger dERInteger3) {
        this.seconds = dERInteger;
        if (dERInteger2 != null && (dERInteger2.getValue().intValue() < 1 || dERInteger2.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid millis field : not in (1..999)");
        }
        this.millis = dERInteger2;
        if (dERInteger3 != null && (dERInteger3.getValue().intValue() < 1 || dERInteger3.getValue().intValue() > 999)) {
            throw new IllegalArgumentException("Invalid micros field : not in (1..999)");
        }
        this.micros = dERInteger3;
    }

    public Accuracy(ASN1Sequence aSN1Sequence) {
        this.seconds = null;
        this.millis = null;
        this.micros = null;
        for (int i = 0; i < aSN1Sequence.size(); i++) {
            if (aSN1Sequence.getObjectAt(i) instanceof DERInteger) {
                this.seconds = (DERInteger) aSN1Sequence.getObjectAt(i);
            } else if (aSN1Sequence.getObjectAt(i) instanceof DERTaggedObject) {
                DERTaggedObject dERTaggedObject = (DERTaggedObject) aSN1Sequence.getObjectAt(i);
                switch (dERTaggedObject.getTagNo()) {
                    case 0:
                        this.millis = DERInteger.getInstance(dERTaggedObject, false);
                        if (this.millis.getValue().intValue() < 1 || this.millis.getValue().intValue() > 999) {
                            throw new IllegalArgumentException("Invalid millis field : not in (1..999).");
                        }
                        break;
                    case 1:
                        this.micros = DERInteger.getInstance(dERTaggedObject, false);
                        if (this.micros.getValue().intValue() < 1 || this.micros.getValue().intValue() > 999) {
                            throw new IllegalArgumentException("Invalid micros field : not in (1..999).");
                        }
                        break;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalig tag number");
                }
            } else {
                continue;
            }
        }
    }

    public static Accuracy getInstance(Object obj) {
        if (obj == null || (obj instanceof Accuracy)) {
            return (Accuracy) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new Accuracy((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Unknown object in 'Accuracy' factory : " + obj.getClass().getName() + ".");
    }

    public DERInteger getSeconds() {
        return this.seconds;
    }

    public DERInteger getMillis() {
        return this.millis;
    }

    public DERInteger getMicros() {
        return this.micros;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.seconds != null) {
            aSN1EncodableVector.add(this.seconds);
        }
        if (this.millis != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 0, this.millis));
        }
        if (this.micros != null) {
            aSN1EncodableVector.add(new DERTaggedObject(false, 1, this.micros));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
