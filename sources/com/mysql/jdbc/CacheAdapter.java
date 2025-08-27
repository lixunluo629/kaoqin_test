package com.mysql.jdbc;

import java.util.Set;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/CacheAdapter.class */
public interface CacheAdapter<K, V> {
    V get(K k);

    void put(K k, V v);

    void invalidate(K k);

    void invalidateAll(Set<K> set);

    void invalidateAll();
}
