package org.ehcache.impl.internal.sizeof;

import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.heap.SizeOfEngine;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/sizeof/NoopSizeOfEngine.class */
public class NoopSizeOfEngine implements SizeOfEngine {
    @Override // org.ehcache.core.spi.store.heap.SizeOfEngine
    public <K, V> long sizeof(K key, Store.ValueHolder<V> holder) {
        return 1L;
    }
}
