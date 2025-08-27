package org.bouncycastle.crypto.tls;

/* loaded from: bcprov-jdk16-1.46.jar:org/bouncycastle/crypto/tls/TlsRuntimeException.class */
public class TlsRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1928023487348344086L;
    Throwable e;

    public TlsRuntimeException(String str, Throwable th) {
        super(str);
        this.e = th;
    }

    public TlsRuntimeException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.e;
    }
}
