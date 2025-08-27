package org.apache.ibatis.cache.decorators;

import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/SynchronizedCache.class */
public class SynchronizedCache implements Cache {
    private final Cache delegate;

    public SynchronizedCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.delegate.getId();
    }

    @Override // org.apache.ibatis.cache.Cache
    public synchronized int getSize() {
        return this.delegate.getSize();
    }

    @Override // org.apache.ibatis.cache.Cache
    public synchronized void putObject(Object key, Object object) {
        this.delegate.putObject(key, object);
    }

    @Override // org.apache.ibatis.cache.Cache
    public synchronized Object getObject(Object key) {
        return this.delegate.getObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public synchronized Object removeObject(Object key) {
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public synchronized void clear() {
        this.delegate.clear();
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
