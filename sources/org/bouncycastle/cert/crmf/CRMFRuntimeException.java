package org.bouncycastle.cert.crmf;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/crmf/CRMFRuntimeException.class */
public class CRMFRuntimeException extends RuntimeException {
    private Throwable cause;

    public CRMFRuntimeException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
