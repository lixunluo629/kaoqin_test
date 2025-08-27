package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateParsingException;
import java.util.Date;
import java.util.Enumeration;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.jcajce.provider.asymmetric.util.PKCS12BagAttributeCarrierImpl;
import org.bouncycastle.jcajce.util.JcaJceHelper;
import org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/x509/X509CertificateObject.class */
class X509CertificateObject extends X509CertificateImpl implements PKCS12BagAttributeCarrier {
    private final Object cacheLock;
    private X509CertificateInternal internalCertificateValue;
    private X500Principal issuerValue;
    private PublicKey publicKeyValue;
    private X500Principal subjectValue;
    private long[] validityValues;
    private volatile boolean hashValueSet;
    private volatile int hashValue;
    private PKCS12BagAttributeCarrier attrCarrier;

    X509CertificateObject(JcaJceHelper jcaJceHelper, Certificate certificate) throws CertificateParsingException {
        super(jcaJceHelper, certificate, createBasicConstraints(certificate), createKeyUsage(certificate));
        this.cacheLock = new Object();
        this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
    }

    @Override // org.bouncycastle.jcajce.provider.asymmetric.x509.X509CertificateImpl, java.security.cert.X509Certificate
    public void checkValidity(Date date) throws CertificateNotYetValidException, CertificateExpiredException {
        long time = date.getTime();
        long[] validityValues = getValidityValues();
        if (time > validityValues[1]) {
            throw new CertificateExpiredException("certificate expired on " + this.c.getEndDate().getTime());
        }
        if (time < validityValues[0]) {
            throw new CertificateNotYetValidException("certificate not valid till " + this.c.getStartDate().getTime());
        }
    }

    @Override // org.bouncycastle.jcajce.provider.asymmetric.x509.X509CertificateImpl, java.security.cert.X509Certificate
    public X500Principal getIssuerX500Principal() {
        X500Principal x500Principal;
        synchronized (this.cacheLock) {
            if (null != this.issuerValue) {
                return this.issuerValue;
            }
            X500Principal issuerX500Principal = super.getIssuerX500Principal();
            synchronized (this.cacheLock) {
                if (null == this.issuerValue) {
                    this.issuerValue = issuerX500Principal;
                }
                x500Principal = this.issuerValue;
            }
            return x500Principal;
        }
    }

    @Override // org.bouncycastle.jcajce.provider.asymmetric.x509.X509CertificateImpl, java.security.cert.Certificate
    public PublicKey getPublicKey() {
        PublicKey publicKey;
        synchronized (this.cacheLock) {
            if (null != this.publicKeyValue) {
                return this.publicKeyValue;
            }
            PublicKey publicKey2 = super.getPublicKey();
            if (null == publicKey2) {
                return null;
            }
            synchronized (this.cacheLock) {
                if (null == this.publicKeyValue) {
                    this.publicKeyValue = publicKey2;
                }
                publicKey = this.publicKeyValue;
            }
            return publicKey;
        }
    }

    @Override // org.bouncycastle.jcajce.provider.asymmetric.x509.X509CertificateImpl, java.security.cert.X509Certificate
    public X500Principal getSubjectX500Principal() {
        X500Principal x500Principal;
        synchronized (this.cacheLock) {
            if (null != this.subjectValue) {
                return this.subjectValue;
            }
            X500Principal subjectX500Principal = super.getSubjectX500Principal();
            synchronized (this.cacheLock) {
                if (null == this.subjectValue) {
                    this.subjectValue = subjectX500Principal;
                }
                x500Principal = this.subjectValue;
            }
            return x500Principal;
        }
    }

