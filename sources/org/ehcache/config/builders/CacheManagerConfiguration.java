package org.ehcache.config.builders;

import org.ehcache.CacheManager;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/CacheManagerConfiguration.class */
public interface CacheManagerConfiguration<T extends CacheManager> {
    CacheManagerBuilder<T> builder(CacheManagerBuilder<? extends CacheManager> cacheManagerBuilder);
}
