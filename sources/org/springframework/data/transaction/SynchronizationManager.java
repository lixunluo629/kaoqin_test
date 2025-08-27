package org.springframework.data.transaction;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/transaction/SynchronizationManager.class */
interface SynchronizationManager {
    void initSynchronization();

    boolean isSynchronizationActive();

    void clearSynchronization();
}
