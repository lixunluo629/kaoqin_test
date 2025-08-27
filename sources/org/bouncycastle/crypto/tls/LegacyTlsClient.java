package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/LegacyTlsClient.class */
public class LegacyTlsClient extends DefaultTlsClient {
    protected CertificateVerifyer verifyer;

    public LegacyTlsClient(CertificateVerifyer certificateVerifyer) {
        this.verifyer = certificateVerifyer;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClient
    public TlsAuthentication getAuthentication() throws IOException {
        return new LegacyTlsAuthentication(this.verifyer);
    }
}
