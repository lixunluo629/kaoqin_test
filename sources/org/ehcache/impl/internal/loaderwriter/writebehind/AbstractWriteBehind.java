package org.ehcache.impl.internal.loaderwriter.writebehind;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.DeleteOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.SingleOperation;
import org.ehcache.impl.internal.loaderwriter.writebehind.operations.WriteOperation;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;
import org.ehcache.spi.loaderwriter.CacheWritingException;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/AbstractWriteBehind.class */
abstract class AbstractWriteBehind<K, V> implements WriteBehind<K, V> {
    private final CacheLoaderWriter<K, V> cacheLoaderWriter;

    protected abstract SingleOperation<K, V> getOperation(K k);

    protected abstract void addOperation(SingleOperation<K, V> singleOperation);

    public AbstractWriteBehind(CacheLoaderWriter<K, V> cacheLoaderWriter) {
        this.cacheLoaderWriter = cacheLoaderWriter;
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public V load(K k) throws Exception {
        SingleOperation<K, V> operation = getOperation(k);
        if (operation == null) {
            return this.cacheLoaderWriter.load(k);
        }
        if (operation.getClass() == WriteOperation.class) {
            return (V) ((WriteOperation) operation).getValue();
        }
        return null;
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
    public void write(K key, V value) throws CacheWritingException {
        addOperation(new WriteOperation(key, value));
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void writeAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) throws Exception {
        for (Map.Entry<? extends K, ? extends V> entry : entries) {
            write(entry.getKey(), entry.getValue());
        }
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void delete(K key) throws CacheWritingException {
        addOperation(new DeleteOperation(key));
    }

    @Override // org.ehcache.spi.loaderwriter.CacheLoaderWriter
    public void deleteAll(Iterable<? extends K> keys) throws Exception {
        for (K k : keys) {
            delete(k);
        }
    }

    protected static <T> void putUninterruptibly(BlockingQueue<T> queue, T r) {
        boolean interrupted = false;
        while (true) {
            try {
                queue.put(r);
                break;
            } catch (InterruptedException e) {
                interrupted = true;
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
