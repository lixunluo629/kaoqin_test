package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsKeyExchange.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsKeyExchange.class */
public interface TlsKeyExchange {
    void skipServerCertificate() throws IOException;

    void processServerCertificate(Certificate certificate) throws IOException;

    void skipServerKeyExchange() throws IOException;

    void processServerKeyExchange(InputStream inputStream) throws IOException;

    void validateCertificateRequest(CertificateRequest certificateRequest) throws IOException;

    void skipClientCredentials() throws IOException;

    void processClientCredentials(TlsCredentials tlsCredentials) throws IOException;

    void generateClientKeyExchange(OutputStream outputStream) throws IOException;

    byte[] generatePremasterSecret() throws IOException;
}
