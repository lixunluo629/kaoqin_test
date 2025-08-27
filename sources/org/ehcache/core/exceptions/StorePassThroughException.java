package org.ehcache.core.exceptions;

import org.ehcache.core.spi.store.StoreAccessException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/exceptions/StorePassThroughException.class */
public class StorePassThroughException extends RuntimeException {
    private static final long serialVersionUID = -2018452326214235671L;

    public StorePassThroughException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorePassThroughException(Throwable cause) {
        super(cause);
    }

    public static void handleRuntimeException(RuntimeException re) throws StoreAccessException {
        if (re instanceof StorePassThroughException) {
            Throwable cause = re.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new StoreAccessException(cause);
        }
        throw new StoreAccessException(re);
    }
}
