package org.springframework.cache.support;

import org.springframework.cache.Cache;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/support/AbstractValueAdaptingCache.class */
public abstract class AbstractValueAdaptingCache implements Cache {
    private final boolean allowNullValues;

    protected abstract Object lookup(Object obj);

    protected AbstractValueAdaptingCache(boolean allowNullValues) {
        this.allowNullValues = allowNullValues;
    }

    public final boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override // org.springframework.cache.Cache
    public Cache.ValueWrapper get(Object key) {
        Object value = lookup(key);
        return toValueWrapper(value);
    }

    @Override // org.springframework.cache.Cache
    public <T> T get(Object obj, Class<T> cls) {
        T t = (T) fromStoreValue(lookup(obj));
        if (t != null && cls != null && !cls.isInstance(t)) {
            throw new IllegalStateException("Cached value is not of required type [" + cls.getName() + "]: " + t);
        }
        return t;
    }

    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && storeValue == NullValue.INSTANCE) {
            return null;
        }
        return storeValue;
    }

    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NullValue.INSTANCE;
        }
        return userValue;
    }

    protected Cache.ValueWrapper toValueWrapper(Object storeValue) {
        if (storeValue != null) {
            return new SimpleValueWrapper(fromStoreValue(storeValue));
        }
        return null;
    }
}
