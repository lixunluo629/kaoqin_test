package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PooledObjectFactory.class */
public interface PooledObjectFactory<T> {
    PooledObject<T> makeObject() throws Exception;

    void destroyObject(PooledObject<T> pooledObject) throws Exception;

    boolean validateObject(PooledObject<T> pooledObject);

    void activateObject(PooledObject<T> pooledObject) throws Exception;

    void passivateObject(PooledObject<T> pooledObject) throws Exception;
}
