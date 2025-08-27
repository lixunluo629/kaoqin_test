package io.netty.handler.ssl;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.Date;
import javax.security.cert.CertificateException;
import javax.security.cert.CertificateExpiredException;
import javax.security.cert.CertificateNotYetValidException;
import javax.security.cert.X509Certificate;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/ssl/OpenSslJavaxX509Certificate.class */
final class OpenSslJavaxX509Certificate extends X509Certificate {
    private final byte[] bytes;
    private X509Certificate wrapped;

    OpenSslJavaxX509Certificate(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override // javax.security.cert.X509Certificate
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        unwrap().checkValidity();
    }

    @Override // javax.security.cert.X509Certificate
    public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        unwrap().checkValidity(date);
    }

    @Override // javax.security.cert.X509Certificate
    public int getVersion() {
        return unwrap().getVersion();
    }

    @Override // javax.security.cert.X509Certificate
    public BigInteger getSerialNumber() {
        return unwrap().getSerialNumber();
    }

    @Override // javax.security.cert.X509Certificate
    public Principal getIssuerDN() {
        return unwrap().getIssuerDN();
    }

    @Override // javax.security.cert.X509Certificate
    public Principal getSubjectDN() {
        return unwrap().getSubjectDN();
    }

    @Override // javax.security.cert.X509Certificate
    public Date getNotBefore() {
        return unwrap().getNotBefore();
    }

    @Override // javax.security.cert.X509Certificate
    public Date getNotAfter() {
        return unwrap().getNotAfter();
    }

    @Override // javax.security.cert.X509Certificate
    public String getSigAlgName() {
        return unwrap().getSigAlgName();
    }

    @Override // javax.security.cert.X509Certificate
    public String getSigAlgOID() {
        return unwrap().getSigAlgOID();
    }

    @Override // javax.security.cert.X509Certificate
    public byte[] getSigAlgParams() {
        return unwrap().getSigAlgParams();
    }

    @Override // javax.security.cert.Certificate
    public byte[] getEncoded() {
        return (byte[]) this.bytes.clone();
    }

    @Override // javax.security.cert.Certificate
    public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        unwrap().verify(key);
    }

    @Override // javax.security.cert.Certificate
    public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        unwrap().verify(key, sigProvider);
    }

    @Override // javax.security.cert.Certificate
    public String toString() {
        return unwrap().toString();
    }

    @Override // javax.security.cert.Certificate
    public PublicKey getPublicKey() {
        return unwrap().getPublicKey();
    }

    private X509Certificate unwrap() throws CertificateException {
        X509Certificate wrapped = this.wrapped;
        if (wrapped == null) {
            try {
                X509Certificate x509Certificate = X509Certificate.getInstance(this.bytes);
                this.wrapped = x509Certificate;
                wrapped = x509Certificate;
            } catch (CertificateException e) {
                throw new IllegalStateException(e);
            }
        }
        return wrapped;
    }
}