    public long[] getValidityValues() {
        long[] jArr;
        synchronized (this.cacheLock) {
            if (null != this.validityValues) {
                return this.validityValues;
            }
            long[] jArr2 = {super.getNotBefore().getTime(), super.getNotAfter().getTime()};
            synchronized (this.cacheLock) {
                if (null == this.validityValues) {
                    this.validityValues = jArr2;
                }
                jArr = this.validityValues;
            }
            return jArr;
        }
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    /* JADX WARN: Type inference failed for: r1v7, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERBitString] */
    @Override // java.security.cert.Certificate
    public boolean equals(Object obj) {
        ?? signature;
        if (obj == this) {
            return true;
        }
        if (obj instanceof X509CertificateObject) {
            X509CertificateObject x509CertificateObject = (X509CertificateObject) obj;
            if (this.hashValueSet && x509CertificateObject.hashValueSet) {
                if (this.hashValue != x509CertificateObject.hashValue) {
                    return false;
                }
            } else if ((null == this.internalCertificateValue || null == x509CertificateObject.internalCertificateValue) && 0 != (signature = this.c.getSignature()) && !signature.equals(x509CertificateObject.c.getSignature())) {
                return false;
            }
        }
        return getInternalCertificate().equals(obj);
    }

    @Override // java.security.cert.Certificate
    public int hashCode() {
        if (!this.hashValueSet) {
            this.hashValue = getInternalCertificate().hashCode();
            this.hashValueSet = true;
        }
        return this.hashValue;
    }

    public int originalHashCode() {
        try {
            int i = 0;
            byte[] encoded = getInternalCertificate().getEncoded();
            for (int i2 = 1; i2 < encoded.length; i2++) {
                i += encoded[i2] * i2;
            }
            return i;
        } catch (CertificateEncodingException e) {
            return 0;
        }
    }

    public void setBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier, ASN1Encodable aSN1Encodable) {
        this.attrCarrier.setBagAttribute(aSN1ObjectIdentifier, aSN1Encodable);
    }

    public ASN1Encodable getBagAttribute(ASN1ObjectIdentifier aSN1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(aSN1ObjectIdentifier);
    }

    @Override // org.bouncycastle.jce.interfaces.PKCS12BagAttributeCarrier
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }

    private X509CertificateInternal getInternalCertificate() {
        byte[] encoded;
        X509CertificateInternal x509CertificateInternal;
        synchronized (this.cacheLock) {
            if (null != this.internalCertificateValue) {
                return this.internalCertificateValue;
            }
            try {
                encoded = getEncoded();
            } catch (CertificateEncodingException e) {
                encoded = null;
            }
            X509CertificateInternal x509CertificateInternal2 = new X509CertificateInternal(this.bcHelper, this.c, this.basicConstraints, this.keyUsage, encoded);
            synchronized (this.cacheLock) {
                if (null == this.internalCertificateValue) {
                    this.internalCertificateValue = x509CertificateInternal2;
                }
                x509CertificateInternal = this.internalCertificateValue;
            }
            return x509CertificateInternal;
        }
    }

    private static BasicConstraints createBasicConstraints(Certificate certificate) throws CertificateParsingException {
        try {
            byte[] extensionOctets = getExtensionOctets(certificate, "2.5.29.19");
            if (null == extensionOctets) {
                return null;
            }
            return BasicConstraints.getInstance(ASN1Primitive.fromByteArray(extensionOctets));
        } catch (Exception e) {
            throw new CertificateParsingException("cannot construct BasicConstraints: " + e);
        }
    }

    /* JADX WARN: Type inference failed for: r0v6, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    private static boolean[] createKeyUsage(Certificate certificate) throws CertificateParsingException {
        try {
            byte[] extensionOctets = getExtensionOctets(certificate, "2.5.29.15");
            if (null == extensionOctets) {
                return null;
            }
            ?? dERBitString = DERBitString.getInstance(ASN1Primitive.fromByteArray(extensionOctets));
            byte[] bytes = dERBitString.getBytes();
            int length = (bytes.length * 8) - dERBitString.getPadBits();
            boolean[] zArr = new boolean[length < 9 ? 9 : length];
            for (int i = 0; i != length; i++) {
                zArr[i] = (bytes[i / 8] & (128 >>> (i % 8))) != 0;
            }
            return zArr;
        } catch (Exception e) {
            throw new CertificateParsingException("cannot construct KeyUsage: " + e);
        }
    }
}
