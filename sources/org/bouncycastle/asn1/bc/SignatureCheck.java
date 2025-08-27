package org.bouncycastle.asn1.bc;

import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/asn1/bc/SignatureCheck.class */
public class SignatureCheck extends ASN1Object {
    private final AlgorithmIdentifier signatureAlgorithm;
    private final ASN1Sequence certificates;
    private final ASN1BitString signatureValue;

    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    public SignatureCheck(AlgorithmIdentifier algorithmIdentifier, byte[] bArr) {
        this.signatureAlgorithm = algorithmIdentifier;
        this.certificates = null;
        this.signatureValue = new DERBitString(Arrays.clone(bArr));
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    public SignatureCheck(AlgorithmIdentifier algorithmIdentifier, Certificate[] certificateArr, byte[] bArr) {
        this.signatureAlgorithm = algorithmIdentifier;
        this.certificates = new DERSequence(certificateArr);
        this.signatureValue = new DERBitString(Arrays.clone(bArr));
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    private SignatureCheck(ASN1Sequence aSN1Sequence) {
        this.signatureAlgorithm = AlgorithmIdentifier.getInstance(aSN1Sequence.getObjectAt(0));
        int i = 1;
        if (aSN1Sequence.getObjectAt(1) instanceof ASN1TaggedObject) {
            i = 1 + 1;
            this.certificates = ASN1Sequence.getInstance(ASN1TaggedObject.getInstance(aSN1Sequence.getObjectAt(1)).getObject());
        } else {
            this.certificates = null;
        }
        this.signatureValue = DERBitString.getInstance(aSN1Sequence.getObjectAt(i));
    }

    public static SignatureCheck getInstance(Object obj) {
        if (obj instanceof SignatureCheck) {
            return (SignatureCheck) obj;
        }
        if (obj != null) {
            return new SignatureCheck(ASN1Sequence.getInstance(obj));
        }
        return null;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    public ASN1BitString getSignature() {
        return new DERBitString(this.signatureValue.getBytes(), this.signatureValue.getPadBits());
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return this.signatureAlgorithm;
    }

    public Certificate[] getCertificates() {
        if (this.certificates == null) {
            return null;
        }
        Certificate[] certificateArr = new Certificate[this.certificates.size()];
        for (int i = 0; i != certificateArr.length; i++) {
            certificateArr[i] = Certificate.getInstance(this.certificates.getObjectAt(i));
        }
        return certificateArr;
    }

    /* JADX WARN: Type inference failed for: r0v5, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERSequence] */
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector aSN1EncodableVector = new ASN1EncodableVector(3);
        aSN1EncodableVector.add((ASN1Encodable) this.signatureAlgorithm);
        if (this.certificates != null) {
            aSN1EncodableVector.add((ASN1Encodable) new DERTaggedObject(0, (ASN1Encodable) this.certificates));
        }
        aSN1EncodableVector.add((ASN1Encodable) this.signatureValue);
        return new DERSequence(aSN1EncodableVector);
    }
}
