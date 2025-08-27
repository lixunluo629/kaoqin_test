package org.terracotta.offheapstore.concurrent;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.terracotta.offheapstore.HashingMap;
import org.terracotta.offheapstore.MapInternals;
import org.terracotta.offheapstore.MetadataTuple;
import org.terracotta.offheapstore.Segment;
import org.terracotta.offheapstore.exceptions.OversizeMappingException;
import org.terracotta.offheapstore.jdk8.BiFunction;
import org.terracotta.offheapstore.jdk8.Function;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapMap.class */
public abstract class AbstractConcurrentOffHeapMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V>, ConcurrentMapInternals, HashingMap<K, V> {
    private static final int MAX_SEGMENTS = 65536;
    private static final int DEFAULT_CONCURRENCY = 16;
    protected final Segment<K, V>[] segments;
    private final int segmentShift;
    private final int segmentMask;
    private Set<K> keySet;
    private Set<Map.Entry<K, V>> entrySet;
    private Collection<V> values;

    public AbstractConcurrentOffHeapMap(Factory<? extends Segment<K, V>> segmentFactory) {
        this(segmentFactory, 16);
    }

    public AbstractConcurrentOffHeapMap(Factory<? extends Segment<K, V>> segmentFactory, int concurrency) {
        int ssize;
        if (concurrency <= 0) {
            throw new IllegalArgumentException("Concurrency must be positive [was: " + concurrency + "]");
        }
        int sshift = 0;
        int i = 1;
        while (true) {
            ssize = i;
            if (ssize >= (concurrency > 65536 ? 65536 : concurrency)) {
                break;
            }
            sshift++;
            i = ssize << 1;
        }
        this.segmentShift = 32 - sshift;
        this.segmentMask = ssize - 1;
        this.segments = new Segment[ssize];
        for (int i2 = 0; i2 < this.segments.length; i2++) {
            try {
                this.segments[i2] = segmentFactory.newInstance();
            } catch (RuntimeException e) {
                Segment[] arr$ = this.segments;
                for (Segment segment : arr$) {
                    if (segment != null) {
                        segment.destroy();
                    }
                }
                throw e;
            }
        }
    }

    protected Segment<K, V> segmentFor(Object key) {
        return segmentFor(key.hashCode());
    }

    protected Segment<K, V> segmentFor(int hash) {
        return this.segments[getIndexFor(hash)];
    }

    public int getIndexFor(int hash) {
        return (spread(hash) >>> this.segmentShift) & this.segmentMask;
    }

    public List<Segment<K, V>> getSegments() {
        return Collections.unmodifiableList(Arrays.asList(this.segments));
    }

    protected int getConcurrency() {
        return this.segments.length;
    }

    private static int spread(int hash) {
        int h = hash + ((hash << 15) ^ (-12931));
        int h2 = h ^ (h >>> 10);
        int h3 = h2 + (h2 << 3);
        int h4 = h3 ^ (h3 >>> 6);
        int h5 = h4 + (h4 << 2) + (h4 << 14);
        return h5 ^ (h5 >>> 16);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        long sum = 0;
        readLockAll();
        try {
            Map<?, ?>[] arr$ = this.segments;
            for (Map<?, ?> m : arr$) {
                sum += m.size();
            }
            if (sum > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) sum;
        } finally {
            readUnlockAll();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return segmentFor(key).containsKey(key);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(Object value) {
        readLockAll();
        try {
            Map<?, ?>[] arr$ = this.segments;
            for (Map<?, ?> m : arr$) {
                if (m.containsValue(value)) {
                    return true;
                }
            }
            readUnlockAll();
            return false;
        } finally {
            readUnlockAll();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        return (V) segmentFor(obj).get(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        try {
            return (V) segmentFor(k).put(k, v);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(k.hashCode())) {
                try {
                    return (V) segmentFor(k).put(k, v);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return (V) segmentFor(k).put(k, v);
                            } catch (OversizeMappingException e3) {
                            }
                        } finally {
                            writeUnlockAll();
                        }
                    } while (handleOversizeMappingException(k.hashCode()));
                    throw e3;
                }
            }
            writeLockAll();
            do {
                return (V) segmentFor(k).put(k, v);
            } while (handleOversizeMappingException(k.hashCode()));
            throw e3;
        }
    }

    public V put(K key, V value, int metadata) {
        try {
            return segmentFor(key).put(key, value, metadata);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).put(key, value, metadata);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).put(key, value, metadata);
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
                return segmentFor(key).put(key, value, metadata);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    public V fill(K key, V value) {
        return segmentFor(key).fill(key, value);
    }

    public V fill(K key, V value, int metadata) {
        return segmentFor(key).fill(key, value, metadata);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        return (V) segmentFor(obj).remove(obj);
    }

    public boolean removeNoReturn(Object key) {
        return segmentFor(key).removeNoReturn(key);
    }

    public Integer getMetadata(K key, int mask) throws IllegalArgumentException {
        return segmentFor(key).getMetadata(key, mask);
    }

