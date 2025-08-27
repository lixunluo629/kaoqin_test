package org.springframework.cache;

import java.util.concurrent.Callable;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/Cache.class */
public interface Cache {

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/Cache$ValueWrapper.class */
    public interface ValueWrapper {
        Object get();
    }

    String getName();

    Object getNativeCache();

    ValueWrapper get(Object obj);

    <T> T get(Object obj, Class<T> cls);

    <T> T get(Object obj, Callable<T> callable);

    void put(Object obj, Object obj2);

    ValueWrapper putIfAbsent(Object obj, Object obj2);

    void evict(Object obj);

    void clear();

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/Cache$ValueRetrievalException.class */
    public static class ValueRetrievalException extends RuntimeException {
        private final Object key;

        public ValueRetrievalException(Object key, Callable<?> loader, Throwable ex) {
            super(String.format("Value for key '%s' could not be loaded using '%s'", key, loader), ex);
            this.key = key;
        }

        public Object getKey() {
            return this.key;
        }
    }
}
