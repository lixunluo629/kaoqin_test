package org.apache.commons.pool2.impl;

import java.io.PrintWriter;
import java.util.Deque;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectState;
import org.apache.commons.pool2.TrackedUse;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/DefaultPooledObject.class */
public class DefaultPooledObject<T> implements PooledObject<T> {
    private final T object;
    private PooledObjectState state = PooledObjectState.IDLE;
    private final long createTime = System.currentTimeMillis();
    private volatile long lastBorrowTime = this.createTime;
    private volatile long lastUseTime = this.createTime;
    private volatile long lastReturnTime = this.createTime;
    private volatile boolean logAbandoned = false;
    private final CallStack borrowedBy = CallStackUtils.newCallStack("'Pooled object created' yyyy-MM-dd HH:mm:ss Z 'by the following code has not been returned to the pool:'", true);
    private final CallStack usedBy = CallStackUtils.newCallStack("The last code to use this object was:", false);
    private volatile long borrowedCount = 0;

    public DefaultPooledObject(T object) {
        this.object = object;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public T getObject() {
        return this.object;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public long getCreateTime() {
        return this.createTime;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public long getActiveTimeMillis() {
        long rTime = this.lastReturnTime;
        long bTime = this.lastBorrowTime;
        if (rTime > bTime) {
            return rTime - bTime;
        }
        return System.currentTimeMillis() - bTime;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public long getIdleTimeMillis() {
        long elapsed = System.currentTimeMillis() - this.lastReturnTime;
        if (elapsed >= 0) {
            return elapsed;
        }
        return 0L;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public long getLastBorrowTime() {
        return this.lastBorrowTime;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public long getLastReturnTime() {
        return this.lastReturnTime;
    }

    public long getBorrowedCount() {
        return this.borrowedCount;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public long getLastUsedTime() {
        if (this.object instanceof TrackedUse) {
            return Math.max(((TrackedUse) this.object).getLastUsed(), this.lastUseTime);
        }
        return this.lastUseTime;
    }

    @Override // java.lang.Comparable
    public int compareTo(PooledObject<T> other) {
        long lastActiveDiff = getLastReturnTime() - other.getLastReturnTime();
        if (lastActiveDiff == 0) {
            return System.identityHashCode(this) - System.identityHashCode(other);
        }
        return (int) Math.min(Math.max(lastActiveDiff, -2147483648L), 2147483647L);
    }

    @Override // org.apache.commons.pool2.PooledObject
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Object: ");
        result.append(this.object.toString());
        result.append(", State: ");
        synchronized (this) {
            result.append(this.state.toString());
        }
        return result.toString();
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized boolean startEvictionTest() {
        if (this.state == PooledObjectState.IDLE) {
            this.state = PooledObjectState.EVICTION;
            return true;
        }
        return false;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized boolean endEvictionTest(Deque<PooledObject<T>> idleQueue) {
        if (this.state == PooledObjectState.EVICTION) {
            this.state = PooledObjectState.IDLE;
            return true;
        }
        if (this.state == PooledObjectState.EVICTION_RETURN_TO_HEAD) {
            this.state = PooledObjectState.IDLE;
            if (!idleQueue.offerFirst(this)) {
            }
            return false;
        }
        return false;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized boolean allocate() {
        if (this.state == PooledObjectState.IDLE) {
            this.state = PooledObjectState.ALLOCATED;
            this.lastBorrowTime = System.currentTimeMillis();
            this.lastUseTime = this.lastBorrowTime;
            this.borrowedCount++;
            if (this.logAbandoned) {
                this.borrowedBy.fillInStackTrace();
                return true;
            }
            return true;
        }
        if (this.state == PooledObjectState.EVICTION) {
            this.state = PooledObjectState.EVICTION_RETURN_TO_HEAD;
            return false;
        }
        return false;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized boolean deallocate() {
        if (this.state == PooledObjectState.ALLOCATED || this.state == PooledObjectState.RETURNING) {
            this.state = PooledObjectState.IDLE;
            this.lastReturnTime = System.currentTimeMillis();
            this.borrowedBy.clear();
            return true;
        }
        return false;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized void invalidate() {
        this.state = PooledObjectState.INVALID;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public void use() {
        this.lastUseTime = System.currentTimeMillis();
        this.usedBy.fillInStackTrace();
    }

    @Override // org.apache.commons.pool2.PooledObject
    public void printStackTrace(PrintWriter writer) {
        boolean written = this.borrowedBy.printStackTrace(writer);
        if (written | this.usedBy.printStackTrace(writer)) {
            writer.flush();
        }
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized PooledObjectState getState() {
        return this.state;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized void markAbandoned() {
        this.state = PooledObjectState.ABANDONED;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public synchronized void markReturning() {
        this.state = PooledObjectState.RETURNING;
    }

    @Override // org.apache.commons.pool2.PooledObject
    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }
}
