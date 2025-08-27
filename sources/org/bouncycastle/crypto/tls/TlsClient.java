package org.bouncycastle.crypto.tls;

import java.io.IOException;
import java.util.Hashtable;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsClient.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsClient.class */
public interface TlsClient {
    void init(TlsClientContext tlsClientContext);

    int[] getCipherSuites();

    short[] getCompressionMethods();

    Hashtable getClientExtensions() throws IOException;

    void notifySessionID(byte[] bArr);

    void notifySelectedCipherSuite(int i);

    void notifySelectedCompressionMethod(short s);

    void notifySecureRenegotiation(boolean z) throws IOException;

    void processServerExtensions(Hashtable hashtable);

    TlsKeyExchange getKeyExchange() throws IOException;

    TlsAuthentication getAuthentication() throws IOException;

    TlsCompression getCompression() throws IOException;

    TlsCipher getCipher() throws IOException;
}
