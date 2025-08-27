package org.apache.ibatis.cache.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/impl/PerpetualCache.class */
public class PerpetualCache implements Cache {
    private final String id;
    private Map<Object, Object> cache = new HashMap();

    public PerpetualCache(String id) {
        this.id = id;
    }

    @Override // org.apache.ibatis.cache.Cache
    public String getId() {
        return this.id;
    }

    @Override // org.apache.ibatis.cache.Cache
    public int getSize() {
        return this.cache.size();
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object value) {
        this.cache.put(key, value);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        return this.cache.get(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        return this.cache.remove(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.cache.clear();
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    public boolean equals(Object o) {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cache)) {
            return false;
        }
        Cache otherCache = (Cache) o;
        return getId().equals(otherCache.getId());
    }

    public int hashCode() {
        if (getId() == null) {
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }
}
