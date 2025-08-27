package net.sf.cglib.core;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/CodeGenerationException.class */
public class CodeGenerationException extends RuntimeException {
    private Throwable cause;

    public CodeGenerationException(Throwable cause) {
        super(new StringBuffer().append(cause.getClass().getName()).append("-->").append(cause.getMessage()).toString());
        this.cause = cause;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
