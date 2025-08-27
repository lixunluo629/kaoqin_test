package org.bouncycastle.asn1.esf;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTF8String;
import org.bouncycastle.asn1.x500.DirectoryString;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/esf/SignerLocation.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/esf/SignerLocation.class */
public class SignerLocation extends ASN1Encodable {
    private DERUTF8String countryName;
    private DERUTF8String localityName;
    private ASN1Sequence postalAddress;

    public SignerLocation(ASN1Sequence aSN1Sequence) {
        Enumeration objects = aSN1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            DERTaggedObject dERTaggedObject = (DERTaggedObject) objects.nextElement();
            switch (dERTaggedObject.getTagNo()) {
                case 0:
                    this.countryName = new DERUTF8String(DirectoryString.getInstance(dERTaggedObject, true).getString());
                    break;
                case 1:
                    this.localityName = new DERUTF8String(DirectoryString.getInstance(dERTaggedObject, true).getString());
                    break;
                case 2:
                    if (dERTaggedObject.isExplicit()) {
                        this.postalAddress = ASN1Sequence.getInstance(dERTaggedObject, true);
                    } else {
                        this.postalAddress = ASN1Sequence.getInstance(dERTaggedObject, false);
                    }
                    if (this.postalAddress != null && this.postalAddress.size() > 6) {
                        throw new IllegalArgumentException("postal address must contain less than 6 strings");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("illegal tag");
            }
        }
    }

    public SignerLocation(DERUTF8String dERUTF8String, DERUTF8String dERUTF8String2, ASN1Sequence aSN1Sequence) {
        if (aSN1Sequence != null && aSN1Sequence.size() > 6) {
            throw new IllegalArgumentException("postal address must contain less than 6 strings");
        }
        if (dERUTF8String != null) {
            this.countryName = DERUTF8String.getInstance(dERUTF8String.toASN1Object());
        }
        if (dERUTF8String2 != null) {
            this.localityName = DERUTF8String.getInstance(dERUTF8String2.toASN1Object());
        }
        if (aSN1Sequence != null) {
            this.postalAddress = ASN1Sequence.getInstance(aSN1Sequence.toASN1Object());
        }
    }

    public static SignerLocation getInstance(Object obj) {
        return (obj == null || (obj instanceof SignerLocation)) ? (SignerLocation) obj : new SignerLocation(ASN1Sequence.getInstance(obj));
    }

    public DERUTF8String getCountryName() {
        return this.countryName;
    }

    public DERUTF8String getLocalityName() {
        return this.localityName;
    }

    public ASN1Sequence getPostalAddress() {
        return this.postalAddress;
    }

    @Override // org.bouncycastle.asn1.ASN1Encodable
    public DERObject toASN1Object() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        if (this.countryName != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 0, this.countryName));
        }
        if (this.localityName != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 1, this.localityName));
        }
        if (this.postalAddress != null) {
            aSN1EncodableVector.add(new DERTaggedObject(true, 2, this.postalAddress));
        }
        return new DERSequence(aSN1EncodableVector);
    }
}
