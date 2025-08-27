package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsHandshakeHash.class */
public interface TlsHandshakeHash extends Digest {
    void init(TlsContext tlsContext);

    TlsHandshakeHash notifyPRFDetermined();

    void trackHashAlgorithm(short s);

    void sealHashAlgorithms();

    TlsHandshakeHash stopTracking();

    Digest forkPRFHash();

    byte[] getFinalHash(short s);
}
