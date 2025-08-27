package org.ehcache.impl.internal.sizeof;

import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.heap.LimitExceededException;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.impl.copy.IdentityCopier;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.internal.sizeof.listeners.EhcacheVisitorListener;
import org.ehcache.impl.internal.sizeof.listeners.exceptions.VisitorListenerException;
import org.ehcache.impl.internal.store.heap.holders.CopiedOnHeapKey;
import org.ehcache.sizeof.SizeOf;
import org.ehcache.sizeof.SizeOfFilterSource;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/sizeof/DefaultSizeOfEngine.class */
public class DefaultSizeOfEngine implements SizeOfEngine {
    private final long maxObjectGraphSize;
    private final long maxObjectSize;
    private final SizeOfFilterSource filterSource = new SizeOfFilterSource(true);
    private final SizeOf sizeOf = SizeOf.newInstance(this.filterSource.getFilters());
    private final long onHeapKeyOffset = this.sizeOf.deepSizeOf(new CopiedOnHeapKey(new Object(), new IdentityCopier()));
    private final long chmTreeBinOffset = this.sizeOf.deepSizeOf(ConcurrentHashMap.FAKE_TREE_BIN);

    public DefaultSizeOfEngine(long maxObjectGraphSize, long maxObjectSize) {
        this.maxObjectGraphSize = maxObjectGraphSize;
        this.maxObjectSize = maxObjectSize;
    }

    @Override // org.ehcache.core.spi.store.heap.SizeOfEngine
    public <K, V> long sizeof(K key, Store.ValueHolder<V> holder) throws LimitExceededException {
        try {
            return this.sizeOf.deepSizeOf(new EhcacheVisitorListener(this.maxObjectGraphSize, this.maxObjectSize), key, holder) + this.chmTreeBinOffset + this.onHeapKeyOffset;
        } catch (VisitorListenerException e) {
            throw new LimitExceededException(e.getMessage());
        }
    }
}
