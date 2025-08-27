package org.springframework.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/guava/GuavaCache.class */
public class GuavaCache extends AbstractValueAdaptingCache {
    private final String name;
    private final Cache<Object, Object> cache;

    public GuavaCache(String name, Cache<Object, Object> cache) {
        this(name, cache, true);
    }

    public GuavaCache(String name, Cache<Object, Object> cache, boolean allowNullValues) {
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
            try {
                Object value = ((LoadingCache) this.cache).get(key);
                return toValueWrapper(value);
            } catch (ExecutionException ex) {
                throw new UncheckedExecutionException(ex.getMessage(), ex);
            }
        }
        return super.get(key);
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, final Callable<T> callable) {
        try {
            return (T) fromStoreValue(this.cache.get(obj, new Callable<Object>() { // from class: org.springframework.cache.guava.GuavaCache.1
                @Override // java.util.concurrent.Callable
                public Object call() throws Exception {
                    return GuavaCache.this.toStoreValue(callable.call());
                }
            }));
        } catch (UncheckedExecutionException e) {
            throw new Cache.ValueRetrievalException(obj, callable, e.getCause());
        } catch (ExecutionException e2) {
            throw new Cache.ValueRetrievalException(obj, callable, e2.getCause());
        }
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
        try {
            PutIfAbsentCallable callable = new PutIfAbsentCallable(value);
            Object result = this.cache.get(key, callable);
            if (callable.called) {
                return null;
            }
            return toValueWrapper(result);
        } catch (ExecutionException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override // org.springframework.cache.Cache
    public void evict(Object key) {
        this.cache.invalidate(key);
    }

    @Override // org.springframework.cache.Cache
    public void clear() {
        this.cache.invalidateAll();
    }

    /* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/guava/GuavaCache$PutIfAbsentCallable.class */
    private class PutIfAbsentCallable implements Callable<Object> {
        private final Object value;
        private boolean called;

        public PutIfAbsentCallable(Object value) {
            this.value = value;
        }

        @Override // java.util.concurrent.Callable
        public Object call() throws Exception {
            this.called = true;
            return GuavaCache.this.toStoreValue(this.value);
        }
    }
}
