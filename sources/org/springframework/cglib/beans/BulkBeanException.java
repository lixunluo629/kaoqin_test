package org.springframework.cglib.beans;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/cglib/beans/BulkBeanException.class */
public class BulkBeanException extends RuntimeException {
    private int index;
    private Throwable cause;

    public BulkBeanException(String message, int index) {
        super(message);
        this.index = index;
    }

    public BulkBeanException(Throwable cause, int index) {
        super(cause.getMessage());
        this.index = index;
        this.cause = cause;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return this.cause;
    }
}
