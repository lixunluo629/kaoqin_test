package org.ehcache.jsr107.internal;

import java.util.Map;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/internal/Jsr107CacheLoaderWriter.class */
public interface Jsr107CacheLoaderWriter<K, V> extends CacheLoaderWriter<K, V> {
    Map<K, V> loadAllAlways(Iterable<? extends K> iterable) throws Exception;
}
