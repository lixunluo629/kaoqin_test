package org.ehcache.impl.internal.loaderwriter.writebehind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/StripedWriteBehind.class */
public class StripedWriteBehind<K, V> implements WriteBehind<K, V> {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = this.rwLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = this.rwLock.writeLock();
    private final List<WriteBehind<K, V>> stripes = new ArrayList();

    public StripedWriteBehind(ExecutionService executionService, String defaultThreadPool, WriteBehindConfiguration config, CacheLoaderWriter<K, V> cacheLoaderWriter) {
        int writeBehindConcurrency = config.getConcurrency();
        for (int i = 0; i < writeBehindConcurrency; i++) {
            if (config.getBatchingConfiguration() == null) {
                this.stripes.add(new NonBatchingLocalHeapWriteBehindQueue(executionService, defaultThreadPool, config, cacheLoaderWriter));
            } else {
                this.stripes.add(new BatchingLocalHeapWriteBehindQueue(executionService, defaultThreadPool, config, cacheLoaderWriter));
            }
        }
    }

    private WriteBehind<K, V> getStripe(Object key) {
        return this.stripes.get(Math.abs(key.hashCode() % this.stripes.size()));
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public void start() {
        this.writeLock.lock();
        try {
            for (WriteBehind<K, V> queue : this.stripes) {
                queue.start();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public V load(K key) throws Exception {
        this.readLock.lock();
        try {
            V v = getStripe(key).load(key);
            this.readLock.unlock();
            return v;
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public Map<K, V> loadAll(Iterable<? extends K> keys) throws Exception {
        Map<K, V> entries = new HashMap<>();
        for (K k : keys) {
            entries.put(k, load(k));
        }
        return entries;
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void write(K key, V value) throws Exception {
        this.readLock.lock();
        try {
            getStripe(key).write(key, value);
            this.readLock.unlock();
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws Exception {
        for (Map.Entry<? extends K, ? extends V> entry : entries) {
            write(entry.getKey(), entry.getValue());
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void delete(K key) throws Exception {
        this.readLock.lock();
        try {
            getStripe(key).delete(key);
            this.readLock.unlock();
        } catch (Throwable th) {
            this.readLock.unlock();
            throw th;
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void deleteAll(Iterable<? extends K> keys) throws Exception {
        for (K k : keys) {
            delete(k);
        }
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public void stop() {
        this.writeLock.lock();
        try {
            for (WriteBehind<K, V> queue : this.stripes) {
                queue.stop();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public long getQueueSize() {
        int size = 0;
        this.readLock.lock();
        try {
            for (WriteBehind<K, V> stripe : this.stripes) {
                size = (int) (size + stripe.getQueueSize());
            }
            return size;
        } finally {
            this.readLock.unlock();
        }
    }
}
