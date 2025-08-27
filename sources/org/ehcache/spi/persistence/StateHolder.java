package org.ehcache.spi.persistence;

import java.util.Map;
import java.util.Set;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/persistence/StateHolder.class */
public interface StateHolder<K, V> {
    V putIfAbsent(K k, V v);

    V get(K k);

    Set<Map.Entry<K, V>> entrySet();
}
