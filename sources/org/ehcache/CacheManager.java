package org.ehcache;

import java.io.Closeable;
import org.ehcache.config.Builder;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.Configuration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/CacheManager.class */
public interface CacheManager extends Closeable {
    <K, V> Cache<K, V> createCache(String str, CacheConfiguration<K, V> cacheConfiguration);

    <K, V> Cache<K, V> createCache(String str, Builder<? extends CacheConfiguration<K, V>> builder);

    <K, V> Cache<K, V> getCache(String str, Class<K> cls, Class<V> cls2);

    void removeCache(String str);

    void init() throws StateTransitionException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close() throws StateTransitionException;

    Status getStatus();

    Configuration getRuntimeConfiguration();
}
