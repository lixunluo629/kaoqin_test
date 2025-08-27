package org.apache.commons.pool2.impl;

import java.util.List;
import java.util.Map;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/GenericKeyedObjectPoolMXBean.class */
public interface GenericKeyedObjectPoolMXBean<K> {
    boolean getBlockWhenExhausted();

    boolean getFairness();

    boolean getLifo();

    int getMaxIdlePerKey();

    int getMaxTotal();

    int getMaxTotalPerKey();

    long getMaxWaitMillis();

    long getMinEvictableIdleTimeMillis();

    int getMinIdlePerKey();

    int getNumActive();

    int getNumIdle();

    int getNumTestsPerEvictionRun();

    boolean getTestOnCreate();

    boolean getTestOnBorrow();

    boolean getTestOnReturn();

    boolean getTestWhileIdle();

    long getTimeBetweenEvictionRunsMillis();

    boolean isClosed();

    Map<String, Integer> getNumActivePerKey();

    long getBorrowedCount();

    long getReturnedCount();

    long getCreatedCount();

    long getDestroyedCount();

    long getDestroyedByEvictorCount();

    long getDestroyedByBorrowValidationCount();

    long getMeanActiveTimeMillis();

    long getMeanIdleTimeMillis();

    long getMeanBorrowWaitTimeMillis();

    long getMaxBorrowWaitTimeMillis();

    String getCreationStackTrace();

    int getNumWaiters();

    Map<String, Integer> getNumWaitersByKey();

    Map<String, List<DefaultPooledObjectInfo>> listAllObjects();
}
