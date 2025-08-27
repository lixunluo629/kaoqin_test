package org.terracotta.offheapstore.storage;

import org.terracotta.offheapstore.storage.StorageEngine;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/storage/BooleanHalfStorageEngine.class */
public final class BooleanHalfStorageEngine implements HalfStorageEngine<Boolean> {
    public static final BooleanHalfStorageEngine INSTANCE = new BooleanHalfStorageEngine();

    private BooleanHalfStorageEngine() {
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public Integer write(Boolean object, int hash) {
        return Integer.valueOf(object.booleanValue() ? 1 : 0);
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public void free(int encoding) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public Boolean read(int encoding) {
        return Boolean.valueOf(encoding == 1);
    }

    @Override // org.terracotta.offheapstore.storage.HalfStorageEngine
    public boolean equals(Object object, int encoding) {
        return (object instanceof Boolean) && write((Boolean) object, 0).intValue() == encoding;
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
