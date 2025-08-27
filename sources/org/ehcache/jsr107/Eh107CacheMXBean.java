package org.ehcache.jsr107;

import javax.cache.management.CacheMXBean;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Eh107CacheMXBean.class */
class Eh107CacheMXBean extends Eh107MXBean implements CacheMXBean {
    private final Eh107Configuration<?, ?> config;

    Eh107CacheMXBean(String cacheName, Eh107CacheManager cacheManager, Eh107Configuration<?, ?> config) {
        super(cacheName, cacheManager, "CacheConfiguration");
        this.config = config;
    }

    public String getKeyType() {
        return this.config.getKeyType().getName();
    }

    public String getValueType() {
        return this.config.getValueType().getName();
    }

    public boolean isReadThrough() {
        return this.config.isReadThrough();
    }

    public boolean isWriteThrough() {
        return this.config.isWriteThrough();
    }

    public boolean isStoreByValue() {
        return this.config.isStoreByValue();
    }

    public boolean isStatisticsEnabled() {
        return this.config.isStatisticsEnabled();
    }

    public boolean isManagementEnabled() {
        return this.config.isManagementEnabled();
    }
}
