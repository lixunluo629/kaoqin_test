package org.ehcache.spi.loaderwriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/CacheLoadingException.class */
public class CacheLoadingException extends RuntimeException {
    private static final long serialVersionUID = 4794738044299044587L;

    public CacheLoadingException() {
    }

    public CacheLoadingException(String message) {
        super(message);
    }

    public CacheLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheLoadingException(Throwable cause) {
        super(cause);
    }
}
