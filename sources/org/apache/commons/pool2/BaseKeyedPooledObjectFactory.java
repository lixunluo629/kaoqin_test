package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/BaseKeyedPooledObjectFactory.class */
public abstract class BaseKeyedPooledObjectFactory<K, V> extends BaseObject implements KeyedPooledObjectFactory<K, V> {
    public abstract V create(K k) throws Exception;

    public abstract PooledObject<V> wrap(V v);

    @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
    public PooledObject<V> makeObject(K key) throws Exception {
        return wrap(create(key));
    }

    @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
    public void destroyObject(K key, PooledObject<V> p) throws Exception {
    }

    @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
    public boolean validateObject(K key, PooledObject<V> p) {
        return true;
    }

    @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
    public void activateObject(K key, PooledObject<V> p) throws Exception {
    }

    @Override // org.apache.commons.pool2.KeyedPooledObjectFactory
    public void passivateObject(K key, PooledObject<V> p) throws Exception {
    }
}
