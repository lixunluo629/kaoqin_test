package org.springframework.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap.class */
public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
    private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;
    private static final int MAXIMUM_CONCURRENCY_LEVEL = 65536;
    private static final int MAXIMUM_SEGMENT_SIZE = 1073741824;
    private final ConcurrentReferenceHashMap<K, V>.Segment[] segments;
    private final float loadFactor;
    private final ReferenceType referenceType;
    private final int shift;
    private volatile Set<Map.Entry<K, V>> entrySet;

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$Reference.class */
    protected interface Reference<K, V> {
        Entry<K, V> get();

        int getHash();

        Reference<K, V> getNext();

        void release();
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$ReferenceType.class */
    public enum ReferenceType {
        SOFT,
        WEAK
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$Restructure.class */
    protected enum Restructure {
        WHEN_NECESSARY,
        NEVER
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$TaskOption.class */
    private enum TaskOption {
        RESTRUCTURE_BEFORE,
        RESTRUCTURE_AFTER,
        SKIP_IF_EMPTY,
        RESIZE
    }

    public ConcurrentReferenceHashMap() {
        this(16, DEFAULT_LOAD_FACTOR, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 16, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, int concurrencyLevel) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, ReferenceType referenceType) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, 16, referenceType);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this(initialCapacity, loadFactor, concurrencyLevel, DEFAULT_REFERENCE_TYPE);
    }

    public ConcurrentReferenceHashMap(int initialCapacity, float loadFactor, int concurrencyLevel, ReferenceType referenceType) {
        Assert.isTrue(initialCapacity >= 0, "Initial capacity must not be negative");
        Assert.isTrue(loadFactor > 0.0f, "Load factor must be positive");
        Assert.isTrue(concurrencyLevel > 0, "Concurrency level must be positive");
        Assert.notNull(referenceType, "Reference type must not be null");
        this.loadFactor = loadFactor;
        this.shift = calculateShift(concurrencyLevel, 65536);
        int size = 1 << this.shift;
        this.referenceType = referenceType;
        int roundedUpSegmentCapacity = (int) (((initialCapacity + size) - 1) / size);
        this.segments = (Segment[]) Array.newInstance((Class<?>) Segment.class, size);
        for (int i = 0; i < this.segments.length; i++) {
            this.segments[i] = new Segment(roundedUpSegmentCapacity);
        }
    }

    protected final float getLoadFactor() {
        return this.loadFactor;
    }

    protected final int getSegmentsSize() {
        return this.segments.length;
    }

    protected final ConcurrentReferenceHashMap<K, V>.Segment getSegment(int index) {
        return this.segments[index];
    }

    protected ConcurrentReferenceHashMap<K, V>.ReferenceManager createReferenceManager() {
        return new ReferenceManager();
    }

    protected int getHash(Object o) {
        int hash = o != null ? o.hashCode() : 0;
        int hash2 = hash + ((hash << 15) ^ (-12931));
        int hash3 = hash2 ^ (hash2 >>> 10);
        int hash4 = hash3 + (hash3 << 3);
        int hash5 = hash4 ^ (hash4 >>> 6);
        int hash6 = hash5 + (hash5 << 2) + (hash5 << 14);
        return hash6 ^ (hash6 >>> 16);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        Entry<K, V> entry = getEntryIfAvailable(key);
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V getOrDefault(Object key, V defaultValue) {
        Entry<K, V> entry = getEntryIfAvailable(key);
        return entry != null ? entry.getValue() : defaultValue;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        Entry<K, V> entry = getEntryIfAvailable(key);
        return entry != null && ObjectUtils.nullSafeEquals(entry.getKey(), key);
    }

    private Entry<K, V> getEntryIfAvailable(Object key) {
        Reference<K, V> ref = getReference(key, Restructure.WHEN_NECESSARY);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    protected final Reference<K, V> getReference(Object key, Restructure restructure) {
        int hash = getHash(key);
        return getSegmentForHash(hash).getReference(key, hash, restructure);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K key, V value) {
        return put(key, value, true);
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V putIfAbsent(K key, V value) {
        return put(key, value, false);
    }

    private V put(K k, final V v, final boolean z) {
        return (V) doTask(k, new ConcurrentReferenceHashMap<K, V>.Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.RESIZE}) { // from class: org.springframework.util.ConcurrentReferenceHashMap.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.springframework.util.ConcurrentReferenceHashMap.Task
            protected V execute(Reference<K, V> reference, Entry<K, V> entry, ConcurrentReferenceHashMap<K, V>.Entries entries) {
                if (entry != 0) {
                    V v2 = (V) entry.getValue();
                    if (z) {
                        entry.setValue(v);
                    }
                    return v2;
                }
                entries.add(v);
                return null;
            }
        });
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        return (V) doTask(obj, new ConcurrentReferenceHashMap<K, V>.Task<V>(TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY) { // from class: org.springframework.util.ConcurrentReferenceHashMap.2
            @Override // org.springframework.util.ConcurrentReferenceHashMap.Task
            protected V execute(Reference<K, V> reference, Entry<K, V> entry) {
                if (entry != null) {
                    reference.release();
                    return (V) ((Entry) entry).value;
                }
                return null;
            }
        });
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean remove(Object obj, final Object obj2) {
        return ((Boolean) doTask(obj, new ConcurrentReferenceHashMap<K, V>.Task<Boolean>(new TaskOption[]{TaskOption.RESTRUCTURE_AFTER, TaskOption.SKIP_IF_EMPTY}) { // from class: org.springframework.util.ConcurrentReferenceHashMap.3
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // org.springframework.util.ConcurrentReferenceHashMap.Task
            public Boolean execute(Reference<K, V> ref, Entry<K, V> entry) {
                if (entry != null && ObjectUtils.nullSafeEquals(entry.getValue(), obj2)) {
                    ref.release();
                    return true;
                }
                return false;
            }
        })).booleanValue();
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public boolean replace(K k, final V v, final V v2) {
        return ((Boolean) doTask(k, new ConcurrentReferenceHashMap<K, V>.Task<Boolean>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY}) { // from class: org.springframework.util.ConcurrentReferenceHashMap.4
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.springframework.util.ConcurrentReferenceHashMap.Task
            public Boolean execute(Reference<K, V> ref, Entry<K, V> entry) {
                if (entry != 0 && ObjectUtils.nullSafeEquals(entry.getValue(), v)) {
                    entry.setValue(v2);
                    return true;
                }
                return false;
            }
        })).booleanValue();
    }

    @Override // java.util.Map, java.util.concurrent.ConcurrentMap
    public V replace(K k, final V v) {
        return (V) doTask(k, new ConcurrentReferenceHashMap<K, V>.Task<V>(new TaskOption[]{TaskOption.RESTRUCTURE_BEFORE, TaskOption.SKIP_IF_EMPTY}) { // from class: org.springframework.util.ConcurrentReferenceHashMap.5
            /* JADX WARN: Multi-variable type inference failed */
            @Override // org.springframework.util.ConcurrentReferenceHashMap.Task
            protected V execute(Reference<K, V> reference, Entry<K, V> entry) {
                if (entry != 0) {
                    V v2 = (V) entry.getValue();
                    entry.setValue(v);
                    return v2;
                }
                return null;
            }
        });
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        for (ConcurrentReferenceHashMap<K, V>.Segment segment : this.segments) {
            segment.clear();
        }
    }

    public void purgeUnreferencedEntries() {
        for (ConcurrentReferenceHashMap<K, V>.Segment segment : this.segments) {
            segment.restructureIfNecessary(false);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        int size = 0;
        for (ConcurrentReferenceHashMap<K, V>.Segment segment : this.segments) {
            size += segment.getCount();
        }
        return size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        for (ConcurrentReferenceHashMap<K, V>.Segment segment : this.segments) {
            if (segment.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entrySet = this.entrySet;
        if (entrySet == null) {
            entrySet = new EntrySet();
            this.entrySet = entrySet;
        }
        return entrySet;
    }

    private <T> T doTask(Object obj, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
        int hash = getHash(obj);
        return (T) getSegmentForHash(hash).doTask(hash, obj, task);
    }

    private ConcurrentReferenceHashMap<K, V>.Segment getSegmentForHash(int hash) {
        return this.segments[(hash >>> (32 - this.shift)) & (this.segments.length - 1)];
    }

    protected static int calculateShift(int minimumValue, int maximumValue) {
        int shift = 0;
        int value = 1;
        while (value < minimumValue && value < maximumValue) {
            value <<= 1;
            shift++;
        }
        return shift;
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$Segment.class */
    protected final class Segment extends ReentrantLock {
        private final ConcurrentReferenceHashMap<K, V>.ReferenceManager referenceManager;
        private final int initialSize;
        private volatile Reference<K, V>[] references;
        private volatile int count = 0;
        private int resizeThreshold;

        static /* synthetic */ int access$508(Segment x0) {
            int i = x0.count;
            x0.count = i + 1;
            return i;
        }

        public Segment(int initialCapacity) {
            this.referenceManager = ConcurrentReferenceHashMap.this.createReferenceManager();
            this.initialSize = 1 << ConcurrentReferenceHashMap.calculateShift(initialCapacity, 1073741824);
            setReferences(createReferenceArray(this.initialSize));
        }

        public Reference<K, V> getReference(Object key, int hash, Restructure restructure) {
            if (restructure == Restructure.WHEN_NECESSARY) {
                restructureIfNecessary(false);
            }
            if (this.count == 0) {
                return null;
            }
            Reference<K, V>[] references = this.references;
            int index = getIndex(hash, references);
            Reference<K, V> head = references[index];
            return findInChain(head, key, hash);
        }

        public <T> T doTask(final int hash, final Object key, ConcurrentReferenceHashMap<K, V>.Task<T> task) {
            boolean resize = task.hasOption(TaskOption.RESIZE);
            if (task.hasOption(TaskOption.RESTRUCTURE_BEFORE)) {
                restructureIfNecessary(resize);
            }
            if (task.hasOption(TaskOption.SKIP_IF_EMPTY) && this.count == 0) {
                return task.execute(null, null, null);
            }
            lock();
            try {
                final int index = getIndex(hash, this.references);
                final Reference<K, V> head = this.references[index];
                Reference<K, V> ref = findInChain(head, key, hash);
                Entry<K, V> entry = ref != null ? ref.get() : null;
                ConcurrentReferenceHashMap<K, V>.Entries entries = new ConcurrentReferenceHashMap<K, V>.Entries() { // from class: org.springframework.util.ConcurrentReferenceHashMap.Segment.1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                    }

                    @Override // org.springframework.util.ConcurrentReferenceHashMap.Entries
                    public void add(V value) {
                        Entry<K, V> newEntry = new Entry<>(key, value);
                        Reference<K, V> newReference = Segment.this.referenceManager.createReference(newEntry, hash, head);
                        Segment.this.references[index] = newReference;
                        Segment.access$508(Segment.this);
                    }
                };
                T tExecute = task.execute(ref, entry, entries);
                unlock();
                if (task.hasOption(TaskOption.RESTRUCTURE_AFTER)) {
                    restructureIfNecessary(resize);
                }
                return tExecute;
            } catch (Throwable th) {
                unlock();
                if (task.hasOption(TaskOption.RESTRUCTURE_AFTER)) {
                    restructureIfNecessary(resize);
                }
                throw th;
            }
        }

        public void clear() {
            if (this.count == 0) {
                return;
            }
            lock();
            try {
                setReferences(createReferenceArray(this.initialSize));
                this.count = 0;
            } finally {
                unlock();
            }
        }

        protected final void restructureIfNecessary(boolean allowResize) {
            boolean needsResize = this.count > 0 && this.count >= this.resizeThreshold;
            Reference<K, V> ref = this.referenceManager.pollForPurge();
            if (ref != null || (needsResize && allowResize)) {
                lock();
                try {
                    int countAfterRestructure = this.count;
                    Set<Reference<K, V>> toPurge = Collections.emptySet();
                    if (ref != null) {
                        toPurge = new HashSet<>();
                        while (ref != null) {
                            toPurge.add(ref);
                            ref = this.referenceManager.pollForPurge();
                        }
                    }
                    int countAfterRestructure2 = countAfterRestructure - toPurge.size();
                    boolean needsResize2 = countAfterRestructure2 > 0 && countAfterRestructure2 >= this.resizeThreshold;
                    boolean resizing = false;
                    int restructureSize = this.references.length;
                    if (allowResize && needsResize2 && restructureSize < 1073741824) {
                        restructureSize <<= 1;
                        resizing = true;
                    }
                    Reference<K, V>[] restructured = resizing ? createReferenceArray(restructureSize) : this.references;
                    for (int i = 0; i < this.references.length; i++) {
                        if (!resizing) {
                            restructured[i] = null;
                        }
                        for (Reference<K, V> ref2 = this.references[i]; ref2 != null; ref2 = ref2.getNext()) {
                            if (!toPurge.contains(ref2) && ref2.get() != null) {
                                int index = getIndex(ref2.getHash(), restructured);
                                restructured[index] = this.referenceManager.createReference(ref2.get(), ref2.getHash(), restructured[index]);
                            }
                        }
                    }
                    if (resizing) {
                        setReferences(restructured);
                    }
                    this.count = Math.max(countAfterRestructure2, 0);
                    unlock();
                } catch (Throwable th) {
                    unlock();
                    throw th;
                }
            }
        }

        private Reference<K, V> findInChain(Reference<K, V> ref, Object key, int hash) {
            Entry<K, V> entry;
            Reference<K, V> next = ref;
            while (true) {
                Reference<K, V> currRef = next;
                if (currRef != null) {
                    if (currRef.getHash() == hash && (entry = currRef.get()) != null) {
                        K entryKey = entry.getKey();
                        if (ObjectUtils.nullSafeEquals(entryKey, key)) {
                            return currRef;
                        }
                    }
                    next = currRef.getNext();
                } else {
                    return null;
                }
            }
        }

        private Reference<K, V>[] createReferenceArray(int size) {
            return new Reference[size];
        }

        private int getIndex(int hash, Reference<K, V>[] references) {
            return hash & (references.length - 1);
        }

        private void setReferences(Reference<K, V>[] references) {
            this.references = references;
            this.resizeThreshold = (int) (references.length * ConcurrentReferenceHashMap.this.getLoadFactor());
        }

        public final int getSize() {
            return this.references.length;
        }

        public final int getCount() {
            return this.count;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$Entry.class */
    protected static final class Entry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private volatile V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V value) {
            V previous = this.value;
            this.value = value;
            return previous;
        }

        public String toString() {
            return this.key + SymbolConstants.EQUAL_SYMBOL + this.value;
        }

        @Override // java.util.Map.Entry
        public final boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Map.Entry)) {
                return false;
            }
            Map.Entry otherEntry = (Map.Entry) other;
            return ObjectUtils.nullSafeEquals(getKey(), otherEntry.getKey()) && ObjectUtils.nullSafeEquals(getValue(), otherEntry.getValue());
        }

        @Override // java.util.Map.Entry
        public final int hashCode() {
            return ObjectUtils.nullSafeHashCode(this.key) ^ ObjectUtils.nullSafeHashCode(this.value);
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$Task.class */
    private abstract class Task<T> {
        private final EnumSet<TaskOption> options;

        public Task(TaskOption... options) {
            this.options = options.length == 0 ? EnumSet.noneOf(TaskOption.class) : EnumSet.of(options[0], options);
        }

        public boolean hasOption(TaskOption option) {
            return this.options.contains(option);
        }

        protected T execute(Reference<K, V> ref, Entry<K, V> entry, ConcurrentReferenceHashMap<K, V>.Entries entries) {
            return execute(ref, entry);
        }

        protected T execute(Reference<K, V> ref, Entry<K, V> entry) {
            return null;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$Entries.class */
    private abstract class Entries {
        public abstract void add(V v);

        private Entries() {
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$EntrySet.class */
    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry) o;
                Reference<K, V> ref = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), Restructure.NEVER);
                Entry<K, V> otherEntry = ref != null ? ref.get() : null;
                if (otherEntry != null) {
                    return ObjectUtils.nullSafeEquals(otherEntry.getValue(), otherEntry.getValue());
                }
                return false;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry) o;
                return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return ConcurrentReferenceHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            ConcurrentReferenceHashMap.this.clear();
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$EntryIterator.class */
    private class EntryIterator implements Iterator<Map.Entry<K, V>> {
        private int segmentIndex;
        private int referenceIndex;
        private Reference<K, V>[] references;
        private Reference<K, V> reference;
        private Entry<K, V> next;
        private Entry<K, V> last;

        public EntryIterator() {
            moveToNextSegment();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            getNextIfNecessary();
            return this.next != null;
        }

        @Override // java.util.Iterator
        public Entry<K, V> next() {
            getNextIfNecessary();
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            this.last = this.next;
            this.next = null;
            return this.last;
        }

        private void getNextIfNecessary() {
            while (this.next == null) {
                moveToNextReference();
                if (this.reference == null) {
                    return;
                } else {
                    this.next = this.reference.get();
                }
            }
        }

        private void moveToNextReference() {
            if (this.reference != null) {
                this.reference = this.reference.getNext();
            }
            while (this.reference == null && this.references != null) {
                if (this.referenceIndex >= this.references.length) {
                    moveToNextSegment();
                    this.referenceIndex = 0;
                } else {
                    this.reference = this.references[this.referenceIndex];
                    this.referenceIndex++;
                }
            }
        }

        private void moveToNextSegment() {
            this.reference = null;
            this.references = null;
            if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
                this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
                this.segmentIndex++;
            }
        }

        @Override // java.util.Iterator
        public void remove() {
            Assert.state(this.last != null, "No element to remove");
            ConcurrentReferenceHashMap.this.remove(this.last.getKey());
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$ReferenceManager.class */
    protected class ReferenceManager {
        private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue<>();

        protected ReferenceManager() {
        }

        public Reference<K, V> createReference(Entry<K, V> entry, int hash, Reference<K, V> next) {
            if (ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK) {
                return new WeakEntryReference(entry, hash, next, this.queue);
            }
            return new SoftEntryReference(entry, hash, next, this.queue);
        }

        public Reference<K, V> pollForPurge() {
            return (Reference) this.queue.poll();
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$SoftEntryReference.class */
    private static final class SoftEntryReference<K, V> extends SoftReference<Entry<K, V>> implements Reference<K, V> {
        private final int hash;
        private final Reference<K, V> nextReference;

        @Override // java.lang.ref.SoftReference, java.lang.ref.Reference, org.springframework.util.ConcurrentReferenceHashMap.Reference
        public /* bridge */ /* synthetic */ Entry get() {
            return (Entry) super.get();
        }

        public SoftEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        @Override // org.springframework.util.ConcurrentReferenceHashMap.Reference
        public int getHash() {
            return this.hash;
        }

        @Override // org.springframework.util.ConcurrentReferenceHashMap.Reference
        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        @Override // org.springframework.util.ConcurrentReferenceHashMap.Reference
        public void release() {
            enqueue();
            clear();
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/ConcurrentReferenceHashMap$WeakEntryReference.class */
    private static final class WeakEntryReference<K, V> extends WeakReference<Entry<K, V>> implements Reference<K, V> {
        private final int hash;
        private final Reference<K, V> nextReference;

        @Override // java.lang.ref.Reference, org.springframework.util.ConcurrentReferenceHashMap.Reference
        public /* bridge */ /* synthetic */ Entry get() {
            return (Entry) super.get();
        }

        public WeakEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
            super(entry, queue);
            this.hash = hash;
            this.nextReference = next;
        }

        @Override // org.springframework.util.ConcurrentReferenceHashMap.Reference
        public int getHash() {
            return this.hash;
        }

        @Override // org.springframework.util.ConcurrentReferenceHashMap.Reference
        public Reference<K, V> getNext() {
            return this.nextReference;
        }

        @Override // org.springframework.util.ConcurrentReferenceHashMap.Reference
        public void release() {
            enqueue();
            clear();
        }
    }
}
