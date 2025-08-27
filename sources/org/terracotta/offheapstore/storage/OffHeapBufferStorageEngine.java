package org.terracotta.offheapstore.storage;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import org.terracotta.offheapstore.paging.OffHeapStorageArea;
import org.terracotta.offheapstore.paging.PageSource;
import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.storage.portability.Portability;
import org.terracotta.offheapstore.storage.portability.WriteContext;
import org.terracotta.offheapstore.util.ByteBufferUtils;
import org.terracotta.offheapstore.util.DebuggingUtils;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/OffHeapBufferStorageEngine.class */
public class OffHeapBufferStorageEngine<K, V> extends PortabilityBasedStorageEngine<K, V> implements OffHeapStorageArea.Owner {
    private static final int KEY_HASH_OFFSET = 0;
    private static final int KEY_LENGTH_OFFSET = 4;
    private static final int VALUE_LENGTH_OFFSET = 8;
    private static final int DATA_OFFSET = 12;
    private static final int HEADER_SIZE = 12;
    protected final OffHeapStorageArea storageArea;
    protected volatile StorageEngine.Owner owner;

    public static <K, V> Factory<OffHeapBufferStorageEngine<K, V>> createFactory(final PointerSize width, final PageSource source, final int pageSize, final Portability<? super K> keyPortability, final Portability<? super V> valuePortability, final boolean thief, final boolean victim) {
        return new Factory<OffHeapBufferStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.storage.OffHeapBufferStorageEngine.1
            @Override // org.terracotta.offheapstore.util.Factory
            public OffHeapBufferStorageEngine<K, V> newInstance() {
                return new OffHeapBufferStorageEngine<>(width, source, pageSize, keyPortability, valuePortability, thief, victim);
            }
        };
    }

    public static <K, V> Factory<OffHeapBufferStorageEngine<K, V>> createFactory(final PointerSize width, final PageSource source, final int pageSize, final Portability<? super K> keyPortability, final Portability<? super V> valuePortability, final boolean thief, final boolean victim, final float compressThreshold) {
        return new Factory<OffHeapBufferStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.storage.OffHeapBufferStorageEngine.2
            @Override // org.terracotta.offheapstore.util.Factory
            public OffHeapBufferStorageEngine<K, V> newInstance() {
                return new OffHeapBufferStorageEngine<>(width, source, pageSize, keyPortability, valuePortability, thief, victim, compressThreshold);
            }
        };
    }

    public static <K, V> Factory<OffHeapBufferStorageEngine<K, V>> createFactory(final PointerSize width, final PageSource source, final int initialPageSize, final int maximalPageSize, final Portability<? super K> keyPortability, final Portability<? super V> valuePortability, final boolean thief, final boolean victim) {
        return new Factory<OffHeapBufferStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.storage.OffHeapBufferStorageEngine.3
            @Override // org.terracotta.offheapstore.util.Factory
            public OffHeapBufferStorageEngine<K, V> newInstance() {
                return new OffHeapBufferStorageEngine<>(width, source, initialPageSize, maximalPageSize, keyPortability, valuePortability, thief, victim);
            }
        };
    }

    public static <K, V> Factory<OffHeapBufferStorageEngine<K, V>> createFactory(final PointerSize width, final PageSource source, final int initialPageSize, final int maximalPageSize, final Portability<? super K> keyPortability, final Portability<? super V> valuePortability, final boolean thief, final boolean victim, final float compressThreshold) {
        return new Factory<OffHeapBufferStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.storage.OffHeapBufferStorageEngine.4
            @Override // org.terracotta.offheapstore.util.Factory
            public OffHeapBufferStorageEngine<K, V> newInstance() {
                return new OffHeapBufferStorageEngine<>(width, source, initialPageSize, maximalPageSize, keyPortability, valuePortability, thief, victim, compressThreshold);
            }
        };
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int pageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability) {
        this(width, source, pageSize, pageSize, keyPortability, valuePortability);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int pageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability, float compressThreshold) {
        this(width, source, pageSize, pageSize, keyPortability, valuePortability, compressThreshold);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int initialPageSize, int maximalPageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability) {
        this(width, source, initialPageSize, maximalPageSize, (Portability) keyPortability, (Portability) valuePortability, false, false);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int initialPageSize, int maximalPageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability, float compressThreshold) {
        this(width, source, initialPageSize, maximalPageSize, keyPortability, valuePortability, false, false, compressThreshold);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int pageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability, boolean thief, boolean victim) {
        this(width, source, pageSize, pageSize, keyPortability, valuePortability, thief, victim);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int pageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability, boolean thief, boolean victim, float compressThreshold) {
        this(width, source, pageSize, pageSize, keyPortability, valuePortability, thief, victim, compressThreshold);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int initialPageSize, int maximalPageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability, boolean thief, boolean victim) {
        super(keyPortability, valuePortability);
        this.storageArea = new OffHeapStorageArea(width, this, source, initialPageSize, maximalPageSize, thief, victim);
    }

    public OffHeapBufferStorageEngine(PointerSize width, PageSource source, int initialPageSize, int maximalPageSize, Portability<? super K> keyPortability, Portability<? super V> valuePortability, boolean thief, boolean victim, float compressThreshold) {
        super(keyPortability, valuePortability);
        this.storageArea = new OffHeapStorageArea(width, this, source, initialPageSize, maximalPageSize, thief, victim, compressThreshold);
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected void clearInternal() {
        this.storageArea.clear();
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected void free(long address) {
        this.storageArea.free(address);
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    public ByteBuffer readKeyBuffer(long address) {
        int length = this.storageArea.readInt(address + 4);
        return this.storageArea.readBuffer(address + 12, length).asReadOnlyBuffer();
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected WriteContext getKeyWriteContext(long address) {
        int keyLength = this.storageArea.readInt(address + 4);
        return getWriteContext(address + 12, keyLength);
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    public ByteBuffer readValueBuffer(long address) {
        int keyLength = this.storageArea.readInt(address + 4);
        int valueLength = this.storageArea.readInt(address + 8);
        return this.storageArea.readBuffer(address + 12 + keyLength, valueLength).asReadOnlyBuffer();
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected WriteContext getValueWriteContext(long address) {
        int keyLength = this.storageArea.readInt(address + 4);
        int valueLength = this.storageArea.readInt(address + 8);
        return getWriteContext(address + 12 + keyLength, valueLength);
    }

    private WriteContext getWriteContext(final long address, final int max) {
        return new WriteContext() { // from class: org.terracotta.offheapstore.storage.OffHeapBufferStorageEngine.5
            @Override // org.terracotta.offheapstore.storage.portability.WriteContext
            public void setLong(int offset, long value) {
                if (offset < 0 || offset > max) {
                    throw new IllegalArgumentException();
                }
                OffHeapBufferStorageEngine.this.storageArea.writeLong(address + offset, value);
            }

            @Override // org.terracotta.offheapstore.storage.portability.WriteContext
            public void flush() {
            }
        };
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected Long writeMappingBuffers(ByteBuffer keyBuffer, ByteBuffer valueBuffer, int hash) {
        int keyLength = keyBuffer.remaining();
        int valueLength = valueBuffer.remaining();
        long address = this.storageArea.allocate(keyLength + valueLength + 12);
        if (address >= 0) {
            this.storageArea.writeInt(address + 0, hash);
            this.storageArea.writeInt(address + 4, keyLength);
            this.storageArea.writeInt(address + 8, valueLength);
            this.storageArea.writeBuffer(address + 12, keyBuffer);
            this.storageArea.writeBuffer(address + 12 + keyLength, valueBuffer);
            return Long.valueOf(address);
        }
        return null;
    }

    @Override // org.terracotta.offheapstore.storage.PortabilityBasedStorageEngine
    protected Long writeMappingBuffersGathering(ByteBuffer[] keyBuffers, ByteBuffer[] valueBuffers, int hash) {
        int keyLength = ByteBufferUtils.totalLength(keyBuffers);
        int valueLength = ByteBufferUtils.totalLength(valueBuffers);
        long address = this.storageArea.allocate(keyLength + valueLength + 12);
        if (address >= 0) {
            this.storageArea.writeInt(address + 0, hash);
            this.storageArea.writeInt(address + 4, keyLength);
            this.storageArea.writeInt(address + 8, valueLength);
            this.storageArea.writeBuffers(address + 12, keyBuffers);
            this.storageArea.writeBuffers(address + 12 + keyLength, valueBuffers);
            return Long.valueOf(address);
        }
        return null;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getAllocatedMemory() {
        return this.storageArea.getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getOccupiedMemory() {
        return this.storageArea.getOccupiedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getVitalMemory() {
        return getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getDataSize() {
        return getOccupiedMemory();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("OffHeapBufferStorageEngine ");
        sb.append("allocated=").append(DebuggingUtils.toBase2SuffixedString(getAllocatedMemory())).append("B ");
        sb.append("occupied=").append(DebuggingUtils.toBase2SuffixedString(getOccupiedMemory())).append("B\n");
        sb.append("Storage Area: ").append(this.storageArea);
        return sb.toString();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void destroy() {
        this.storageArea.destroy();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean shrink() {
        return this.storageArea.shrink();
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public boolean evictAtAddress(long address, boolean shrink) {
        int hash = this.storageArea.readInt(address + 0);
        int slot = this.owner.getSlotForHashAndEncoding(hash, address, -1L).intValue();
        return this.owner.evict(slot, shrink);
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public boolean isThief() {
        return this.owner.isThiefForTableAllocations();
    }

    @Override // org.terracotta.offheapstore.storage.BinaryStorageEngine
    public int readKeyHash(long encoding) {
        return this.storageArea.readInt(encoding + 0);
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public boolean moved(long from, long to) {
        return this.owner.updateEncoding(readKeyHash(to), from, to, -1L);
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public int sizeOf(long address) {
        int keyLength = this.storageArea.readInt(address + 4);
        int valueLength = this.storageArea.readInt(address + 8);
        return 12 + keyLength + valueLength;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void bind(StorageEngine.Owner m) {
        if (this.owner != null) {
            throw new AssertionError();
        }
        this.owner = m;
    }

    @Override // org.terracotta.offheapstore.paging.OffHeapStorageArea.Owner
    public Lock writeLock() {
        return this.owner.writeLock();
    }
}
