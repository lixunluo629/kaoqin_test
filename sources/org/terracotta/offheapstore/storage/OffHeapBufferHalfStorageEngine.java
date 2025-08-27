package org.terracotta.offheapstore.storage;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import org.terracotta.offheapstore.paging.OffHeapStorageArea;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/OffHeapBufferHalfStorageEngine.class */
public class OffHeapBufferHalfStorageEngine<T> extends PortabilityBasedHalfStorageEngine<T> implements OffHeapStorageArea.Owner {
    private static final int KEY_HASH_OFFSET = 0;
    private static final int LENGTH_OFFSET = 4;
    private static final int DATA_OFFSET = 8;
    private static final int HEADER_LENGTH = 8;
    private volatile StorageEngine.Owner owner;
    private volatile long mask;
    private final OffHeapStorageArea storageArea;

    public static <T> Factory<OffHeapBufferHalfStorageEngine<T>> createFactory(PageSource source, int pageSize, Portability<? super T> portability) {
        return createFactory(source, pageSize, portability, false, false);
    }

    public static <T> Factory<OffHeapBufferHalfStorageEngine<T>> createFactory(PageSource source, int pageSize, Portability<? super T> portability, boolean thief, boolean victim) {
        return createFactory(source, pageSize, pageSize, portability, thief, victim);
    }

    public static <T> Factory<OffHeapBufferHalfStorageEngine<T>> createFactory(final PageSource source, final int initialPageSize, final int maximalPageSize, final Portability<? super T> portability, final boolean thief, final boolean victim) {
        return new Factory<OffHeapBufferHalfStorageEngine<T>>() { // from class: org.terracotta.offheapstore.storage.OffHeapBufferHalfStorageEngine.1
            @Override // org.terracotta.offheapstore.util.Factory
            public OffHeapBufferHalfStorageEngine<T> newInstance() {
                return new OffHeapBufferHalfStorageEngine<>(source, initialPageSize, maximalPageSize, portability, thief, victim);
            }
        };
    }

    public OffHeapBufferHalfStorageEngine(PageSource source, int pageSize, Portability<? super T> portability) {
        this(source, pageSize, portability, false, false);
    }

    public OffHeapBufferHalfStorageEngine(PageSource source, int pageSize, Portability<? super T> portability, boolean thief, boolean victim) {
        this(source, pageSize, pageSize, portability, thief, victim);
    }

    public OffHeapBufferHalfStorageEngine(PageSource source, int initialPageSize, int maximalPageSize, Portability<? super T> portability, boolean thief, boolean victim) {
        super(portability);
        this.storageArea = new OffHeapStorageArea(PointerSize.INT, this, source, initialPageSize, maximalPageSize, thief, victim);
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void clear() {
        this.storageArea.clear();
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedHalfStorageEngine, org.terracotta.offheapstore.storage.HalfStorageEngine
    public void free(int address) {
        this.storageArea.free(address);
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedHalfStorageEngine
    protected ByteBuffer readBuffer(int address) {
        int length = this.storageArea.readInt(address + 4);
        return this.storageArea.readBuffer(address + 8, length);
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedHalfStorageEngine
    protected Integer writeBuffer(ByteBuffer buffer, int hash) {
        int length = buffer.remaining();
        int address = (int) this.storageArea.allocate(length + 8);
        if (address >= 0) {
            this.storageArea.writeInt(address + 0, hash);
            this.storageArea.writeInt(address + 4, length);
            this.storageArea.writeBuffer(address + 8, buffer);
            return Integer.valueOf(address);
        }
        return null;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getAllocatedMemory() {
        return this.storageArea.getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getOccupiedMemory() {
        return this.storageArea.getOccupiedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getVitalMemory() {
        return getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getDataSize() {
        return getOccupiedMemory();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("OffHeapBufferStorageEngine ");
        sb.append("allocated=").append(DebuggingUtils.toBase2SuffixedString(getAllocatedMemory())).append("B ");
        sb.append("occupied=").append(DebuggingUtils.toBase2SuffixedString(getOccupiedMemory())).append("B\n");
        sb.append("Allocator: ").append(this.storageArea);
        return sb.toString();
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void bind(StorageEngine.Owner o, long m) {
        if (this.owner != null) {
            throw new AssertionError();
        }
        this.owner = o;
        this.mask = m;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void destroy() {
        this.storageArea.destroy();
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public boolean shrink() {
        return this.storageArea.shrink();
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public boolean evictAtAddress(long address, boolean shrink) {
        int hash = this.storageArea.readInt(address + 0);
        int slot = this.owner.getSlotForHashAndEncoding(hash, address, this.mask).intValue();
        return this.owner.evict(slot, shrink);
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public Lock writeLock() {
        return this.owner.writeLock();
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public boolean isThief() {
        return this.owner.isThiefForTableAllocations();
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public boolean moved(long from, long to) {
        int hash = this.storageArea.readInt(to + 0);
        return this.owner.updateEncoding(hash, from, to, this.mask);
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public int sizeOf(long address) {
        int length = this.storageArea.readInt(address + 4);
        return 8 + length;
    }
}
