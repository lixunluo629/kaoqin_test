package org.ehcache.spi.loaderwriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/CacheWritingException.class */
public class CacheWritingException extends RuntimeException {
    private static final long serialVersionUID = -3547750364991417531L;

    public CacheWritingException() {
    }

    public CacheWritingException(String message) {
        super(message);
    }

    public CacheWritingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheWritingException(Throwable cause) {
        super(cause);
    }
}
