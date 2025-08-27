package org.ehcache.impl.internal.loaderwriter.writebehind.operations;

import java.util.Map;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/operations/WriteAllOperation.class */
public class WriteAllOperation<K, V> implements BatchOperation<K, V> {
    private final Iterable<? extends Map.Entry<? extends K, ? extends V>> entries;

    public WriteAllOperation(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
        this.entries = entries;
    }

    @Override // org.ehcache.impl.internal.loaderwriter.writebehind.operations.BatchOperation
    public void performOperation(CacheLoaderWriter<K, V> cacheLoaderWriter) throws Exception {
        cacheLoaderWriter.writeAll(this.entries);
    }
}
