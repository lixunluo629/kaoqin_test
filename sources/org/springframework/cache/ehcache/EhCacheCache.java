package org.springframework.cache.ehcache;

import java.util.concurrent.Callable;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/ehcache/EhCacheCache.class */
public class EhCacheCache implements Cache {
    private final Ehcache cache;

    public EhCacheCache(Ehcache ehcache) {
        Assert.notNull(ehcache, "Ehcache must not be null");
        Status status = ehcache.getStatus();
        if (!Status.STATUS_ALIVE.equals(status)) {
            throw new IllegalArgumentException("An 'alive' Ehcache is required - current cache is " + status.toString());
        }
        this.cache = ehcache;
    }

    @Override // org.springframework.cache.Cache
    public final String getName() {
        return this.cache.getName();
    }

    @Override // org.springframework.cache.Cache
    public final Ehcache getNativeCache() {
        return this.cache;
    }

    @Override // org.springframework.cache.Cache
    public Cache.ValueWrapper get(Object key) {
        Element element = lookup(key);
        return toValueWrapper(element);
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, Callable<T> callable) {
        Element elementLookup = lookup(obj);
        if (elementLookup != null) {
            return (T) elementLookup.getObjectValue();
        }
        this.cache.acquireWriteLockOnKey(obj);
        try {
            Element elementLookup2 = lookup(obj);
            if (elementLookup2 != null) {
                T t = (T) elementLookup2.getObjectValue();
                this.cache.releaseWriteLockOnKey(obj);
                return t;
            }
            T t2 = (T) loadValue(obj, callable);
            this.cache.releaseWriteLockOnKey(obj);
            return t2;
        } catch (Throwable th) {
            this.cache.releaseWriteLockOnKey(obj);
            throw th;
        }
    }

    private <T> T loadValue(Object key, Callable<T> valueLoader) {
        try {
            T value = valueLoader.call();
            put(key, value);
            return value;
        } catch (Throwable ex) {
            throw new Cache.ValueRetrievalException(key, valueLoader, ex);
        }
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, Class<T> cls) {
        Element element = this.cache.get(obj);
        T t = (T) (element != null ? element.getObjectValue() : null);
        if (t != null && cls != null && !cls.isInstance(t)) {
            throw new IllegalStateException("Cached value is not of required type [" + cls.getName() + "]: " + t);
        }
        return t;
    }

    @Override // org.springframework.cache.Cache
    public void put(Object key, Object value) {
        this.cache.put(new Element(key, value));
    }

    @Override // org.springframework.cache.Cache
    public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
        Element existingElement = this.cache.putIfAbsent(new Element(key, value));
        return toValueWrapper(existingElement);
    }

    @Override // org.springframework.cache.Cache
    public void evict(Object key) {
        this.cache.remove(key);
    }

    @Override // org.springframework.cache.Cache
    public void clear() {
        this.cache.removeAll();
    }

    private Element lookup(Object key) {
        return this.cache.get(key);
    }

    private Cache.ValueWrapper toValueWrapper(Element element) {
        if (element != null) {
            return new SimpleValueWrapper(element.getObjectValue());
        }
        return null;
    }
}
