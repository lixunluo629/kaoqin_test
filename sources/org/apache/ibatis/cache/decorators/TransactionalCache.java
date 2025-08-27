package org.apache.ibatis.cache.decorators;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/decorators/TransactionalCache.class */
public class TransactionalCache implements Cache {
    private static final Log log = LogFactory.getLog((Class<?>) TransactionalCache.class);
    private final Cache delegate;
    private boolean clearOnCommit = false;
    private final Map<Object, Object> entriesToAddOnCommit = new HashMap();
    private final Set<Object> entriesMissedInCache = new HashSet();

    public TransactionalCache(Cache delegate) {
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
    public Object getObject(Object key) {
        Object object = this.delegate.getObject(key);
        if (object == null) {
            this.entriesMissedInCache.add(key);
        }
        if (this.clearOnCommit) {
            return null;
        }
        return object;
    }

    @Override // org.apache.ibatis.cache.Cache
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    @Override // org.apache.ibatis.cache.Cache
    public void putObject(Object key, Object object) {
        this.entriesToAddOnCommit.put(key, object);
    }

    @Override // org.apache.ibatis.cache.Cache
    public Object removeObject(Object key) {
        return null;
    }

    @Override // org.apache.ibatis.cache.Cache
    public void clear() {
        this.clearOnCommit = true;
        this.entriesToAddOnCommit.clear();
    }

    public void commit() {
        if (this.clearOnCommit) {
            this.delegate.clear();
        }
        flushPendingEntries();
        reset();
    }

    public void rollback() {
        unlockMissedEntries();
        reset();
    }

    private void reset() {
        this.clearOnCommit = false;
        this.entriesToAddOnCommit.clear();
        this.entriesMissedInCache.clear();
    }

    private void flushPendingEntries() {
        for (Map.Entry<Object, Object> entry : this.entriesToAddOnCommit.entrySet()) {
            this.delegate.putObject(entry.getKey(), entry.getValue());
        }
        for (Object entry2 : this.entriesMissedInCache) {
            if (!this.entriesToAddOnCommit.containsKey(entry2)) {
                this.delegate.putObject(entry2, null);
            }
        }
    }

    private void unlockMissedEntries() {
        for (Object entry : this.entriesMissedInCache) {
            try {
                this.delegate.removeObject(entry);
            } catch (Exception e) {
                log.warn("Unexpected exception while notifiying a rollback to the cache adapter.Consider upgrading your cache adapter to the latest version.  Cause: " + e);
            }
        }
    }
}
