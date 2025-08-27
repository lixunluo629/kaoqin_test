package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/ObjectPool.class */
public interface ObjectPool<T> {
    T borrowObject() throws Exception;

    void returnObject(T t) throws Exception;

    void invalidateObject(T t) throws Exception;

    void addObject() throws Exception;

    int getNumIdle();

    int getNumActive();

    void clear() throws Exception;

    void close();
}
