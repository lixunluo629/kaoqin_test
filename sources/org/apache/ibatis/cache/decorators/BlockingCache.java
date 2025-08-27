package org.apache.ibatis.cache.decorators;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/BlockingCache.class */
public class BlockingCache implements Cache {
    private long timeout;
    private final Cache delegate;
    private final ConcurrentHashMap<Object, ReentrantLock> locks = new ConcurrentHashMap<>();

    public BlockingCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.delegate.getId();
    }

    @Override // org.apache.ibatis.cache.Cache
    public int getSize() {
        return this.delegate.getSize();
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object value) {
        try {
            this.delegate.putObject(key, value);
        } finally {
            releaseLock(key);
        }
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) throws InterruptedException {
        acquireLock(key);
        Object value = this.delegate.getObject(key);
        if (value != null) {
            releaseLock(key);
        }
        return value;
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        releaseLock(key);
        return null;
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.delegate.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private ReentrantLock getLockForKey(Object key) {
        ReentrantLock lock = new ReentrantLock();
        ReentrantLock previous = this.locks.putIfAbsent(key, lock);
        return previous == null ? lock : previous;
    }

    private void acquireLock(Object key) throws InterruptedException {
        Lock lock = getLockForKey(key);
        if (this.timeout > 0) {
            try {
                boolean acquired = lock.tryLock(this.timeout, TimeUnit.MILLISECONDS);
                if (!acquired) {
                    throw new CacheException("Couldn't get a lock in " + this.timeout + " for the key " + key + " at the cache " + this.delegate.getId());
                }
                return;
            } catch (InterruptedException e) {
                throw new CacheException("Got interrupted while trying to acquire lock for key " + key, e);
            }
        }
        lock.lock();
    }

    private void releaseLock(Object key) {
        ReentrantLock lock = this.locks.get(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
