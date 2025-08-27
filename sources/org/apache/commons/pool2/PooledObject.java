package org.apache.commons.pool2;

import java.io.PrintWriter;
import java.util.Deque;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/PooledObject.class */
public interface PooledObject<T> extends Comparable<PooledObject<T>> {
    T getObject();

    long getCreateTime();

    long getActiveTimeMillis();

    long getIdleTimeMillis();

    long getLastBorrowTime();

    long getLastReturnTime();

    long getLastUsedTime();

    int compareTo(PooledObject<T> pooledObject);

    boolean equals(Object obj);

    int hashCode();

    String toString();

    boolean startEvictionTest();

    boolean endEvictionTest(Deque<PooledObject<T>> deque);

    boolean allocate();

    boolean deallocate();

    void invalidate();

    void setLogAbandoned(boolean z);

    void use();

    void printStackTrace(PrintWriter printWriter);

    PooledObjectState getState();

    void markAbandoned();

    void markReturning();
}
