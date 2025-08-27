package org.apache.ibatis.cache.decorators;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/LruCache.class */
public class LruCache implements Cache {
    private final Cache delegate;
    private Map<Object, Object> keyMap;
    private Object eldestKey;

    public LruCache(Cache delegate) {
        this.delegate = delegate;
        setSize(1024);
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.delegate.getId();
    }

    @Override // org.apache.ibatis.cache.Cache
    public int getSize() {
        return this.delegate.getSize();
    }

    public void setSize(final int size) {
        this.keyMap = new LinkedHashMap<Object, Object>(size, 0.75f, true) { // from class: org.apache.ibatis.cache.decorators.LruCache.1
            private static final long serialVersionUID = 4267176411845948333L;

            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
                boolean tooBig = size() > size;
                if (tooBig) {
                    LruCache.this.eldestKey = eldest.getKey();
                }
                return tooBig;
            }
        };
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object value) {
        this.delegate.putObject(key, value);
        cycleKeyList(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        this.keyMap.get(key);
        return this.delegate.getObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.delegate.clear();
        this.keyMap.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    private void cycleKeyList(Object key) {
        this.keyMap.put(key, key);
        if (this.eldestKey != null) {
            this.delegate.removeObject(this.eldestKey);
            this.eldestKey = null;
        }
    }
}
