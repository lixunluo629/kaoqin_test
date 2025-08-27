package org.bouncycastle.eac;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/eac/EACException.class */
public class EACException extends Exception {
    private Throwable cause;

    public EACException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public EACException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
