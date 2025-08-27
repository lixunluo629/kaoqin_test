package org.ehcache.impl.internal.loaderwriter.writebehind;

import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/loaderwriter/writebehind/WriteBehind.class */
public interface WriteBehind<K, V> extends CacheLoaderWriter<K, V> {
    void start();

    void stop();

    long getQueueSize();
}
