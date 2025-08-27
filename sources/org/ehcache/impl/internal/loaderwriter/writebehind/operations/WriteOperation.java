package org.ehcache.impl.internal.loaderwriter.writebehind.operations;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/operations/WriteOperation.class */
public class WriteOperation<K, V> implements SingleOperation<K, V> {
    private final K key;
    private final V value;
    private final long creationTime;

    public WriteOperation(K k, V v) {
        this(k, v, System.nanoTime());
    }

    public WriteOperation(K k, V v, long creationTime) {
        this.key = k;
        this.value = v;
        this.creationTime = creationTime;
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.SingleOperation
    public void performOperation(CacheLoaderWriter<K, V> cacheWriter) throws Exception {
        cacheWriter.write(this.key, this.value);
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.KeyBasedOperation
    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.KeyBasedOperation
    public long getCreationTime() {
        return this.creationTime;
    }

    public int hashCode() {
        int hash = (int) getCreationTime();
        return (hash * 31) + getKey().hashCode();
    }

    public boolean equals(Object other) {
        return (other instanceof WriteOperation) && getCreationTime() == ((WriteOperation) other).getCreationTime() && getKey().equals(((WriteOperation) other).getKey());
    }
}
