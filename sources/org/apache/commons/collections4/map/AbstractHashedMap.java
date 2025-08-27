package org.apache.commons.collections4.map;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.iterators.EmptyIterator;
import org.apache.commons.collections4.iterators.EmptyMapIterator;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap.class */
public class AbstractHashedMap<K, V> extends AbstractMap<K, V> implements IterableMap<K, V> {
    protected static final String NO_NEXT_ENTRY = "No next() entry in the iteration";
    protected static final String NO_PREVIOUS_ENTRY = "No previous() entry in the iteration";
    protected static final String REMOVE_INVALID = "remove() can only be called once after next()";
    protected static final String GETKEY_INVALID = "getKey() can only be called after next() and before remove()";
    protected static final String GETVALUE_INVALID = "getValue() can only be called after next() and before remove()";
    protected static final String SETVALUE_INVALID = "setValue() can only be called after next() and before remove()";
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int DEFAULT_THRESHOLD = 12;
    protected static final float DEFAULT_LOAD_FACTOR = 0.75f;
    protected static final int MAXIMUM_CAPACITY = 1073741824;
    protected static final Object NULL = new Object();
    transient float loadFactor;
    transient int size;
    transient HashEntry<K, V>[] data;
    transient int threshold;
    transient int modCount;
    transient EntrySet<K, V> entrySet;
    transient KeySet<K> keySet;
    transient Values<V> values;

    protected AbstractHashedMap() {
    }

    protected AbstractHashedMap(int initialCapacity, float loadFactor, int threshold) {
        this.loadFactor = loadFactor;
        this.data = new HashEntry[initialCapacity];
        this.threshold = threshold;
        init();
    }

