package org.apache.commons.collections4;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/FunctorException.class */
public class FunctorException extends RuntimeException {
    private static final long serialVersionUID = -4704772662059351193L;

    public FunctorException() {
    }

    public FunctorException(String msg) {
        super(msg);
    }

    public FunctorException(Throwable rootCause) {
        super(rootCause);
    }

    public FunctorException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}
