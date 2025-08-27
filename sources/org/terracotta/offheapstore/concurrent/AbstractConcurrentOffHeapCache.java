package org.terracotta.offheapstore.concurrent;

import java.util.Arrays;
import java.util.Comparator;
import org.terracotta.offheapstore.Segment;
import org.terracotta.offheapstore.exceptions.OversizeMappingException;
import org.terracotta.offheapstore.pinning.PinnableCache;
import org.terracotta.offheapstore.pinning.PinnableSegment;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapCache.class */
public abstract class AbstractConcurrentOffHeapCache<K, V> extends AbstractConcurrentOffHeapMap<K, V> implements PinnableCache<K, V> {
    private static final Comparator<Segment<?, ?>> SIZE_COMPARATOR = new Comparator<Segment<?, ?>>() { // from class: org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapCache.1
        @Override // java.util.Comparator
        public int compare(Segment<?, ?> o1, Segment<?, ?> o2) {
            return (int) (o2.getSize() - o1.getSize());
        }
    };

    public AbstractConcurrentOffHeapCache(Factory<? extends PinnableSegment<K, V>> segmentFactory) {
        super(segmentFactory);
    }

    public AbstractConcurrentOffHeapCache(Factory<? extends Segment<K, V>> segmentFactory, int concurrency) {
        super(segmentFactory, concurrency);
    }

    @Override // org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap
    public V fill(K k, V v) {
        try {
            return (V) super.fill(k, v);
        } catch (OversizeMappingException e) {
            return null;
        }
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public V getAndPin(K key) {
        return segmentFor((Object) key).getValueAndSetMetadata(key, 1073741824, 1073741824);
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public V putPinned(K key, V value) {
        try {
            return segmentFor((Object) key).putPinned(key, value);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor((Object) key).putPinned(key, value);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor((Object) key).putPinned(key, value);
                            } catch (OversizeMappingException ex) {
                            }
                        } finally {
                            writeUnlockAll();
                        }
                    } while (handleOversizeMappingException(key.hashCode()));
                    throw ex;
                }
            }
            writeLockAll();
            do {
                return segmentFor((Object) key).putPinned(key, value);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public boolean isPinned(Object key) {
        return segmentFor(key).isPinned(key);
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public void setPinning(K key, boolean pinned) {
        segmentFor((Object) key).setPinning(key, pinned);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap
    public PinnableSegment<K, V> segmentFor(Object key) {
        return (PinnableSegment) super.segmentFor(key);
    }

    public boolean shrink() {
        Segment<?, ?>[] sorted = (Segment[]) this.segments.clone();
        Arrays.sort(sorted, SIZE_COMPARATOR);
        for (Segment<?, ?> s : sorted) {
            if (s.shrink()) {
                return true;
            }
        }
        return false;
    }

    public boolean shrinkOthers(int excludedHash) {
        boolean evicted = false;
        Segment segmentSegmentFor = segmentFor(excludedHash);
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            if (segment != segmentSegmentFor) {
                evicted |= segment.shrink();
            }
        }
        return evicted;
    }
}
