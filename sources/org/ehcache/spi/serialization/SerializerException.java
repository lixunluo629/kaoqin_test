package org.ehcache.spi.serialization;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/serialization/SerializerException.class */
public class SerializerException extends RuntimeException {
    private static final long serialVersionUID = -4008956327217206643L;

    public SerializerException() {
    }

    public SerializerException(String message) {
        super(message);
    }

    public SerializerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializerException(Throwable cause) {
        super(cause);
    }
}
