package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/KeyedObjectPool.class */
public interface KeyedObjectPool<K, V> {
    V borrowObject(K k) throws Exception;

    void returnObject(K k, V v) throws Exception;

    void invalidateObject(K k, V v) throws Exception;

    void addObject(K k) throws Exception;

    int getNumIdle(K k);

    int getNumActive(K k);

    int getNumIdle();

    int getNumActive();

    void clear() throws Exception;

    void clear(K k) throws Exception;

    void close();
}
