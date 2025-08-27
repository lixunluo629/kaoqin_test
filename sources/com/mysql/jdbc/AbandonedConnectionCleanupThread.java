package com.mysql.jdbc;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/AbandonedConnectionCleanupThread.class */
public class AbandonedConnectionCleanupThread implements Runnable {
    private static final ReferenceQueue<MySQLConnection> referenceQueue = new ReferenceQueue<>();
    static Thread threadRef = null;
    private static Lock threadRefLock = new ReentrantLock();
    private static final Map<ConnectionFinalizerPhantomReference, ConnectionFinalizerPhantomReference> connectionFinalizerPhantomRefs = new ConcurrentHashMap();
    private static final ExecutorService cleanupThreadExcecutorService = Executors.newSingleThreadExecutor(new ThreadFactory() { // from class: com.mysql.jdbc.AbandonedConnectionCleanupThread.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "mysql-cj-abandoned-connection-cleanup");
            t.setDaemon(true);
            ClassLoader classLoader = AbandonedConnectionCleanupThread.class.getClassLoader();
            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            t.setContextClassLoader(classLoader);
            AbandonedConnectionCleanupThread.threadRef = t;
            return t;
        }
    });

    static {
        cleanupThreadExcecutorService.execute(new AbandonedConnectionCleanupThread());
    }

    private AbandonedConnectionCleanupThread() {
    }

    @Override // java.lang.Runnable
    public void run() throws InterruptedException, IllegalArgumentException {
        while (true) {
            try {
                checkThreadContextClassLoader();
                Reference<? extends MySQLConnection> reference = referenceQueue.remove(5000L);
                if (reference != null) {
                    finalizeResource((ConnectionFinalizerPhantomReference) reference);
                }
            } catch (InterruptedException e) {
                threadRefLock.lock();
                try {
                    threadRef = null;
                    while (true) {
                        Reference<? extends MySQLConnection> reference2 = referenceQueue.poll();
                        if (reference2 != null) {
                            finalizeResource((ConnectionFinalizerPhantomReference) reference2);
                        } else {
                            connectionFinalizerPhantomRefs.clear();
                            return;
                        }
                    }
                } finally {
                    threadRefLock.unlock();
                }
            } catch (Exception e2) {
            }
        }
    }

    private void checkThreadContextClassLoader() {
        try {
            threadRef.getContextClassLoader().getResource("");
        } catch (Throwable th) {
            uncheckedShutdown();
        }
    }

    private static boolean consistentClassLoaders() {
        threadRefLock.lock();
        try {
            if (threadRef == null) {
                return false;
            }
            ClassLoader callerCtxClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader threadCtxClassLoader = threadRef.getContextClassLoader();
            return (callerCtxClassLoader == null || threadCtxClassLoader == null || callerCtxClassLoader != threadCtxClassLoader) ? false : true;
        } finally {
            threadRefLock.unlock();
        }
    }

    private static void shutdown(boolean checked) {
        if (checked && !consistentClassLoaders()) {
            return;
        }
        cleanupThreadExcecutorService.shutdownNow();
    }

    public static void checkedShutdown() {
        shutdown(true);
    }

    public static void uncheckedShutdown() {
        shutdown(false);
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x001b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean isAlive() {
        /*
            java.util.concurrent.locks.Lock r0 = com.mysql.jdbc.AbandonedConnectionCleanupThread.threadRefLock
            r0.lock()
            java.lang.Thread r0 = com.mysql.jdbc.AbandonedConnectionCleanupThread.threadRef     // Catch: java.lang.Throwable -> L22
            if (r0 == 0) goto L1b
            java.lang.Thread r0 = com.mysql.jdbc.AbandonedConnectionCleanupThread.threadRef     // Catch: java.lang.Throwable -> L22
            boolean r0 = r0.isAlive()     // Catch: java.lang.Throwable -> L22
            if (r0 == 0) goto L1b
            r0 = 1
            goto L1c
        L1b:
            r0 = 0
        L1c:
            r2 = r0
            r0 = jsr -> L28
        L20:
            r1 = r2
            return r1
        L22:
            r3 = move-exception
            r0 = jsr -> L28
        L26:
            r1 = r3
            throw r1
        L28:
            r4 = r0
            java.util.concurrent.locks.Lock r0 = com.mysql.jdbc.AbandonedConnectionCleanupThread.threadRefLock
            r0.unlock()
            ret r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mysql.jdbc.AbandonedConnectionCleanupThread.isAlive():boolean");
    }

    protected static void trackConnection(MySQLConnection conn, NetworkResources io2) {
        threadRefLock.lock();
        try {
            if (isAlive()) {
                ConnectionFinalizerPhantomReference reference = new ConnectionFinalizerPhantomReference(conn, io2, referenceQueue);
                connectionFinalizerPhantomRefs.put(reference, reference);
            }
        } finally {
            threadRefLock.unlock();
        }
    }

    private static void finalizeResource(ConnectionFinalizerPhantomReference reference) {
        try {
            reference.finalizeResources();
            reference.clear();
        } finally {
            connectionFinalizerPhantomRefs.remove(reference);
        }
    }

    /* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/AbandonedConnectionCleanupThread$ConnectionFinalizerPhantomReference.class */
    private static class ConnectionFinalizerPhantomReference extends PhantomReference<MySQLConnection> {
        private NetworkResources networkResources;

        ConnectionFinalizerPhantomReference(MySQLConnection conn, NetworkResources networkResources, ReferenceQueue<? super MySQLConnection> refQueue) {
            super(conn, refQueue);
            this.networkResources = networkResources;
        }

        void finalizeResources() {
            if (this.networkResources != null) {
                try {
                    this.networkResources.forceClose();
                } finally {
                    this.networkResources = null;
                }
            }
        }
    }

    public static Thread getThread() {
        return threadRef;
    }
}
