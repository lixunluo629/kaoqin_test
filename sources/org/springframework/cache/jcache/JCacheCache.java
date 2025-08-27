package org.springframework.cache.jcache;

import java.util.concurrent.Callable;
import javax.cache.Cache;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.MutableEntry;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/JCacheCache.class */
public class JCacheCache extends AbstractValueAdaptingCache {
    private final Cache<Object, Object> cache;

    public JCacheCache(Cache<Object, Object> jcache) {
        this(jcache, true);
    }

    public JCacheCache(Cache<Object, Object> jcache, boolean allowNullValues) {
        super(allowNullValues);
        Assert.notNull(jcache, "Cache must not be null");
        this.cache = jcache;
    }

    @Override // org.springframework.cache.Cache
    public final String getName() {
        return this.cache.getName();
    }

    @Override // org.springframework.cache.Cache
    public final Cache<Object, Object> getNativeCache() {
        return this.cache;
    }

    @Override // org.springframework.cache.support.AbstractValueAdaptingCache
    protected Object lookup(Object key) {
        return this.cache.get(key);
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, Callable<T> callable) {
        try {
            return (T) this.cache.invoke(obj, new ValueLoaderEntryProcessor(), new Object[]{callable});
        } catch (EntryProcessorException e) {
            throw new Cache.ValueRetrievalException(obj, callable, e.getCause());
        }
    }

    @Override // org.springframework.cache.Cache
    public void put(Object key, Object value) {
        this.cache.put(key, toStoreValue(value));
    }

    @Override // org.springframework.cache.Cache
    public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
        boolean set = this.cache.putIfAbsent(key, toStoreValue(value));
        if (set) {
            return null;
        }
        return get(key);
    }

    @Override // org.springframework.cache.Cache
    public void evict(Object key) {
        this.cache.remove(key);
    }

    @Override // org.springframework.cache.Cache
    public void clear() {
        this.cache.removeAll();
    }

    /* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/JCacheCache$ValueLoaderEntryProcessor.class */
    private class ValueLoaderEntryProcessor<T> implements EntryProcessor<Object, Object, T> {
        private ValueLoaderEntryProcessor() {
        }

        /* JADX INFO: Thrown type has an unknown type hierarchy: javax.cache.processor.EntryProcessorException */
        /* JADX WARN: Type inference failed for: r0v7, types: [T, java.lang.Object] */
        public T process(MutableEntry<Object, Object> mutableEntry, Object... objArr) throws Exception {
            Callable callable = (Callable) objArr[0];
            if (mutableEntry.exists()) {
                return (T) JCacheCache.this.fromStoreValue(mutableEntry.getValue());
            }
            try {
                ?? Call = callable.call();
                mutableEntry.setValue(JCacheCache.this.toStoreValue(Call));
                return Call;
            } catch (Exception e) {
                throw new EntryProcessorException("Value loader '" + callable + "' failed to compute  value for key '" + mutableEntry.getKey() + "'", e);
            }
        }
    }
}
