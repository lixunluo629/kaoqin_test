package org.ehcache.impl.internal.loaderwriter.writebehind;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.impl.internal.concurrent.ConcurrentHashMap;
import org.ehcache.impl.internal.executor.ExecutorUtil;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.BatchOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.DeleteAllOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.DeleteOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.SingleOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.WriteAllOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.WriteOperation;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheWritingException;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/BatchingLocalHeapWriteBehindQueue.class */
public class BatchingLocalHeapWriteBehindQueue<K, V> extends AbstractWriteBehind<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) BatchingLocalHeapWriteBehindQueue.class);
    private final CacheLoaderWriter<K, V> cacheLoaderWriter;
    private final ConcurrentMap<K, SingleOperation<K, V>> latest;
    private final BlockingQueue<Runnable> executorQueue;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final long maxWriteDelayMs;
    private final int batchSize;
    private final boolean coalescing;
    private volatile BatchingLocalHeapWriteBehindQueue<K, V>.Batch openBatch;

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

    public BatchingLocalHeapWriteBehindQueue(ExecutionService executionService, String defaultThreadPool, WriteBehindConfiguration config, CacheLoaderWriter<K, V> cacheLoaderWriter) {
        super(cacheLoaderWriter);
        this.latest = new ConcurrentHashMap();
        this.cacheLoaderWriter = cacheLoaderWriter;
        WriteBehindConfiguration.BatchingConfiguration batchingConfig = config.getBatchingConfiguration();
        this.maxWriteDelayMs = batchingConfig.getMaxDelayUnit().toMillis(batchingConfig.getMaxDelay());
        this.batchSize = batchingConfig.getBatchSize();
        this.coalescing = batchingConfig.isCoalescing();
        this.executorQueue = new LinkedBlockingQueue(config.getMaxQueueSize() / this.batchSize);
        if (config.getThreadPoolAlias() == null) {
            this.executor = executionService.getOrderedExecutor(defaultThreadPool, this.executorQueue);
        } else {
            this.executor = executionService.getOrderedExecutor(config.getThreadPoolAlias(), this.executorQueue);
        }
        if (config.getThreadPoolAlias() == null) {
            this.scheduledExecutor = executionService.getScheduledExecutor(defaultThreadPool);
        } else {
            this.scheduledExecutor = executionService.getScheduledExecutor(config.getThreadPoolAlias());
        }
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind
    protected SingleOperation<K, V> getOperation(K key) {
        return this.latest.get(key);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.AbstractWriteBehind
    protected void addOperation(SingleOperation<K, V> operation) {
        this.latest.put(operation.getKey(), operation);
        synchronized (this) {
            if (this.openBatch == null) {
                this.openBatch = newBatch();
            }
            if (this.openBatch.add(operation)) {
                submit(this.openBatch);
                this.openBatch = null;
            }
        }
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public void start() {
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public void stop() {
        try {
            try {
                synchronized (this) {
                    if (this.openBatch != null) {
                        ExecutorUtil.waitFor(submit(this.openBatch));
                        this.openBatch = null;
                    }
                }
            } catch (ExecutionException e) {
                LOGGER.error("Exception running batch on shutdown", (Throwable) e);
                ExecutorUtil.shutdownNow(this.scheduledExecutor);
                ExecutorUtil.shutdown(this.executor);
            }
        } finally {
            ExecutorUtil.shutdownNow(this.scheduledExecutor);
            ExecutorUtil.shutdown(this.executor);
        }
    }

    private BatchingLocalHeapWriteBehindQueue<K, V>.Batch newBatch() {
        if (this.coalescing) {
            return new CoalescingBatch(this.batchSize);
        }
        return new SimpleBatch(this.batchSize);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Future<?> submit(BatchingLocalHeapWriteBehindQueue<K, V>.Batch batch) {
        return this.executor.submit(batch);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.WriteBehind
    public long getQueueSize() {
        BatchingLocalHeapWriteBehindQueue<K, V>.Batch snapshot = this.openBatch;
        return (this.executorQueue.size() * this.batchSize) + (snapshot == null ? 0 : snapshot.size());
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/BatchingLocalHeapWriteBehindQueue$Batch.class */
    abstract class Batch implements Runnable {
        private final int batchSize;
        private final ScheduledFuture<?> expireTask;

        protected abstract void internalAdd(SingleOperation<K, V> singleOperation);

        protected abstract Iterable<SingleOperation<K, V>> operations();

        protected abstract int size();

        Batch(int size) {
            this.batchSize = size;
            this.expireTask = BatchingLocalHeapWriteBehindQueue.this.scheduledExecutor.schedule(new Runnable() { // from class: org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch.1
                @Override // java.lang.Runnable
                public void run() {
                    synchronized (BatchingLocalHeapWriteBehindQueue.this) {
                        if (BatchingLocalHeapWriteBehindQueue.this.openBatch == Batch.this) {
                            BatchingLocalHeapWriteBehindQueue.this.submit(BatchingLocalHeapWriteBehindQueue.this.openBatch);
                            BatchingLocalHeapWriteBehindQueue.this.openBatch = null;
                        }
                    }
                }
            }, BatchingLocalHeapWriteBehindQueue.this.maxWriteDelayMs, TimeUnit.MILLISECONDS);
        }

        public boolean add(SingleOperation<K, V> operation) {
            internalAdd(operation);
            return size() >= this.batchSize;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                List<BatchOperation<K, V>> batches = BatchingLocalHeapWriteBehindQueue.createMonomorphicBatches(operations());
                for (BatchOperation<K, V> batch : batches) {
                    try {
                        batch.performOperation(BatchingLocalHeapWriteBehindQueue.this.cacheLoaderWriter);
                    } catch (Exception e) {
                        BatchingLocalHeapWriteBehindQueue.LOGGER.warn("Exception while bulk processing in write behind queue", (Throwable) e);
                    }
                }
                try {
                    for (SingleOperation<K, V> op : operations()) {
                        BatchingLocalHeapWriteBehindQueue.this.latest.remove(op.getKey(), op);
                    }
                    BatchingLocalHeapWriteBehindQueue.LOGGER.debug("Cancelling batch expiry task");
                    this.expireTask.cancel(false);
                } finally {
                }
            } catch (Throwable th) {
                try {
                    for (SingleOperation<K, V> op2 : operations()) {
                        BatchingLocalHeapWriteBehindQueue.this.latest.remove(op2.getKey(), op2);
                    }
                    BatchingLocalHeapWriteBehindQueue.LOGGER.debug("Cancelling batch expiry task");
                    this.expireTask.cancel(false);
                    throw th;
                } finally {
                }
            }
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/BatchingLocalHeapWriteBehindQueue$SimpleBatch.class */
    private class SimpleBatch extends Batch {
        private final List<SingleOperation<K, V>> operations;

        SimpleBatch(int size) {
            super(size);
            this.operations = new ArrayList(size);
        }

        @Override // org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch
        public void internalAdd(SingleOperation<K, V> operation) {
            this.operations.add(operation);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch
        public List<SingleOperation<K, V>> operations() {
            return this.operations;
        }

        @Override // org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch
        protected int size() {
            return this.operations.size();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/BatchingLocalHeapWriteBehindQueue$CoalescingBatch.class */
    private class CoalescingBatch extends Batch {
        private final LinkedHashMap<K, SingleOperation<K, V>> operations;

        public CoalescingBatch(int size) {
            super(size);
            this.operations = new LinkedHashMap<>(size);
        }

        @Override // org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch
        public void internalAdd(SingleOperation<K, V> operation) {
            this.operations.put(operation.getKey(), operation);
        }

        @Override // org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch
        protected Iterable<SingleOperation<K, V>> operations() {
            return this.operations.values();
        }

        @Override // org.ehcache.impl.internal.loaderwriter.writebehind.BatchingLocalHeapWriteBehindQueue.Batch
        protected int size() {
            return this.operations.size();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <K, V> List<BatchOperation<K, V>> createMonomorphicBatches(Iterable<SingleOperation<K, V>> batch) {
        List<BatchOperation<K, V>> closedBatches = new ArrayList<>();
        Set<K> activeDeleteKeys = new HashSet<>();
        Set<K> activeWrittenKeys = new HashSet<>();
        List<K> activeDeleteBatch = new ArrayList<>();
        List<Map.Entry<K, V>> activeWriteBatch = new ArrayList<>();
        for (SingleOperation<K, V> item : batch) {
            if (item instanceof WriteOperation) {
                if (activeDeleteKeys.contains(item.getKey())) {
                    closedBatches.add(new DeleteAllOperation<>(activeDeleteBatch));
                    activeDeleteBatch = new ArrayList<>();
                    activeDeleteKeys = new HashSet<>();
                }
                activeWriteBatch.add(new AbstractMap.SimpleEntry<>(item.getKey(), ((WriteOperation) item).getValue()));
                activeWrittenKeys.add(item.getKey());
            } else if (item instanceof DeleteOperation) {
                if (activeWrittenKeys.contains(item.getKey())) {
                    closedBatches.add(new WriteAllOperation<>(activeWriteBatch));
                    activeWriteBatch = new ArrayList<>();
                    activeWrittenKeys = new HashSet<>();
                }
                activeDeleteBatch.add(item.getKey());
                activeDeleteKeys.add(item.getKey());
            } else {
                throw new AssertionError();
            }
        }
        if (!activeWriteBatch.isEmpty()) {
            closedBatches.add(new WriteAllOperation<>(activeWriteBatch));
        }
        if (!activeDeleteBatch.isEmpty()) {
            closedBatches.add(new DeleteAllOperation<>(activeDeleteBatch));
        }
        return closedBatches;
    }
}
