package org.apache.ibatis.cache.decorators;

import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/ScheduledCache.class */
public class ScheduledCache implements Cache {
    private final Cache delegate;
    protected long clearInterval = 3600000;
    protected long lastClear = System.currentTimeMillis();

    public ScheduledCache(Cache delegate) {
        this.delegate = delegate;
    }

    public void setClearInterval(long clearInterval) {
        this.clearInterval = clearInterval;
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.delegate.getId();
    }

    @Override // org.apache.ibatis.cache.Cache
    public int getSize() {
        clearWhenStale();
        return this.delegate.getSize();
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object object) {
        clearWhenStale();
        this.delegate.putObject(key, object);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        if (clearWhenStale()) {
            return null;
        }
        return this.delegate.getObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        clearWhenStale();
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.lastClear = System.currentTimeMillis();
        this.delegate.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public int hashCode() {
        return this.delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return this.delegate.equals(obj);
    }

    private boolean clearWhenStale() {
        if (System.currentTimeMillis() - this.lastClear > this.clearInterval) {
            clear();
            return true;
        }
        return false;
    }
}
