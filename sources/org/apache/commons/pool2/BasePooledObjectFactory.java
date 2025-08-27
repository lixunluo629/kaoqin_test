package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/BasePooledObjectFactory.class */
public abstract class BasePooledObjectFactory<T> extends BaseObject implements PooledObjectFactory<T> {
    public abstract T create() throws Exception;

    public abstract PooledObject<T> wrap(T t);

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public PooledObject<T> makeObject() throws Exception {
        return wrap(create());
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public void destroyObject(PooledObject<T> p) throws Exception {
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public boolean validateObject(PooledObject<T> p) {
        return true;
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public void activateObject(PooledObject<T> p) throws Exception {
    }

    @Override // org.apache.commons.pool2.PooledObjectFactory
    public void passivateObject(PooledObject<T> p) throws Exception {
    }
}
