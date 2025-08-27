package org.apache.commons.pool2.impl;

import java.lang.ref.SoftReference;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/PooledSoftReference.class */
public class PooledSoftReference<T> extends DefaultPooledObject<T> {
    private volatile SoftReference<T> reference;

    public PooledSoftReference(SoftReference<T> reference) {
        super(null);
        this.reference = reference;
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObject, org.apache.commons.pool2.PooledObject
    public T getObject() {
        return this.reference.get();
    }

    @Override // org.apache.commons.pool2.impl.DefaultPooledObject, org.apache.commons.pool2.PooledObject
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Referenced Object: ");
        result.append(getObject().toString());
        result.append(", State: ");
        synchronized (this) {
            result.append(getState().toString());
        }
        return result.toString();
    }

    public synchronized SoftReference<T> getReference() {
        return this.reference;
    }

    public synchronized void setReference(SoftReference<T> reference) {
        this.reference = reference;
    }
}
