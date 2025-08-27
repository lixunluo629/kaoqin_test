package org.ehcache.impl.internal.loaderwriter.writebehind.operations;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/operations/DeleteOperation.class */
public class DeleteOperation<K, V> implements SingleOperation<K, V> {
    private final K key;
    private final long creationTime;

    public DeleteOperation(K key) {
        this(key, System.nanoTime());
    }

    public DeleteOperation(K key, long creationTime) {
        this.key = key;
        this.creationTime = creationTime;
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.SingleOperation
    public void performOperation(CacheLoaderWriter<K, V> cacheLoaderWriter) throws Exception {
        cacheLoaderWriter.delete(this.key);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.KeyBasedOperation
    public K getKey() {
        return this.key;
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.KeyBasedOperation
    public long getCreationTime() {
        return this.creationTime;
    }

    public int hashCode() {
        return getKey().hashCode();
    }

    public boolean equals(Object other) {
        return (other instanceof DeleteOperation) && getCreationTime() == ((DeleteOperation) other).getCreationTime() && getKey().equals(((DeleteOperation) other).getKey());
    }
}
