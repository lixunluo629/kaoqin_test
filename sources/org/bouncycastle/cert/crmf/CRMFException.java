package org.bouncycastle.cert.crmf;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/crmf/CRMFException.class */
public class CRMFException extends Exception {
    private Throwable cause;

    public CRMFException(String str) {
        this(str, null);
    }

    public CRMFException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
