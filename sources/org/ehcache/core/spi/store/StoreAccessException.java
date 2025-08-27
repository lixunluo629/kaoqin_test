package org.ehcache.core.spi.store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/StoreAccessException.class */
public class StoreAccessException extends Exception {
    private static final long serialVersionUID = 5249505200891654776L;

    public StoreAccessException(Throwable cause) {
        super(cause);
    }

    public StoreAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreAccessException(String message) {
        super(message);
    }
}
