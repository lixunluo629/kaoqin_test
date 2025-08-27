package org.apache.commons.pool2.impl;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.pool2.BaseObjectPool;
import org.apache.commons.pool2.PoolUtils;
import org.apache.commons.pool2.PooledObjectFactory;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/SoftReferenceObjectPool.class */
public class SoftReferenceObjectPool<T> extends BaseObjectPool<T> {
    private final PooledObjectFactory<T> factory;
    private final ReferenceQueue<T> refQueue = new ReferenceQueue<>();
    private int numActive = 0;
    private long destroyCount = 0;
    private long createCount = 0;
    private final LinkedBlockingDeque<PooledSoftReference<T>> idleReferences = new LinkedBlockingDeque<>();
    private final ArrayList<PooledSoftReference<T>> allReferences = new ArrayList<>();

    public SoftReferenceObjectPool(PooledObjectFactory<T> factory) {
        this.factory = factory;
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized T borrowObject() throws Exception {
        assertOpen();
        T obj = null;
        boolean newlyCreated = false;
        PooledSoftReference<T> ref = null;
        while (null == obj) {
            if (this.idleReferences.isEmpty()) {
                if (null == this.factory) {
                    throw new NoSuchElementException();
                }
                newlyCreated = true;
                obj = this.factory.makeObject().getObject();
                this.createCount++;
                ref = new PooledSoftReference<>(new SoftReference(obj));
                this.allReferences.add(ref);
            } else {
                ref = this.idleReferences.pollFirst();
                obj = ref.getObject();
                ref.getReference().clear();
                ref.setReference(new SoftReference<>(obj));
            }
            if (null != this.factory && null != obj) {
                try {
                    this.factory.activateObject(ref);
                    if (!this.factory.validateObject(ref)) {
                        throw new Exception("ValidateObject failed");
                    }
                } catch (Throwable t) {
                    PoolUtils.checkRethrow(t);
                    try {
                        try {
                            destroy(ref);
                            obj = null;
                        } catch (Throwable t2) {
                            PoolUtils.checkRethrow(t2);
                            obj = null;
                        }
                        if (newlyCreated) {
                            throw new NoSuchElementException("Could not create a validated object, cause: " + t.getMessage());
                        }
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
        }
        this.numActive++;
        ref.allocate();
        return obj;
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized void returnObject(T obj) throws Exception {
        boolean success = !isClosed();
        PooledSoftReference<T> ref = findReference(obj);
        if (ref == null) {
            throw new IllegalStateException("Returned object not currently part of this pool");
        }
        if (this.factory != null) {
            if (!this.factory.validateObject(ref)) {
                success = false;
            } else {
                try {
                    this.factory.passivateObject(ref);
                } catch (Exception e) {
                    success = false;
                }
            }
        }
        boolean shouldDestroy = !success;
        this.numActive--;
        if (success) {
            ref.deallocate();
            this.idleReferences.add(ref);
        }
        notifyAll();
        if (shouldDestroy && this.factory != null) {
            try {
                destroy(ref);
            } catch (Exception e2) {
            }
        }
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized void invalidateObject(T obj) throws Exception {
        PooledSoftReference<T> ref = findReference(obj);
        if (ref == null) {
            throw new IllegalStateException("Object to invalidate is not currently part of this pool");
        }
        if (this.factory != null) {
            destroy(ref);
        }
        this.numActive--;
        notifyAll();
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized void addObject() throws Exception {
        assertOpen();
        if (this.factory == null) {
            throw new IllegalStateException("Cannot add objects without a factory.");
        }
        T obj = this.factory.makeObject().getObject();
        this.createCount++;
        PooledSoftReference<T> ref = new PooledSoftReference<>(new SoftReference(obj, this.refQueue));
        this.allReferences.add(ref);
        boolean success = true;
        if (!this.factory.validateObject(ref)) {
            success = false;
        } else {
            this.factory.passivateObject(ref);
        }
        boolean shouldDestroy = !success;
        if (success) {
            this.idleReferences.add(ref);
            notifyAll();
        }
        if (shouldDestroy) {
            try {
                destroy(ref);
            } catch (Exception e) {
            }
        }
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized int getNumIdle() {
        pruneClearedReferences();
        return this.idleReferences.size();
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized int getNumActive() {
        return this.numActive;
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public synchronized void clear() {
        if (null != this.factory) {
            Iterator<PooledSoftReference<T>> iter = this.idleReferences.iterator();
            while (iter.hasNext()) {
                try {
                    PooledSoftReference<T> ref = iter.next();
                    if (null != ref.getObject()) {
                        this.factory.destroyObject(ref);
                    }
                } catch (Exception e) {
                }
            }
        }
        this.idleReferences.clear();
        pruneClearedReferences();
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.ObjectPool
    public void close() {
        super.close();
        clear();
    }

    public synchronized PooledObjectFactory<T> getFactory() {
        return this.factory;
    }

    private void pruneClearedReferences() {
        removeClearedReferences(this.idleReferences.iterator());
        removeClearedReferences(this.allReferences.iterator());
        while (this.refQueue.poll() != null) {
        }
    }

    private PooledSoftReference<T> findReference(T obj) {
        Iterator<PooledSoftReference<T>> iterator = this.allReferences.iterator();
        while (iterator.hasNext()) {
            PooledSoftReference<T> reference = iterator.next();
            if (reference.getObject() != null && reference.getObject().equals(obj)) {
                return reference;
            }
        }
        return null;
    }

    private void destroy(PooledSoftReference<T> toDestroy) throws Exception {
        toDestroy.invalidate();
        this.idleReferences.remove(toDestroy);
        this.allReferences.remove(toDestroy);
        try {
            this.factory.destroyObject(toDestroy);
        } finally {
            this.destroyCount++;
            toDestroy.getReference().clear();
        }
    }

    private void removeClearedReferences(Iterator<PooledSoftReference<T>> iterator) {
        while (iterator.hasNext()) {
            PooledSoftReference<T> ref = iterator.next();
            if (ref.getReference() == null || ref.getReference().isEnqueued()) {
                iterator.remove();
            }
        }
    }

    @Override // org.apache.commons.pool2.BaseObjectPool, org.apache.commons.pool2.BaseObject
    protected void toStringAppendFields(StringBuilder builder) {
        super.toStringAppendFields(builder);
        builder.append(", factory=");
        builder.append(this.factory);
        builder.append(", refQueue=");
        builder.append(this.refQueue);
        builder.append(", numActive=");
        builder.append(this.numActive);
        builder.append(", destroyCount=");
        builder.append(this.destroyCount);
        builder.append(", createCount=");
        builder.append(this.createCount);
        builder.append(", idleReferences=");
        builder.append(this.idleReferences);
        builder.append(", allReferences=");
        builder.append(this.allReferences);
    }
}
