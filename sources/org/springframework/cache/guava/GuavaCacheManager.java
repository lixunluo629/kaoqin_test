package org.springframework.cache.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/guava/GuavaCacheManager.class */
public class GuavaCacheManager implements CacheManager {
    private CacheLoader<Object, Object> cacheLoader;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap(16);
    private boolean dynamic = true;
    private CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
    private boolean allowNullValues = true;

    public GuavaCacheManager() {
    }

    public GuavaCacheManager(String... cacheNames) {
        setCacheNames(Arrays.asList(cacheNames));
    }

    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createGuavaCache(name));
            }
            this.dynamic = false;
            return;
        }
        this.dynamic = true;
    }

    public void setCacheBuilder(CacheBuilder<Object, Object> cacheBuilder) {
        Assert.notNull(cacheBuilder, "CacheBuilder must not be null");
        doSetCacheBuilder(cacheBuilder);
    }

    public void setCacheBuilderSpec(CacheBuilderSpec cacheBuilderSpec) {
        doSetCacheBuilder(CacheBuilder.from(cacheBuilderSpec));
    }

    public void setCacheSpecification(String cacheSpecification) {
        doSetCacheBuilder(CacheBuilder.from(cacheSpecification));
    }

    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader)) {
            this.cacheLoader = cacheLoader;
            refreshKnownCaches();
        }
    }

    public void setAllowNullValues(boolean allowNullValues) {
        if (this.allowNullValues != allowNullValues) {
            this.allowNullValues = allowNullValues;
            refreshKnownCaches();
        }
    }

    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override // org.springframework.cache.CacheManager
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    @Override // org.springframework.cache.CacheManager
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createGuavaCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    protected Cache createGuavaCache(String name) {
        return new GuavaCache(name, createNativeGuavaCache(name), isAllowNullValues());
    }

    protected com.google.common.cache.Cache<Object, Object> createNativeGuavaCache(String name) {
        if (this.cacheLoader != null) {
            return this.cacheBuilder.build(this.cacheLoader);
        }
        return this.cacheBuilder.build();
    }

    private void doSetCacheBuilder(CacheBuilder<Object, Object> cacheBuilder) {
        if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
            this.cacheBuilder = cacheBuilder;
            refreshKnownCaches();
        }
    }

    private void refreshKnownCaches() {
        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
            entry.setValue(createGuavaCache(entry.getKey()));
        }
    }
}
