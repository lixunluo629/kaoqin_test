package org.ehcache.impl.internal.store.offheap;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.ehcache.config.EvictionAdvisor;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.terracotta.offheapstore.MetadataTuple;
import org.terracotta.offheapstore.Segment;
import org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapCache;
import org.terracotta.offheapstore.pinning.PinnableSegment;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/EhcacheConcurrentOffHeapClockCache.class */
public class EhcacheConcurrentOffHeapClockCache<K, V> extends AbstractConcurrentOffHeapCache<K, V> implements EhcacheOffHeapBackingMap<K, V> {
    private final EvictionAdvisor<? super K, ? super V> evictionAdvisor;
    private final AtomicLong[] counters;

    public EhcacheConcurrentOffHeapClockCache(EvictionAdvisor<? super K, ? super V> evictionAdvisor, Factory<? extends PinnableSegment<K, V>> segmentFactory, int ssize) {
        super(segmentFactory, ssize);
        this.evictionAdvisor = evictionAdvisor;
        this.counters = new AtomicLong[this.segments.length];
        for (int i = 0; i < this.segments.length; i++) {
            this.counters[i] = new AtomicLong();
        }
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long allocatedMemory() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getAllocatedMemory();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long occupiedMemory() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getOccupiedMemory();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long dataAllocatedMemory() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getDataAllocatedMemory();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long dataOccupiedMemory() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getDataOccupiedMemory();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long dataSize() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getDataSize();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long longSize() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getSize();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long tableCapacity() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getTableCapacity();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long usedSlotCount() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getUsedSlotCount();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long removedSlotCount() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getRemovedSlotCount();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long reprobeLength() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getReprobeLength();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long vitalMemory() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getVitalMemory();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.OffHeapMapStatistics
    public long dataVitalMemory() {
        long total = 0;
        Segment<K, V>[] arr$ = this.segments;
        for (Segment<K, V> segment : arr$) {
            total += segment.getDataVitalMemory();
        }
        return total;
    }

    @Override // org.ehcache.impl.internal.store.offheap.EhcacheOffHeapBackingMap
    public V compute(K key, final BiFunction<K, V, V> mappingFunction, final boolean pin) {
        MetadataTuple<V> result = computeWithMetadata(key, new org.terracotta.offheapstore.jdk8.BiFunction<K, MetadataTuple<V>, MetadataTuple<V>>() { // from class: org.ehcache.impl.internal.store.offheap.EhcacheConcurrentOffHeapClockCache.1
            @Override // org.terracotta.offheapstore.jdk8.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass1) x0, (MetadataTuple) x1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public MetadataTuple<V> apply(K k, MetadataTuple<V> current) {
                V oldValue = current == null ? null : current.value();
                Object objApply = mappingFunction.apply(k, oldValue);
                if (objApply == null) {
                    return null;
                }
                if (oldValue == objApply) {
                    return MetadataTuple.metadataTuple(objApply, (pin ? 1073741824 : 0) | current.metadata());
                }
                return MetadataTuple.metadataTuple(objApply, (pin ? 1073741824 : 0) | (EhcacheConcurrentOffHeapClockCache.this.evictionAdvisor.adviseAgainstEviction(k, objApply) ? 536870912 : 0));
            }
        });
        if (result == null) {
            return null;
        }
        return result.value();
    }

