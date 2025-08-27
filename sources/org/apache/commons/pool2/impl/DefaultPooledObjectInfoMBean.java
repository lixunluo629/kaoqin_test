package org.apache.commons.pool2.impl;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/DefaultPooledObjectInfoMBean.class */
public interface DefaultPooledObjectInfoMBean {
    long getCreateTime();

    String getCreateTimeFormatted();

    long getLastBorrowTime();

    String getLastBorrowTimeFormatted();

    String getLastBorrowTrace();

    long getLastReturnTime();

    String getLastReturnTimeFormatted();

    String getPooledObjectType();

    String getPooledObjectToString();

    long getBorrowedCount();
}
