package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap.class */
public final class HashBiMap<K, V> extends AbstractMap<K, V> implements BiMap<K, V>, Serializable {
    private static final double LOAD_FACTOR = 1.0d;
    private transient BiEntry<K, V>[] hashTableKToV;
    private transient BiEntry<K, V>[] hashTableVToK;
    private transient int size;
    private transient int mask;
    private transient int modCount;
    private transient BiMap<V, K> inverse;

    @GwtIncompatible("Not needed in emulated source")
    private static final long serialVersionUID = 0;

    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int expectedSize) {
        return new HashBiMap<>(expectedSize);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> bimap = create(map.size());
        bimap.putAll(map);
        return bimap;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$BiEntry.class */
    private static final class BiEntry<K, V> extends ImmutableEntry<K, V> {
        final int keyHash;
        final int valueHash;

        @Nullable
        BiEntry<K, V> nextInKToVBucket;

        @Nullable
        BiEntry<K, V> nextInVToKBucket;

        BiEntry(K key, int keyHash, V value, int valueHash) {
            super(key, value);
            this.keyHash = keyHash;
            this.valueHash = valueHash;
        }
    }

    private HashBiMap(int expectedSize) {
        init(expectedSize);
    }

    private void init(int expectedSize) {
        CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
        int tableSize = Hashing.closedTableSize(expectedSize, LOAD_FACTOR);
        this.hashTableKToV = createTable(tableSize);
        this.hashTableVToK = createTable(tableSize);
        this.mask = tableSize - 1;
        this.modCount = 0;
        this.size = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delete(BiEntry<K, V> entry) {
        int keyBucket = entry.keyHash & this.mask;
        BiEntry<K, V> prevBucketEntry = null;
        BiEntry<K, V> biEntry = this.hashTableKToV[keyBucket];
        while (true) {
            BiEntry<K, V> bucketEntry = biEntry;
            if (bucketEntry == entry) {
                break;
            }
            prevBucketEntry = bucketEntry;
            biEntry = bucketEntry.nextInKToVBucket;
        }
        if (prevBucketEntry == null) {
            this.hashTableKToV[keyBucket] = entry.nextInKToVBucket;
        } else {
            prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
        }
        int valueBucket = entry.valueHash & this.mask;
        BiEntry<K, V> prevBucketEntry2 = null;
        BiEntry<K, V> biEntry2 = this.hashTableVToK[valueBucket];
        while (true) {
            BiEntry<K, V> bucketEntry2 = biEntry2;
            if (bucketEntry2 == entry) {
                break;
            }
            prevBucketEntry2 = bucketEntry2;
            biEntry2 = bucketEntry2.nextInVToKBucket;
        }
        if (prevBucketEntry2 == null) {
            this.hashTableVToK[valueBucket] = entry.nextInVToKBucket;
        } else {
            prevBucketEntry2.nextInVToKBucket = entry.nextInVToKBucket;
        }
        this.size--;
        this.modCount++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void insert(BiEntry<K, V> entry) {
        int keyBucket = entry.keyHash & this.mask;
        entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
        this.hashTableKToV[keyBucket] = entry;
        int valueBucket = entry.valueHash & this.mask;
        entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
        this.hashTableVToK[valueBucket] = entry;
        this.size++;
        this.modCount++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int hash(@Nullable Object o) {
        return Hashing.smear(o == null ? 0 : o.hashCode());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
        BiEntry<K, V> biEntry = this.hashTableKToV[keyHash & this.mask];
        while (true) {
            BiEntry<K, V> entry = biEntry;
            if (entry != null) {
                if (keyHash != entry.keyHash || !Objects.equal(key, entry.key)) {
                    biEntry = entry.nextInKToVBucket;
                } else {
                    return entry;
                }
            } else {
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
        BiEntry<K, V> biEntry = this.hashTableVToK[valueHash & this.mask];
        while (true) {
            BiEntry<K, V> entry = biEntry;
            if (entry != null) {
                if (valueHash != entry.valueHash || !Objects.equal(value, entry.value)) {
                    biEntry = entry.nextInVToKBucket;
                } else {
                    return entry;
                }
            } else {
                return null;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(@Nullable Object key) {
        return seekByKey(key, hash(key)) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsValue(@Nullable Object value) {
        return seekByValue(value, hash(value)) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @Nullable
    public V get(@Nullable Object key) {
        BiEntry<K, V> entry = seekByKey(key, hash(key));
        if (entry == null) {
            return null;
        }
        return entry.value;
    }

    @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
    public V put(@Nullable K key, @Nullable V value) {
        return put(key, value, false);
    }

    @Override // com.google.common.collect.BiMap
    public V forcePut(@Nullable K key, @Nullable V value) {
        return put(key, value, true);
    }

    private V put(@Nullable K key, @Nullable V value, boolean force) {
        int keyHash = hash(key);
        int valueHash = hash(value);
        BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
        if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && Objects.equal(value, oldEntryForKey.value)) {
            return value;
        }
        BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
        if (oldEntryForValue != null) {
            if (force) {
                delete(oldEntryForValue);
            } else {
                String strValueOf = String.valueOf(String.valueOf(value));
                throw new IllegalArgumentException(new StringBuilder(23 + strValueOf.length()).append("value already present: ").append(strValueOf).toString());
            }
        }
        if (oldEntryForKey != null) {
            delete(oldEntryForKey);
        }
        BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
        insert(newEntry);
        rehashIfNecessary();
        if (oldEntryForKey == null) {
            return null;
        }
        return oldEntryForKey.value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Nullable
    public K putInverse(@Nullable V value, @Nullable K key, boolean force) {
        int valueHash = hash(value);
        int keyHash = hash(key);
        BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
        if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && Objects.equal(key, oldEntryForValue.key)) {
            return key;
        }
        BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
        if (oldEntryForKey != null) {
            if (force) {
                delete(oldEntryForKey);
            } else {
                String strValueOf = String.valueOf(String.valueOf(key));
                throw new IllegalArgumentException(new StringBuilder(23 + strValueOf.length()).append("value already present: ").append(strValueOf).toString());
            }
        }
        if (oldEntryForValue != null) {
            delete(oldEntryForValue);
        }
        BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
        insert(newEntry);
        rehashIfNecessary();
        if (oldEntryForValue == null) {
            return null;
        }
        return oldEntryForValue.key;
    }

    private void rehashIfNecessary() {
        BiEntry<K, V>[] oldKToV = this.hashTableKToV;
        if (Hashing.needsResizing(this.size, oldKToV.length, LOAD_FACTOR)) {
            int newTableSize = oldKToV.length * 2;
            this.hashTableKToV = createTable(newTableSize);
            this.hashTableVToK = createTable(newTableSize);
            this.mask = newTableSize - 1;
            this.size = 0;
            for (BiEntry<K, V> biEntry : oldKToV) {
                while (true) {
                    BiEntry<K, V> entry = biEntry;
                    if (entry != null) {
                        BiEntry<K, V> nextEntry = entry.nextInKToVBucket;
                        insert(entry);
                        biEntry = nextEntry;
                    }
                }
            }
            this.modCount++;
        }
    }

    private BiEntry<K, V>[] createTable(int length) {
        return new BiEntry[length];
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(@Nullable Object key) {
        BiEntry<K, V> entry = seekByKey(key, hash(key));
        if (entry == null) {
            return null;
        }
        delete(entry);
        return entry.value;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.size = 0;
        Arrays.fill(this.hashTableKToV, (Object) null);
        Arrays.fill(this.hashTableVToK, (Object) null);
        this.modCount++;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$Itr.class */
    abstract class Itr<T> implements Iterator<T> {
        int nextBucket = 0;
        BiEntry<K, V> next = null;
        BiEntry<K, V> toRemove = null;
        int expectedModCount;

        abstract T output(BiEntry<K, V> biEntry);

        Itr() {
            this.expectedModCount = HashBiMap.this.modCount;
        }

        private void checkForConcurrentModification() {
            if (HashBiMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            checkForConcurrentModification();
            if (this.next == null) {
                while (this.nextBucket < HashBiMap.this.hashTableKToV.length) {
                    if (HashBiMap.this.hashTableKToV[this.nextBucket] != null) {
                        BiEntry<K, V>[] biEntryArr = HashBiMap.this.hashTableKToV;
                        int i = this.nextBucket;
                        this.nextBucket = i + 1;
                        this.next = biEntryArr[i];
                        return true;
                    }
                    this.nextBucket++;
                }
                return false;
            }
            return true;
        }

        @Override // java.util.Iterator
        public T next() {
            checkForConcurrentModification();
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            BiEntry<K, V> entry = this.next;
            this.next = entry.nextInKToVBucket;
            this.toRemove = entry;
            return output(entry);
        }

        @Override // java.util.Iterator
        public void remove() {
            checkForConcurrentModification();
            CollectPreconditions.checkRemove(this.toRemove != null);
            HashBiMap.this.delete(this.toRemove);
            this.expectedModCount = HashBiMap.this.modCount;
            this.toRemove = null;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        return new KeySet();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$KeySet.class */
    private final class KeySet extends Maps.KeySet<K, V> {
        KeySet() {
            super(HashBiMap.this);
        }

        @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new HashBiMap<K, V>.Itr<K>() { // from class: com.google.common.collect.HashBiMap.KeySet.1
                {
                    HashBiMap hashBiMap = HashBiMap.this;
                }

                @Override // com.google.common.collect.HashBiMap.Itr
                K output(BiEntry<K, V> entry) {
                    return entry.key;
                }
            };
        }

        @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(@Nullable Object o) {
            BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, HashBiMap.hash(o));
            if (entry != null) {
                HashBiMap.this.delete(entry);
                return true;
            }
            return false;
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<V> values() {
        return inverse().keySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$EntrySet.class */
    private final class EntrySet extends Maps.EntrySet<K, V> {
        private EntrySet() {
        }

        @Override // com.google.common.collect.Maps.EntrySet
        Map<K, V> map() {
            return HashBiMap.this;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new HashBiMap<K, V>.Itr<Map.Entry<K, V>>() { // from class: com.google.common.collect.HashBiMap.EntrySet.1
                {
                    HashBiMap hashBiMap = HashBiMap.this;
                }

                /* JADX INFO: Access modifiers changed from: package-private */
                @Override // com.google.common.collect.HashBiMap.Itr
                public Map.Entry<K, V> output(BiEntry<K, V> entry) {
                    return new MapEntry(entry);
                }

                /* renamed from: com.google.common.collect.HashBiMap$EntrySet$1$MapEntry */
                /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$EntrySet$1$MapEntry.class */
                class MapEntry extends AbstractMapEntry<K, V> {
                    BiEntry<K, V> delegate;

                    MapEntry(BiEntry<K, V> entry) {
                        this.delegate = entry;
                    }

                    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                    public K getKey() {
                        return this.delegate.key;
                    }

                    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                    public V getValue() {
                        return this.delegate.value;
                    }

                    @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                    public V setValue(V value) {
                        V oldValue = this.delegate.value;
                        int valueHash = HashBiMap.hash(value);
                        if (valueHash != this.delegate.valueHash || !Objects.equal(value, oldValue)) {
                            Preconditions.checkArgument(HashBiMap.this.seekByValue(value, valueHash) == null, "value already present: %s", value);
                            HashBiMap.this.delete(this.delegate);
                            BiEntry<K, V> newEntry = new BiEntry<>(this.delegate.key, this.delegate.keyHash, value, valueHash);
                            HashBiMap.this.insert(newEntry);
                            AnonymousClass1.this.expectedModCount = HashBiMap.this.modCount;
                            if (AnonymousClass1.this.toRemove == this.delegate) {
                                AnonymousClass1.this.toRemove = newEntry;
                            }
                            this.delegate = newEntry;
                            return oldValue;
                        }
                        return value;
                    }
                }
            };
        }
    }

    @Override // com.google.common.collect.BiMap
    public BiMap<V, K> inverse() {
        if (this.inverse != null) {
            return this.inverse;
        }
        Inverse inverse = new Inverse();
        this.inverse = inverse;
        return inverse;
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$Inverse.class */
    private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {
        private Inverse() {
        }

        BiMap<K, V> forward() {
            return HashBiMap.this;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            return HashBiMap.this.size;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            forward().clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(@Nullable Object value) {
            return forward().containsValue(value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public K get(@Nullable Object value) {
            BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
            if (entry == null) {
                return null;
            }
            return entry.key;
        }

        @Override // java.util.AbstractMap, java.util.Map, com.google.common.collect.BiMap
        public K put(@Nullable V v, @Nullable K k) {
            return (K) HashBiMap.this.putInverse(v, k, false);
        }

        @Override // com.google.common.collect.BiMap
        public K forcePut(@Nullable V v, @Nullable K k) {
            return (K) HashBiMap.this.putInverse(v, k, true);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public K remove(@Nullable Object value) {
            BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, HashBiMap.hash(value));
            if (entry != null) {
                HashBiMap.this.delete(entry);
                return entry.key;
            }
            return null;
        }

        @Override // com.google.common.collect.BiMap
        public BiMap<K, V> inverse() {
            return forward();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<V> keySet() {
            return new InverseKeySet();
        }

        /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$Inverse$InverseKeySet.class */
        private final class InverseKeySet extends Maps.KeySet<V, K> {
            InverseKeySet() {
                super(Inverse.this);
            }

            @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.util.Set
            public boolean remove(@Nullable Object o) {
                BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, HashBiMap.hash(o));
                if (entry != null) {
                    HashBiMap.this.delete(entry);
                    return true;
                }
                return false;
            }

            @Override // com.google.common.collect.Maps.KeySet, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<V> iterator() {
                return new HashBiMap<K, V>.Itr<V>() { // from class: com.google.common.collect.HashBiMap.Inverse.InverseKeySet.1
                    {
                        HashBiMap hashBiMap = HashBiMap.this;
                    }

                    @Override // com.google.common.collect.HashBiMap.Itr
                    V output(BiEntry<K, V> entry) {
                        return entry.value;
                    }
                };
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<K> values() {
            return forward().keySet();
        }

        /* renamed from: com.google.common.collect.HashBiMap$Inverse$1, reason: invalid class name */
        /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$Inverse$1.class */
        class AnonymousClass1 extends Maps.EntrySet<V, K> {
            AnonymousClass1() {
            }

            @Override // com.google.common.collect.Maps.EntrySet
            Map<V, K> map() {
                return Inverse.this;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public Iterator<Map.Entry<V, K>> iterator() {
                return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>() { // from class: com.google.common.collect.HashBiMap.Inverse.1.1
                    {
                        HashBiMap hashBiMap = HashBiMap.this;
                    }

                    /* JADX INFO: Access modifiers changed from: package-private */
                    @Override // com.google.common.collect.HashBiMap.Itr
                    public Map.Entry<V, K> output(BiEntry<K, V> entry) {
                        return new InverseEntry(entry);
                    }

                    /* renamed from: com.google.common.collect.HashBiMap$Inverse$1$1$InverseEntry */
                    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$Inverse$1$1$InverseEntry.class */
                    class InverseEntry extends AbstractMapEntry<V, K> {
                        BiEntry<K, V> delegate;

                        InverseEntry(BiEntry<K, V> entry) {
                            this.delegate = entry;
                        }

                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public V getKey() {
                            return this.delegate.value;
                        }

                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public K getValue() {
                            return this.delegate.key;
                        }

                        @Override // com.google.common.collect.AbstractMapEntry, java.util.Map.Entry
                        public K setValue(K key) {
                            K oldKey = this.delegate.key;
                            int keyHash = HashBiMap.hash(key);
                            if (keyHash != this.delegate.keyHash || !Objects.equal(key, oldKey)) {
                                Preconditions.checkArgument(HashBiMap.this.seekByKey(key, keyHash) == null, "value already present: %s", key);
                                HashBiMap.this.delete(this.delegate);
                                BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, this.delegate.value, this.delegate.valueHash);
                                HashBiMap.this.insert(newEntry);
                                C00131.this.expectedModCount = HashBiMap.this.modCount;
                                return oldKey;
                            }
                            return key;
                        }
                    }
                };
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<V, K>> entrySet() {
            return new AnonymousClass1();
        }

        Object writeReplace() {
            return new InverseSerializedForm(HashBiMap.this);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/collect/HashBiMap$InverseSerializedForm.class */
    private static final class InverseSerializedForm<K, V> implements Serializable {
        private final HashBiMap<K, V> bimap;

        InverseSerializedForm(HashBiMap<K, V> bimap) {
            this.bimap = bimap;
        }

        Object readResolve() {
            return this.bimap.inverse();
        }
    }

    @GwtIncompatible("java.io.ObjectOutputStream")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Serialization.writeMap(this, stream);
    }

    @GwtIncompatible("java.io.ObjectInputStream")
    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        stream.defaultReadObject();
        int size = Serialization.readCount(stream);
        init(size);
        Serialization.populateMap(this, stream, size);
    }
}
