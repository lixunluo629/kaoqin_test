package org.apache.commons.pool2.impl;

import java.util.Collection;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/InterruptibleReentrantLock.class */
class InterruptibleReentrantLock extends ReentrantLock {
    private static final long serialVersionUID = 1;

    public InterruptibleReentrantLock(boolean fairness) {
        super(fairness);
    }

    public void interruptWaiters(Condition condition) {
        Collection<Thread> threads = getWaitingThreads(condition);
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