    public Integer getAndSetMetadata(K key, int mask, int values) throws IllegalArgumentException {
        return segmentFor(key).getAndSetMetadata(key, mask, values);
    }

    public V getValueAndSetMetadata(K key, int mask, int values) {
        return segmentFor(key).getValueAndSetMetadata(key, mask, values);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        writeLockAll();
        try {
            Map<?, ?>[] arr$ = this.segments;
            for (Map<?, ?> m : arr$) {
                m.clear();
            }
        } finally {
            writeUnlockAll();
        }
    }

    public void destroy() {
        writeLockAll();
        try {
            Segment[] arr$ = this.segments;
            for (Segment segment : arr$) {
                segment.destroy();
            }
        } finally {
            writeUnlockAll();
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K key, V value) {
        try {
            return segmentFor(key).putIfAbsent(key, value);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).putIfAbsent(key, value);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).putIfAbsent(key, value);
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
                return segmentFor(key).putIfAbsent(key, value);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object key, Object value) {
        return segmentFor(key).remove(key, value);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K key, V oldValue, V newValue) {
        try {
            return segmentFor(key).replace(key, oldValue, newValue);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).replace(key, oldValue, newValue);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).replace(key, oldValue, newValue);
                            } finally {
                                writeUnlockAll();
                            }
                        } catch (OversizeMappingException ex) {
                            if (handleOversizeMappingException(key.hashCode())) {
                                throw ex;
                            }
                        }
                    } while (handleOversizeMappingException(key.hashCode()));
                    throw ex;
                }
            }
            writeLockAll();
            do {
                return segmentFor(key).replace(key, oldValue, newValue);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K key, V value) {
        try {
            return segmentFor(key).replace(key, value);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).replace(key, value);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).replace(key, value);
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
                return segmentFor(key).replace(key, value);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        AggregateKeySet aggregateKeySet = new AggregateKeySet();
        this.keySet = aggregateKeySet;
        return aggregateKeySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        Collection<V> vc = this.values;
        if (vc != null) {
            return vc;
        }
        AggregatedValuesCollection aggregatedValuesCollection = new AggregatedValuesCollection();
        this.values = aggregatedValuesCollection;
        return aggregatedValuesCollection;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        AggregateEntrySet aggregateEntrySet = new AggregateEntrySet();
        this.entrySet = aggregateEntrySet;
        return aggregateEntrySet;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapMap$AggregateKeySet.class */
    class AggregateKeySet extends AbstractConcurrentOffHeapMap<K, V>.BaseAggregateSet<K> {
        AggregateKeySet() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return AbstractConcurrentOffHeapMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return AbstractConcurrentOffHeapMap.this.remove(o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new AbstractConcurrentOffHeapMap<K, V>.AggregateIterator<K>() { // from class: org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap.AggregateKeySet.1
                {
                    AbstractConcurrentOffHeapMap abstractConcurrentOffHeapMap = AbstractConcurrentOffHeapMap.this;
                }

                @Override // org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap.AggregateIterator
                protected Iterator<K> getNextIterator() {
                    return this.listIterator.next().keySet().iterator();
                }
            };
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapMap$AggregateEntrySet.class */
    class AggregateEntrySet extends AbstractConcurrentOffHeapMap<K, V>.BaseAggregateSet<Map.Entry<K, V>> {
        AggregateEntrySet() {
            super();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new AbstractConcurrentOffHeapMap<K, V>.AggregateIterator<Map.Entry<K, V>>() { // from class: org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap.AggregateEntrySet.1
                {
                    AbstractConcurrentOffHeapMap abstractConcurrentOffHeapMap = AbstractConcurrentOffHeapMap.this;
                }

                @Override // org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap.AggregateIterator
                protected Iterator<Map.Entry<K, V>> getNextIterator() {
                    return this.listIterator.next().entrySet().iterator();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object obj = AbstractConcurrentOffHeapMap.this.get(e.getKey());
            return obj != null && obj.equals(e.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object obj = AbstractConcurrentOffHeapMap.this.get(e.getKey());
            return (obj == null || !obj.equals(e.getValue()) || AbstractConcurrentOffHeapMap.this.remove(e.getKey()) == null) ? false : true;
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapMap$AggregatedValuesCollection.class */
    class AggregatedValuesCollection extends AbstractCollection<V> {
        AggregatedValuesCollection() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return new AbstractConcurrentOffHeapMap<K, V>.AggregateIterator<V>() { // from class: org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap.AggregatedValuesCollection.1
                {
                    AbstractConcurrentOffHeapMap abstractConcurrentOffHeapMap = AbstractConcurrentOffHeapMap.this;
                }

                @Override // org.terracotta.offheapstore.concurrent.AbstractConcurrentOffHeapMap.AggregateIterator
                protected Iterator<V> getNextIterator() {
                    return this.listIterator.next().values().iterator();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return AbstractConcurrentOffHeapMap.this.size();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapMap$BaseAggregateSet.class */
    private abstract class BaseAggregateSet<T> extends AbstractSet<T> {
        private BaseAggregateSet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractConcurrentOffHeapMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractConcurrentOffHeapMap.this.clear();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/concurrent/AbstractConcurrentOffHeapMap$AggregateIterator.class */
    protected abstract class AggregateIterator<T> implements Iterator<T> {
        protected final Iterator<Map<K, V>> listIterator;
        protected Iterator<T> currentIterator;

        protected abstract Iterator<T> getNextIterator();

        public AggregateIterator() {
            this.listIterator = Arrays.asList(AbstractConcurrentOffHeapMap.this.segments).iterator();
            while (this.listIterator.hasNext()) {
                this.currentIterator = getNextIterator();
                if (this.currentIterator.hasNext()) {
                    return;
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.currentIterator == null) {
                return false;
            }
            if (this.currentIterator.hasNext()) {
                return true;
            }
            while (this.listIterator.hasNext()) {
                this.currentIterator = getNextIterator();
                if (this.currentIterator.hasNext()) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Iterator
        public T next() {
            if (this.currentIterator == null) {
                throw new NoSuchElementException();
            }
            if (this.currentIterator.hasNext()) {
                return this.currentIterator.next();
            }
            while (this.listIterator.hasNext()) {
                this.currentIterator = getNextIterator();
                if (this.currentIterator.hasNext()) {
                    return this.currentIterator.next();
                }
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.currentIterator.remove();
        }
    }

    protected void readLockAll() {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            segment.readLock().lock();
        }
    }

    protected void readUnlockAll() {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            segment.readLock().unlock();
        }
    }

    public final void writeLockAll() {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            segment.writeLock().lock();
        }
    }

    public final void writeUnlockAll() {
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            segment.writeLock().unlock();
        }
    }

    @Override // org.terracotta.offheapstore.concurrent.ConcurrentMapInternals
    public List<MapInternals> getSegmentInternals() {
        return Collections.unmodifiableList(Arrays.asList(this.segments));
    }

    @Override // org.terracotta.offheapstore.MapInternals, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public long getSize() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getSize();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getTableCapacity() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getTableCapacity();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getUsedSlotCount() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getUsedSlotCount();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getRemovedSlotCount() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getRemovedSlotCount();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public int getReprobeLength() {
        throw new UnsupportedOperationException("Segmented maps do not have a reprobe length");
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getAllocatedMemory() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getAllocatedMemory();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getOccupiedMemory() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getOccupiedMemory();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getVitalMemory() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getVitalMemory();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataAllocatedMemory() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getDataAllocatedMemory();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataOccupiedMemory() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getDataOccupiedMemory();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataVitalMemory() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getDataVitalMemory();
        }
        return sum;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataSize() {
        long sum = 0;
        MapInternals[] arr$ = this.segments;
        for (MapInternals m : arr$) {
            sum += m.getDataSize();
        }
        return sum;
    }

    public final boolean handleOversizeMappingException(int hash) {
        boolean evicted = false;
        Segment segmentSegmentFor = segmentFor(hash);
        Segment[] arr$ = this.segments;
        for (Segment segment : arr$) {
            if (segment != segmentSegmentFor) {
                evicted |= segment.shrink();
            }
        }
        return evicted;
    }

    public MetadataTuple<V> computeWithMetadata(K key, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> remappingFunction) {
        try {
            return segmentFor(key).computeWithMetadata(key, remappingFunction);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).computeWithMetadata(key, remappingFunction);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).computeWithMetadata(key, remappingFunction);
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
                return segmentFor(key).computeWithMetadata(key, remappingFunction);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    public MetadataTuple<V> computeIfAbsentWithMetadata(K key, Function<? super K, ? extends MetadataTuple<V>> mappingFunction) {
        try {
            return segmentFor(key).computeIfAbsentWithMetadata(key, mappingFunction);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).computeIfAbsentWithMetadata(key, mappingFunction);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).computeIfAbsentWithMetadata(key, mappingFunction);
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
                return segmentFor(key).computeIfAbsentWithMetadata(key, mappingFunction);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    public MetadataTuple<V> computeIfPresentWithMetadata(K key, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> remappingFunction) {
        try {
            return segmentFor(key).computeIfPresentWithMetadata(key, remappingFunction);
        } catch (OversizeMappingException e) {
            if (handleOversizeMappingException(key.hashCode())) {
                try {
                    return segmentFor(key).computeIfPresentWithMetadata(key, remappingFunction);
                } catch (OversizeMappingException e2) {
                    writeLockAll();
                    do {
                        try {
                            try {
                                return segmentFor(key).computeIfPresentWithMetadata(key, remappingFunction);
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
                return segmentFor(key).computeIfPresentWithMetadata(key, remappingFunction);
            } while (handleOversizeMappingException(key.hashCode()));
            throw ex;
        }
    }

    @Override // org.terracotta.offheapstore.HashingMap
    public Map<K, V> removeAllWithHash(int keyHash) {
        return segmentFor(keyHash).removeAllWithHash(keyHash);
    }
}
