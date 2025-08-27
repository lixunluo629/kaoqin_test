package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.RandomGenerator;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsContext.class */
public interface TlsContext {
    RandomGenerator getNonceRandomGenerator();

    SecureRandom getSecureRandom();

    SecurityParameters getSecurityParameters();

    boolean isServer();

    ProtocolVersion getClientVersion();

    ProtocolVersion getServerVersion();

    TlsSession getResumableSession();

    Object getUserObject();

    void setUserObject(Object obj);

    byte[] exportKeyingMaterial(String str, byte[] bArr, int i);
}
