package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/KeyedPooledObjectFactory.class */
public interface KeyedPooledObjectFactory<K, V> {
    PooledObject<V> makeObject(K k) throws Exception;

    void destroyObject(K k, PooledObject<V> pooledObject) throws Exception;

    boolean validateObject(K k, PooledObject<V> pooledObject);

    void activateObject(K k, PooledObject<V> pooledObject) throws Exception;

    void passivateObject(K k, PooledObject<V> pooledObject) throws Exception;
}
