package org.bouncycastle.crypto.tls;

import java.io.IOException;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsPeer.class */
public interface TlsPeer {
    void notifyCloseHandle(TlsCloseable tlsCloseable);

    void cancel() throws IOException;

    boolean requiresExtendedMasterSecret();

    boolean shouldUseGMTUnixTime();

    void notifySecureRenegotiation(boolean z) throws IOException;

    TlsCompression getCompression() throws IOException;

    TlsCipher getCipher() throws IOException;

    void notifyAlertRaised(short s, short s2, String str, Throwable th);

    void notifyAlertReceived(short s, short s2);

    void notifyHandshakeComplete() throws IOException;
}
