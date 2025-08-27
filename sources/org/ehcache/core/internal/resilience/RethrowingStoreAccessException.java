package org.ehcache.core.internal.resilience;

import org.ehcache.core.spi.store.StoreAccessException;

@Deprecated
/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/resilience/RethrowingStoreAccessException.class */
public class RethrowingStoreAccessException extends StoreAccessException {
    public RethrowingStoreAccessException(RuntimeException cause) {
        super(cause);
    }

    @Override // java.lang.Throwable
    public synchronized RuntimeException getCause() {
        return (RuntimeException) super.getCause();
    }
}
