package org.bouncycastle.cert.cmp;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/cert/cmp/CMPException.class */
public class CMPException extends Exception {
    private Throwable cause;

    public CMPException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public CMPException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
