package org.terracotta.offheapstore;

import java.nio.ByteBuffer;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import org.terracotta.offheapstore.OffHeapHashMap;
import org.terracotta.offheapstore.jdk8.BiFunction;
import org.terracotta.offheapstore.jdk8.Function;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/AbstractLockedOffHeapHashMap.class */
public abstract class AbstractLockedOffHeapHashMap<K, V> extends OffHeapHashMap<K, V> implements Segment<K, V> {
    private Set<Map.Entry<K, V>> entrySet;
    private Set<K> keySet;
    private Collection<V> values;

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.concurrent.locks.ReadWriteLock
    public abstract Lock readLock();

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.concurrent.locks.ReadWriteLock
    public abstract Lock writeLock();

    public AbstractLockedOffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, storageEngine);
    }

    public AbstractLockedOffHeapHashMap(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, tableAllocationsSteal, storageEngine);
    }

    public AbstractLockedOffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine, boolean bootstrap) {
        super(source, storageEngine, bootstrap);
    }

    public AbstractLockedOffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, storageEngine, tableSize);
    }

    public AbstractLockedOffHeapHashMap(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, tableAllocationsSteal, storageEngine, tableSize);
    }

    public AbstractLockedOffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap) {
        super(source, false, storageEngine, tableSize, bootstrap);
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public int size() {
        Lock l = readLock();
        l.lock();
        try {
            int size = super.size();
            l.unlock();
            return size;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        Lock l = readLock();
        l.lock();
        try {
            boolean zContainsKey = super.containsKey(key);
            l.unlock();
            return zContainsKey;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Lock lock = readLock();
        lock.lock();
        try {
            V v = (V) super.get(obj);
            lock.unlock();
            return v;
        } catch (Throwable th) {
            lock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public Long getEncodingForHashAndBinary(int hash, ByteBuffer binaryKey) {
        Lock l = readLock();
        l.lock();
        try {
            Long encodingForHashAndBinary = super.getEncodingForHashAndBinary(hash, binaryKey);
            l.unlock();
            return encodingForHashAndBinary;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public long installMappingForHashAndEncoding(int pojoHash, ByteBuffer offheapBinaryKey, ByteBuffer offheapBinaryValue, int metadata) {
        Lock l = writeLock();
        l.lock();
        try {
            long jInstallMappingForHashAndEncoding = super.installMappingForHashAndEncoding(pojoHash, offheapBinaryKey, offheapBinaryValue, metadata);
            l.unlock();
            return jInstallMappingForHashAndEncoding;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public V put(K k, V v) {
        Lock lockWriteLock = writeLock();
        lockWriteLock.lock();
        try {
            V v2 = (V) super.put(k, v);
            lockWriteLock.unlock();
            return v2;
        } catch (Throwable th) {
            lockWriteLock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public V put(K k, V v, int i) {
        Lock lockWriteLock = writeLock();
        lockWriteLock.lock();
        try {
            V v2 = (V) super.put(k, v, i);
            lockWriteLock.unlock();
            return v2;
        } catch (Throwable th) {
            lockWriteLock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public V fill(K k, V v) {
        Lock lockWriteLock = writeLock();
        lockWriteLock.lock();
        try {
            V v2 = (V) super.fill(k, v);
            lockWriteLock.unlock();
            return v2;
        } catch (Throwable th) {
            lockWriteLock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public V fill(K k, V v, int i) {
        Lock lockWriteLock = writeLock();
        lockWriteLock.lock();
        try {
            V v2 = (V) super.fill(k, v, i);
            lockWriteLock.unlock();
            return v2;
        } catch (Throwable th) {
            lockWriteLock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        Lock lockWriteLock = writeLock();
        lockWriteLock.lock();
        try {
            V v = (V) super.remove(obj);
            lockWriteLock.unlock();
            return v;
        } catch (Throwable th) {
            lockWriteLock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public boolean removeNoReturn(Object key) {
        Lock l = writeLock();
        l.lock();
        try {
            boolean zRemoveNoReturn = super.removeNoReturn(key);
            l.unlock();
            return zRemoveNoReturn;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public void clear() {
        Lock l = writeLock();
        l.lock();
        try {
            super.clear();
            l.unlock();
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K key, V value) {
        Lock l = writeLock();
        l.lock();
        try {
            if (key == null || value == null) {
                throw new NullPointerException();
            }
            V existing = get(key);
            if (existing == null) {
                put(key, value);
            }
            return existing;
        } finally {
            l.unlock();
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object key, Object value) {
        Lock l = writeLock();
        l.lock();
        try {
            if (key == null) {
                throw new NullPointerException();
            }
            if (value == null) {
                return false;
            }
            V existing = get(key);
            if (value.equals(existing)) {
                remove(key);
                l.unlock();
                return true;
            }
            l.unlock();
            return false;
        } finally {
            l.unlock();
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K key, V oldValue, V newValue) {
        Lock l = writeLock();
        l.lock();
        try {
            V existing = get(key);
            if (oldValue.equals(existing)) {
                put(key, newValue);
                l.unlock();
                return true;
            }
            return false;
        } finally {
            l.unlock();
        }
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K key, V value) {
        Lock l = writeLock();
        l.lock();
        try {
            if (value == null || key == null) {
                throw new NullPointerException();
            }
            V existing = get(key);
            if (existing != null) {
                put(key, value);
            }
            return existing;
        } finally {
            l.unlock();
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public Integer getMetadata(Object key, int mask) {
        Lock l = readLock();
        l.lock();
        try {
            Integer metadata = super.getMetadata(key, mask);
            l.unlock();
            return metadata;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public Integer getAndSetMetadata(Object key, int mask, int values) {
        Lock l = writeLock();
        l.lock();
        try {
            Integer andSetMetadata = super.getAndSetMetadata(key, mask, values);
            l.unlock();
            return andSetMetadata;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public V getValueAndSetMetadata(Object obj, int i, int i2) {
        Lock lockWriteLock = writeLock();
        lockWriteLock.lock();
        try {
            V v = (V) super.getValueAndSetMetadata(obj, i, i2);
            lockWriteLock.unlock();
            return v;
        } catch (Throwable th) {
            lockWriteLock.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap
    protected boolean removeMapping(Object o) {
        Lock l = writeLock();
        l.lock();
        try {
            boolean zRemoveMapping = super.removeMapping(o);
            l.unlock();
            return zRemoveMapping;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public boolean evict(int index, boolean shrink) {
        Lock l = writeLock();
        l.lock();
        try {
            boolean zEvict = super.evict(index, shrink);
            l.unlock();
            return zEvict;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        LockedEntrySet lockedEntrySet = new LockedEntrySet();
        this.entrySet = lockedEntrySet;
        return lockedEntrySet;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/AbstractLockedOffHeapHashMap$LockedEntrySet.class */
    class LockedEntrySet extends AbstractSet<Map.Entry<K, V>> {
        LockedEntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            Lock l = AbstractLockedOffHeapHashMap.this.readLock();
            l.lock();
            try {
                LockedEntryIterator lockedEntryIterator = new LockedEntryIterator();
                l.unlock();
                return lockedEntryIterator;
            } catch (Throwable th) {
                l.unlock();
                throw th;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0042  */
        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean contains(java.lang.Object r4) {
            /*
                r3 = this;
                r0 = r4
                boolean r0 = r0 instanceof java.util.Map.Entry
                if (r0 != 0) goto L9
                r0 = 0
                return r0
            L9:
                r0 = r4
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0
                r5 = r0
                r0 = r3
                org.terracotta.offheapstore.AbstractLockedOffHeapHashMap r0 = org.terracotta.offheapstore.AbstractLockedOffHeapHashMap.this
                java.util.concurrent.locks.Lock r0 = r0.readLock()
                r6 = r0
                r0 = r6
                r0.lock()
                r0 = r3
                org.terracotta.offheapstore.AbstractLockedOffHeapHashMap r0 = org.terracotta.offheapstore.AbstractLockedOffHeapHashMap.this     // Catch: java.lang.Throwable -> L4e
                r1 = r5
                java.lang.Object r1 = r1.getKey()     // Catch: java.lang.Throwable -> L4e
                java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> L4e
                r7 = r0
                r0 = r7
                if (r0 == 0) goto L42
                r0 = r7
                r1 = r5
                java.lang.Object r1 = r1.getValue()     // Catch: java.lang.Throwable -> L4e
                boolean r0 = r0.equals(r1)     // Catch: java.lang.Throwable -> L4e
                if (r0 == 0) goto L42
                r0 = 1
                goto L43
            L42:
                r0 = 0
            L43:
                r8 = r0
                r0 = r6
                r0.unlock()
                r0 = r8
                return r0
            L4e:
                r9 = move-exception
                r0 = r6
                r0.unlock()
                r0 = r9
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.terracotta.offheapstore.AbstractLockedOffHeapHashMap.LockedEntrySet.contains(java.lang.Object):boolean");
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return AbstractLockedOffHeapHashMap.this.removeMapping(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractLockedOffHeapHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractLockedOffHeapHashMap.this.clear();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/AbstractLockedOffHeapHashMap$LockedEntryIterator.class */
    class LockedEntryIterator extends OffHeapHashMap.EntryIterator {
        LockedEntryIterator() {
            super();
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator, java.util.Iterator
        public Map.Entry<K, V> next() {
            Lock l = AbstractLockedOffHeapHashMap.this.readLock();
            l.lock();
            try {
                Map.Entry<K, V> entry = (Map.Entry) super.next();
                l.unlock();
                return entry;
            } catch (Throwable th) {
                l.unlock();
                throw th;
            }
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator, java.util.Iterator
        public void remove() {
            Lock l = AbstractLockedOffHeapHashMap.this.writeLock();
            l.lock();
            try {
                super.remove();
                l.unlock();
            } catch (Throwable th) {
                l.unlock();
                throw th;
            }
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator
        protected void checkForConcurrentModification() {
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new LockedKeySet();
        }
        return this.keySet;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/AbstractLockedOffHeapHashMap$LockedKeySet.class */
    class LockedKeySet extends AbstractSet<K> {
        LockedKeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            Lock l = AbstractLockedOffHeapHashMap.this.readLock();
            l.lock();
            try {
                LockedKeyIterator lockedKeyIterator = new LockedKeyIterator();
                l.unlock();
                return lockedKeyIterator;
            } catch (Throwable th) {
                l.unlock();
                throw th;
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return AbstractLockedOffHeapHashMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return AbstractLockedOffHeapHashMap.this.remove(o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return AbstractLockedOffHeapHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            AbstractLockedOffHeapHashMap.this.clear();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/AbstractLockedOffHeapHashMap$LockedKeyIterator.class */
    class LockedKeyIterator extends OffHeapHashMap.KeyIterator {
        LockedKeyIterator() {
            super();
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator, java.util.Iterator
        public K next() {
            Lock lock = AbstractLockedOffHeapHashMap.this.readLock();
            lock.lock();
            try {
                K k = (K) super.next();
                lock.unlock();
                return k;
            } catch (Throwable th) {
                lock.unlock();
                throw th;
            }
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator, java.util.Iterator
        public void remove() {
            Lock l = AbstractLockedOffHeapHashMap.this.writeLock();
            l.lock();
            try {
                super.remove();
                l.unlock();
            } catch (Throwable th) {
                l.unlock();
                throw th;
            }
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator
        protected void checkForConcurrentModification() {
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        if (this.values == null) {
            this.values = new AbstractCollection<V>() { // from class: org.terracotta.offheapstore.AbstractLockedOffHeapHashMap.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
                public Iterator<V> iterator() {
                    return new Iterator<V>() { // from class: org.terracotta.offheapstore.AbstractLockedOffHeapHashMap.1.1
                        private final Iterator<Map.Entry<K, V>> i;

                        {
                            this.i = AbstractLockedOffHeapHashMap.this.entrySet().iterator();
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.i.hasNext();
                        }

                        @Override // java.util.Iterator
                        public V next() {
                            return this.i.next().getValue();
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            this.i.remove();
                        }
                    };
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public int size() {
                    return AbstractLockedOffHeapHashMap.this.size();
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean isEmpty() {
                    return AbstractLockedOffHeapHashMap.this.isEmpty();
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public void clear() {
                    AbstractLockedOffHeapHashMap.this.clear();
                }

                @Override // java.util.AbstractCollection, java.util.Collection
                public boolean contains(Object v) {
                    return AbstractLockedOffHeapHashMap.this.containsValue(v);
                }
            };
        }
        return this.values;
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public void destroy() {
        Lock l = writeLock();
        l.lock();
        try {
            super.destroy();
            l.unlock();
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.Segment
    public boolean shrink() {
        Lock l = writeLock();
        l.lock();
        try {
            boolean zShrink = this.storageEngine.shrink();
            l.unlock();
            return zShrink;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public MetadataTuple<V> computeWithMetadata(K key, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> remappingFunction) {
        Lock l = writeLock();
        l.lock();
        try {
            MetadataTuple<V> metadataTupleComputeWithMetadata = super.computeWithMetadata(key, remappingFunction);
            l.unlock();
            return metadataTupleComputeWithMetadata;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public MetadataTuple<V> computeIfAbsentWithMetadata(K key, Function<? super K, ? extends MetadataTuple<V>> mappingFunction) {
        Lock l = writeLock();
        l.lock();
        try {
            MetadataTuple<V> metadataTupleComputeIfAbsentWithMetadata = super.computeIfAbsentWithMetadata(key, mappingFunction);
            l.unlock();
            return metadataTupleComputeIfAbsentWithMetadata;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.Segment
    public MetadataTuple<V> computeIfPresentWithMetadata(K key, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> remappingFunction) {
        Lock l = writeLock();
        l.lock();
        try {
            MetadataTuple<V> metadataTupleComputeIfPresentWithMetadata = super.computeIfPresentWithMetadata(key, remappingFunction);
            l.unlock();
            return metadataTupleComputeIfPresentWithMetadata;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.HashingMap
    public Map<K, V> removeAllWithHash(int hash) {
        Lock l = writeLock();
        l.lock();
        try {
            Map<K, V> mapRemoveAllWithHash = super.removeAllWithHash(hash);
            l.unlock();
            return mapRemoveAllWithHash;
        } catch (Throwable th) {
            l.unlock();
            throw th;
        }
    }
}
