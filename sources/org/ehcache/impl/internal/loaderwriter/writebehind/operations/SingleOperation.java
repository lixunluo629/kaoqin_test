package org.ehcache.impl.internal.loaderwriter.writebehind.operations;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/operations/SingleOperation.class */
public interface SingleOperation<K, V> extends KeyBasedOperation<K> {
    void performOperation(CacheLoaderWriter<K, V> cacheLoaderWriter) throws Exception;
}
