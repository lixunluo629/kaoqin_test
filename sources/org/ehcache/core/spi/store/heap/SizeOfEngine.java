package org.ehcache.core.spi.store.heap;

import org.ehcache.core.spi.store.Store;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/store/heap/SizeOfEngine.class */
public interface SizeOfEngine {
    <K, V> long sizeof(K k, Store.ValueHolder<V> valueHolder) throws LimitExceededException;
}
