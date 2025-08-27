package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/IssuerSerial.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/IssuerSerial.class */
public class IssuerSerial extends ASN1Encodable {
    GeneralNames issuer;
    DERInteger serial;
    DERBitString issuerUID;

    public static IssuerSerial getInstance(Object obj) {
        if (obj == null || (obj instanceof IssuerSerial)) {
            return (IssuerSerial) obj;
        }
        if (obj instanceof ASN1Sequence) {
            return new IssuerSerial((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    public static IssuerSerial getInstance(ASN1TaggedObject aSN1TaggedObject, boolean z) {
        return getInstance(ASN1Sequence.getInstance(aSN1TaggedObject, z));
    }

    public IssuerSerial(ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence.size() != 2 && aSN1Sequence.size() != 3) {
            throw new IllegalArgumentException("Bad sequence size: " + aSN1Sequence.size());
        }
        this.issuer = GeneralNames.getInstance(aSN1Sequence.getObjectAt(0));
        this.serial = DERInteger.getInstance(aSN1Sequence.getObjectAt(1));
        if (aSN1Sequence.size() == 3) {
            this.issuerUID = DERBitString.getInstance(aSN1Sequence.getObjectAt(2));
        }
    }

    public IssuerSerial(GeneralNames generalNames, DERInteger dERInteger) {
        this.issuer = generalNames;
        this.serial = dERInteger;
    }

    public GeneralNames getIssuer() {
        return this.issuer;
    }

    public DERInteger getSerial() {
        return this.serial;
    }

    public DERBitString getIssuerUID() {
        return this.issuerUID;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.issuer);
        aSN1EncodableVector.add(this.serial);
        if (this.issuerUID != null) {
            aSN1EncodableVector.add(this.issuerUID);
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
