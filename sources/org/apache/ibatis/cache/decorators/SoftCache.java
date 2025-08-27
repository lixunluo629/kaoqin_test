package org.apache.ibatis.cache.decorators;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/SoftCache.class */
public class SoftCache implements Cache {
    private final Cache delegate;
    private int numberOfHardLinks = 256;
    private final Deque<Object> hardLinksToAvoidGarbageCollection = new LinkedList();
    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries = new ReferenceQueue<>();

    public SoftCache(Cache delegate) {
        this.delegate = delegate;
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.delegate.getId();
    }

    @Override // org.apache.ibatis.cache.Cache
    public int getSize() {
        removeGarbageCollectedItems();
        return this.delegate.getSize();
    }

    public void setSize(int size) {
        this.numberOfHardLinks = size;
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object value) {
        removeGarbageCollectedItems();
        this.delegate.putObject(key, new SoftEntry(key, value, this.queueOfGarbageCollectedEntries));
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        Object result = null;
        SoftReference<Object> softReference = (SoftReference) this.delegate.getObject(key);
        if (softReference != null) {
            result = softReference.get();
            if (result == null) {
                this.delegate.removeObject(key);
            } else {
                synchronized (this.hardLinksToAvoidGarbageCollection) {
                    this.hardLinksToAvoidGarbageCollection.addFirst(result);
                    if (this.hardLinksToAvoidGarbageCollection.size() > this.numberOfHardLinks) {
                        this.hardLinksToAvoidGarbageCollection.removeLast();
                    }
                }
            }
        }
        return result;
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        removeGarbageCollectedItems();
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        synchronized (this.hardLinksToAvoidGarbageCollection) {
            this.hardLinksToAvoidGarbageCollection.clear();
        }
        removeGarbageCollectedItems();
        this.delegate.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private void removeGarbageCollectedItems() {
        while (true) {
            SoftEntry sv = (SoftEntry) this.queueOfGarbageCollectedEntries.poll();
            if (sv == null) {
                return;
            } else {
                this.delegate.removeObject(sv.key);
            }
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/SoftCache$SoftEntry.class */
    private static class SoftEntry extends SoftReference<Object> {
        private final Object key;

        SoftEntry(Object key, Object value, ReferenceQueue<Object> garbageCollectionQueue) {
            super(value, garbageCollectionQueue);
            this.key = key;
        }
    }
}
