package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/LegacyTlsAuthentication.class */
public class LegacyTlsAuthentication implements TlsAuthentication {
    protected CertificateVerifyer verifyer;

    public LegacyTlsAuthentication(CertificateVerifyer certificateVerifyer) {
        this.verifyer = certificateVerifyer;
    }

    @Override // org.bouncycastle.crypto.tls.TlsAuthentication
    public void notifyServerCertificate(Certificate certificate) throws IOException {
        if (!this.verifyer.isValid(certificate.getCerts())) {
            throw new TlsFatalAlert((short) 90);
        }
    }

    @Override // org.bouncycastle.crypto.tls.TlsAuthentication
    public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException {
        return null;
    }
}
