package org.apache.ibatis.cache.decorators;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/WeakCache.class */
public class WeakCache implements Cache {
    private final Cache delegate;
    private int numberOfHardLinks = 256;
    private final Deque<Object> hardLinksToAvoidGarbageCollection = new LinkedList();
    private final ReferenceQueue<Object> queueOfGarbageCollectedEntries = new ReferenceQueue<>();

    public WeakCache(Cache delegate) {
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
        this.delegate.putObject(key, new WeakEntry(key, value, this.queueOfGarbageCollectedEntries));
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        Object result = null;
        WeakReference<Object> weakReference = (WeakReference) this.delegate.getObject(key);
        if (weakReference != null) {
            result = weakReference.get();
            if (result == null) {
                this.delegate.removeObject(key);
            } else {
                this.hardLinksToAvoidGarbageCollection.addFirst(result);
                if (this.hardLinksToAvoidGarbageCollection.size() > this.numberOfHardLinks) {
                    this.hardLinksToAvoidGarbageCollection.removeLast();
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
        this.hardLinksToAvoidGarbageCollection.clear();
        removeGarbageCollectedItems();
        this.delegate.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private void removeGarbageCollectedItems() {
        while (true) {
            WeakEntry sv = (WeakEntry) this.queueOfGarbageCollectedEntries.poll();
            if (sv == null) {
                return;
            } else {
                this.delegate.removeObject(sv.key);
            }
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/WeakCache$WeakEntry.class */
    private static class WeakEntry extends WeakReference<Object> {
        private final Object key;

        private WeakEntry(Object key, Object value, ReferenceQueue<Object> garbageCollectionQueue) {
            super(value, garbageCollectionQueue);
            this.key = key;
        }
    }
}
