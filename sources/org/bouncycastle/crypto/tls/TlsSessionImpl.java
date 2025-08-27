package org.bouncycastle.crypto.tls;

import org.bouncycastle.util.Arrays;

/* loaded from: bcprov-jdk15on-1.64.jar:org/bouncycastle/crypto/tls/TlsSessionImpl.class */
class TlsSessionImpl implements TlsSession {
    final byte[] sessionID;
    final SessionParameters sessionParameters;
    boolean resumable;

    TlsSessionImpl(byte[] bArr, SessionParameters sessionParameters) {
        if (bArr == null) {
            throw new IllegalArgumentException("'sessionID' cannot be null");
        }
        if (bArr.length > 32) {
            throw new IllegalArgumentException("'sessionID' cannot be longer than 32 bytes");
        }
        this.sessionID = Arrays.clone(bArr);
        this.sessionParameters = sessionParameters;
        this.resumable = bArr.length > 0 && null != sessionParameters && sessionParameters.isExtendedMasterSecret();
    }

    @Override // org.bouncycastle.crypto.tls.TlsSession
    public synchronized SessionParameters exportSessionParameters() {
        if (this.sessionParameters == null) {
            return null;
        }
        return this.sessionParameters.copy();
    }

    @Override // org.bouncycastle.crypto.tls.TlsSession
    public synchronized byte[] getSessionID() {
        return this.sessionID;
    }

    @Override // org.bouncycastle.crypto.tls.TlsSession
    public synchronized void invalidate() {
        this.resumable = false;
    }

    @Override // org.bouncycastle.crypto.tls.TlsSession
    public synchronized boolean isResumable() {
        return this.resumable;
    }
}
