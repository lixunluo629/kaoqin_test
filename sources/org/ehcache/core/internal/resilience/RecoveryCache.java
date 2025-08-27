package org.ehcache.core.internal.resilience;

import org.ehcache.core.spi.store.StoreAccessException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/internal/resilience/RecoveryCache.class */
public interface RecoveryCache<K> {
    void obliterate() throws StoreAccessException;

    void obliterate(K k) throws StoreAccessException;

    void obliterate(Iterable<? extends K> iterable) throws StoreAccessException;
}
