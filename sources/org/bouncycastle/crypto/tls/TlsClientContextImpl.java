package org.bouncycastle.crypto.tls;

import java.security.SecureRandom;

/* JADX WARN: Classes with same name are omitted:
  bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsClientContextImpl.class
 */
/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsClientContextImpl.class */
class TlsClientContextImpl implements TlsClientContext {
    private SecureRandom secureRandom;
    private SecurityParameters securityParameters;
    private Object userObject = null;

    TlsClientContextImpl(SecureRandom secureRandom, SecurityParameters securityParameters) {
        this.secureRandom = secureRandom;
        this.securityParameters = securityParameters;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClientContext
    public SecureRandom getSecureRandom() {
        return this.secureRandom;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClientContext
    public SecurityParameters getSecurityParameters() {
        return this.securityParameters;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClientContext
    public Object getUserObject() {
        return this.userObject;
    }

    @Override // org.bouncycastle.crypto.tls.TlsClientContext
    public void setUserObject(Object obj) {
        this.userObject = obj;
    }
}
