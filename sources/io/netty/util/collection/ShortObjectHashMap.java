package io.netty.util.collection;

import io.netty.util.collection.ShortObjectMap;
import io.netty.util.internal.MathUtil;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ShortObjectHashMap.class */
public class ShortObjectHashMap<V> implements ShortObjectMap<V> {
    public static final int DEFAULT_CAPACITY = 8;
    public static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final Object NULL_VALUE;
    private int maxSize;
    private final float loadFactor;
    private short[] keys;
    private V[] values;
    private int size;
    private int mask;
    private final Set<Short> keySet;
    private final Set<Map.Entry<Short, V>> entrySet;
    private final Iterable<ShortObjectMap.PrimitiveEntry<V>> entries;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Object put(Short sh, Object obj) {
        return put2(sh, (Short) obj);
    }

    static {
        $assertionsDisabled = !ShortObjectHashMap.class.desiredAssertionStatus();
        NULL_VALUE = new Object();
    }

    public ShortObjectHashMap() {
        this(8, 0.5f);
    }

    public ShortObjectHashMap(int initialCapacity) {
        this(initialCapacity, 0.5f);
    }

    public ShortObjectHashMap(int i, float f) {
        this.keySet = new KeySet();
        this.entrySet = new EntrySet();
        this.entries = new Iterable<ShortObjectMap.PrimitiveEntry<V>>() { // from class: io.netty.util.collection.ShortObjectHashMap.1
            @Override // java.lang.Iterable
            public Iterator<ShortObjectMap.PrimitiveEntry<V>> iterator() {
                return new PrimitiveIterator();
            }
        };
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
        }
        this.loadFactor = f;
        int iSafeFindNextPositivePowerOfTwo = MathUtil.safeFindNextPositivePowerOfTwo(i);
        this.mask = iSafeFindNextPositivePowerOfTwo - 1;
        this.keys = new short[iSafeFindNextPositivePowerOfTwo];
        this.values = (V[]) new Object[iSafeFindNextPositivePowerOfTwo];
        this.maxSize = calcMaxSize(iSafeFindNextPositivePowerOfTwo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T toExternal(T value) {
        if (!$assertionsDisabled && value == null) {
            throw new AssertionError("null is not a legitimate internal value. Concurrent Modification?");
        }
        if (value == NULL_VALUE) {
            return null;
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> T toInternal(T t) {
        return t == null ? (T) NULL_VALUE : t;
    }

    @Override // io.netty.util.collection.ShortObjectMap
    public V get(short s) {
        int iIndexOf = indexOf(s);
        if (iIndexOf == -1) {
            return null;
        }
        return (V) toExternal(this.values[iIndexOf]);
    }

    @Override // io.netty.util.collection.ShortObjectMap
    public V put(short s, V v) {
        int iHashIndex = hashIndex(s);
        int i = iHashIndex;
        while (this.values[i] != null) {
            if (this.keys[i] == s) {
                V v2 = this.values[i];
                ((V[]) this.values)[i] = toInternal(v);
                return (V) toExternal(v2);
            }
            int iProbeNext = probeNext(i);
            i = iProbeNext;
            if (iProbeNext == iHashIndex) {
                throw new IllegalStateException("Unable to insert");
            }
        }
        this.keys[i] = s;
        ((V[]) this.values)[i] = toInternal(v);
        growSize();
        return null;
    }

    @Override // java.util.Map
    public void putAll(Map<? extends Short, ? extends V> sourceMap) {
        if (sourceMap instanceof ShortObjectHashMap) {
            ShortObjectHashMap<V> source = (ShortObjectHashMap) sourceMap;
            for (int i = 0; i < source.values.length; i++) {
                V sourceValue = source.values[i];
                if (sourceValue != null) {
                    put(source.keys[i], (short) sourceValue);
                }
            }
            return;
        }
        for (Map.Entry<? extends Short, ? extends V> entry : sourceMap.entrySet()) {
            put2(entry.getKey(), (Short) entry.getValue());
        }
    }

    @Override // io.netty.util.collection.ShortObjectMap
    public V remove(short s) {
        int iIndexOf = indexOf(s);
        if (iIndexOf == -1) {
            return null;
        }
        V v = this.values[iIndexOf];
        removeAt(iIndexOf);
        return (V) toExternal(v);
    }

    @Override // java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.Map
    public void clear() {
        Arrays.fill(this.keys, (short) 0);
        Arrays.fill(this.values, (Object) null);
        this.size = 0;
    }

    @Override // io.netty.util.collection.ShortObjectMap
    public boolean containsKey(short key) {
        return indexOf(key) >= 0;
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        Object internal = toInternal(value);
        for (V v2 : this.values) {
            if (v2 != null && v2.equals(internal)) {
                return true;
            }
        }
        return false;
    }

    @Override // io.netty.util.collection.ShortObjectMap
    public Iterable<ShortObjectMap.PrimitiveEntry<V>> entries() {
        return this.entries;
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return new AbstractCollection<V>() { // from class: io.netty.util.collection.ShortObjectHashMap.2
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
            public Iterator<V> iterator() {
                return new Iterator<V>() { // from class: io.netty.util.collection.ShortObjectHashMap.2.1
                    final ShortObjectHashMap<V>.PrimitiveIterator iter;

                    {
                        this.iter = new PrimitiveIterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }

                    @Override // java.util.Iterator
                    public V next() {
                        return this.iter.next().value();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        this.iter.remove();
                    }
                };
            }

            @Override // java.util.AbstractCollection, java.util.Collection
            public int size() {
                return ShortObjectHashMap.this.size;
            }
        };
    }

    @Override // java.util.Map
    public int hashCode() {
        int hash = this.size;
        for (short key : this.keys) {
            hash ^= hashCode(key);
        }
        return hash;
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ShortObjectMap)) {
            return false;
        }
        ShortObjectMap other = (ShortObjectMap) obj;
        if (this.size != other.size()) {
            return false;
        }
        for (int i = 0; i < this.values.length; i++) {
            V value = this.values[i];
            if (value != null) {
                short key = this.keys[i];
                Object otherValue = other.get(key);
                if (value == NULL_VALUE) {
                    if (otherValue != null) {
                        return false;
                    }
                } else if (!value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return containsKey(objectToKey(key));
    }

    @Override // java.util.Map
    public V get(Object key) {
        return get(objectToKey(key));
    }

    /* renamed from: put, reason: avoid collision after fix types in other method */
    public V put2(Short key, V value) {
        return put(objectToKey(key), (short) value);
    }

    @Override // java.util.Map
    public V remove(Object key) {
        return remove(objectToKey(key));
    }

    @Override // java.util.Map
    public Set<Short> keySet() {
        return this.keySet;
    }

    @Override // java.util.Map
    public Set<Map.Entry<Short, V>> entrySet() {
        return this.entrySet;
    }

    private short objectToKey(Object key) {
        return ((Short) key).shortValue();
    }

    private int indexOf(short key) {
        int startIndex = hashIndex(key);
        int index = startIndex;
        while (this.values[index] != null) {
            if (key == this.keys[index]) {
                return index;
            }
            int iProbeNext = probeNext(index);
            index = iProbeNext;
            if (iProbeNext == startIndex) {
                return -1;
            }
        }
        return -1;
    }

    private int hashIndex(short key) {
        return hashCode(key) & this.mask;
    }

    private static int hashCode(short key) {
        return key;
    }

    private int probeNext(int index) {
        return (index + 1) & this.mask;
    }

    private void growSize() {
        this.size++;
        if (this.size > this.maxSize) {
            if (this.keys.length == Integer.MAX_VALUE) {
                throw new IllegalStateException("Max capacity reached at size=" + this.size);
            }
            rehash(this.keys.length << 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean removeAt(int index) {
        this.size--;
        this.keys[index] = 0;
        this.values[index] = null;
        int nextFree = index;
        int i = probeNext(index);
        V v = this.values[i];
        while (true) {
            V value = v;
            if (value == null) {
                break;
            }
            short key = this.keys[i];
            int bucket = hashIndex(key);
            if ((i < bucket && (bucket <= nextFree || nextFree <= i)) || (bucket <= nextFree && nextFree <= i)) {
                this.keys[nextFree] = key;
                this.values[nextFree] = value;
                this.keys[i] = 0;
                this.values[i] = null;
                nextFree = i;
            }
            V[] vArr = this.values;
            int iProbeNext = probeNext(i);
            i = iProbeNext;
            v = vArr[iProbeNext];
        }
        return nextFree != index;
    }

    private int calcMaxSize(int capacity) {
        int upperBound = capacity - 1;
        return Math.min(upperBound, (int) (capacity * this.loadFactor));
    }

    private void rehash(int i) {
        int i2;
        short[] sArr = this.keys;
        V[] vArr = this.values;
        this.keys = new short[i];
        this.values = (V[]) new Object[i];
        this.maxSize = calcMaxSize(i);
        this.mask = i - 1;
        for (int i3 = 0; i3 < vArr.length; i3++) {
            V v = vArr[i3];
            if (v != null) {
                short s = sArr[i3];
                int iHashIndex = hashIndex(s);
                while (true) {
                    i2 = iHashIndex;
                    if (this.values[i2] == null) {
                        break;
                    } else {
                        iHashIndex = probeNext(i2);
                    }
                }
                this.keys[i2] = s;
                this.values[i2] = v;
            }
        }
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder(4 * this.size);
        sb.append('{');
        boolean first = true;
        for (int i = 0; i < this.values.length; i++) {
            V value = this.values[i];
            if (value != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(keyToString(this.keys[i])).append('=').append(value == this ? "(this Map)" : toExternal(value));
                first = false;
            }
        }
        return sb.append('}').toString();
    }

    protected String keyToString(short key) {
        return Short.toString(key);
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ShortObjectHashMap$EntrySet.class */
    private final class EntrySet extends AbstractSet<Map.Entry<Short, V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<Short, V>> iterator() {
            return new MapIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return ShortObjectHashMap.this.size();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ShortObjectHashMap$KeySet.class */
    private final class KeySet extends AbstractSet<Short> {
        private KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return ShortObjectHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return ShortObjectHashMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return ShortObjectHashMap.this.remove(o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean retainAll(Collection<?> retainedKeys) {
            boolean changed = false;
            Iterator<ShortObjectMap.PrimitiveEntry<V>> iter = ShortObjectHashMap.this.entries().iterator();
            while (iter.hasNext()) {
                ShortObjectMap.PrimitiveEntry<V> entry = iter.next();
                if (!retainedKeys.contains(Short.valueOf(entry.key()))) {
                    changed = true;
                    iter.remove();
                }
            }
            return changed;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            ShortObjectHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Short> iterator() {
            return new Iterator<Short>() { // from class: io.netty.util.collection.ShortObjectHashMap.KeySet.1
                private final Iterator<Map.Entry<Short, V>> iter;

                {
                    this.iter = ShortObjectHashMap.this.entrySet.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.iter.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Short next() {
                    return this.iter.next().getKey();
                }

                @Override // java.util.Iterator
                public void remove() {
                    this.iter.remove();
                }
            };
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ShortObjectHashMap$PrimitiveIterator.class */
    private final class PrimitiveIterator implements Iterator<ShortObjectMap.PrimitiveEntry<V>>, ShortObjectMap.PrimitiveEntry<V> {
        private int prevIndex;
        private int nextIndex;
        private int entryIndex;

        private PrimitiveIterator() {
            this.prevIndex = -1;
            this.nextIndex = -1;
            this.entryIndex = -1;
        }

        private void scanNext() {
            do {
                int i = this.nextIndex + 1;
                this.nextIndex = i;
                if (i == ShortObjectHashMap.this.values.length) {
                    return;
                }
            } while (ShortObjectHashMap.this.values[this.nextIndex] == null);
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.nextIndex == -1) {
                scanNext();
            }
            return this.nextIndex != ShortObjectHashMap.this.values.length;
        }

        @Override // java.util.Iterator
        public ShortObjectMap.PrimitiveEntry<V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.prevIndex = this.nextIndex;
            scanNext();
            this.entryIndex = this.prevIndex;
            return this;
        }

        @Override // java.util.Iterator
        public void remove() {
            if (this.prevIndex != -1) {
                if (ShortObjectHashMap.this.removeAt(this.prevIndex)) {
                    this.nextIndex = this.prevIndex;
                }
                this.prevIndex = -1;
                return;
            }
            throw new IllegalStateException("next must be called before each remove.");
        }

        @Override // io.netty.util.collection.ShortObjectMap.PrimitiveEntry
        public short key() {
            return ShortObjectHashMap.this.keys[this.entryIndex];
        }

        @Override // io.netty.util.collection.ShortObjectMap.PrimitiveEntry
        public V value() {
            return (V) ShortObjectHashMap.toExternal(ShortObjectHashMap.this.values[this.entryIndex]);
        }

        @Override // io.netty.util.collection.ShortObjectMap.PrimitiveEntry
        public void setValue(V value) {
            ShortObjectHashMap.this.values[this.entryIndex] = ShortObjectHashMap.toInternal(value);
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ShortObjectHashMap$MapIterator.class */
    private final class MapIterator implements Iterator<Map.Entry<Short, V>> {
        private final ShortObjectHashMap<V>.PrimitiveIterator iter;

        private MapIterator() {
            this.iter = new PrimitiveIterator();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }

        @Override // java.util.Iterator
        public Map.Entry<Short, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.iter.next();
            return new MapEntry(((PrimitiveIterator) this.iter).entryIndex);
        }

        @Override // java.util.Iterator
        public void remove() {
            this.iter.remove();
        }
    }

    /* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/collection/ShortObjectHashMap$MapEntry.class */
    final class MapEntry implements Map.Entry<Short, V> {
        private final int entryIndex;

        MapEntry(int entryIndex) {
            this.entryIndex = entryIndex;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Map.Entry
        public Short getKey() {
            verifyExists();
            return Short.valueOf(ShortObjectHashMap.this.keys[this.entryIndex]);
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            verifyExists();
            return (V) ShortObjectHashMap.toExternal(ShortObjectHashMap.this.values[this.entryIndex]);
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            verifyExists();
            V v2 = (V) ShortObjectHashMap.toExternal(ShortObjectHashMap.this.values[this.entryIndex]);
            ShortObjectHashMap.this.values[this.entryIndex] = ShortObjectHashMap.toInternal(v);
            return v2;
        }

        private void verifyExists() {
            if (ShortObjectHashMap.this.values[this.entryIndex] == null) {
                throw new IllegalStateException("The map entry has been removed");
            }
        }
    }
}
