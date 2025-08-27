package org.bouncycastle.asn1.eac;

import java.io.IOException;
import org.bouncycastle.asn1.ASN1ApplicationSpecific;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERApplicationSpecific;
import org.bouncycastle.asn1.DEROctetString;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/eac/CertificateBody.class */
public class CertificateBody extends ASN1Object {
    ASN1InputStream seq;
    private ASN1ApplicationSpecific certificateProfileIdentifier;
    private ASN1ApplicationSpecific certificationAuthorityReference;
    private PublicKeyDataObject publicKey;
    private ASN1ApplicationSpecific certificateHolderReference;
    private CertificateHolderAuthorization certificateHolderAuthorization;
    private ASN1ApplicationSpecific certificateEffectiveDate;
    private ASN1ApplicationSpecific certificateExpirationDate;
    private int certificateType = 0;
    private static final int CPI = 1;
    private static final int CAR = 2;
    private static final int PK = 4;
    private static final int CHR = 8;
    private static final int CHA = 16;
    private static final int CEfD = 32;
    private static final int CExD = 64;
    public static final int profileType = 127;
    public static final int requestType = 13;

    private void setIso7816CertificateBody(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IOException, IllegalArgumentException {
        if (aSN1ApplicationSpecific.getApplicationTag() != 78) {
            throw new IOException("Bad tag : not an iso7816 CERTIFICATE_CONTENT_TEMPLATE");
        }
        ASN1InputStream aSN1InputStream = new ASN1InputStream(aSN1ApplicationSpecific.getContents());
        while (true) {
            ASN1Primitive object = aSN1InputStream.readObject();
            if (object == null) {
                aSN1InputStream.close();
                return;
            }
            if (!(object instanceof ASN1ApplicationSpecific)) {
                throw new IOException("Not a valid iso7816 content : not a ASN1ApplicationSpecific Object :" + EACTags.encodeTag(aSN1ApplicationSpecific) + object.getClass());
            }
            ASN1ApplicationSpecific aSN1ApplicationSpecific2 = (ASN1ApplicationSpecific) object;
            switch (aSN1ApplicationSpecific2.getApplicationTag()) {
                case 2:
                    setCertificationAuthorityReference(aSN1ApplicationSpecific2);
                    break;
                case 32:
                    setCertificateHolderReference(aSN1ApplicationSpecific2);
                    break;
                case 36:
                    setCertificateExpirationDate(aSN1ApplicationSpecific2);
                    break;
                case 37:
                    setCertificateEffectiveDate(aSN1ApplicationSpecific2);
                    break;
                case 41:
                    setCertificateProfileIdentifier(aSN1ApplicationSpecific2);
                    break;
                case 73:
                    setPublicKey(PublicKeyDataObject.getInstance(aSN1ApplicationSpecific2.getObject(16)));
                    break;
                case 76:
                    setCertificateHolderAuthorization(new CertificateHolderAuthorization(aSN1ApplicationSpecific2));
                    break;
                default:
                    this.certificateType = 0;
                    throw new IOException("Not a valid iso7816 ASN1ApplicationSpecific tag " + aSN1ApplicationSpecific2.getApplicationTag());
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1ApplicationSpecific, org.bouncycastle.asn1.DERApplicationSpecific] */
    /* JADX WARN: Type inference failed for: r1v4, types: [org.bouncycastle.asn1.ASN1ApplicationSpecific, org.bouncycastle.asn1.DERApplicationSpecific] */
    /* JADX WARN: Type inference failed for: r1v7, types: [org.bouncycastle.asn1.ASN1ApplicationSpecific, org.bouncycastle.asn1.DERApplicationSpecific] */
    /* JADX WARN: Type inference failed for: r1v8, types: [org.bouncycastle.asn1.ASN1ApplicationSpecific, org.bouncycastle.asn1.DERApplicationSpecific] */
    public CertificateBody(ASN1ApplicationSpecific aSN1ApplicationSpecific, CertificationAuthorityReference certificationAuthorityReference, PublicKeyDataObject publicKeyDataObject, CertificateHolderReference certificateHolderReference, CertificateHolderAuthorization certificateHolderAuthorization, PackedDate packedDate, PackedDate packedDate2) throws IllegalArgumentException {
        setCertificateProfileIdentifier(aSN1ApplicationSpecific);
        setCertificationAuthorityReference(new DERApplicationSpecific(2, certificationAuthorityReference.getEncoded()));
        setPublicKey(publicKeyDataObject);
        setCertificateHolderReference(new DERApplicationSpecific(32, certificateHolderReference.getEncoded()));
        setCertificateHolderAuthorization(certificateHolderAuthorization);
        try {
            setCertificateEffectiveDate(new DERApplicationSpecific(false, 37, (ASN1Encodable) new DEROctetString(packedDate.getEncoding())));
            setCertificateExpirationDate(new DERApplicationSpecific(false, 36, (ASN1Encodable) new DEROctetString(packedDate2.getEncoding())));
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to encode dates: " + e.getMessage());
        }
    }

    private CertificateBody(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IOException, IllegalArgumentException {
        setIso7816CertificateBody(aSN1ApplicationSpecific);
    }

    /* JADX WARN: Type inference failed for: r0v8, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERApplicationSpecific] */
    private ASN1Primitive profileToASN1Object() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(7);
        aSN1EncodableVector.add((ASN1Encodable) this.certificateProfileIdentifier);
        aSN1EncodableVector.add((ASN1Encodable) this.certificationAuthorityReference);
        aSN1EncodableVector.add((ASN1Encodable) new DERApplicationSpecific(false, 73, (ASN1Encodable) this.publicKey));
        aSN1EncodableVector.add((ASN1Encodable) this.certificateHolderReference);
        aSN1EncodableVector.add((ASN1Encodable) this.certificateHolderAuthorization);
        aSN1EncodableVector.add((ASN1Encodable) this.certificateEffectiveDate);
        aSN1EncodableVector.add((ASN1Encodable) this.certificateExpirationDate);
        return new DERApplicationSpecific(78, aSN1EncodableVector);
    }

    private void setCertificateProfileIdentifier(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IllegalArgumentException {
        if (aSN1ApplicationSpecific.getApplicationTag() != 41) {
            throw new IllegalArgumentException("Not an Iso7816Tags.INTERCHANGE_PROFILE tag :" + EACTags.encodeTag(aSN1ApplicationSpecific));
        }
        this.certificateProfileIdentifier = aSN1ApplicationSpecific;
        this.certificateType |= 1;
    }

    private void setCertificateHolderReference(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IllegalArgumentException {
        if (aSN1ApplicationSpecific.getApplicationTag() != 32) {
            throw new IllegalArgumentException("Not an Iso7816Tags.CARDHOLDER_NAME tag");
        }
        this.certificateHolderReference = aSN1ApplicationSpecific;
        this.certificateType |= 8;
    }

    private void setCertificationAuthorityReference(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IllegalArgumentException {
        if (aSN1ApplicationSpecific.getApplicationTag() != 2) {
            throw new IllegalArgumentException("Not an Iso7816Tags.ISSUER_IDENTIFICATION_NUMBER tag");
        }
        this.certificationAuthorityReference = aSN1ApplicationSpecific;
        this.certificateType |= 2;
    }

    private void setPublicKey(PublicKeyDataObject publicKeyDataObject) {
        this.publicKey = PublicKeyDataObject.getInstance(publicKeyDataObject);
        this.certificateType |= 4;
    }

    /* JADX WARN: Type inference failed for: r0v4, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERApplicationSpecific] */
    private ASN1Primitive requestToASN1Object() throws IOException {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(3);
        aSN1EncodableVector.add((ASN1Encodable) this.certificateProfileIdentifier);
        aSN1EncodableVector.add((ASN1Encodable) new DERApplicationSpecific(false, 73, (ASN1Encodable) this.publicKey));
        aSN1EncodableVector.add((ASN1Encodable) this.certificateHolderReference);
        return new DERApplicationSpecific(78, aSN1EncodableVector);
    }

    public ASN1Primitive toASN1Primitive() {
        try {
            if (this.certificateType == 127) {
                return profileToASN1Object();
            }
            if (this.certificateType == 13) {
                return requestToASN1Object();
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public int getCertificateType() {
        return this.certificateType;
    }

    public static CertificateBody getInstance(Object obj) throws IOException {
        if (obj instanceof CertificateBody) {
            return (CertificateBody) obj;
        }
        if (obj != null) {
            return new CertificateBody(ASN1ApplicationSpecific.getInstance(obj));
        }
        return null;
    }

    public PackedDate getCertificateEffectiveDate() {
        if ((this.certificateType & 32) == 32) {
            return new PackedDate(this.certificateEffectiveDate.getContents());
        }
        return null;
    }

    private void setCertificateEffectiveDate(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IllegalArgumentException {
        if (aSN1ApplicationSpecific.getApplicationTag() != 37) {
            throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EFFECTIVE_DATE tag :" + EACTags.encodeTag(aSN1ApplicationSpecific));
        }
        this.certificateEffectiveDate = aSN1ApplicationSpecific;
        this.certificateType |= 32;
    }

    public PackedDate getCertificateExpirationDate() throws IOException {
        if ((this.certificateType & 64) == 64) {
            return new PackedDate(this.certificateExpirationDate.getContents());
        }
        throw new IOException("certificate Expiration Date not set");
    }

    private void setCertificateExpirationDate(ASN1ApplicationSpecific aSN1ApplicationSpecific) throws IllegalArgumentException {
        if (aSN1ApplicationSpecific.getApplicationTag() != 36) {
            throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EXPIRATION_DATE tag");
        }
        this.certificateExpirationDate = aSN1ApplicationSpecific;
        this.certificateType |= 64;
    }

    public CertificateHolderAuthorization getCertificateHolderAuthorization() throws IOException {
        if ((this.certificateType & 16) == 16) {
            return this.certificateHolderAuthorization;
        }
        throw new IOException("Certificate Holder Authorisation not set");
    }

    private void setCertificateHolderAuthorization(CertificateHolderAuthorization certificateHolderAuthorization) {
        this.certificateHolderAuthorization = certificateHolderAuthorization;
        this.certificateType |= 16;
    }

    public CertificateHolderReference getCertificateHolderReference() {
        return new CertificateHolderReference(this.certificateHolderReference.getContents());
    }

    public ASN1ApplicationSpecific getCertificateProfileIdentifier() {
        return this.certificateProfileIdentifier;
    }

    public CertificationAuthorityReference getCertificationAuthorityReference() throws IOException {
        if ((this.certificateType & 2) == 2) {
            return new CertificationAuthorityReference(this.certificationAuthorityReference.getContents());
        }
        throw new IOException("Certification authority reference not set");
    }

    public PublicKeyDataObject getPublicKey() {
        return this.publicKey;
    }
}
