package org.ehcache.jsr107;

import java.io.ObjectStreamException;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import org.ehcache.Cache;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107ReverseConfiguration.class */
class Eh107ReverseConfiguration<K, V> extends Eh107Configuration<K, V> {
    private static final long serialVersionUID = 7690458739466020356L;
    private final transient Cache<K, V> cache;
    private final boolean readThrough;
    private final boolean writeThrough;
    private final boolean storeByValueOnHeap;
    private boolean managementEnabled = false;
    private boolean statisticsEnabled = true;

    Eh107ReverseConfiguration(Cache<K, V> cache, boolean readThrough, boolean writeThrough, boolean storeByValueOnHeap) {
        this.cache = cache;
        this.readThrough = readThrough;
        this.writeThrough = writeThrough;
        this.storeByValueOnHeap = storeByValueOnHeap;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isReadThrough() {
        return this.readThrough;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isWriteThrough() {
        return this.writeThrough;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isStatisticsEnabled() {
        return this.statisticsEnabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public void setStatisticsEnabled(boolean enabled) {
        this.statisticsEnabled = enabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public boolean isManagementEnabled() {
        return this.managementEnabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public void setManagementEnabled(boolean enabled) {
        this.managementEnabled = enabled;
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public void addCacheEntryListenerConfiguration(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public void removeCacheEntryListenerConfiguration(CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
    }

    @Override // org.ehcache.jsr107.Eh107Configuration
    public <T> T unwrap(Class<T> cls) {
        return (T) Unwrap.unwrap(cls, this, this.cache.getRuntimeConfiguration());
    }

    public Class<K> getKeyType() {
        return this.cache.getRuntimeConfiguration().getKeyType();
    }

    public Class<V> getValueType() {
        return this.cache.getRuntimeConfiguration().getValueType();
    }

    public boolean isStoreByValue() {
        return this.storeByValueOnHeap;
    }

    private Object writeReplace() throws ObjectStreamException {
        throw new UnsupportedOperationException("Serialization of Ehcache provider configuration classes is not supported");
    }
}
