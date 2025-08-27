package org.springframework.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.LoadingCache;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.lang.UsesJava8;
import org.springframework.util.Assert;

@UsesJava8
/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/caffeine/CaffeineCache.class */
public class CaffeineCache extends AbstractValueAdaptingCache {
    private final String name;
    private final Cache<Object, Object> cache;

    public CaffeineCache(String name, Cache<Object, Object> cache) {
        this(name, cache, true);
    }

    public CaffeineCache(String name, Cache<Object, Object> cache, boolean allowNullValues) {
        super(allowNullValues);
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(cache, "Cache must not be null");
        this.name = name;
        this.cache = cache;
    }

    @Override // org.springframework.cache.Cache
    public final String getName() {
        return this.name;
    }

    @Override // org.springframework.cache.Cache
    public final Cache<Object, Object> getNativeCache() {
        return this.cache;
    }

    @Override // org.springframework.cache.support.AbstractValueAdaptingCache, org.springframework.cache.Cache
    public Cache.ValueWrapper get(Object key) {
        if (this.cache instanceof LoadingCache) {
            Object value = this.cache.get(key);
            return toValueWrapper(value);
        }
        return super.get(key);
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, Callable<T> callable) {
        return (T) fromStoreValue(this.cache.get(obj, new LoadFunction(callable)));
    }

    @Override // org.springframework.cache.support.AbstractValueAdaptingCache
    protected Object lookup(Object key) {
        return this.cache.getIfPresent(key);
    }

    @Override // org.springframework.cache.Cache
    public void put(Object key, Object value) {
        this.cache.put(key, toStoreValue(value));
    }

    @Override // org.springframework.cache.Cache
    public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
        PutIfAbsentFunction callable = new PutIfAbsentFunction(value);
        Object result = this.cache.get(key, callable);
        if (callable.called) {
            return null;
        }
        return toValueWrapper(result);
    }

    @Override // org.springframework.cache.Cache
    public void evict(Object key) {
        this.cache.invalidate(key);
    }

    @Override // org.springframework.cache.Cache
    public void clear() {
        this.cache.invalidateAll();
    }

    /* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/caffeine/CaffeineCache$PutIfAbsentFunction.class */
    private class PutIfAbsentFunction implements Function<Object, Object> {
        private final Object value;
        private boolean called;

        public PutIfAbsentFunction(Object value) {
            this.value = value;
        }

        @Override // java.util.function.Function
        public Object apply(Object key) {
            this.called = true;
            return CaffeineCache.this.toStoreValue(this.value);
        }
    }

    /* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/caffeine/CaffeineCache$LoadFunction.class */
    private class LoadFunction implements Function<Object, Object> {
        private final Callable<?> valueLoader;

        public LoadFunction(Callable<?> valueLoader) {
            this.valueLoader = valueLoader;
        }

        @Override // java.util.function.Function
        public Object apply(Object o) {
            try {
                return CaffeineCache.this.toStoreValue(this.valueLoader.call());
            } catch (Exception ex) {
                throw new Cache.ValueRetrievalException(o, this.valueLoader, ex);
            }
        }
    }
}
