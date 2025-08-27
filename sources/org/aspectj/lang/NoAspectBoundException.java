package org.aspectj.lang;

/* JADX WARN: Classes with same name are omitted:
  aspectjrt-1.8.14.jar:org/aspectj/lang/NoAspectBoundException.class
 */
/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/lang/NoAspectBoundException.class */
public class NoAspectBoundException extends RuntimeException {
    Throwable cause;

    public NoAspectBoundException(String aspectName, Throwable inner) {
        super(inner == null ? aspectName : new StringBuffer().append("Exception while initializing ").append(aspectName).append(": ").append(inner).toString());
        this.cause = inner;
    }

    public NoAspectBoundException() {
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
