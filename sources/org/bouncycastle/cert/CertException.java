package org.bouncycastle.cert;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/CertException.class */
public class CertException extends Exception {
    private Throwable cause;

    public CertException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public CertException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
