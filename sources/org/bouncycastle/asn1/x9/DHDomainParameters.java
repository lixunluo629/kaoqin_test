package org.bouncycastle.asn1.x9;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x9/DHDomainParameters.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x9/DHDomainParameters.class */
public class DHDomainParameters extends ASN1Encodable {
    private DERInteger p;
    private DERInteger g;
    private DERInteger q;
    private DERInteger j;
    private DHValidationParms validationParms;

    public static DHDomainParameters getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public static DHDomainParameters getInstance(Object obj) {
        if (obj == null || (obj instanceof DHDomainParameters)) {
            return (DHDomainParameters) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new DHDomainParameters((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("Invalid DHDomainParameters: " + obj.getClass().getName());
    }

    public DHDomainParameters(DERInteger dERInteger, DERInteger dERInteger2, DERInteger dERInteger3, DERInteger dERInteger4, DHValidationParms dHValidationParms) {
        if (dERInteger == null) {
            throw new IllegalArgumentException("'p' cannot be null");
        }
        if (dERInteger2 == null) {
            throw new IllegalArgumentException("'g' cannot be null");
        }
        if (dERInteger3 == null) {
            throw new IllegalArgumentException("'q' cannot be null");
        }
        this.p = dERInteger;
        this.g = dERInteger2;
        this.q = dERInteger3;
        this.j = dERInteger4;
        this.validationParms = dHValidationParms;
    }

    private DHDomainParameters(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() < 3 || aSN1Sequence.size() > 5) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        Enumeration objects = aSN1Sequence.getObjects();
        this.p = DERInteger.getInstance(objects.nextElement());
        this.g = DERInteger.getInstance(objects.nextElement());
        this.q = DERInteger.getInstance(objects.nextElement());
        DEREncodable next = getNext(objects);
        if (next != null && (next instanceof DERInteger)) {
            this.j = DERInteger.getInstance(next);
            next = getNext(objects);
        }
        if (next != null) {
            this.validationParms = DHValidationParms.getInstance(next.getDERObject());
        }
    }

    private static DEREncodable getNext(Enumeration enumeration) {
        if (enumeration.hasMoreElements()) {
            return (DEREncodable) enumeration.nextElement();
        }
        return null;
    }

    public DERInteger getP() {
        return this.p;
    }

    public DERInteger getG() {
        return this.g;
    }

    public DERInteger getQ() {
        return this.q;
    }

    public DERInteger getJ() {
        return this.j;
    }

    public DHValidationParms getValidationParms() {
        return this.validationParms;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.p);
        aSN1EncodableVector.add(this.g);
        aSN1EncodableVector.add(this.q);
        if (this.j != null) {
            aSN1EncodableVector.add(this.j);
        }
        if (this.validationParms != null) {
            aSN1EncodableVector.add(this.validationParms);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
