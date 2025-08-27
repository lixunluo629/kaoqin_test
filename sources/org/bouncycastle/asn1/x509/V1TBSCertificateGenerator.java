package org.bouncycastle.asn1.x509;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.x500.X500Name;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/x509/V1TBSCertificateGenerator.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/asn1/x509/V1TBSCertificateGenerator.class */
public class V1TBSCertificateGenerator {
    DERTaggedObject version = new DERTaggedObject(0, new DERInteger(0));
    DERInteger serialNumber;
    AlgorithmIdentifier signature;
    X509Name issuer;
    Time startDate;
    Time endDate;
    X509Name subject;
    SubjectPublicKeyInfo subjectPublicKeyInfo;

    public void setSerialNumber(DERInteger dERInteger) {
        this.serialNumber = dERInteger;
    }

    public void setSignature(AlgorithmIdentifier algorithmIdentifier) {
        this.signature = algorithmIdentifier;
    }

    public void setIssuer(X509Name x509Name) {
        this.issuer = x509Name;
    }

    public void setIssuer(X500Name x500Name) {
        this.issuer = X509Name.getInstance(x500Name.getDERObject());
    }

    public void setStartDate(Time time) {
        this.startDate = time;
    }

    public void setStartDate(DERUTCTime dERUTCTime) {
        this.startDate = new Time(dERUTCTime);
    }

    public void setEndDate(Time time) {
        this.endDate = time;
    }

    public void setEndDate(DERUTCTime dERUTCTime) {
        this.endDate = new Time(dERUTCTime);
    }

    public void setSubject(X509Name x509Name) {
        this.subject = x509Name;
    }

    public void setSubject(X500Name x500Name) {
        this.subject = X509Name.getInstance(x500Name.getDERObject());
    }

    public void setSubjectPublicKeyInfo(SubjectPublicKeyInfo subjectPublicKeyInfo) {
        this.subjectPublicKeyInfo = subjectPublicKeyInfo;
    }

    public TBSCertificateStructure generateTBSCertificate() {
        if (this.serialNumber == null || this.signature == null || this.issuer == null || this.startDate == null || this.endDate == null || this.subject == null || this.subjectPublicKeyInfo == null) {
            throw new IllegalStateException("not all mandatory fields set in V1 TBScertificate generator");
        }
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector();
        aSN1EncodableVector.add(this.serialNumber);
        aSN1EncodableVector.add(this.signature);
        aSN1EncodableVector.add(this.issuer);
        ASN1EncodableVector aSN1EncodableVector2 = new ASN1EncodableVector();
        aSN1EncodableVector2.add(this.startDate);
        aSN1EncodableVector2.add(this.endDate);
        aSN1EncodableVector.add(new DERSequence(aSN1EncodableVector2));
        aSN1EncodableVector.add(this.subject);
        aSN1EncodableVector.add(this.subjectPublicKeyInfo);
        return new TBSCertificateStructure(new DERSequence(aSN1EncodableVector));
    }
}
