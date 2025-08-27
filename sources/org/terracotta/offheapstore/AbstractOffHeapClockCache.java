package org.terracotta.offheapstore;

import java.nio.IntBuffer;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terracotta.offheapstore.exceptions.OversizeMappingException;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.pinning.PinnableCache;
import org.terracotta.offheapstore.pinning.PinnableSegment;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.DebuggingUtils;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/AbstractOffHeapClockCache.class */
public abstract class AbstractOffHeapClockCache<K, V> extends AbstractLockedOffHeapHashMap<K, V> implements PinnableCache<K, V>, PinnableSegment<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) AbstractOffHeapClockCache.class);
    private static final int PRESENT_CLOCK = Integer.MIN_VALUE;
    private final Random rndm;
    private int clockHand;

    public AbstractOffHeapClockCache(PageSource source, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, storageEngine);
        this.rndm = new Random();
    }

    public AbstractOffHeapClockCache(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine) {
        super(source, tableAllocationsSteal, storageEngine);
        this.rndm = new Random();
    }

    public AbstractOffHeapClockCache(PageSource source, StorageEngine<? super K, ? super V> storageEngine, boolean bootstrap) {
        super(source, storageEngine, bootstrap);
        this.rndm = new Random();
    }

    public AbstractOffHeapClockCache(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, storageEngine, tableSize);
        this.rndm = new Random();
    }

    public AbstractOffHeapClockCache(PageSource source, boolean tableAllocationsSteal, StorageEngine<? super K, ? super V> storageEngine, int tableSize) {
        super(source, tableAllocationsSteal, storageEngine, tableSize);
        this.rndm = new Random();
    }

    public AbstractOffHeapClockCache(PageSource source, StorageEngine<? super K, ? super V> storageEngine, int tableSize, boolean bootstrap) {
        super(source, storageEngine, tableSize, bootstrap);
        this.rndm = new Random();
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap
    protected void storageEngineFailure(Object failure) {
        if (isEmpty()) {
            StringBuilder sb = new StringBuilder("Storage Engine and Eviction Failed - Empty Map\n");
            sb.append("Storage Engine : ").append(this.storageEngine);
            throw new OversizeMappingException(sb.toString());
        }
        int evictionIndex = getEvictionIndex();
        if (evictionIndex < 0) {
            StringBuilder sb2 = new StringBuilder("Storage Engine and Eviction Failed - Everything Pinned (" + getSize() + " mappings) \n");
            sb2.append("Storage Engine : ").append(this.storageEngine);
            throw new OversizeMappingException(sb2.toString());
        }
        evict(evictionIndex, false);
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap
    protected void tableExpansionFailure(int start, int length) {
        int evictionIndex = getEvictionIndex(start, length);
        if (evictionIndex < 0) {
            if (tryIncreaseReprobe()) {
                LOGGER.debug("Increased reprobe to {} slots for a {} slot table in a last ditch attempt to avoid storage failure.", Integer.valueOf(getReprobeLength()), Long.valueOf(getTableCapacity()));
                return;
            }
            StringBuilder sb = new StringBuilder("Table Expansion and Eviction Failed.\n");
            sb.append("Current Table Size (slots) : ").append(getTableCapacity()).append('\n');
            sb.append("Current Reprobe Length     : ").append(getReprobeLength()).append('\n');
            sb.append("Resize Will Require        : ").append(DebuggingUtils.toBase2SuffixedString(getTableCapacity() * 4 * 4 * 2)).append("B\n");
            sb.append("Table Page Source          : ").append(this.tableSource);
            throw new OversizeMappingException(sb.toString());
        }
        evict(evictionIndex, false);
    }

    @Override // org.terracotta.offheapstore.OffHeapHashMap
    protected void hit(IntBuffer entry) {
        entry.put(0, Integer.MIN_VALUE | entry.get(0));
    }

    public int getEvictionIndex() {
        if (this.clockHand >= this.hashtable.capacity()) {
            this.clockHand = 0;
        }
        int initialHand = this.clockHand;
        int loops = 0;
        while (true) {
            int i = this.clockHand + 4;
            this.clockHand = i;
            if (i + 0 >= this.hashtable.capacity()) {
                this.clockHand = 0;
            }
            int hand = this.hashtable.get(this.clockHand + 0);
            if (evictable(hand) && (hand & Integer.MIN_VALUE) == 0) {
                return this.clockHand;
            }
            if ((hand & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                this.hashtable.put(this.clockHand + 0, hand & Integer.MAX_VALUE);
            }
            if (initialHand == this.clockHand) {
                loops++;
                if (loops == 2) {
                    return -1;
                }
            }
        }
    }

    private int getEvictionIndex(int start, int length) {
        int index = getEvictionIndex();
        int tableLength = this.hashtable.capacity();
        int probeLength = length * 4;
        if (probeLength >= this.hashtable.capacity()) {
            return index;
        }
        int end = (start + probeLength) & (tableLength - 1);
        if (index < 0) {
            return index;
        }
        if (end > start && index >= start && index <= end) {
            return index;
        }
        if (end < start && (index >= start || index < end)) {
            return index;
        }
        evict(index, false);
        int clock = start;
        for (int i = 0; i < length; i++) {
            clock += 4;
            if (clock >= tableLength) {
                clock = start;
            }
            int hand = this.hashtable.get(clock + 0);
            if (evictable(hand) && (hand & Integer.MIN_VALUE) == 0) {
                return clock;
            }
        }
        int lastEvictable = -1;
        int i2 = 0;
        int clock2 = start;
        while (true) {
            if (i2 >= this.rndm.nextInt(length) && (lastEvictable >= 0 || i2 >= length)) {
                break;
            }
            clock2 += 4;
            if (clock2 >= tableLength) {
                clock2 = start;
            }
            if (evictable(this.hashtable.get(clock2 + 0))) {
                lastEvictable = clock2;
            }
            i2++;
        }
        return lastEvictable;
    }

    protected boolean evictable(int status) {
        return (status & 1) == 1 && (status & 1073741824) == 0;
    }

    @Override // org.terracotta.offheapstore.AbstractLockedOffHeapHashMap, org.terracotta.offheapstore.OffHeapHashMap, org.terracotta.offheapstore.storage.StorageEngine.Owner
    public boolean evict(int index, boolean shrink) {
        Lock l = writeLock();
        l.lock();
        try {
            if (evictable(this.hashtable.get(index + 0))) {
                removeAtTableOffset(index, shrink);
                l.unlock();
                return true;
            }
            return false;
        } finally {
            l.unlock();
        }
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public boolean isPinned(Object key) {
        Integer metadata = getMetadata(key, 1073741824);
        return (metadata == null || metadata.intValue() == 0) ? false : true;
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public void setPinning(K key, boolean pinned) {
        if (pinned) {
            getAndSetMetadata(key, 1073741824, 1073741824);
        } else {
            getAndSetMetadata(key, 1073741824, 0);
        }
    }

    public V putPinned(K key, V value) {
        return put(key, value, 1073741824);
    }

    @Override // org.terracotta.offheapstore.pinning.PinnableCache
    public V getAndPin(K key) {
        return getValueAndSetMetadata(key, 1073741824, 1073741824);
    }
}