    @Override // org.ehcache.impl.internal.store.offheap.EhcacheOffHeapBackingMap
    public V computeIfPresent(K key, final BiFunction<K, V, V> mappingFunction) {
        MetadataTuple<V> result = computeIfPresentWithMetadata(key, new org.terracotta.offheapstore.jdk8.BiFunction<K, MetadataTuple<V>, MetadataTuple<V>>() { // from class: org.ehcache.impl.internal.store.offheap.EhcacheConcurrentOffHeapClockCache.2
            @Override // org.terracotta.offheapstore.jdk8.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass2) x0, (MetadataTuple) x1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public MetadataTuple<V> apply(K k, MetadataTuple<V> current) {
                V oldValue = current.value();
                Object objApply = mappingFunction.apply(k, oldValue);
                if (objApply == null) {
                    return null;
                }
                if (oldValue == objApply) {
                    return current;
                }
                return MetadataTuple.metadataTuple(objApply, EhcacheConcurrentOffHeapClockCache.this.evictionAdvisor.adviseAgainstEviction(k, objApply) ? 536870912 : 0);
            }
        });
        if (result == null) {
            return null;
        }
        return result.value();
    }

    @Override // org.ehcache.impl.internal.store.offheap.EhcacheOffHeapBackingMap
    public V computeIfPresentAndPin(K key, final BiFunction<K, V, V> mappingFunction) {
        MetadataTuple<V> result = computeIfPresentWithMetadata(key, new org.terracotta.offheapstore.jdk8.BiFunction<K, MetadataTuple<V>, MetadataTuple<V>>() { // from class: org.ehcache.impl.internal.store.offheap.EhcacheConcurrentOffHeapClockCache.3
            @Override // org.terracotta.offheapstore.jdk8.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass3) x0, (MetadataTuple) x1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public MetadataTuple<V> apply(K k, MetadataTuple<V> current) {
                V oldValue = current.value();
                Object objApply = mappingFunction.apply(k, oldValue);
                if (objApply == null) {
                    return null;
                }
                if (oldValue == objApply) {
                    return MetadataTuple.metadataTuple(objApply, 1073741824 | current.metadata());
                }
                return MetadataTuple.metadataTuple(objApply, 1073741824 | (EhcacheConcurrentOffHeapClockCache.this.evictionAdvisor.adviseAgainstEviction(k, objApply) ? 536870912 : 0));
            }
        });
        if (result == null) {
            return null;
        }
        return result.value();
    }

    @Override // org.ehcache.impl.internal.store.offheap.EhcacheOffHeapBackingMap
    public boolean computeIfPinned(K key, final BiFunction<K, V, V> remappingFunction, final Function<V, Boolean> unpinFunction) {
        final AtomicBoolean unpin = new AtomicBoolean();
        computeIfPresentWithMetadata(key, new org.terracotta.offheapstore.jdk8.BiFunction<K, MetadataTuple<V>, MetadataTuple<V>>() { // from class: org.ehcache.impl.internal.store.offheap.EhcacheConcurrentOffHeapClockCache.4
            @Override // org.terracotta.offheapstore.jdk8.BiFunction
            public /* bridge */ /* synthetic */ Object apply(Object x0, Object x1) {
                return apply((AnonymousClass4) x0, (MetadataTuple) x1);
            }

            /* JADX WARN: Multi-variable type inference failed */
            public MetadataTuple<V> apply(K k, MetadataTuple<V> current) {
                if ((current.metadata() & 1073741824) != 0) {
                    V oldValue = current.value();
                    Object objApply = remappingFunction.apply(k, oldValue);
                    Boolean unpinLocal = (Boolean) unpinFunction.apply(oldValue);
                    if (objApply == null) {
                        unpin.set(true);
                        return null;
                    }
                    if (oldValue == objApply) {
                        unpin.set(unpinLocal.booleanValue());
                        return MetadataTuple.metadataTuple(oldValue, current.metadata() & (unpinLocal.booleanValue() ? -1073741825 : -1));
                    }
                    unpin.set(false);
                    return MetadataTuple.metadataTuple(objApply, EhcacheConcurrentOffHeapClockCache.this.evictionAdvisor.adviseAgainstEviction(k, objApply) ? 536870912 : 0);
                }
                return current;
            }
        });
        return unpin.get();
    }

    @Override // org.ehcache.impl.internal.store.offheap.EhcacheOffHeapBackingMap
    public long nextIdFor(K key) {
        return this.counters[getIndexFor(key.hashCode())].getAndIncrement();
    }
}
