package org.apache.xmlbeans.impl.common;

/* loaded from: xmlbeans-3.1.0.jar:org/apache/xmlbeans/impl/common/GlobalLock.class */
public class GlobalLock {
    private static final Mutex GLOBAL_MUTEX = new Mutex();

    public static void acquire() throws InterruptedException {
        GLOBAL_MUTEX.acquire();
    }

    public static void tryToAcquire() {
        GLOBAL_MUTEX.tryToAcquire();
    }

    public static void release() {
        GLOBAL_MUTEX.release();
    }
}
