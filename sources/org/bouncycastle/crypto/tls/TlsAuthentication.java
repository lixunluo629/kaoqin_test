package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsAuthentication.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsAuthentication.class */
public interface TlsAuthentication {
    void notifyServerCertificate(Certificate certificate) throws IOException;

    TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException;
}
