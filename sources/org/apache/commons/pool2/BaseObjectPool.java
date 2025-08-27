package org.apache.commons.pool2;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/BaseObjectPool.class */
public abstract class BaseObjectPool<T> extends BaseObject implements ObjectPool<T> {
    private volatile boolean closed = false;

    @Override // org.apache.commons.pool2.ObjectPool
    public abstract T borrowObject() throws Exception;

    @Override // org.apache.commons.pool2.ObjectPool
    public abstract void returnObject(T t) throws Exception;

    @Override // org.apache.commons.pool2.ObjectPool
    public abstract void invalidateObject(T t) throws Exception;

    @Override // org.apache.commons.pool2.ObjectPool
    public int getNumIdle() {
        return -1;
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public int getNumActive() {
        return -1;
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void clear() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void addObject() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override // org.apache.commons.pool2.ObjectPool
    public void close() {
        this.closed = true;
    }

    public final boolean isClosed() {
        return this.closed;
    }

    protected final void assertOpen() throws IllegalStateException {
        if (isClosed()) {
            throw new IllegalStateException("Pool not open");
        }
    }

    @Override // org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        builder.append("closed=");
        builder.append(this.closed);
    }
}
