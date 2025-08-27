package org.ehcache.impl.internal.loaderwriter.writebehind.operations;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/operations/DeleteAllOperation.class */
public class DeleteAllOperation<K, V> implements BatchOperation<K, V> {
    private final Iterable<? extends K> entries;

    public DeleteAllOperation(Iterable<? extends K> entries) {
        this.entries = entries;
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.BatchOperation
    public void performOperation(CacheLoaderWriter<K, V> cacheLoaderWriter) throws Exception {
        cacheLoaderWriter.deleteAll(this.entries);
    }
}
