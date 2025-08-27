package org.bouncycastle.operator;

/* loaded from: bcpkix-jdk15on-1.64.jar:org/bouncycastle/operator/OperatorException.class */
public class OperatorException extends Exception {
    private Throwable cause;

    public OperatorException(String str, Throwable th) {
        super(str);
        this.cause = th;
    }

    public OperatorException(String str) {
        super(str);
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
