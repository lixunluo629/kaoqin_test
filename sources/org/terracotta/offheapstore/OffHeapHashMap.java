package org.terracotta.offheapstore;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.exceptions.OversizeMappingException;
import org.terracotta.offheapstore.jdk8.BiFunction;
import org.terracotta.offheapstore.jdk8.Function;
import org.terracotta.offheapstore.paging.Page;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.BinaryStorageEngine;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.FindbugsSuppressWarnings;
import org.terracotta.offheapstore.util.NoOpLock;
import org.terracotta.offheapstore.util.WeakIdentityHashMap;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap.class */
public class OffHeapHashMap<K, V> extends AbstractMap<K, V> implements MapInternals, StorageEngine.Owner, HashingMap<K, V> {
    private static final int INITIAL_TABLE_SIZE = 128;
    private static final float TABLE_RESIZE_THRESHOLD = 0.5f;
    private static final float TABLE_SHRINK_THRESHOLD = 0.2f;
    private static final int INITIAL_REPROBE_LENGTH = 16;
    private static final int REPROBE_WARNING_THRESHOLD = 1024;
    private static final int ALLOCATE_ON_CLEAR_THRESHOLD_RATIO = 2;
    protected static final int ENTRY_SIZE = 4;
    protected static final int STATUS = 0;
    protected static final int KEY_HASHCODE = 1;
    protected static final int ENCODING = 2;
    protected static final int STATUS_USED = 1;
    protected static final int STATUS_REMOVED = 2;
    public static final int RESERVED_STATUS_BITS = 3;
    protected final StorageEngine<? super K, ? super V> storageEngine;
    protected final PageSource tableSource;
    private final WeakIdentityHashMap<IntBuffer, PendingPage> pendingTableFrees;
    private final int initialTableSize;
    private final boolean tableAllocationsSteal;
    private final ThreadLocal<Boolean> tableResizing;
    protected volatile int size;
    protected volatile int modCount;
    protected int reprobeLimit;
    private float currentTableShrinkThreshold;
    private volatile boolean hasUsedIterators;
    protected volatile IntBuffer hashtable;
    protected volatile Page hashTablePage;
    private Set<Map.Entry<K, V>> entrySet;
    private Set<K> keySet;
    private Set<Long> encodingSet;
    protected volatile int removedSlots;
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) OffHeapHashMap.class);
    private static final IntBuffer DESTROYED_TABLE = IntBuffer.allocate(0);
    protected static final int ENTRY_BIT_SHIFT = Integer.numberOfTrailingZeros(4);

    public OffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine) {
        this(source, storageEngine, 128);
    }

    public OffHeapHashMap(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine) {
        this(source, tableAllocationsSteal, storageEngine, 128);
    }

    public OffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine, boolean bootstrap) {
        this(source, false, storageEngine, 128, bootstrap);
    }

    public OffHeapHashMap(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        this(source, false, storageEngine, tableSize, true);
    }

    public OffHeapHashMap(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        this(source, tableAllocationsSteal, storageEngine, tableSize, true);
    }

    @FindbugsSuppressWarnings({"ICAST_INTEGER_MULTIPLY_CAST_TO_LONG"})
    protected OffHeapHashMap(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap) {
        int capacity;
        this.pendingTableFrees = new WeakIdentityHashMap<>(new WeakIdentityHashMap.ReaperTask<PendingPage>() { // from class: org.terracotta.offheapstore.OffHeapHashMap.1
            @Override // org.terracotta.offheapstore.util.WeakIdentityHashMap.ReaperTask
            public void reap(PendingPage pending) {
                OffHeapHashMap.this.freeTable(pending.tablePage);
            }
        });
        this.tableResizing = new ThreadLocal<Boolean>() { // from class: org.terracotta.offheapstore.OffHeapHashMap.2
            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.lang.ThreadLocal
            public Boolean initialValue() {
                return Boolean.FALSE;
            }
        };
        this.reprobeLimit = 16;
        this.currentTableShrinkThreshold = TABLE_SHRINK_THRESHOLD;
        if (storageEngine == null) {
            throw new NullPointerException("StorageEngine implementation must be non-null");
        }
        this.storageEngine = storageEngine;
        this.tableSource = source;
        this.tableAllocationsSteal = tableAllocationsSteal;
        int i = 1;
        while (true) {
            capacity = i;
            if (capacity >= tableSize) {
                break;
            } else {
                i = capacity << 1;
            }
        }
        this.initialTableSize = capacity;
        if (bootstrap) {
            this.hashTablePage = allocateTable(this.initialTableSize);
            if (this.hashTablePage == null) {
                StringBuilder sb = new StringBuilder("Initial table allocation failed.\n");
                sb.append("Initial Table Size (slots) : ").append(this.initialTableSize).append('\n');
                sb.append("Allocation Will Require    : ").append(DebuggingUtils.toBase2SuffixedString(this.initialTableSize * 4 * 4)).append("B\n");
                sb.append("Table Page Source        : ").append(this.tableSource);
                throw new IllegalArgumentException(sb.toString());
            }
            this.hashtable = this.hashTablePage.asIntBuffer();
        }
        this.storageEngine.bind(this);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        int hash = key.hashCode();
        if (this.size == 0) {
            return false;
        }
        IntBuffer view = (IntBuffer) this.hashtable.duplicate().position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!view.hasRemaining()) {
                view.rewind();
            }
            IntBuffer entry = (IntBuffer) view.slice().limit(4);
            if (isTerminating(entry)) {
                return false;
            }
            if (isPresent(entry) && keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                hit(entry);
                return true;
            }
            view.position(view.position() + 4);
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object key) {
        int hash = key.hashCode();
        if (this.size == 0) {
            return null;
        }
        IntBuffer view = (IntBuffer) this.hashtable.duplicate().position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!view.hasRemaining()) {
                view.rewind();
            }
            IntBuffer entry = (IntBuffer) view.slice().limit(4);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                hit(entry);
                return this.storageEngine.readValue(readLong(entry, 2));
            }
            view.position(view.position() + 4);
        }
        return null;
    }

    public Long getEncodingForHashAndBinary(int hash, ByteBuffer binaryKey) {
        if (this.size == 0) {
            return null;
        }
        IntBuffer view = (IntBuffer) this.hashtable.duplicate().position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!view.hasRemaining()) {
                view.rewind();
            }
            IntBuffer entry = (IntBuffer) view.slice().limit(4);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && binaryKeyEquals(binaryKey, hash, readLong(entry, 2), entry.get(1))) {
                return Long.valueOf(readLong(entry, 2));
            }
            view.position(view.position() + 4);
        }
        return null;
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public long installMappingForHashAndEncoding(int pojoHash, ByteBuffer offheapBinaryKey, ByteBuffer offheapBinaryValue, int metadata) {
        freePendingTables();
        int[] newEntry = installEntry(offheapBinaryKey, pojoHash, offheapBinaryValue, metadata);
        int start = indexFor(spread(pojoHash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isAvailable(entry)) {
                if (isRemoved(entry)) {
                    this.removedSlots--;
                }
                entry.put(newEntry);
                slotAdded(entry);
                hit(entry);
                return readLong(newEntry, 2);
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        this.storageEngine.freeMapping(readLong(newEntry, 2), newEntry[1], false);
        expand(start, limit);
        return installMappingForHashAndEncoding(pojoHash, offheapBinaryKey, offheapBinaryValue, metadata);
    }

    public Integer getMetadata(Object key, int mask) {
        int safeMask = mask & (-4);
        freePendingTables();
        int hash = key.hashCode();
        this.hashtable.position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            long encoding = readLong(entry, 2);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && keyEquals(key, hash, encoding, entry.get(1))) {
                return Integer.valueOf(entry.get(0) & safeMask);
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return null;
    }

    public Integer getAndSetMetadata(Object key, int mask, int values) {
        int safeMask = mask & (-4);
        freePendingTables();
        int hash = key.hashCode();
        this.hashtable.position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            long encoding = readLong(entry, 2);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && keyEquals(key, hash, encoding, entry.get(1))) {
                int previous = entry.get(0);
                entry.put(0, (previous & (safeMask ^ (-1))) | (values & safeMask));
                return Integer.valueOf(previous & safeMask);
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return null;
    }

    public V getValueAndSetMetadata(Object key, int mask, int values) {
        int safeMask = mask & (-4);
        freePendingTables();
        int hash = key.hashCode();
        this.hashtable.position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            long encoding = readLong(entry, 2);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && keyEquals(key, hash, encoding, entry.get(1))) {
                hit(entry);
                entry.put(0, (entry.get(0) & (safeMask ^ (-1))) | (values & safeMask));
                return this.storageEngine.readValue(readLong(entry, 2));
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V put(K key, V value) {
        return put(key, value, 0);
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public V put(K key, V value, int metadata) {
        freePendingTables();
        int hash = key.hashCode();
        int[] newEntry = writeEntry(key, hash, value, metadata);
        int start = indexFor(spread(hash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        int i = 0;
        while (i < limit) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isAvailable(entry)) {
                this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, metadata);
                this.storageEngine.invalidateCache();
                IntBuffer laterEntry = entry;
                while (i < limit && !isTerminating(laterEntry)) {
                    if (isPresent(laterEntry) && keyEquals(key, hash, readLong(laterEntry, 2), laterEntry.get(1))) {
                        V old = this.storageEngine.readValue(readLong(laterEntry, 2));
                        this.storageEngine.freeMapping(readLong(laterEntry, 2), laterEntry.get(1), false);
                        long oldEncoding = readLong(laterEntry, 2);
                        laterEntry.put(newEntry);
                        slotUpdated((IntBuffer) laterEntry.flip(), oldEncoding);
                        hit(laterEntry);
                        return old;
                    }
                    this.hashtable.position(this.hashtable.position() + 4);
                    if (!this.hashtable.hasRemaining()) {
                        this.hashtable.rewind();
                    }
                    laterEntry = (IntBuffer) this.hashtable.slice().limit(4);
                    i++;
                }
                if (isRemoved(entry)) {
                    this.removedSlots--;
                }
                entry.put(newEntry);
                slotAdded(entry);
                hit(entry);
                return null;
            }
            if (keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, metadata);
                this.storageEngine.invalidateCache();
                V old2 = this.storageEngine.readValue(readLong(entry, 2));
                this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), false);
                long oldEncoding2 = readLong(entry, 2);
                entry.put(newEntry);
                slotUpdated((IntBuffer) entry.flip(), oldEncoding2);
                hit(entry);
                return old2;
            }
            this.hashtable.position(this.hashtable.position() + 4);
            i++;
        }
        this.storageEngine.freeMapping(readLong(newEntry, 2), newEntry[1], false);
        expand(start, limit);
        return put(key, value, metadata);
    }

    public V fill(K key, V value) {
        return fill(key, value, 0);
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public V fill(K key, V value, int metadata) {
        freePendingTables();
        int hash = key.hashCode();
        int start = indexFor(spread(hash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        int i = 0;
        while (i < limit) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isAvailable(entry)) {
                IntBuffer laterEntry = entry;
                while (i < limit && !isTerminating(laterEntry)) {
                    if (isPresent(laterEntry) && keyEquals(key, hash, readLong(laterEntry, 2), laterEntry.get(1))) {
                        return put(key, value, metadata);
                    }
                    this.hashtable.position(this.hashtable.position() + 4);
                    if (!this.hashtable.hasRemaining()) {
                        this.hashtable.rewind();
                    }
                    laterEntry = (IntBuffer) this.hashtable.slice().limit(4);
                    i++;
                }
                int[] newEntry = tryWriteEntry(key, hash, value, metadata);
                if (newEntry == null) {
                    return null;
                }
                return fill(key, value, hash, newEntry, metadata);
            }
            if (keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                return put(key, value, metadata);
            }
            this.hashtable.position(this.hashtable.position() + 4);
            i++;
        }
        if (tryExpandTable()) {
            return fill(key, value, metadata);
        }
        return null;
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    protected final V fill(K key, V value, int hash, int[] newEntry, int metadata) {
        freePendingTables();
        int start = indexFor(spread(hash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        int i = 0;
        while (i < limit) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isAvailable(entry)) {
                this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, metadata);
                this.storageEngine.invalidateCache();
                IntBuffer laterEntry = entry;
                while (i < limit && !isTerminating(laterEntry)) {
                    if (isPresent(laterEntry) && keyEquals(key, hash, readLong(laterEntry, 2), laterEntry.get(1))) {
                        V old = this.storageEngine.readValue(readLong(laterEntry, 2));
                        this.storageEngine.freeMapping(readLong(laterEntry, 2), laterEntry.get(1), false);
                        long oldEncoding = readLong(laterEntry, 2);
                        laterEntry.put(newEntry);
                        slotUpdated((IntBuffer) laterEntry.flip(), oldEncoding);
                        hit(laterEntry);
                        return old;
                    }
                    this.hashtable.position(this.hashtable.position() + 4);
                    if (!this.hashtable.hasRemaining()) {
                        this.hashtable.rewind();
                    }
                    laterEntry = (IntBuffer) this.hashtable.slice().limit(4);
                    i++;
                }
                if (isRemoved(entry)) {
                    this.removedSlots--;
                }
                entry.put(newEntry);
                slotAdded(entry);
                hit(entry);
                return null;
            }
            if (keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, metadata);
                this.storageEngine.invalidateCache();
                V old2 = this.storageEngine.readValue(readLong(entry, 2));
                this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), false);
                long oldEncoding2 = readLong(entry, 2);
                entry.put(newEntry);
                slotUpdated((IntBuffer) entry.flip(), oldEncoding2);
                hit(entry);
                return old2;
            }
            this.hashtable.position(this.hashtable.position() + 4);
            i++;
        }
        this.storageEngine.freeMapping(readLong(newEntry, 2), newEntry[1], true);
        return null;
    }

    private int[] writeEntry(K key, int hash, V value, int metadata) {
        while (true) {
            int[] entry = tryWriteEntry(key, hash, value, metadata);
            if (entry == null) {
                storageEngineFailure(key);
            } else {
                return entry;
            }
        }
    }

    @FindbugsSuppressWarnings({"PZLA_PREFER_ZERO_LENGTH_ARRAYS"})
    private int[] tryWriteEntry(K key, int hash, V value, int metadata) {
        if (this.hashtable == null) {
            throw new NullPointerException();
        }
        if (this.hashtable == DESTROYED_TABLE) {
            throw new IllegalStateException("Offheap map/cache has been destroyed");
        }
        if ((metadata & 3) == 0) {
            Long encoding = this.storageEngine.writeMapping(key, value, hash, metadata);
            if (encoding == null) {
                return null;
            }
            return createEntry(hash, encoding.longValue(), metadata);
        }
        throw new IllegalArgumentException("Invalid metadata for key '" + key + "' : " + Integer.toBinaryString(metadata));
    }

    private int[] installEntry(ByteBuffer offheapBinaryKey, int pojoHash, ByteBuffer offheapBinaryValue, int metadata) {
        while (true) {
            int[] entry = tryInstallEntry(offheapBinaryKey, pojoHash, offheapBinaryValue, metadata);
            if (entry == null) {
                storageEngineFailure("<binary-key>");
            } else {
                return entry;
            }
        }
    }

    @FindbugsSuppressWarnings({"PZLA_PREFER_ZERO_LENGTH_ARRAYS"})
    private int[] tryInstallEntry(ByteBuffer offheapBinaryKey, int pojoHash, ByteBuffer offheapBinaryValue, int metadata) {
        if (this.hashtable == null) {
            throw new NullPointerException();
        }
        if (this.hashtable == DESTROYED_TABLE) {
            throw new IllegalStateException("Offheap map/cache has been destroyed");
        }
        if ((metadata & 3) == 0) {
            Long encoding = ((BinaryStorageEngine) this.storageEngine).writeBinaryMapping(offheapBinaryKey, offheapBinaryValue, pojoHash, metadata);
            if (encoding == null) {
                return null;
            }
            return createEntry(pojoHash, encoding.longValue(), metadata);
        }
        throw new IllegalArgumentException("Invalid metadata for binary key : " + Integer.toBinaryString(metadata));
    }

    private static int[] createEntry(int hash, long encoding, int metadata) {
        return new int[]{1 | metadata, hash, (int) (encoding >>> 32), (int) encoding};
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object key) {
        freePendingTables();
        int hash = key.hashCode();
        if (this.size == 0) {
            return null;
        }
        this.hashtable.position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                V removedValue = this.storageEngine.readValue(readLong(entry, 2));
                this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), true);
                entry.put(0, 2);
                slotRemoved(entry);
                shrink();
                return removedValue;
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return null;
    }

    public Map<K, V> removeAllWithHash(int hash) {
        freePendingTables();
        if (this.size == 0) {
            return Collections.emptyMap();
        }
        Map<K, V> removed = new HashMap<>();
        this.hashtable.position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isTerminating(entry)) {
                return removed;
            }
            if (isPresent(entry) && hash == entry.get(1)) {
                V removedValue = this.storageEngine.readValue(readLong(entry, 2));
                K removedKey = this.storageEngine.readKey(readLong(entry, 2), hash);
                this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), true);
                removed.put(removedKey, removedValue);
                entry.put(0, 2);
                slotRemoved(entry);
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        shrink();
        return removed;
    }

    public boolean removeNoReturn(Object key) {
        freePendingTables();
        int hash = key.hashCode();
        this.hashtable.position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isTerminating(entry)) {
                return false;
            }
            if (isPresent(entry) && keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), true);
                entry.put(2);
                slotRemoved(entry);
                shrink();
                return true;
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return false;
    }

    @Override // java.util.AbstractMap, java.util.Map
    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public void clear() {
        if (this.hashtable != DESTROYED_TABLE) {
            freePendingTables();
            this.modCount++;
            this.removedSlots = 0;
            this.size = 0;
            this.storageEngine.clear();
            allocateOrClearTable(this.initialTableSize);
        }
    }

    public void destroy() {
        this.removedSlots = 0;
        this.size = 0;
        freeTable(this.hashTablePage);
        Iterator<PendingPage> it = this.pendingTableFrees.values();
        while (it.hasNext()) {
            freeTable(it.next().tablePage);
        }
        this.hashTablePage = null;
        this.hashtable = DESTROYED_TABLE;
        this.storageEngine.destroy();
    }

    private void allocateOrClearTable(int size) {
        Page newTablePage;
        int[] zeros = new int[256];
        this.hashtable.clear();
        while (this.hashtable.hasRemaining()) {
            if (this.hashtable.remaining() < zeros.length) {
                this.hashtable.put(zeros, 0, this.hashtable.remaining());
            } else {
                this.hashtable.put(zeros);
            }
        }
        this.hashtable.clear();
        wipePendingTables();
        if (this.hashtable.capacity() > size * 4 * 2 && (newTablePage = allocateTable(size)) != null) {
            freeTable(this.hashTablePage, this.hashtable, reprobeLimit());
            this.hashTablePage = newTablePage;
            this.hashtable = newTablePage.asIntBuffer();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> es = this.entrySet;
        if (es != null) {
            return es;
        }
        EntrySet entrySet = new EntrySet();
        this.entrySet = entrySet;
        return entrySet;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine.Owner
    public Set<Long> encodingSet() {
        Set<Long> es = this.encodingSet;
        if (es != null) {
            return es;
        }
        EncodingSet encodingSet = new EncodingSet();
        this.encodingSet = encodingSet;
        return encodingSet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<K> keySet() {
        Set<K> ks = this.keySet;
        if (ks != null) {
            return ks;
        }
        KeySet keySet = new KeySet();
        this.keySet = keySet;
        return keySet;
    }

    protected static boolean isPresent(IntBuffer entry) {
        return (entry.get(0) & 1) != 0;
    }

    protected static boolean isAvailable(IntBuffer entry) {
        return (entry.get(0) & 1) == 0;
    }

    protected static boolean isTerminating(IntBuffer entry) {
        return isTerminating(entry.get(0));
    }

    protected static boolean isTerminating(int entryStatus) {
        return (entryStatus & 3) == 0;
    }

    protected static boolean isRemoved(IntBuffer entry) {
        return isRemoved(entry.get(0));
    }

    protected static boolean isRemoved(int entryStatus) {
        return (entryStatus & 2) != 0;
    }

    protected static long readLong(int[] array, int offset) {
        return (array[offset] << 32) | (4294967295L & array[offset + 1]);
    }

    protected static long readLong(IntBuffer entry, int offset) {
        return (entry.get(offset) << 32) | (4294967295L & entry.get(offset + 1));
    }

    protected int indexFor(int hash) {
        return indexFor(hash, this.hashtable);
    }

    protected static int indexFor(int hash, IntBuffer table) {
        return (hash << ENTRY_BIT_SHIFT) & Math.max(0, table.capacity() - 1);
    }

    private boolean keyEquals(Object probeKey, int probeHash, long targetEncoding, int targetHash) {
        return probeHash == targetHash && this.storageEngine.equalsKey(probeKey, targetEncoding);
    }

    private boolean binaryKeyEquals(ByteBuffer binaryProbeKey, int probeHash, long targetEncoding, int targetHash) {
        if (this.storageEngine instanceof BinaryStorageEngine) {
            return probeHash == targetHash && ((BinaryStorageEngine) this.storageEngine).equalsBinaryKey(binaryProbeKey, targetEncoding);
        }
        throw new UnsupportedOperationException("Cannot check binary quality unless configured with a BinaryStorageEngine");
    }

    private void expand(int start, int length) {
        if (!tryExpand()) {
            tableExpansionFailure(start, length);
        }
    }

    private boolean tryExpand() {
        if (this.size / getTableCapacity() > 0.5f) {
            return tryExpandTable();
        }
        return tryIncreaseReprobe();
    }

    private boolean tryExpandTable() {
        if (this.tableResizing.get().booleanValue()) {
            throw new AssertionError("Expand requested in context of an existing resize - this should be impossible");
        }
        this.tableResizing.set(Boolean.TRUE);
        try {
            Page newTablePage = expandTable(1);
            if (newTablePage == null) {
                return false;
            }
            freeTable(this.hashTablePage, this.hashtable, reprobeLimit());
            this.hashTablePage = newTablePage;
            this.hashtable = newTablePage.asIntBuffer();
            this.removedSlots = 0;
            this.tableResizing.remove();
            return true;
        } finally {
            this.tableResizing.remove();
        }
    }

    private Page expandTable(int scale) {
        if (this.hashtable == DESTROYED_TABLE) {
            throw new IllegalStateException("This map/cache has been destroyed");
        }
        int newsize = this.hashtable.capacity() << scale;
        if (newsize <= 0) {
            return null;
        }
        long startTime = -1;
        if (LOGGER.isDebugEnabled()) {
            startTime = System.nanoTime();
            int slots = this.hashtable.capacity() / 4;
            int newslots = newsize / 4;
            LOGGER.debug("Expanding table from {} slots to {} slots [load-factor={}]", DebuggingUtils.toBase2SuffixedString(slots), DebuggingUtils.toBase2SuffixedString(newslots), Float.valueOf(this.size / slots));
        }
        Page newTablePage = allocateTable(newsize / 4);
        if (newTablePage == null) {
            return null;
        }
        IntBuffer newTable = newTablePage.asIntBuffer();
        this.hashtable.clear();
        while (this.hashtable.hasRemaining()) {
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (!isPresent(entry) || writeEntry(newTable, entry)) {
                this.hashtable.position(this.hashtable.position() + 4);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Table expansion from {} slots to {} slots abandoned - not enough table space", DebuggingUtils.toBase2SuffixedString(this.hashtable.capacity() / 4), DebuggingUtils.toBase2SuffixedString(newsize / 4));
                }
                freeTable(newTablePage);
                return expandTable(scale + 1);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            long time = System.nanoTime() - startTime;
            LOGGER.debug("Table expansion from {} slots to {} slots complete : took {}ms", DebuggingUtils.toBase2SuffixedString(this.hashtable.capacity() / 4), DebuggingUtils.toBase2SuffixedString(newsize / 4), Float.valueOf(time / 1000000.0f));
        }
        return newTablePage;
    }

    protected boolean tryIncreaseReprobe() {
        if (reprobeLimit() >= getTableCapacity()) {
            return false;
        }
        int newReprobeLimit = reprobeLimit() << 1;
        if (newReprobeLimit >= 1024) {
            long slots = getTableCapacity();
            LOGGER.warn("Expanding reprobe sequence from {} slots to {} slots [load-factor={}]", Integer.valueOf(reprobeLimit()), Integer.valueOf(newReprobeLimit), Float.valueOf(this.size / slots));
        } else if (LOGGER.isDebugEnabled()) {
            long slots2 = getTableCapacity();
            LOGGER.debug("Expanding reprobe sequence from {} slots to {} slots [load-factor={}]", Integer.valueOf(reprobeLimit()), Integer.valueOf(newReprobeLimit), Float.valueOf(this.size / slots2));
        }
        this.reprobeLimit = newReprobeLimit;
        return true;
    }

    protected void shrinkTable() {
        shrink();
    }

    private void shrink() {
        if (this.size / getTableCapacity() <= this.currentTableShrinkThreshold) {
            shrinkTableImpl();
        }
    }

    private void shrinkTableImpl() {
        if (this.tableResizing.get().booleanValue()) {
            LOGGER.debug("Shrink request ignored in the context of an in-process expand - likely self stealing");
            return;
        }
        this.tableResizing.set(Boolean.TRUE);
        try {
            float shrinkRatio = (0.5f * getTableCapacity()) / this.size;
            int shrinkShift = Integer.numberOfTrailingZeros(Integer.highestOneBit(Math.max(2, (int) shrinkRatio)));
            Page newTablePage = shrinkTableImpl(shrinkShift);
            if (newTablePage == null) {
                this.currentTableShrinkThreshold /= 2.0f;
            } else {
                this.currentTableShrinkThreshold = TABLE_SHRINK_THRESHOLD;
                freeTable(this.hashTablePage, this.hashtable, reprobeLimit());
                this.hashTablePage = newTablePage;
                this.hashtable = newTablePage.asIntBuffer();
                this.removedSlots = 0;
            }
        } finally {
            this.tableResizing.remove();
        }
    }

    private Page shrinkTableImpl(int scale) {
        int newsize = this.hashtable.capacity() >>> scale;
        if (newsize < 4) {
            if (scale > 1) {
                return shrinkTableImpl(scale - 1);
            }
            return null;
        }
        long startTime = -1;
        if (LOGGER.isDebugEnabled()) {
            startTime = System.nanoTime();
            int slots = this.hashtable.capacity() / 4;
            int newslots = newsize / 4;
            LOGGER.debug("Shrinking table from {} slots to {} slots [load-factor={}]", DebuggingUtils.toBase2SuffixedString(slots), DebuggingUtils.toBase2SuffixedString(newslots), Float.valueOf(this.size / slots));
        }
        Page newTablePage = allocateTable(newsize / 4);
        if (newTablePage == null) {
            return null;
        }
        IntBuffer newTable = newTablePage.asIntBuffer();
        this.hashtable.clear();
        while (this.hashtable.hasRemaining()) {
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (!isPresent(entry) || writeEntry(newTable, entry)) {
                this.hashtable.position(this.hashtable.position() + 4);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Table shrinking from {} slots to {} slots abandoned - too little table space", DebuggingUtils.toBase2SuffixedString(this.hashtable.capacity() / 4), DebuggingUtils.toBase2SuffixedString(newsize / 4));
                }
                freeTable(newTablePage);
                if (scale > 1) {
                    return shrinkTableImpl(scale - 1);
                }
                this.hashtable.clear();
                return null;
            }
        }
        if (LOGGER.isDebugEnabled()) {
            long time = System.nanoTime() - startTime;
            LOGGER.debug("Table shrinking from {} slots to {} slots complete : took {}ms", DebuggingUtils.toBase2SuffixedString(this.hashtable.capacity() / 4), DebuggingUtils.toBase2SuffixedString(newsize / 4), Float.valueOf(time / 1000000.0f));
        }
        return newTablePage;
    }

    private boolean writeEntry(IntBuffer table, IntBuffer entry) {
        int start = indexFor(spread(entry.get(1)), table);
        int tableMask = table.capacity() - 1;
        for (int i = 0; i < reprobeLimit() * 4; i += 4) {
            int address = (start + i) & tableMask;
            int existingStatus = table.get(address + 0);
            if (isTerminating(existingStatus)) {
                table.position(address);
                table.put(entry);
                return true;
            }
            if (isRemoved(existingStatus)) {
                throw new AssertionError();
            }
        }
        return false;
    }

    protected static int spread(int hash) {
        int h = hash + ((hash << 15) ^ (-12931));
        int h2 = h ^ (h >>> 10);
        int h3 = h2 + (h2 << 3);
        int h4 = h3 ^ (h3 >>> 6);
        int h5 = h4 + (h4 << 2) + (h4 << 14);
        return h5 ^ (h5 >>> 16);
    }

    private Page allocateTable(int size) {
        Page newTablePage = this.tableSource.allocate(size * 4 * 4, this.tableAllocationsSteal, false, null);
        if (newTablePage != null) {
            ByteBuffer buffer = newTablePage.asByteBuffer();
            byte[] zeros = new byte[1024];
            buffer.clear();
            while (buffer.hasRemaining()) {
                if (buffer.remaining() < zeros.length) {
                    buffer.put(zeros, 0, buffer.remaining());
                } else {
                    buffer.put(zeros);
                }
            }
            buffer.clear();
        }
        return newTablePage;
    }

    private void freeTable(Page tablePage, IntBuffer table, int finalReprobe) {
        if (this.hasUsedIterators) {
            this.pendingTableFrees.put(table, new PendingPage(tablePage, finalReprobe));
        } else {
            freeTable(tablePage);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void freeTable(Page tablePage) {
        this.tableSource.free(tablePage);
    }

    private int reprobeLimit() {
        return this.reprobeLimit;
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$EntrySet.class */
    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry) o;
            Object obj = OffHeapHashMap.this.get(e.getKey());
            return obj != null && obj.equals(e.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return OffHeapHashMap.this.removeMapping(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return OffHeapHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            OffHeapHashMap.this.clear();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$EncodingSet.class */
    class EncodingSet extends AbstractSet<Long> {
        EncodingSet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Long> iterator() {
            return new EncodingIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return OffHeapHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$KeySet.class */
    class KeySet extends AbstractSet<K> {
        KeySet() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object o) {
            return OffHeapHashMap.this.containsKey(o);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object o) {
            return OffHeapHashMap.this.remove(o) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return OffHeapHashMap.this.size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            OffHeapHashMap.this.clear();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$HashIterator.class */
    abstract class HashIterator<T> implements Iterator<T> {
        final int expectedModCount;
        final IntBuffer table;
        final IntBuffer tableView;
        T next;

        protected abstract T create(IntBuffer intBuffer);

        HashIterator() {
            this.next = null;
            OffHeapHashMap.this.hasUsedIterators = true;
            this.table = OffHeapHashMap.this.hashtable;
            this.tableView = (IntBuffer) this.table.asReadOnlyBuffer().clear();
            this.expectedModCount = OffHeapHashMap.this.modCount;
            if (OffHeapHashMap.this.size > 0) {
                while (this.tableView.hasRemaining()) {
                    IntBuffer entry = (IntBuffer) this.tableView.slice().limit(4);
                    this.tableView.position(this.tableView.position() + 4);
                    if (OffHeapHashMap.isPresent(entry)) {
                        this.next = create(entry);
                        return;
                    }
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.next != null;
        }

        @Override // java.util.Iterator
        public T next() {
            checkForConcurrentModification();
            T e = this.next;
            if (e == null) {
                throw new NoSuchElementException();
            }
            this.next = null;
            while (true) {
                if (!this.tableView.hasRemaining()) {
                    break;
                }
                IntBuffer entry = (IntBuffer) this.tableView.slice().limit(4);
                this.tableView.position(this.tableView.position() + 4);
                if (OffHeapHashMap.isPresent(entry)) {
                    this.next = create(entry);
                    break;
                }
            }
            return e;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        protected void checkForConcurrentModification() {
            if (OffHeapHashMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$PendingPage.class */
    static class PendingPage {
        final Page tablePage;
        final int reprobe;

        PendingPage(Page tablePage, int reprobe) {
            this.tablePage = tablePage;
            this.reprobe = reprobe;
        }
    }

    protected void freePendingTables() {
        if (this.hasUsedIterators) {
            this.pendingTableFrees.reap();
        }
    }

    private void updatePendingTables(int hash, long oldEncoding, IntBuffer newEntry) {
        if (this.hasUsedIterators) {
            this.pendingTableFrees.reap();
            Iterator<PendingPage> it = this.pendingTableFrees.values();
            while (it.hasNext()) {
                PendingPage pending = it.next();
                IntBuffer pendingTable = pending.tablePage.asIntBuffer();
                pendingTable.position(indexFor(spread(hash), pendingTable));
                int i = 0;
                while (true) {
                    if (i >= pending.reprobe) {
                        break;
                    }
                    if (!pendingTable.hasRemaining()) {
                        pendingTable.rewind();
                    }
                    IntBuffer entry = (IntBuffer) pendingTable.slice().limit(4);
                    if (!isTerminating(entry)) {
                        if (isPresent(entry) && hash == entry.get(1) && oldEncoding == readLong(entry, 2)) {
                            entry.put(newEntry.duplicate());
                            break;
                        } else {
                            pendingTable.position(pendingTable.position() + 4);
                            i++;
                        }
                    }
                }
            }
        }
    }

    private void wipePendingTables() {
        if (this.hasUsedIterators) {
            this.pendingTableFrees.reap();
            int[] zeros = new int[256];
            Iterator<PendingPage> it = this.pendingTableFrees.values();
            while (it.hasNext()) {
                PendingPage pending = it.next();
                IntBuffer pendingTable = pending.tablePage.asIntBuffer();
                pendingTable.clear();
                while (pendingTable.hasRemaining()) {
                    if (pendingTable.remaining() < zeros.length) {
                        pendingTable.put(zeros, 0, pendingTable.remaining());
                    } else {
                        pendingTable.put(zeros);
                    }
                }
                pendingTable.clear();
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$KeyIterator.class */
    class KeyIterator extends OffHeapHashMap<K, V>.HashIterator<K> {
        KeyIterator() {
            super();
        }

        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator
        protected K create(IntBuffer entry) {
            return OffHeapHashMap.this.storageEngine.readKey(OffHeapHashMap.readLong(entry, 2), entry.get(1));
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$EntryIterator.class */
    class EntryIterator extends OffHeapHashMap<K, V>.HashIterator<Map.Entry<K, V>> {
        EntryIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator
        public Map.Entry<K, V> create(IntBuffer entry) {
            return new DirectEntry(entry);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$EncodingIterator.class */
    class EncodingIterator extends OffHeapHashMap<K, V>.HashIterator<Long> {
        EncodingIterator() {
            super();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.terracotta.offheapstore.OffHeapHashMap.HashIterator
        public Long create(IntBuffer entry) {
            return Long.valueOf(OffHeapHashMap.readLong(entry, 2));
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/OffHeapHashMap$DirectEntry.class */
    class DirectEntry implements Map.Entry<K, V> {
        private final K key;
        private final V value;

        DirectEntry(IntBuffer entry) {
            this.key = OffHeapHashMap.this.storageEngine.readKey(OffHeapHashMap.readLong(entry, 2), entry.get(1));
            this.value = OffHeapHashMap.this.storageEngine.readValue(OffHeapHashMap.readLong(entry, 2));
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
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o) {
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry) o;
                return this.key.equals(e.getKey()) && this.value.equals(e.getValue());
            }
            return false;
        }

        public String toString() {
            return this.key + SymbolConstants.EQUAL_SYMBOL + this.value;
        }
    }

    protected boolean removeMapping(Object o) {
        freePendingTables();
        if (!(o instanceof Map.Entry)) {
            return false;
        }
        Map.Entry<K, V> e = (Map.Entry) o;
        Object key = e.getKey();
        int hash = key.hashCode();
        this.hashtable.position(indexFor(spread(hash)));
        for (int i = 0; i < reprobeLimit(); i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isTerminating(entry)) {
                return false;
            }
            if (isPresent(entry) && keyEquals(key, hash, readLong(entry, 2), entry.get(1)) && this.storageEngine.equalsValue(e.getValue(), readLong(entry, 2))) {
                this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), true);
                entry.put(2);
                slotRemoved(entry);
                shrink();
                return true;
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return false;
    }

    public boolean evict(int index, boolean shrink) {
        return false;
    }

    protected void removeAtTableOffset(int offset, boolean shrink) {
        IntBuffer entry = ((IntBuffer) this.hashtable.duplicate().position(offset).limit(offset + 4)).slice();
        if (isPresent(entry)) {
            this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), true);
            entry.put(0, 2);
            slotRemoved(entry);
            if (shrink) {
                shrink();
                return;
            }
            return;
        }
        throw new AssertionError();
    }

    protected V getAtTableOffset(int offset) {
        IntBuffer entry = ((IntBuffer) this.hashtable.duplicate().position(offset).limit(offset + 4)).slice();
        if (isPresent(entry)) {
            return this.storageEngine.readValue(readLong(entry, 2));
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map.Entry<K, V> getEntryAtTableOffset(int offset) {
        IntBuffer entry = ((IntBuffer) this.hashtable.duplicate().position(offset).limit(offset + 4)).slice();
        if (isPresent(entry)) {
            return new DirectEntry(entry);
        }
        throw new AssertionError();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine.Owner
    public Integer getSlotForHashAndEncoding(int hash, long encoding, long mask) {
        IntBuffer view = (IntBuffer) this.hashtable.duplicate().position(indexFor(spread(hash)));
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!view.hasRemaining()) {
                view.rewind();
            }
            IntBuffer entry = (IntBuffer) view.slice().limit(4);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && hash == entry.get(1) && (encoding & mask) == (readLong(entry, 2) & mask)) {
                return Integer.valueOf(view.position());
            }
            view.position(view.position() + 4);
        }
        return null;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine.Owner
    public boolean updateEncoding(int hash, long oldEncoding, long newEncoding, long mask) {
        boolean updated = updateEncodingInTable(this.hashtable, reprobeLimit(), hash, oldEncoding, newEncoding, mask);
        if (this.hasUsedIterators) {
            this.pendingTableFrees.reap();
            Iterator<PendingPage> it = this.pendingTableFrees.values();
            while (it.hasNext()) {
                PendingPage pending = it.next();
                updated |= updateEncodingInTable(pending.tablePage.asIntBuffer(), pending.reprobe, hash, oldEncoding, newEncoding, mask);
            }
        }
        return updated;
    }

    private static boolean updateEncodingInTable(IntBuffer table, int limit, int hash, long oldEncoding, long newEncoding, long mask) {
        table.position(indexFor(spread(hash), table));
        for (int i = 0; i < limit; i++) {
            if (!table.hasRemaining()) {
                table.rewind();
            }
            IntBuffer entry = (IntBuffer) table.slice().limit(4);
            if (isTerminating(entry)) {
                return false;
            }
            if (isPresent(entry) && hash == entry.get(1) && (oldEncoding & mask) == (readLong(entry, 2) & mask)) {
                entry.put(createEntry(hash, (readLong(entry, 2) & (mask ^ (-1))) | (newEncoding & mask), entry.get(0)));
                return true;
            }
            table.position(table.position() + 4);
        }
        return false;
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    private void slotRemoved(IntBuffer entry) {
        this.modCount++;
        this.removedSlots++;
        this.size--;
        updatePendingTables(entry.get(1), readLong(entry, 2), entry);
        removed(entry);
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    private void slotAdded(IntBuffer entry) {
        this.modCount++;
        this.size++;
        added(entry);
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    private void slotUpdated(IntBuffer entry, long oldEncoding) {
        this.modCount++;
        updatePendingTables(entry.get(1), oldEncoding, entry);
        updated(entry);
    }

    protected void added(IntBuffer entry) {
    }

    protected void hit(IntBuffer entry) {
    }

    protected void removed(IntBuffer entry) {
    }

    protected void updated(IntBuffer entry) {
    }

    protected void tableExpansionFailure(int start, int length) {
        StringBuilder sb = new StringBuilder("Failed to expand table.\n");
        sb.append("Current Table Size (slots) : ").append(getTableCapacity()).append('\n');
        sb.append("Resize Will Require        : ").append(DebuggingUtils.toBase2SuffixedString(getTableCapacity() * 4 * 4 * 2)).append("B\n");
        sb.append("Table Buffer Source        : ").append(this.tableSource);
        throw new OversizeMappingException(sb.toString());
    }

    protected void storageEngineFailure(Object failure) {
        StringBuilder sb = new StringBuilder("Storage engine failed to store: ");
        sb.append(failure).append('\n');
        sb.append("StorageEngine: ").append(this.storageEngine);
        throw new OversizeMappingException(sb.toString());
    }

    @Override // org.terracotta.offheapstore.MapInternals, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public long getSize() {
        return this.size;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getTableCapacity() {
        IntBuffer table = this.hashtable;
        if (table == null) {
            return 0L;
        }
        return table.capacity() / 4;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getUsedSlotCount() {
        return getSize();
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getRemovedSlotCount() {
        return this.removedSlots;
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public int getReprobeLength() {
        return reprobeLimit();
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getAllocatedMemory() {
        return getDataAllocatedMemory() + (getTableCapacity() * 4 * 4);
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getOccupiedMemory() {
        return getDataOccupiedMemory() + (getUsedSlotCount() * 4 * 4);
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getVitalMemory() {
        return getDataVitalMemory() + (getTableCapacity() * 4 * 4);
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataAllocatedMemory() {
        return this.storageEngine.getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataOccupiedMemory() {
        return this.storageEngine.getOccupiedMemory();
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataVitalMemory() {
        return this.storageEngine.getVitalMemory();
    }

    @Override // org.terracotta.offheapstore.MapInternals
    public long getDataSize() {
        return this.storageEngine.getDataSize();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine.Owner
    public boolean isThiefForTableAllocations() {
        return this.tableAllocationsSteal;
    }

    public Lock readLock() {
        return NoOpLock.INSTANCE;
    }

    public Lock writeLock() {
        return NoOpLock.INSTANCE;
    }

    public StorageEngine<? super K, ? super V> getStorageEngine() {
        return this.storageEngine;
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public MetadataTuple<V> computeWithMetadata(K key, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> remappingFunction) {
        freePendingTables();
        int hash = key.hashCode();
        IntBuffer originalTable = this.hashtable;
        int start = indexFor(spread(hash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        int i = 0;
        while (i < limit) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isAvailable(entry)) {
                IntBuffer laterEntry = entry;
                while (i < limit && !isTerminating(laterEntry)) {
                    if (isPresent(laterEntry) && keyEquals(key, hash, readLong(laterEntry, 2), laterEntry.get(1))) {
                        long encoding = readLong(laterEntry, 2);
                        MetadataTuple<V> existingValue = MetadataTuple.metadataTuple(this.storageEngine.readValue(encoding), laterEntry.get(0) & (-4));
                        MetadataTuple<V> result = remappingFunction.apply(key, existingValue);
                        if (result == null) {
                            this.storageEngine.freeMapping(readLong(laterEntry, 2), laterEntry.get(1), true);
                            laterEntry.put(2);
                            slotRemoved(laterEntry);
                            shrink();
                        } else if (result != existingValue) {
                            if (result.value() == existingValue.value()) {
                                int previous = laterEntry.get(0);
                                laterEntry.put(0, (previous & 3) | (result.metadata() & (-4)));
                            } else {
                                int[] newEntry = writeEntry(key, hash, result.value(), result.metadata());
                                if (this.hashtable != originalTable || !isPresent(laterEntry)) {
                                    this.storageEngine.freeMapping(readLong(newEntry, 2), newEntry[1], false);
                                    return computeWithMetadata(key, remappingFunction);
                                }
                                this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, result.metadata());
                                this.storageEngine.invalidateCache();
                                this.storageEngine.freeMapping(readLong(laterEntry, 2), laterEntry.get(1), false);
                                long oldEncoding = readLong(laterEntry, 2);
                                laterEntry.put(newEntry);
                                slotUpdated((IntBuffer) laterEntry.flip(), oldEncoding);
                                hit(laterEntry);
                            }
                        }
                        return result;
                    }
                    this.hashtable.position(this.hashtable.position() + 4);
                    if (!this.hashtable.hasRemaining()) {
                        this.hashtable.rewind();
                    }
                    laterEntry = (IntBuffer) this.hashtable.slice().limit(4);
                    i++;
                }
                MetadataTuple<V> result2 = remappingFunction.apply(key, null);
                if (result2 != null) {
                    int[] newEntry2 = writeEntry(key, hash, result2.value(), result2.metadata());
                    if (this.hashtable != originalTable) {
                        this.storageEngine.freeMapping(readLong(newEntry2, 2), newEntry2[1], false);
                        return computeWithMetadata(key, remappingFunction);
                    }
                    if (!isAvailable(entry)) {
                        throw new AssertionError();
                    }
                    if (isRemoved(entry)) {
                        this.removedSlots--;
                    }
                    this.storageEngine.attachedMapping(readLong(newEntry2, 2), hash, result2.metadata());
                    this.storageEngine.invalidateCache();
                    entry.put(newEntry2);
                    slotAdded(entry);
                    hit(entry);
                }
                return result2;
            }
            if (keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                long existingEncoding = readLong(entry, 2);
                int existingStatus = entry.get(0);
                MetadataTuple<V> existingTuple = MetadataTuple.metadataTuple(this.storageEngine.readValue(existingEncoding), existingStatus & (-4));
                MetadataTuple<V> result3 = remappingFunction.apply(key, existingTuple);
                if (result3 == null) {
                    this.storageEngine.freeMapping(existingEncoding, hash, true);
                    entry.put(2);
                    slotRemoved(entry);
                    shrink();
                } else if (result3 != existingTuple) {
                    if (result3.value() == existingTuple.value()) {
                        entry.put(0, (existingStatus & 3) | (result3.metadata() & (-4)));
                    } else {
                        int[] newEntry3 = writeEntry(key, hash, result3.value(), result3.metadata());
                        if (this.hashtable != originalTable || !isPresent(entry)) {
                            this.storageEngine.freeMapping(readLong(newEntry3, 2), newEntry3[1], false);
                            return computeWithMetadata(key, remappingFunction);
                        }
                        this.storageEngine.attachedMapping(readLong(newEntry3, 2), hash, result3.metadata());
                        this.storageEngine.invalidateCache();
                        this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), false);
                        entry.put(newEntry3);
                        slotUpdated((IntBuffer) entry.flip(), existingEncoding);
                        hit(entry);
                    }
                }
                return result3;
            }
            this.hashtable.position(this.hashtable.position() + 4);
            i++;
        }
        expand(start, limit);
        return computeWithMetadata(key, remappingFunction);
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public MetadataTuple<V> computeIfAbsentWithMetadata(K key, Function<? super K, ? extends MetadataTuple<V>> mappingFunction) {
        freePendingTables();
        int hash = key.hashCode();
        IntBuffer originalTable = this.hashtable;
        int start = indexFor(spread(hash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        int i = 0;
        while (i < limit) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isAvailable(entry)) {
                IntBuffer laterEntry = entry;
                while (i < limit && !isTerminating(laterEntry)) {
                    if (isPresent(laterEntry) && keyEquals(key, hash, readLong(laterEntry, 2), laterEntry.get(1))) {
                        return MetadataTuple.metadataTuple(this.storageEngine.readValue(readLong(laterEntry, 2)), laterEntry.get(0) & (-4));
                    }
                    this.hashtable.position(this.hashtable.position() + 4);
                    if (!this.hashtable.hasRemaining()) {
                        this.hashtable.rewind();
                    }
                    laterEntry = (IntBuffer) this.hashtable.slice().limit(4);
                    i++;
                }
                MetadataTuple<V> result = mappingFunction.apply(key);
                if (result != null) {
                    int[] newEntry = writeEntry(key, hash, result.value(), result.metadata());
                    if (this.hashtable != originalTable) {
                        this.storageEngine.freeMapping(readLong(newEntry, 2), newEntry[1], false);
                        return computeIfAbsentWithMetadata(key, mappingFunction);
                    }
                    if (!isAvailable(entry)) {
                        throw new AssertionError();
                    }
                    if (isRemoved(entry)) {
                        this.removedSlots--;
                    }
                    this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, result.metadata());
                    this.storageEngine.invalidateCache();
                    entry.put(newEntry);
                    slotAdded(entry);
                    hit(entry);
                }
                return result;
            }
            if (keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                return MetadataTuple.metadataTuple(this.storageEngine.readValue(readLong(entry, 2)), entry.get(0) & (-4));
            }
            this.hashtable.position(this.hashtable.position() + 4);
            i++;
        }
        expand(start, limit);
        return computeIfAbsentWithMetadata(key, mappingFunction);
    }

    @FindbugsSuppressWarnings({"VO_VOLATILE_INCREMENT"})
    public MetadataTuple<V> computeIfPresentWithMetadata(K key, BiFunction<? super K, ? super MetadataTuple<V>, ? extends MetadataTuple<V>> remappingFunction) {
        freePendingTables();
        int hash = key.hashCode();
        IntBuffer originalTable = this.hashtable;
        int start = indexFor(spread(hash));
        this.hashtable.position(start);
        int limit = reprobeLimit();
        for (int i = 0; i < limit; i++) {
            if (!this.hashtable.hasRemaining()) {
                this.hashtable.rewind();
            }
            IntBuffer entry = (IntBuffer) this.hashtable.slice().limit(4);
            if (isTerminating(entry)) {
                return null;
            }
            if (isPresent(entry) && keyEquals(key, hash, readLong(entry, 2), entry.get(1))) {
                long existingEncoding = readLong(entry, 2);
                int existingStatus = entry.get(0);
                MetadataTuple<V> existingValue = MetadataTuple.metadataTuple(this.storageEngine.readValue(existingEncoding), existingStatus & (-4));
                MetadataTuple<V> result = remappingFunction.apply(key, existingValue);
                if (result == null) {
                    this.storageEngine.freeMapping(existingEncoding, hash, true);
                    entry.put(2);
                    slotRemoved(entry);
                    shrink();
                } else if (result != existingValue) {
                    if (result.value() == existingValue.value()) {
                        entry.put(0, (existingStatus & 3) | (result.metadata() & (-4)));
                    } else {
                        int[] newEntry = writeEntry(key, hash, result.value(), result.metadata());
                        if (this.hashtable != originalTable || !isPresent(entry)) {
                            this.storageEngine.freeMapping(readLong(newEntry, 2), newEntry[1], false);
                            return computeIfPresentWithMetadata(key, remappingFunction);
                        }
                        this.storageEngine.attachedMapping(readLong(newEntry, 2), hash, result.metadata());
                        this.storageEngine.invalidateCache();
                        this.storageEngine.freeMapping(readLong(entry, 2), entry.get(1), false);
                        entry.put(newEntry);
                        slotUpdated((IntBuffer) entry.flip(), readLong(entry, 2));
                        hit(entry);
                    }
                }
                return result;
            }
            this.hashtable.position(this.hashtable.position() + 4);
        }
        return null;
    }
}
