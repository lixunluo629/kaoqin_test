package org.bouncycastle.crypto.tls;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/ServerOnlyTlsAuthentication.class */
public abstract class ServerOnlyTlsAuthentication implements TlsAuthentication {
    @Override // org.bouncycastle.crypto.tls.TlsAuthentication
    public final TlsCredentials getClientCredentials(CertificateRequest certificateRequest) {
        return null;
    }
}
