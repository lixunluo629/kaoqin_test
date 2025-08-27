package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsServerContextImpl.class */
class TlsServerContextImpl extends AbstractTlsContext implements TlsServerContext {
    TlsServerContextImpl(SecureRandom secureRandom, SecurityParameters securityParameters) {
        super(secureRandom, securityParameters);
    }

    @Override // org.bouncycastle.crypto.tls.TlsContext
    public boolean isServer() {
        return true;
    }
}
