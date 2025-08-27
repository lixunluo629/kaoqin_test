package org.ehcache.core.spi.store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/StoreAccessTimeoutException.class */
public class StoreAccessTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 7824475930240423944L;

    public StoreAccessTimeoutException(Throwable cause) {
        super(cause);
    }

    public StoreAccessTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
