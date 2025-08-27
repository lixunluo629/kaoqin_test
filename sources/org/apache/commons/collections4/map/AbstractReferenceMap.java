package org.apache.commons.collections4.map;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;
import org.apache.commons.collections4.map.AbstractHashedMap;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap.class */
public abstract class AbstractReferenceMap<K, V> extends AbstractHashedMap<K, V> {
    private ReferenceStrength keyType;
    private ReferenceStrength valueType;
    private boolean purgeValues;
    private transient ReferenceQueue<Object> queue;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected /* bridge */ /* synthetic */ AbstractHashedMap.HashEntry createEntry(AbstractHashedMap.HashEntry x0, int x1, Object obj, Object obj2) {
        return createEntry((AbstractHashedMap.HashEntry<int, Object>) x0, x1, (int) obj, obj2);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceStrength.class */
    public enum ReferenceStrength {
        HARD(0),
        SOFT(1),
        WEAK(2);

        public final int value;

        public static ReferenceStrength resolve(int value) {
            switch (value) {
                case 0:
                    return HARD;
                case 1:
                    return SOFT;
                case 2:
                    return WEAK;
                default:
                    throw new IllegalArgumentException();
            }
        }

        ReferenceStrength(int value) {
            this.value = value;
        }
    }

    protected AbstractReferenceMap() {
    }

    protected AbstractReferenceMap(ReferenceStrength keyType, ReferenceStrength valueType, int capacity, float loadFactor, boolean purgeValues) {
        super(capacity, loadFactor);
        this.keyType = keyType;
        this.valueType = valueType;
        this.purgeValues = purgeValues;
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected void init() {
        this.queue = new ReferenceQueue<>();
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public int size() {
        purgeBeforeRead();
        return super.size();
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public boolean isEmpty() {
        purgeBeforeRead();
        return super.isEmpty();
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public boolean containsKey(Object key) {
        purgeBeforeRead();
        Map.Entry<K, V> entry = getEntry(key);
        return (entry == null || entry.getValue() == null) ? false : true;
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public boolean containsValue(Object value) {
        purgeBeforeRead();
        if (value == null) {
            return false;
        }
        return super.containsValue(value);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public V get(Object key) {
        purgeBeforeRead();
        Map.Entry<K, V> entry = getEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Put
    public V put(K k, V v) {
        if (k == null) {
            throw new NullPointerException("null keys not allowed");
        }
        if (v == null) {
            throw new NullPointerException("null values not allowed");
        }
        purgeBeforeWrite();
        return (V) super.put(k, v);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public V remove(Object obj) {
        if (obj == null) {
            return null;
        }
        purgeBeforeWrite();
        return (V) super.remove(obj);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Put
    public void clear() {
        super.clear();
        while (this.queue.poll() != null) {
        }
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, org.apache.commons.collections4.IterableGet
    public MapIterator<K, V> mapIterator() {
        return new ReferenceMapIterator(this);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new ReferenceEntrySet(this);
        }
        return this.entrySet;
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = new ReferenceKeySet(this);
        }
        return this.keySet;
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap, java.util.AbstractMap, java.util.Map, org.apache.commons.collections4.Get
    public Collection<V> values() {
        if (this.values == null) {
            this.values = new ReferenceValues(this);
        }
        return this.values;
    }

    protected void purgeBeforeRead() {
        purge();
    }

    protected void purgeBeforeWrite() {
        purge();
    }

    protected void purge() {
        Reference<?> referencePoll = this.queue.poll();
        while (true) {
            Reference<?> ref = referencePoll;
            if (ref != null) {
                purge(ref);
                referencePoll = this.queue.poll();
            } else {
                return;
            }
        }
    }

    protected void purge(Reference<?> ref) {
        int hash = ref.hashCode();
        int index = hashIndex(hash, this.data.length);
        AbstractHashedMap.HashEntry<K, V> previous = null;
        AbstractHashedMap.HashEntry<K, V> hashEntry = this.data[index];
        while (true) {
            AbstractHashedMap.HashEntry<K, V> entry = hashEntry;
            if (entry != null) {
                if (((ReferenceEntry) entry).purge(ref)) {
                    if (previous == null) {
                        this.data[index] = entry.next;
                    } else {
                        previous.next = entry.next;
                    }
                    this.size--;
                    return;
                }
                previous = entry;
                hashEntry = entry.next;
            } else {
                return;
            }
        }
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected AbstractHashedMap.HashEntry<K, V> getEntry(Object key) {
        if (key == null) {
            return null;
        }
        return super.getEntry(key);
    }

    protected int hashEntry(Object key, Object value) {
        return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected boolean isEqualKey(Object key1, Object key2) {
        Object key22 = this.keyType == ReferenceStrength.HARD ? key2 : ((Reference) key2).get();
        return key1 == key22 || key1.equals(key22);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected ReferenceEntry<K, V> createEntry(AbstractHashedMap.HashEntry<K, V> next, int hashCode, K key, V value) {
        return new ReferenceEntry<>(this, next, hashCode, key, value);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected Iterator<Map.Entry<K, V>> createEntrySetIterator() {
        return new ReferenceEntrySetIterator(this);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected Iterator<K> createKeySetIterator() {
        return new ReferenceKeySetIterator(this);
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected Iterator<V> createValuesIterator() {
        return new ReferenceValuesIterator(this);
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceEntrySet.class */
    static class ReferenceEntrySet<K, V> extends AbstractHashedMap.EntrySet<K, V> {
        protected ReferenceEntrySet(AbstractHashedMap<K, V> parent) {
            super(parent);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return toArray(new Object[size()]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            ArrayList arrayList = new ArrayList(size());
            Iterator<Map.Entry<K, V>> it = iterator();
            while (it.hasNext()) {
                arrayList.add(new DefaultMapEntry(it.next()));
            }
            return (T[]) arrayList.toArray(tArr);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceKeySet.class */
    static class ReferenceKeySet<K> extends AbstractHashedMap.KeySet<K> {
        protected ReferenceKeySet(AbstractHashedMap<K, ?> parent) {
            super(parent);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public Object[] toArray() {
            return toArray(new Object[size()]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public <T> T[] toArray(T[] tArr) {
            ArrayList arrayList = new ArrayList(size());
            Iterator<K> it = iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            return (T[]) arrayList.toArray(tArr);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceValues.class */
    static class ReferenceValues<V> extends AbstractHashedMap.Values<V> {
        protected ReferenceValues(AbstractHashedMap<?, V> parent) {
            super(parent);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public Object[] toArray() {
            return toArray(new Object[size()]);
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            ArrayList arrayList = new ArrayList(size());
            Iterator<V> it = iterator();
            while (it.hasNext()) {
                arrayList.add(it.next());
            }
            return (T[]) arrayList.toArray(tArr);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceEntry.class */
    protected static class ReferenceEntry<K, V> extends AbstractHashedMap.HashEntry<K, V> {
        private final AbstractReferenceMap<K, V> parent;

        public ReferenceEntry(AbstractReferenceMap<K, V> parent, AbstractHashedMap.HashEntry<K, V> next, int hashCode, K key, V value) {
            super(next, hashCode, null, null);
            this.parent = parent;
            this.key = toReference(((AbstractReferenceMap) parent).keyType, key, hashCode);
            this.value = toReference(((AbstractReferenceMap) parent).valueType, value, hashCode);
        }

        @Override // org.apache.commons.collections4.map.AbstractHashedMap.HashEntry, java.util.Map.Entry, org.apache.commons.collections4.KeyValue
        public K getKey() {
            return ((AbstractReferenceMap) this.parent).keyType == ReferenceStrength.HARD ? (K) this.key : (K) ((Reference) this.key).get();
        }

        @Override // org.apache.commons.collections4.map.AbstractHashedMap.HashEntry, java.util.Map.Entry, org.apache.commons.collections4.KeyValue
        public V getValue() {
            return ((AbstractReferenceMap) this.parent).valueType == ReferenceStrength.HARD ? (V) this.value : (V) ((Reference) this.value).get();
        }

        @Override // org.apache.commons.collections4.map.AbstractHashedMap.HashEntry, java.util.Map.Entry
        public V setValue(V obj) {
            V old = getValue();
            if (((AbstractReferenceMap) this.parent).valueType != ReferenceStrength.HARD) {
                ((Reference) this.value).clear();
            }
            this.value = toReference(((AbstractReferenceMap) this.parent).valueType, obj, this.hashCode);
            return old;
        }

        @Override // org.apache.commons.collections4.map.AbstractHashedMap.HashEntry, java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry) obj;
            Object entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            return entryKey != null && entryValue != null && this.parent.isEqualKey(entryKey, this.key) && this.parent.isEqualValue(entryValue, getValue());
        }

        @Override // org.apache.commons.collections4.map.AbstractHashedMap.HashEntry, java.util.Map.Entry
        public int hashCode() {
            return this.parent.hashEntry(getKey(), getValue());
        }

        protected <T> Object toReference(ReferenceStrength type, T referent, int hash) {
            if (type == ReferenceStrength.HARD) {
                return referent;
            }
            if (type == ReferenceStrength.SOFT) {
                return new SoftRef(hash, referent, ((AbstractReferenceMap) this.parent).queue);
            }
            if (type == ReferenceStrength.WEAK) {
                return new WeakRef(hash, referent, ((AbstractReferenceMap) this.parent).queue);
            }
            throw new Error();
        }

        boolean purge(Reference<?> ref) {
            boolean r = (((AbstractReferenceMap) this.parent).keyType != ReferenceStrength.HARD && this.key == ref) || (((AbstractReferenceMap) this.parent).valueType != ReferenceStrength.HARD && this.value == ref);
            if (r) {
                if (((AbstractReferenceMap) this.parent).keyType != ReferenceStrength.HARD) {
                    ((Reference) this.key).clear();
                }
                if (((AbstractReferenceMap) this.parent).valueType == ReferenceStrength.HARD) {
                    if (((AbstractReferenceMap) this.parent).purgeValues) {
                        this.value = null;
                    }
                } else {
                    ((Reference) this.value).clear();
                }
            }
            return r;
        }

        protected ReferenceEntry<K, V> next() {
            return (ReferenceEntry) this.next;
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceBaseIterator.class */
    static class ReferenceBaseIterator<K, V> {
        final AbstractReferenceMap<K, V> parent;
        int index;
        ReferenceEntry<K, V> entry;
        ReferenceEntry<K, V> previous;
        K currentKey;
        K nextKey;
        V currentValue;
        V nextValue;
        int expectedModCount;

        public ReferenceBaseIterator(AbstractReferenceMap<K, V> parent) {
            this.parent = parent;
            this.index = parent.size() != 0 ? parent.data.length : 0;
            this.expectedModCount = parent.modCount;
        }

        public boolean hasNext() {
            checkMod();
            while (nextNull()) {
                ReferenceEntry<K, V> e = this.entry;
                int i = this.index;
                while (e == null && i > 0) {
                    i--;
                    e = (ReferenceEntry) this.parent.data[i];
                }
                this.entry = e;
                this.index = i;
                if (e == null) {
                    this.currentKey = null;
                    this.currentValue = null;
                    return false;
                }
                this.nextKey = e.getKey();
                this.nextValue = e.getValue();
                if (nextNull()) {
                    this.entry = this.entry.next();
                }
            }
            return true;
        }

        private void checkMod() {
            if (this.parent.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }

        private boolean nextNull() {
            return this.nextKey == null || this.nextValue == null;
        }

        protected ReferenceEntry<K, V> nextEntry() {
            checkMod();
            if (nextNull() && !hasNext()) {
                throw new NoSuchElementException();
            }
            this.previous = this.entry;
            this.entry = this.entry.next();
            this.currentKey = this.nextKey;
            this.currentValue = this.nextValue;
            this.nextKey = null;
            this.nextValue = null;
            return this.previous;
        }

        protected ReferenceEntry<K, V> currentEntry() {
            checkMod();
            return this.previous;
        }

        public void remove() {
            checkMod();
            if (this.previous == null) {
                throw new IllegalStateException();
            }
            this.parent.remove(this.currentKey);
            this.previous = null;
            this.currentKey = null;
            this.currentValue = null;
            this.expectedModCount = this.parent.modCount;
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceEntrySetIterator.class */
    static class ReferenceEntrySetIterator<K, V> extends ReferenceBaseIterator<K, V> implements Iterator<Map.Entry<K, V>> {
        public ReferenceEntrySetIterator(AbstractReferenceMap<K, V> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceKeySetIterator.class */
    static class ReferenceKeySetIterator<K> extends ReferenceBaseIterator<K, Object> implements Iterator<K> {
        ReferenceKeySetIterator(AbstractReferenceMap<K, ?> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceValuesIterator.class */
    static class ReferenceValuesIterator<V> extends ReferenceBaseIterator<Object, V> implements Iterator<V> {
        ReferenceValuesIterator(AbstractReferenceMap<?, V> parent) {
            super(parent);
        }

        @Override // java.util.Iterator
        public V next() {
            return nextEntry().getValue();
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$ReferenceMapIterator.class */
    static class ReferenceMapIterator<K, V> extends ReferenceBaseIterator<K, V> implements MapIterator<K, V> {
        protected ReferenceMapIterator(AbstractReferenceMap<K, V> parent) {
            super(parent);
        }

        @Override // org.apache.commons.collections4.MapIterator, java.util.Iterator
        public K next() {
            return nextEntry().getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public K getKey() {
            AbstractHashedMap.HashEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException("getKey() can only be called after next() and before remove()");
            }
            return current.getKey();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V getValue() {
            AbstractHashedMap.HashEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException("getValue() can only be called after next() and before remove()");
            }
            return current.getValue();
        }

        @Override // org.apache.commons.collections4.MapIterator
        public V setValue(V value) {
            AbstractHashedMap.HashEntry<K, V> current = currentEntry();
            if (current == null) {
                throw new IllegalStateException("setValue() can only be called after next() and before remove()");
            }
            return current.setValue(value);
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$SoftRef.class */
    static class SoftRef<T> extends SoftReference<T> {
        private final int hash;

        public SoftRef(int hash, T r, ReferenceQueue<? super T> q) {
            super(r, q);
            this.hash = hash;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    /* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/map/AbstractReferenceMap$WeakRef.class */
    static class WeakRef<T> extends WeakReference<T> {
        private final int hash;

        public WeakRef(int hash, T r, ReferenceQueue<? super T> q) {
            super(r, q);
            this.hash = hash;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected void doWriteObject(ObjectOutputStream out) throws IOException {
        out.writeInt(this.keyType.value);
        out.writeInt(this.valueType.value);
        out.writeBoolean(this.purgeValues);
        out.writeFloat(this.loadFactor);
        out.writeInt(this.data.length);
        MapIterator<K, V> it = mapIterator();
        while (it.hasNext()) {
            out.writeObject(it.next());
            out.writeObject(it.getValue());
        }
        out.writeObject(null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.collections4.map.AbstractHashedMap
    protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.keyType = ReferenceStrength.resolve(in.readInt());
        this.valueType = ReferenceStrength.resolve(in.readInt());
        this.purgeValues = in.readBoolean();
        this.loadFactor = in.readFloat();
        int capacity = in.readInt();
        init();
        this.data = new AbstractHashedMap.HashEntry[capacity];
        while (true) {
            Object object = in.readObject();
            if (object != null) {
                put(object, in.readObject());
            } else {
                this.threshold = calculateThreshold(this.data.length, this.loadFactor);
                return;
            }
        }
    }

    protected boolean isKeyType(ReferenceStrength type) {
        return this.keyType == type;
    }
}
