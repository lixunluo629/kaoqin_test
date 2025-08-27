package org.ehcache.impl.internal.concurrent;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.ProtectionDomain;
import org.ehcache.impl.internal.concurrent.ForkJoinPool;
import org.ehcache.impl.internal.concurrent.JSR166Helper;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinWorkerThread.class */
class ForkJoinWorkerThread extends Thread {
    final ForkJoinPool pool;
    final ForkJoinPool.WorkQueue workQueue;
    private static final JSR166Helper.Unsafe U;
    private static final long THREADLOCALS;
    private static final long INHERITABLETHREADLOCALS;
    private static final long INHERITEDACCESSCONTROLCONTEXT;

    protected ForkJoinWorkerThread(ForkJoinPool pool) {
        super("aForkJoinWorkerThread");
        this.pool = pool;
        this.workQueue = pool.registerWorker(this);
    }

    ForkJoinWorkerThread(ForkJoinPool pool, ThreadGroup threadGroup, AccessControlContext acc) {
        super(threadGroup, null, "aForkJoinWorkerThread");
        U.putOrderedObject(this, INHERITEDACCESSCONTROLCONTEXT, acc);
        eraseThreadLocals();
        this.pool = pool;
        this.workQueue = pool.registerWorker(this);
    }

    public ForkJoinPool getPool() {
        return this.pool;
    }

    public int getPoolIndex() {
        return this.workQueue.getPoolIndex();
    }

    protected void onStart() {
    }

    protected void onTermination(Throwable exception) {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (this.workQueue.array == null) {
            Throwable exception = null;
            try {
                onStart();
                this.pool.runWorker(this.workQueue);
                try {
                    onTermination(null);
                    this.pool.deregisterWorker(this, null);
                } catch (Throwable ex) {
                    if (0 == 0) {
                        exception = ex;
                    }
                    this.pool.deregisterWorker(this, exception);
                }
            } catch (Throwable ex2) {
                Throwable exception2 = ex2;
                try {
                    onTermination(exception2);
                    this.pool.deregisterWorker(this, exception2);
                } catch (Throwable th) {
                    this.pool.deregisterWorker(this, exception2);
                    throw th;
                }
            }
        }
    }

    final void eraseThreadLocals() {
        U.putObject(this, THREADLOCALS, null);
        U.putObject(this, INHERITABLETHREADLOCALS, null);
    }

    void afterTopLevelExec() {
    }

    static {
        try {
            U = JSR166Helper.Unsafe.getUnsafe();
            THREADLOCALS = U.objectFieldOffset(Thread.class.getDeclaredField("threadLocals"));
            INHERITABLETHREADLOCALS = U.objectFieldOffset(Thread.class.getDeclaredField("inheritableThreadLocals"));
            INHERITEDACCESSCONTROLCONTEXT = U.objectFieldOffset(Thread.class.getDeclaredField("inheritedAccessControlContext"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinWorkerThread$InnocuousForkJoinWorkerThread.class */
    static final class InnocuousForkJoinWorkerThread extends ForkJoinWorkerThread {
        private static final ThreadGroup innocuousThreadGroup = createThreadGroup();
        private static final AccessControlContext INNOCUOUS_ACC = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, null)});

        InnocuousForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool, innocuousThreadGroup, INNOCUOUS_ACC);
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinWorkerThread
        void afterTopLevelExec() {
            eraseThreadLocals();
        }

        @Override // java.lang.Thread
        public ClassLoader getContextClassLoader() {
            return ClassLoader.getSystemClassLoader();
        }

        @Override // java.lang.Thread
        public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler x) {
        }

        @Override // java.lang.Thread
        public void setContextClassLoader(ClassLoader cl) {
            throw new SecurityException("setContextClassLoader");
        }

        private static ThreadGroup createThreadGroup() {
            try {
                JSR166Helper.Unsafe u = JSR166Helper.Unsafe.getUnsafe();
                long tg = u.objectFieldOffset(Thread.class.getDeclaredField("group"));
                long gp = u.objectFieldOffset(ThreadGroup.class.getDeclaredField("parent"));
                ThreadGroup group = (ThreadGroup) u.getObject(Thread.currentThread(), tg);
                while (group != null) {
                    ThreadGroup parent = (ThreadGroup) u.getObject(group, gp);
                    if (parent == null) {
                        return new ThreadGroup(group, "InnocuousForkJoinWorkerThreadGroup");
                    }
                    group = parent;
                }
                throw new Error("Cannot create ThreadGroup");
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }
}
