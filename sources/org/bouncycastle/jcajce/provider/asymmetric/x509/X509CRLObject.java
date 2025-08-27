package org.bouncycastle.jcajce.provider.asymmetric.x509;

import java.security.cert.CRLException;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.IssuingDistributionPoint;
import org.bouncycastle.jcajce.util.JcaJceHelper;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/jcajce/provider/asymmetric/x509/X509CRLObject.class */
class X509CRLObject extends X509CRLImpl {
    private final Object cacheLock;
    private X509CRLInternal internalCRLValue;
    private volatile boolean hashValueSet;
    private volatile int hashValue;

    X509CRLObject(JcaJceHelper jcaJceHelper, CertificateList certificateList) throws CRLException {
        super(jcaJceHelper, certificateList, createSigAlgName(certificateList), createSigAlgParams(certificateList), isIndirectCRL(certificateList));
        this.cacheLock = new Object();
    }

    /* JADX WARN: Type inference failed for: r0v13, types: [org.bouncycastle.asn1.ASN1BitString, org.bouncycastle.asn1.DERBitString] */
    /* JADX WARN: Type inference failed for: r1v7, types: [org.bouncycastle.asn1.ASN1Primitive, org.bouncycastle.asn1.DERBitString] */
    @Override // java.security.cert.X509CRL
    public boolean equals(Object obj) {
        ?? signature;
        if (this == obj) {
            return true;
        }
        if (obj instanceof X509CRLObject) {
            X509CRLObject x509CRLObject = (X509CRLObject) obj;
            if (this.hashValueSet && x509CRLObject.hashValueSet) {
                if (this.hashValue != x509CRLObject.hashValue) {
                    return false;
                }
            } else if ((null == this.internalCRLValue || null == x509CRLObject.internalCRLValue) && 0 != (signature = this.c.getSignature()) && !signature.equals(x509CRLObject.c.getSignature())) {
                return false;
            }
        }
        return getInternalCRL().equals(obj);
    }

    @Override // java.security.cert.X509CRL
    public int hashCode() {
        if (!this.hashValueSet) {
            this.hashValue = getInternalCRL().hashCode();
            this.hashValueSet = true;
        }
        return this.hashValue;
    }

    private X509CRLInternal getInternalCRL() {
        byte[] encoded;
        X509CRLInternal x509CRLInternal;
        synchronized (this.cacheLock) {
            if (null != this.internalCRLValue) {
                return this.internalCRLValue;
            }
            try {
                encoded = getEncoded();
            } catch (CRLException e) {
                encoded = null;
            }
            X509CRLInternal x509CRLInternal2 = new X509CRLInternal(this.bcHelper, this.c, this.sigAlgName, this.sigAlgParams, this.isIndirect, encoded);
            synchronized (this.cacheLock) {
                if (null == this.internalCRLValue) {
                    this.internalCRLValue = x509CRLInternal2;
                }
                x509CRLInternal = this.internalCRLValue;
            }
            return x509CRLInternal;
        }
    }

    private static String createSigAlgName(CertificateList certificateList) throws CRLException {
        try {
            return X509SignatureUtil.getSignatureName(certificateList.getSignatureAlgorithm());
        } catch (Exception e) {
            throw new CRLException("CRL contents invalid: " + e);
        }
    }

    private static byte[] createSigAlgParams(CertificateList certificateList) throws CRLException {
        try {
            ASN1Encodable parameters = certificateList.getSignatureAlgorithm().getParameters();
            if (null == parameters) {
                return null;
            }
            return parameters.toASN1Primitive().getEncoded("DER");
        } catch (Exception e) {
            throw new CRLException("CRL contents invalid: " + e);
        }
    }

    private static boolean isIndirectCRL(CertificateList certificateList) throws CRLException {
        try {
            byte[] extensionOctets = getExtensionOctets(certificateList, Extension.issuingDistributionPoint.getId());
            if (null == extensionOctets) {
                return false;
            }
            return IssuingDistributionPoint.getInstance(extensionOctets).isIndirectCRL();
        } catch (Exception e) {
            throw new ExtCRLException("Exception reading IssuingDistributionPoint", e);
        }
    }
}