    protected AbstractHashedMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    protected AbstractHashedMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity must be a non negative number");
        }
        if (loadFactor <= 0.0f || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be greater than 0");
        }
        this.loadFactor = loadFactor;
        int initialCapacity2 = calculateNewCapacity(initialCapacity);
        this.threshold = calculateThreshold(initialCapacity2, loadFactor);
        this.data = new HashEntry[initialCapacity2];
        init();
    }

    protected AbstractHashedMap(Map<? extends K, ? extends V> map) {
        this(Math.max(2 * map.size(), 16), DEFAULT_LOAD_FACTOR);
        _putAll(map);
    }

    protected void init() {
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public V get(Object key) {
        Object key2 = convertKey(key);
        int hashCode = hash(key2);
        HashEntry<K, V> hashEntry = this.data[hashIndex(hashCode, this.data.length)];
        while (true) {
            HashEntry<K, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(key2, entry.key)) {
                    return entry.getValue();
                }
                hashEntry = entry.next;
            } else {
                return null;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public boolean containsKey(Object key) {
        Object key2 = convertKey(key);
        int hashCode = hash(key2);
        HashEntry<K, V> hashEntry = this.data[hashIndex(hashCode, this.data.length)];
        while (true) {
            HashEntry<K, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(key2, entry.key)) {
                    return true;
                }
                hashEntry = entry.next;
            } else {
                return false;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0038, code lost:
    
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007a, code lost:
    
        r8 = r8 + 1;
     */
    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean containsValue(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = r5
            if (r0 != 0) goto L41
            r0 = r4
            org.apache.commons.collections4.map.AbstractHashedMap$HashEntry<K, V>[] r0 = r0.data
            r6 = r0
            r0 = r6
            int r0 = r0.length
            r7 = r0
            r0 = 0
            r8 = r0
        Lf:
            r0 = r8
            r1 = r7
            if (r0 >= r1) goto L3e
            r0 = r6
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r9
            r10 = r0
        L1f:
            r0 = r10
            if (r0 == 0) goto L38
            r0 = r10
            java.lang.Object r0 = r0.getValue()
            if (r0 != 0) goto L2e
            r0 = 1
            return r0
        L2e:
            r0 = r10
            org.apache.commons.collections4.map.AbstractHashedMap$HashEntry<K, V> r0 = r0.next
            r10 = r0
            goto L1f
        L38:
            int r8 = r8 + 1
            goto Lf
        L3e:
            goto L80
        L41:
            r0 = r4
            org.apache.commons.collections4.map.AbstractHashedMap$HashEntry<K, V>[] r0 = r0.data
            r6 = r0
            r0 = r6
            int r0 = r0.length
            r7 = r0
            r0 = 0
            r8 = r0
        L4c:
            r0 = r8
            r1 = r7
            if (r0 >= r1) goto L80
            r0 = r6
            r1 = r8
            r0 = r0[r1]
            r9 = r0
            r0 = r9
            r10 = r0
        L5c:
            r0 = r10
            if (r0 == 0) goto L7a
            r0 = r4
            r1 = r5
            r2 = r10
            java.lang.Object r2 = r2.getValue()
            boolean r0 = r0.isEqualValue(r1, r2)
            if (r0 == 0) goto L70
            r0 = 1
            return r0
        L70:
            r0 = r10
            org.apache.commons.collections4.map.AbstractHashedMap$HashEntry<K, V> r0 = r0.next
            r10 = r0
            goto L5c
        L7a:
            int r8 = r8 + 1
            goto L4c
        L80:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.collections4.map.AbstractHashedMap.containsValue(java.lang.Object):boolean");
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Put
    public V put(K key, V value) {
        Object convertedKey = convertKey(key);
        int hashCode = hash(convertedKey);
        int index = hashIndex(hashCode, this.data.length);
        HashEntry<K, V> hashEntry = this.data[index];
        while (true) {
            HashEntry<K, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(convertedKey, entry.key)) {
                    V oldValue = entry.getValue();
                    updateEntry(entry, value);
                    return oldValue;
                }
                hashEntry = entry.next;
            } else {
                addMapping(index, hashCode, key, value);
                return null;
            }
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Put
    public void putAll(Map<? extends K, ? extends V> map) {
        _putAll(map);
    }

    private void _putAll(Map<? extends K, ? extends V> map) {
        int mapSize = map.size();
        if (mapSize == 0) {
            return;
        }
        int newSize = (int) (((this.size + mapSize) / this.loadFactor) + 1.0f);
        ensureCapacity(calculateNewCapacity(newSize));
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public V remove(Object key) {
        Object key2 = convertKey(key);
        int hashCode = hash(key2);
        int index = hashIndex(hashCode, this.data.length);
        HashEntry<K, V> previous = null;
        for (HashEntry<K, V> entry = this.data[index]; entry != null; entry = entry.next) {
            if (entry.hashCode == hashCode && isEqualKey(key2, entry.key)) {
                V oldValue = entry.getValue();
                removeMapping(entry, index, previous);
                return oldValue;
            }
            previous = entry;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Put
    public void clear() {
        this.modCount++;
        HashEntry<K, V>[] data = this.data;
        for (int i = data.length - 1; i >= 0; i--) {
            data[i] = null;
        }
        this.size = 0;
    }

    protected Object convertKey(Object key) {
        return key == null ? NULL : key;
    }

    protected int hash(Object key) {
        int h = key.hashCode();
        int h2 = h + ((h << 9) ^ (-1));
        int h3 = h2 ^ (h2 >>> 14);
        int h4 = h3 + (h3 << 4);
        return h4 ^ (h4 >>> 10);
    }

    protected boolean isEqualKey(Object key1, Object key2) {
        return key1 == key2 || key1.equals(key2);
    }

    protected boolean isEqualValue(Object value1, Object value2) {
        return value1 == value2 || value1.equals(value2);
    }

    protected int hashIndex(int hashCode, int dataSize) {
        return hashCode & (dataSize - 1);
    }

    protected HashEntry<K, V> getEntry(Object key) {
        Object key2 = convertKey(key);
        int hashCode = hash(key2);
        HashEntry<K, V> hashEntry = this.data[hashIndex(hashCode, this.data.length)];
        while (true) {
            HashEntry<K, V> entry = hashEntry;
            if (entry != null) {
                if (entry.hashCode == hashCode && isEqualKey(key2, entry.key)) {
                    return entry;
                }
                hashEntry = entry.next;
            } else {
                return null;
            }
        }
    }

    protected void updateEntry(HashEntry<K, V> entry, V newValue) {
        entry.setValue(newValue);
    }

    protected void reuseEntry(HashEntry<K, V> entry, int hashIndex, int hashCode, K key, V value) {
        entry.next = this.data[hashIndex];
        entry.hashCode = hashCode;
        entry.key = key;
        entry.value = value;
    }

    protected void addMapping(int hashIndex, int hashCode, K key, V value) {
        this.modCount++;
        HashEntry<K, V> entry = createEntry(this.data[hashIndex], hashCode, key, value);
        addEntry(entry, hashIndex);
        this.size++;
        checkCapacity();
    }

    protected HashEntry<K, V> createEntry(HashEntry<K, V> next, int hashCode, K key, V value) {
        return new HashEntry<>(next, hashCode, convertKey(key), value);
    }

    protected void addEntry(HashEntry<K, V> entry, int hashIndex) {
        this.data[hashIndex] = entry;
    }

    protected void removeMapping(HashEntry<K, V> entry, int hashIndex, HashEntry<K, V> previous) {
        this.modCount++;
        removeEntry(entry, hashIndex, previous);
        this.size--;
        destroyEntry(entry);
    }

    protected void removeEntry(HashEntry<K, V> entry, int hashIndex, HashEntry<K, V> previous) {
        if (previous == null) {
            this.data[hashIndex] = entry.next;
        } else {
            previous.next = entry.next;
        }
    }

    protected void destroyEntry(HashEntry<K, V> entry) {
        entry.next = null;
        entry.key = null;
        entry.value = null;
    }

    protected void checkCapacity() {
        int newCapacity;
        if (this.size >= this.threshold && (newCapacity = this.data.length * 2) <= 1073741824) {
            ensureCapacity(newCapacity);
        }
    }

    protected void ensureCapacity(int newCapacity) {
        int oldCapacity = this.data.length;
        if (newCapacity <= oldCapacity) {
            return;
        }
        if (this.size == 0) {
            this.threshold = calculateThreshold(newCapacity, this.loadFactor);
            this.data = new HashEntry[newCapacity];
            return;
        }
        HashEntry<K, V>[] oldEntries = this.data;
        HashEntry<K, V>[] newEntries = new HashEntry[newCapacity];
        this.modCount++;
        for (int i = oldCapacity - 1; i >= 0; i--) {
            HashEntry<K, V> entry = oldEntries[i];
            if (entry != null) {
                oldEntries[i] = null;
                do {
                    HashEntry<K, V> next = entry.next;
                    int index = hashIndex(entry.hashCode, newCapacity);
                    entry.next = newEntries[index];
                    newEntries[index] = entry;
                    entry = next;
                } while (entry != null);
            }
        }
        this.threshold = calculateThreshold(newCapacity, this.loadFactor);
        this.data = newEntries;
    }

    protected int calculateNewCapacity(int proposedCapacity) {
        int newCapacity = 1;
        if (proposedCapacity > 1073741824) {
            newCapacity = 1073741824;
        } else {
            while (newCapacity < proposedCapacity) {
                newCapacity <<= 1;
            }
            if (newCapacity > 1073741824) {
                newCapacity = 1073741824;
            }
        }
        return newCapacity;
    }

    protected int calculateThreshold(int newCapacity, float factor) {
        return (int) (newCapacity * factor);
    }

    protected HashEntry<K, V> entryNext(HashEntry<K, V> entry) {
        return entry.next;
    }

    protected int entryHashCode(HashEntry<K, V> entry) {
        return entry.hashCode;
    }

    protected K entryKey(HashEntry<K, V> entry) {
        return entry.getKey();
    }

    protected V entryValue(HashEntry<K, V> entry) {
        return entry.getValue();
    }

    @Override // org.apache.commons.collections4.IterableGet
    public MapIterator<K, V> mapIterator() {
        if (this.size == 0) {
            return EmptyMapIterator.emptyMapIterator();
        }
        return new HashMapIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$HashMapIterator.class */
    protected static class HashMapIterator<K, V> extends HashIterator<K, V> implements MapIterator<K, V> {
        protected HashMapIterator(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public K next() {
            return super.nextEntry().getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public K getKey() {
            HashEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(AbstractHashedMap.GETKEY_INVALID);
            }
            return current.getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V getValue() {
            HashEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(AbstractHashedMap.GETVALUE_INVALID);
            }
            return current.getValue();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V setValue(V value) {
            HashEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException(AbstractHashedMap.SETVALUE_INVALID);
            }
            return current.setValue(value);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySet<>(this);
        }
        return this.entrySet;
    }

    protected Iterator<Map.Entry<K, V>> createEntrySetIterator() {
        if (size() == 0) {
            return EmptyIterator.emptyIterator();
        }
        return new EntrySetIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$EntrySet.class */
    protected static class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>> {
        private final AbstractHashedMap<K, V> parent;

        protected EntrySet(AbstractHashedMap<K, V> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.parent.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.parent.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object entry) {
            if (entry instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) entry;
                Map.Entry<K, V> match = this.parent.getEntry(e.getKey());
                return match != null && match.equals(e);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry) || !contains(obj)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) obj;
            this.parent.remove(entry.getKey());
            return true;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return this.parent.createEntrySetIterator();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$EntrySetIterator.class */
    protected static class EntrySetIterator<K, V> extends HashIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        protected EntrySetIterator(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            return super.nextEntry();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new KeySet<>(this);
        }
        return this.keySet;
    }

    protected Iterator<K> createKeySetIterator() {
        if (size() == 0) {
            return EmptyIterator.emptyIterator();
        }
        return new KeySetIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$KeySet.class */
    protected static class KeySet<K> extends AbstractSet<K> {
        private final AbstractHashedMap<K, ?> parent;

        protected KeySet(AbstractHashedMap<K, ?> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.parent.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            this.parent.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object key) {
            return this.parent.containsKey(key);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object key) {
            boolean result = this.parent.containsKey(key);
            this.parent.remove(key);
            return result;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return this.parent.createKeySetIterator();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$KeySetIterator.class */
    protected static class KeySetIterator<K> extends HashIterator<K, Object> implements Iterator<K> {
        protected KeySetIterator(AbstractHashedMap<K, ?> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public K next() {
            return super.nextEntry().getKey();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public Collection<V> values() {
        if (this.values == null) {
            this.values = new Values<>(this);
        }
        return this.values;
    }

    protected Iterator<V> createValuesIterator() {
        if (size() == 0) {
            return EmptyIterator.emptyIterator();
        }
        return new ValuesIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$Values.class */
    protected static class Values<V> extends AbstractCollection<V> {
        private final AbstractHashedMap<?, V> parent;

        protected Values(AbstractHashedMap<?, V> parent) {
            this.parent = parent;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.parent.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            this.parent.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object value) {
            return this.parent.containsValue(value);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return this.parent.createValuesIterator();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$ValuesIterator.class */
    protected static class ValuesIterator<V> extends HashIterator<Object, V> implements Iterator<V> {
        protected ValuesIterator(AbstractHashedMap<?, V> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public V next() {
            return super.nextEntry().getValue();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$HashEntry.class */
    protected static class HashEntry<K, V> implements Map.Entry<K, V>, KeyValue<K, V> {
        protected HashEntry<K, V> next;
        protected int hashCode;
        protected Object key;
        protected Object value;

        protected HashEntry(HashEntry<K, V> next, int hashCode, Object key, V value) {
            this.next = next;
            this.hashCode = hashCode;
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry, org.apache.commons.collections4.KeyValue
        public K getKey() {
            if (this.key == AbstractHashedMap.NULL) {
                return null;
            }
            return (K) this.key;
        }

        @Override // java.util.Map.Entry, org.apache.commons.collections4.KeyValue
        public V getValue() {
            return (V) this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V v2 = (V) this.value;
            this.value = v;
            return v2;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> other = (Map.Entry) obj;
            if (getKey() != null ? getKey().equals(other.getKey()) : other.getKey() == null) {
                if (getValue() != null ? getValue().equals(other.getValue()) : other.getValue() == null) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (getKey() == null ? 0 : getKey().hashCode()) ^ (getValue() == null ? 0 : getValue().hashCode());
        }

        public String toString() {
            return new StringBuilder().append(getKey()).append('=').append(getValue()).toString();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractHashedMap$HashIterator.class */
    protected static abstract class HashIterator<K, V> {
        private final AbstractHashedMap<K, V> parent;
        private int hashIndex;
        private HashEntry<K, V> last;
        private HashEntry<K, V> next;
        private int expectedModCount;

        protected HashIterator(AbstractHashedMap<K, V> parent) {
            HashEntry<K, V> next;
            this.parent = parent;
            HashEntry<K, V>[] data = parent.data;
            int i = data.length;
            HashEntry<K, V> hashEntry = null;
            while (true) {
                next = hashEntry;
                if (i <= 0 || next != null) {
                    break;
                }
                i--;
                hashEntry = data[i];
            }
            this.next = next;
            this.hashIndex = i;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext() {
            return this.next != null;
        }

        protected HashEntry<K, V> nextEntry() {
            HashEntry<K, V> n;
            if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            HashEntry<K, V> newCurrent = this.next;
            if (newCurrent == null) {
                throw new NoSuchElementException(AbstractHashedMap.NO_NEXT_ENTRY);
            }
            HashEntry<K, V>[] data = this.parent.data;
            int i = this.hashIndex;
            HashEntry<K, V> hashEntry = newCurrent.next;
            while (true) {
                n = hashEntry;
                if (n != null || i <= 0) {
                    break;
                }
                i--;
                hashEntry = data[i];
            }
            this.next = n;
            this.hashIndex = i;
            this.last = newCurrent;
            return newCurrent;
        }

        protected HashEntry<K, V> currentEntry() {
            return this.last;
        }

        public void remove() {
            if (this.last == null) {
                throw new IllegalStateException(AbstractHashedMap.REMOVE_INVALID);
            }
            if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.parent.remove(this.last.getKey());
            this.last = null;
            this.expectedModCount = this.parent.modCount;
        }

        public String toString() {
            if (this.last != null) {
                return "Iterator[" + this.last.getKey() + SymbolConstants.EQUAL_SYMBOL + this.last.getValue() + "]";
            }
            return "Iterator[]";
        }
    }

    protected void doWriteObject(ObjectOutputStream out) throws IOException {
        out.writeFloat(this.loadFactor);
        out.writeInt(this.data.length);
        out.writeInt(this.size);
        MapIterator<K, V> it = mapIterator();
        while (it.hasNext()) {
            out.writeObject(it.next());
            out.writeObject(it.getValue());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.loadFactor = in.readFloat();
        int capacity = in.readInt();
        int size = in.readInt();
        init();
        this.threshold = calculateThreshold(capacity, this.loadFactor);
        this.data = new HashEntry[capacity];
        for (int i = 0; i < size; i++) {
            put(in.readObject(), in.readObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.util.AbstractMap
    public AbstractHashedMap<K, V> clone() {
        try {
            AbstractHashedMap<K, V> cloned = (AbstractHashedMap) super.clone();
            cloned.data = new HashEntry[this.data.length];
            cloned.entrySet = null;
            cloned.keySet = null;
            cloned.values = null;
            cloned.modCount = 0;
            cloned.size = 0;
            cloned.init();
            cloned.putAll(this);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map<?, ?> map = (Map) obj;
        if (map.size() != size()) {
            return false;
        }
        MapIterator<K, V> mapIterator = mapIterator();
        while (mapIterator.hasNext()) {
            try {
                Object key = mapIterator.next();
                Object value = mapIterator.getValue();
                if (value == null) {
                    if (map.get(key) != null || !map.containsKey(key)) {
                        return false;
                    }
                } else if (!value.equals(map.get(key))) {
                    return false;
                }
            } catch (ClassCastException e) {
                return false;
            } catch (NullPointerException e2) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int total = 0;
        Iterator<Map.Entry<K, V>> it = createEntrySetIterator();
        while (it.hasNext()) {
            total += it.next().hashCode();
        }
        return total;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        if (size() == 0) {
            return "{}";
        }
        StringBuilder buf = new StringBuilder(32 * size());
        buf.append('{');
        MapIterator<K, V> it = mapIterator();
        boolean hasNext = it.hasNext();
        while (hasNext) {
            K key = it.next();
            V value = it.getValue();
            buf.append(key == this ? "(this Map)" : key).append('=').append(value == this ? "(this Map)" : value);
            hasNext = it.hasNext();
            if (hasNext) {
                buf.append(',').append(' ');
            }
        }
        buf.append('}');
        return buf.toString();
    }
}
