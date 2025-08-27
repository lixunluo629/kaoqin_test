package org.apache.xmlbeans.impl.common;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/Mutex.class */
public class Mutex {
    private Thread owner = null;
    private int lock_count = 0;

    public synchronized void acquire() throws InterruptedException {
        while (!tryToAcquire()) {
            wait();
        }
    }

    public synchronized boolean tryToAcquire() {
        if (this.owner == null) {
            this.owner = Thread.currentThread();
            this.lock_count = 1;
            return true;
        }
        if (this.owner == Thread.currentThread()) {
            this.lock_count++;
            return true;
        }
        return false;
    }

    public synchronized void release() {
        if (this.owner != Thread.currentThread()) {
            throw new IllegalStateException("Thread calling release() doesn't own mutex");
        }
        int i = this.lock_count - 1;
        this.lock_count = i;
        if (i <= 0) {
            this.owner = null;
            notify();
        }
    }
}
