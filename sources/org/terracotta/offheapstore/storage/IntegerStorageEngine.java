package org.terracotta.offheapstore.storage;

import org.terracotta.offheapstore.storage.StorageEngine;
import org.terracotta.offheapstore.util.Factory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/IntegerStorageEngine.class */
public class IntegerStorageEngine implements HalfStorageEngine<Integer> {
    private static final IntegerStorageEngine SINGLETON = new IntegerStorageEngine();
    private static final Factory<IntegerStorageEngine> FACTORY = new Factory<IntegerStorageEngine>() { // from class: org.terracotta.offheapstore.storage.IntegerStorageEngine.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.terracotta.offheapstore.util.Factory
        public IntegerStorageEngine newInstance() {
            return IntegerStorageEngine.SINGLETON;
        }
    };

    public static IntegerStorageEngine instance() {
        return SINGLETON;
    }

    public static Factory<IntegerStorageEngine> createFactory() {
        return FACTORY;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public Integer read(int address) {
        return Integer.valueOf(address);
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public Integer write(Integer value, int hash) {
        return value;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void free(int address) {
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public boolean equals(Object key, int address) {
        return (key instanceof Integer) && ((Integer) key).intValue() == address;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void clear() {
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getAllocatedMemory() {
        return 0L;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getOccupiedMemory() {
        return 0L;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getVitalMemory() {
        return 0L;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public long getDataSize() {
        return 0L;
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void invalidateCache() {
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void bind(StorageEngine.Owner owner, long mask) {
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void destroy() {
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public boolean shrink() {
        return false;
    }
}
