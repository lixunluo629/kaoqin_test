package org.apache.ibatis.cache.decorators;

import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/LoggingCache.class */
public class LoggingCache implements Cache {
    private final Cache delegate;
    protected int requests = 0;
    protected int hits = 0;
    private final Log log = LogFactory.getLog(getId());

    public LoggingCache(Cache delegate) {
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
    public void putObject(Object key, Object object) {
        this.delegate.putObject(key, object);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object getObject(Object key) {
        this.requests++;
        Object value = this.delegate.getObject(key);
        if (value != null) {
            this.hits++;
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Cache Hit Ratio [" + getId() + "]: " + getHitRatio());
        }
        return value;
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        return this.delegate.removeObject(key);
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
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

    private double getHitRatio() {
        return this.hits / this.requests;
    }
}
