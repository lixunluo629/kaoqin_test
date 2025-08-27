package org.apache.ibatis.cache.decorators;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/FifoCache.class */
public class FifoCache implements Cache {
    private final Cache delegate;
    private final Deque<Object> keyList = new LinkedList();
    private int size = 1024;

    public FifoCache(Cache delegate) {
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

    public void setSize(int size) {
        this.size = size;
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object value) {
        cycleKeyList(key);
        this.delegate.putObject(key, value);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        return this.delegate.getObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.delegate.clear();
        this.keyList.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private void cycleKeyList(Object key) {
        this.keyList.addLast(key);
        if (this.keyList.size() > this.size) {
            Object oldestKey = this.keyList.removeFirst();
            this.delegate.removeObject(oldestKey);
        }
    }
}
