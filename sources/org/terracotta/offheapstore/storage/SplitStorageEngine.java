package org.terracotta.offheapstore.storage;

import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/SplitStorageEngine.class */
public class SplitStorageEngine<K, V> implements StorageEngine<K, V> {
    protected final HalfStorageEngine<? super K> keyStorageEngine;
    protected final HalfStorageEngine<? super V> valueStorageEngine;

    public static <K, V> Factory<SplitStorageEngine<K, V>> createFactory(final Factory<? extends HalfStorageEngine<K>> keyFactory, final Factory<? extends HalfStorageEngine<V>> valueFactory) {
        return new Factory<SplitStorageEngine<K, V>>() { // from class: org.terracotta.offheapstore.storage.SplitStorageEngine.1
            @Override // org.terracotta.offheapstore.util.Factory
            public SplitStorageEngine<K, V> newInstance() {
                return new SplitStorageEngine<>((HalfStorageEngine) keyFactory.newInstance(), (HalfStorageEngine) valueFactory.newInstance());
            }
        };
    }

    public SplitStorageEngine(HalfStorageEngine<? super K> keyStorageEngine, HalfStorageEngine<? super V> valueStorageEngine) {
        this.keyStorageEngine = keyStorageEngine;
        this.valueStorageEngine = valueStorageEngine;
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public Long writeMapping(K key, V value, int hash, int metadata) {
        Integer keyEncoding = this.keyStorageEngine.write(key, hash);
        if (keyEncoding == null) {
            return null;
        }
        Integer valueEncoding = this.valueStorageEngine.write(value, hash);
        if (valueEncoding == null) {
            this.keyStorageEngine.free(keyEncoding.intValue());
            return null;
        }
        return Long.valueOf(encoding(keyEncoding.intValue(), valueEncoding.intValue()));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void attachedMapping(long encoding, int hash, int metadata) {
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void freeMapping(long encoding, int hash, boolean removal) {
        this.keyStorageEngine.free(keyEncoding(encoding));
        this.valueStorageEngine.free(valueEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public V readValue(long encoding) {
        return this.valueStorageEngine.read(valueEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean equalsValue(Object value, long encoding) {
        return this.valueStorageEngine.equals(value, valueEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public K readKey(long encoding, int hashCode) {
        return this.keyStorageEngine.read(keyEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean equalsKey(Object key, long encoding) {
        return this.keyStorageEngine.equals(key, keyEncoding(encoding));
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void clear() {
        this.keyStorageEngine.clear();
        this.valueStorageEngine.clear();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getAllocatedMemory() {
        return this.keyStorageEngine.getAllocatedMemory() + this.valueStorageEngine.getAllocatedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getOccupiedMemory() {
        return this.keyStorageEngine.getOccupiedMemory() + this.valueStorageEngine.getOccupiedMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getVitalMemory() {
        return this.keyStorageEngine.getVitalMemory() + this.valueStorageEngine.getVitalMemory();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public long getDataSize() {
        return this.keyStorageEngine.getDataSize() + this.valueStorageEngine.getDataSize();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SplitStorageEngine:\n");
        sb.append("Keys:\n");
        sb.append(this.keyStorageEngine).append('\n');
        sb.append("Values:\n");
        sb.append(this.valueStorageEngine);
        return sb.toString();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void invalidateCache() {
        this.keyStorageEngine.invalidateCache();
        this.valueStorageEngine.invalidateCache();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void bind(StorageEngine.Owner owner) {
        this.keyStorageEngine.bind(owner, -4294967296L);
        this.valueStorageEngine.bind(owner, 4294967295L);
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public void destroy() {
        this.keyStorageEngine.destroy();
        this.valueStorageEngine.destroy();
    }

    @Override // org.terracotta.offheapstore.storage.StorageEngine
    public boolean shrink() {
        return this.keyStorageEngine.shrink() || this.valueStorageEngine.shrink();
    }

    public static int valueEncoding(long encoding) {
        return (int) encoding;
    }

    public static int keyEncoding(long encoding) {
        return (int) (encoding >>> 32);
    }

    public static long encoding(int keyEncoding, int valueEncoding) {
        return (keyEncoding << 32) | (valueEncoding & 4294967295L);
    }
}
