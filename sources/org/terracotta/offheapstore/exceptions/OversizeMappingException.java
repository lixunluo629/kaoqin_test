package org.terracotta.offheapstore.exceptions;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/exceptions/OversizeMappingException.class */
public class OversizeMappingException extends IllegalArgumentException {
    private static final long serialVersionUID = 3918022751469816074L;

    public OversizeMappingException() {
    }

    public OversizeMappingException(String s) {
        super(s);
    }

    public OversizeMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public OversizeMappingException(Throwable cause) {
        super(cause);
    }
}
