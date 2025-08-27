package org.ehcache.core;

import java.util.Map;
import org.ehcache.UserManagedCache;
import org.ehcache.core.spi.LifeCycled;
import org.ehcache.core.statistics.BulkOps;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.terracotta.statistics.jsr166e.LongAdder;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/InternalCache.class */
public interface InternalCache<K, V> extends UserManagedCache<K, V> {
    Map<BulkOps, LongAdder> getBulkMethodEntries();

    Jsr107Cache<K, V> getJsr107Cache();

    CacheLoaderWriter<? super K, V> getCacheLoaderWriter();

    void addHook(LifeCycled lifeCycled);
}
