package org.terracotta.offheapstore.storage;

import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/LongStorageEngine.class */
public class LongStorageEngine<V> implements StorageEngine<Long, V> {
    private final HalfStorageEngine<V> valueStorage;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public /* bridge */ /* synthetic */ Long writeMapping(Long l, Object obj, int x2, int x3) {
        return writeMapping2(l, (Long) obj, x2, x3);
    }

    public static <V> Factory<LongStorageEngine<V>> createFactory(final Factory<? extends HalfStorageEngine<V>> valueFactory) {
        return new Factory<LongStorageEngine<V>>() { // from class: org.terracotta.offheapstore.storage.LongStorageEngine.1
            @Override // org.terracotta.offheapstore.util.Factory
            public LongStorageEngine<V> newInstance() {
                return new LongStorageEngine<>((HalfStorageEngine) valueFactory.newInstance());
            }
        };
    }

    public LongStorageEngine(HalfStorageEngine<V> valueStorage) {
        this.valueStorage = valueStorage;
    }

    /* renamed from: writeMapping, reason: avoid collision after fix types in other method */
    public Long writeMapping2(Long key, V value, int hash, int metadata) {
        Integer valueEncoding = this.valueStorage.write(value, hash);
        if (valueEncoding == null) {
            return null;
        }
        return Long.valueOf(SplitStorageEngine.encoding(key.intValue(), valueEncoding.intValue()));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void attachedMapping(long encoding, int hash, int metadata) {
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void freeMapping(long encoding, int hash, boolean removal) {
        this.valueStorage.free(SplitStorageEngine.valueEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public V readValue(long encoding) {
        return this.valueStorage.read(SplitStorageEngine.valueEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean equalsValue(Object value, long encoding) {
        return this.valueStorage.equals(value, SplitStorageEngine.valueEncoding(encoding));
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public Long readKey(long encoding, int hashCode) {
        int keyEncoding = SplitStorageEngine.keyEncoding(encoding);
        return Long.valueOf(((hashCode ^ keyEncoding) << 32) | (keyEncoding & 4294967295L));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean equalsKey(Object key, long encoding) {
        return (key instanceof Long) && ((Long) key).intValue() == SplitStorageEngine.keyEncoding(encoding);
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void clear() {
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getAllocatedMemory() {
        return this.valueStorage.getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getOccupiedMemory() {
        return this.valueStorage.getOccupiedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getVitalMemory() {
        return this.valueStorage.getVitalMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getDataSize() {
        return this.valueStorage.getDataSize();
    }

    public String toString() {
        return "LongStorageEngine : " + this.valueStorage;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void invalidateCache() {
        this.valueStorage.invalidateCache();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void bind(StorageEngine.Owner owner) {
        this.valueStorage.bind(owner, 4294967295L);
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void destroy() {
        this.valueStorage.destroy();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean shrink() {
        return this.valueStorage.shrink();
    }
}
