package org.ehcache.impl.internal.loaderwriter.writebehind;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.internal.executor.ExecutorUtil;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.SingleOperation;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/NonBatchingLocalHeapWriteBehindQueue.class */
public class NonBatchingLocalHeapWriteBehindQueue<K, V> extends AbstractWriteBehind<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) NonBatchingLocalHeapWriteBehindQueue.class);
    private final CacheLoaderWriter<K, V> cacheLoaderWriter;
    private final ConcurrentMap<K, SingleOperation<K, V>> latest;
    private final BlockingQueue<Runnable> executorQueue;
    private final ExecutorService executor;

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind, org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public /* bridge */ /* synthetic */ void deleteAll(Iterable x0) throws Exception {
        super.deleteAll(x0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind, org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public /* bridge */ /* synthetic */ void delete(Object obj) throws CacheWritingException {
        super.delete(obj);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind, org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public /* bridge */ /* synthetic */ void writeAll(Iterable x0) throws Exception {
        super.writeAll(x0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind, org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public /* bridge */ /* synthetic */ void write(Object obj, Object obj2) throws CacheWritingException {
        super.write(obj, obj2);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind, org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public /* bridge */ /* synthetic */ Map loadAll(Iterable x0) throws Exception {
        return super.loadAll(x0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind, org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public /* bridge */ /* synthetic */ Object load(Object obj) throws Exception {
        return super.load(obj);
    }

    public NonBatchingLocalHeapWriteBehindQueue(ExecutionService executionService, String defaultThreadPool, WriteBehindConfiguration config, CacheLoaderWriter<K, V> cacheLoaderWriter) {
        super(cacheLoaderWriter);
        this.latest = new ConcurrentHashMap();
        this.cacheLoaderWriter = cacheLoaderWriter;
        this.executorQueue = new LinkedBlockingQueue(config.getMaxQueueSize());
        if (config.getThreadPoolAlias() == null) {
            this.executor = executionService.getOrderedExecutor(defaultThreadPool, this.executorQueue);
        } else {
            this.executor = executionService.getOrderedExecutor(config.getThreadPoolAlias(), this.executorQueue);
        }
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind
    protected SingleOperation<K, V> getOperation(K key) {
        return this.latest.get(key);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind
    protected void addOperation(final SingleOperation<K, V> operation) {
        this.latest.put(operation.getKey(), operation);
        submit(new Runnable() { // from class: org.ehcache.impl.internal.loaderwriter.writebehind.NonBatchingLocalHeapWriteBehindQueue.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    try {
                        operation.performOperation(NonBatchingLocalHeapWriteBehindQueue.this.cacheLoaderWriter);
                        NonBatchingLocalHeapWriteBehindQueue.this.latest.remove(operation.getKey(), operation);
                    } catch (Exception e) {
                        NonBatchingLocalHeapWriteBehindQueue.LOGGER.warn("Exception while processing key '{}' write behind queue : {}", operation.getKey(), e);
                        NonBatchingLocalHeapWriteBehindQueue.this.latest.remove(operation.getKey(), operation);
                    }
                } catch (Throwable th) {
                    NonBatchingLocalHeapWriteBehindQueue.this.latest.remove(operation.getKey(), operation);
                    throw th;
                }
            }
        });
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public void start() {
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public void stop() {
        ExecutorUtil.shutdown(this.executor);
    }

    private void submit(Runnable operation) {
        this.executor.submit(operation);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public long getQueueSize() {
        return this.executorQueue.size();
    }
}
