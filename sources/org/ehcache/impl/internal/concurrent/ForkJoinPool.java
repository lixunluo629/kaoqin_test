package org.ehcache.impl.internal.concurrent;

import java.lang.Thread;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.ehcache.impl.internal.concurrent.ForkJoinTask;
import org.ehcache.impl.internal.concurrent.ForkJoinWorkerThread;
import org.ehcache.impl.internal.concurrent.JSR166Helper;
import org.springframework.beans.PropertyAccessor;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool.class */
class ForkJoinPool extends AbstractExecutorService {
    static final int SMASK = 65535;
    static final int MAX_CAP = 32767;
    static final int EVENMASK = 65534;
    static final int SQMASK = 126;
    static final int SCANNING = 1;
    static final int INACTIVE = Integer.MIN_VALUE;
    static final int SS_SEQ = 65536;
    static final int MODE_MASK = -65536;
    static final int LIFO_QUEUE = 0;
    static final int FIFO_QUEUE = 65536;
    static final int SHARED_QUEUE = Integer.MIN_VALUE;
    public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory;
    private static final RuntimePermission modifyThreadPermission;
    static final ForkJoinPool common;
    static final int commonParallelism;
    private static int commonMaxSpares;
    private static int poolNumberSequence;
    private static final long IDLE_TIMEOUT = 2000000000;
    private static final long TIMEOUT_SLOP = 20000000;
    private static final int DEFAULT_COMMON_MAX_SPARES = 256;
    private static final int SPINS = 2048;
    private static final int SEED_INCREMENT = -1640531527;
    private static final long SP_MASK = 4294967295L;
    private static final long UC_MASK = -4294967296L;
    private static final int AC_SHIFT = 48;
    private static final long AC_UNIT = 281474976710656L;
    private static final long AC_MASK = -281474976710656L;
    private static final int TC_SHIFT = 32;
    private static final long TC_UNIT = 4294967296L;
    private static final long TC_MASK = 281470681743360L;
    private static final long ADD_WORKER = 140737488355328L;
    private static final int RSLOCK = 1;
    private static final int RSIGNAL = 2;
    private static final int STARTED = 4;
    private static final int STOP = 536870912;
    private static final int TERMINATED = 1073741824;
    private static final int SHUTDOWN = Integer.MIN_VALUE;
    volatile long ctl;
    volatile int runState;
    final int config;
    int indexSeed;
    volatile WorkQueue[] workQueues;
    final ForkJoinWorkerThreadFactory factory;
    final Thread.UncaughtExceptionHandler ueh;
    final String workerNamePrefix;
    volatile AtomicLong stealCounter;
    private static final JSR166Helper.Unsafe U;
    private static final int ABASE;
    private static final int ASHIFT;
    private static final long CTL;
    private static final long RUNSTATE;
    private static final long STEALCOUNTER;
    private static final long PARKBLOCKER;
    private static final long QTOP;
    private static final long QLOCK;
    private static final long QSCANSTATE;
    private static final long QPARKER;
    private static final long QCURRENTSTEAL;
    private static final long QCURRENTJOIN;

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool$ForkJoinWorkerThreadFactory.class */
    public interface ForkJoinWorkerThreadFactory {
        ForkJoinWorkerThread newThread(ForkJoinPool forkJoinPool);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool$ManagedBlocker.class */
    public interface ManagedBlocker {
        boolean block() throws InterruptedException;

        boolean isReleasable();
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public /* bridge */ /* synthetic */ Future submit(Runnable x0, Object x1) {
        return submit(x0, (Runnable) x1);
    }

    private static void checkPermission() {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(modifyThreadPermission);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool$DefaultForkJoinWorkerThreadFactory.class */
    static final class DefaultForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        DefaultForkJoinWorkerThreadFactory() {
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
        public final ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            return new ForkJoinWorkerThread(pool);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool$EmptyTask.class */
    static final class EmptyTask extends ForkJoinTask<Void> {
        private static final long serialVersionUID = -7721805057305804111L;

        EmptyTask() {
            this.status = -268435456;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final void setRawResult(Void x) {
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final boolean exec() {
            return true;
        }
    }

    static final ForkJoinTask<?> getAt(ForkJoinTask<?>[] a, int i) {
        return (ForkJoinTask) U.getObjectVolatile(a, (i << ASHIFT) + ABASE);
    }

    static final void setAt(ForkJoinTask<?>[] a, int i, ForkJoinTask<?> x) {
        U.putOrderedObject(a, (i << ASHIFT) + ABASE, x);
    }

    static final boolean casAt(ForkJoinTask<?>[] a, int i, ForkJoinTask<?> c, ForkJoinTask<?> v) {
        return U.compareAndSwapObject(a, (i << ASHIFT) + ABASE, c, v);
    }

    static final ForkJoinTask<?> xchgAt(ForkJoinTask<?>[] a, int i, ForkJoinTask<?> x) {
        return (ForkJoinTask) U.getAndSetObject(a, (i << ASHIFT) + ABASE, x);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool$WorkQueue.class */
    static final class WorkQueue {
        static final int INITIAL_QUEUE_CAPACITY = 8192;
        static final int MAXIMUM_QUEUE_CAPACITY = 67108864;
        volatile int scanState;
        int stackPred;
        int nsteals;
        int hint;
        int config;
        volatile int qlock;
        ForkJoinTask<?>[] array;
        final ForkJoinPool pool;
        final ForkJoinWorkerThread owner;
        volatile Thread parker;
        volatile ForkJoinTask<?> currentJoin;
        volatile ForkJoinTask<?> currentSteal;
        private static final JSR166Helper.Unsafe U;
        private static final int ABASE;
        private static final int ASHIFT;
        private static final long QTOP;
        private static final long QLOCK;
        private static final long QCURRENTSTEAL;
        int top = 4096;
        volatile int base = 4096;

        static final ForkJoinTask<?> getAt(ForkJoinTask<?>[] a, int i) {
            return (ForkJoinTask) U.getObjectVolatile(a, (i << ASHIFT) + ABASE);
        }

        static final void setAt(ForkJoinTask<?>[] a, int i, ForkJoinTask<?> x) {
            U.putOrderedObject(a, (i << ASHIFT) + ABASE, x);
        }

        static final boolean casAt(ForkJoinTask<?>[] a, int i, ForkJoinTask<?> c, ForkJoinTask<?> v) {
            return U.compareAndSwapObject(a, (i << ASHIFT) + ABASE, c, v);
        }

        static final ForkJoinTask<?> xchgAt(ForkJoinTask<?>[] a, int i, ForkJoinTask<?> x) {
            return (ForkJoinTask) U.getAndSetObject(a, (i << ASHIFT) + ABASE, x);
        }

        WorkQueue(ForkJoinPool pool, ForkJoinWorkerThread owner) {
            this.pool = pool;
            this.owner = owner;
        }

        final int getPoolIndex() {
            return (this.config & 65535) >>> 1;
        }

        final int queueSize() {
            int n = this.base - this.top;
            if (n >= 0) {
                return 0;
            }
            return -n;
        }

        final boolean isEmpty() {
            ForkJoinTask<?>[] a;
            int al;
            int i = this.base;
            int s = this.top;
            int n = i - s;
            return n >= 0 || (n == -1 && ((a = this.array) == null || (al = a.length) == 0 || getAt(a, (al - 1) & (s - 1)) == null));
        }

        final void push(ForkJoinTask<?> task) {
            ForkJoinTask<?>[] a = this.array;
            if (a != null) {
                int b = this.base;
                int m = a.length - 1;
                int s = this.top;
                if (m > 0) {
                    setAt(a, m & s, task);
                    U.putOrderedInt(this, QTOP, s + 1);
                    int n = s - b;
                    if (n <= 1) {
                        ForkJoinPool p = this.pool;
                        if (p != null) {
                            p.signalWork(p.workQueues, this);
                            return;
                        }
                        return;
                    }
                    if (n >= m) {
                        growArray();
                    }
                }
            }
        }

        final ForkJoinTask<?>[] growArray() {
            int oldMask;
            ForkJoinTask<?>[] oldA = this.array;
            int size = oldA != null ? oldA.length << 1 : 8192;
            if (size < 8192 || size > 67108864) {
                throw new RejectedExecutionException("Queue capacity exceeded");
            }
            ForkJoinTask<?>[] a = new ForkJoinTask[size];
            this.array = a;
            if (oldA != null && (oldMask = oldA.length - 1) > 0) {
                int t = this.top;
                int i = this.base;
                int b = i;
                if (t - i > 0) {
                    int mask = size - 1;
                    do {
                        int oldj = b & oldMask;
                        int j = b & mask;
                        ForkJoinTask<?> x = getAt(oldA, oldj);
                        if (x != null && casAt(oldA, oldj, x, null)) {
                            setAt(a, j, x);
                        }
                        b++;
                    } while (b != t);
                }
            }
            return a;
        }

        final ForkJoinTask<?> pop() {
            int al;
            int s;
            int j;
            ForkJoinTask<?> t;
            ForkJoinTask<?>[] a = this.array;
            if (a != null && (al = a.length) > 0) {
                do {
                    s = this.top - 1;
                    if (s - this.base < 0 || (t = getAt(a, (j = (al - 1) & s))) == null) {
                        return null;
                    }
                } while (!casAt(a, j, t, null));
                U.putOrderedInt(this, QTOP, s);
                return t;
            }
            return null;
        }

        final ForkJoinTask<?> pollAt(int b) {
            ForkJoinTask<?> t;
            ForkJoinTask<?>[] a = this.array;
            if (a != null) {
                int al = a.length;
                int j = (al - 1) & b;
                if (al > 0 && (t = getAt(a, j)) != null && this.base == b && casAt(a, j, t, null)) {
                    this.base = b + 1;
                    return t;
                }
                return null;
            }
            return null;
        }

        final ForkJoinTask<?> poll() {
            ForkJoinTask<?>[] a;
            int al;
            while (true) {
                int b = this.base;
                if (b - this.top < 0 && (a = this.array) != null && (al = a.length) > 0) {
                    int j = (al - 1) & b;
                    ForkJoinTask<?> t = getAt(a, j);
                    if (this.base == b) {
                        if (t != null) {
                            if (casAt(a, j, t, null)) {
                                this.base = b + 1;
                                return t;
                            }
                        } else if (b + 1 == this.top) {
                            return null;
                        }
                    }
                } else {
                    return null;
                }
            }
        }

        final ForkJoinTask<?> nextLocalTask() {
            return (this.config & 65536) == 0 ? pop() : poll();
        }

        final ForkJoinTask<?> peek() {
            int al;
            ForkJoinTask<?>[] a = this.array;
            if (a != null && (al = a.length) > 0) {
                int i = (this.config & 65536) == 0 ? this.top - 1 : this.base;
                return getAt(a, (al - 1) & i);
            }
            return null;
        }

        final boolean tryUnpush(ForkJoinTask<?> t) {
            ForkJoinTask<?>[] a = this.array;
            if (a != null) {
                int b = this.base;
                int al = a.length;
                int s = this.top;
                if (s != b && al > 0 && casAt(a, (al - 1) & (s - 1), t, null)) {
                    U.putOrderedInt(this, QTOP, s - 1);
                    return true;
                }
                return false;
            }
            return false;
        }

        final void cancelAll() {
            ForkJoinTask<?> t = this.currentJoin;
            if (t != null) {
                this.currentJoin = null;
                ForkJoinTask.cancelIgnoringExceptions(t);
            }
            ForkJoinTask<?> t2 = this.currentSteal;
            if (t2 != null) {
                this.currentSteal = null;
                ForkJoinTask.cancelIgnoringExceptions(t2);
            }
            while (true) {
                ForkJoinTask<?> t3 = poll();
                if (t3 != null) {
                    ForkJoinTask.cancelIgnoringExceptions(t3);
                } else {
                    return;
                }
            }
        }

        final void pollAndExecAll() {
            while (true) {
                ForkJoinTask<?> t = poll();
                if (t != null) {
                    t.doExec();
                } else {
                    return;
                }
            }
        }

        final void popAndExecAll() {
            ForkJoinTask<?> t;
            while (true) {
                ForkJoinTask<?>[] a = this.array;
                if (a != null) {
                    int b = this.base;
                    int al = a.length;
                    int s = this.top;
                    int i = (al - 1) & (s - 1);
                    if (b != s && al > 0 && (t = xchgAt(a, i, null)) != null) {
                        U.putOrderedInt(this, QTOP, s - 1);
                        t.doExec();
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        final void runTask(ForkJoinTask<?> task) {
            if (task != null) {
                this.scanState &= -2;
                this.currentSteal = task;
                task.doExec();
                U.putOrderedObject(this, QCURRENTSTEAL, null);
                if ((this.config & 65536) != 0) {
                    pollAndExecAll();
                } else {
                    popAndExecAll();
                }
                ForkJoinWorkerThread thread = this.owner;
                int i = this.nsteals + 1;
                this.nsteals = i;
                if (i < 0) {
                    transferStealCount(this.pool);
                }
                this.scanState |= 1;
                if (thread != null) {
                    thread.afterTopLevelExec();
                }
            }
        }

        final void transferStealCount(ForkJoinPool p) {
            AtomicLong sc;
            if (p != null && (sc = p.stealCounter) != null) {
                int s = this.nsteals;
                this.nsteals = 0;
                sc.getAndAdd(s < 0 ? Integer.MAX_VALUE : s);
            }
        }

        final boolean tryRemoveAndExec(ForkJoinTask<?> task) {
            int al;
            ForkJoinTask<?>[] a = this.array;
            if (a != null && (al = a.length) > 0 && task != null) {
                do {
                    int i = this.top;
                    int s = i;
                    int b = this.base;
                    int i2 = i - b;
                    int n = i2;
                    if (i2 > 0) {
                        do {
                            s--;
                            int j = s & (al - 1);
                            ForkJoinTask<?> t = getAt(a, j);
                            if (t == null) {
                                return s + 1 == this.top;
                            }
                            if (t == task) {
                                boolean removed = false;
                                if (s + 1 == this.top) {
                                    if (casAt(a, j, task, null)) {
                                        U.putOrderedInt(this, QTOP, s);
                                        removed = true;
                                    }
                                } else if (this.base == b) {
                                    removed = casAt(a, j, task, new EmptyTask());
                                }
                                if (removed) {
                                    task.doExec();
                                }
                            } else if (t.status < 0 && s + 1 == this.top) {
                                if (casAt(a, j, t, null)) {
                                    U.putOrderedInt(this, QTOP, s);
                                }
                            } else {
                                n--;
                            }
                        } while (n != 0);
                        return false;
                    }
                    return true;
                } while (task.status >= 0);
                return false;
            }
            return true;
        }

        final CountedCompleter<?> popCC(CountedCompleter<?> task, int mode) {
            ForkJoinTask<?>[] a = this.array;
            if (a != null) {
                int b = this.base;
                int al = a.length;
                int s = this.top;
                int i = (al - 1) & (s - 1);
                if (b == s || al <= 0) {
                    return null;
                }
                ForkJoinTask<?> o = a[i];
                if (o instanceof CountedCompleter) {
                    CountedCompleter<?> t = (CountedCompleter) o;
                    CountedCompleter<?> r = t;
                    while (r != task) {
                        CountedCompleter<?> countedCompleter = r.completer;
                        r = countedCompleter;
                        if (countedCompleter == null) {
                            return null;
                        }
                    }
                    if (mode < 0) {
                        if (U.compareAndSwapInt(this, QLOCK, 0, 1)) {
                            if (this.top == s && this.array == a && casAt(a, i, t, null)) {
                                U.putOrderedInt(this, QTOP, s - 1);
                                U.putOrderedInt(this, QLOCK, 0);
                                return t;
                            }
                            U.compareAndSwapInt(this, QLOCK, 1, 0);
                            return null;
                        }
                        return null;
                    }
                    if (casAt(a, i, t, null)) {
                        U.putOrderedInt(this, QTOP, s - 1);
                        return t;
                    }
                    return null;
                }
                return null;
            }
            return null;
        }

        final int pollAndExecCC(CountedCompleter<?> task) {
            int h;
            ForkJoinTask<?>[] a;
            int al;
            int b = this.base;
            if (b - this.top >= 0 || (a = this.array) == null || (al = a.length) <= 0) {
                h = b | Integer.MIN_VALUE;
            } else {
                int j = (al - 1) & b;
                Object o = getAt(a, j);
                if (o == null) {
                    h = 2;
                } else if (!(o instanceof CountedCompleter)) {
                    h = -1;
                } else {
                    CountedCompleter<?> t = (CountedCompleter) o;
                    CountedCompleter<?> r = t;
                    while (true) {
                        if (r == task) {
                            if (this.base == b && casAt(a, j, t, null)) {
                                this.base = b + 1;
                                t.doExec();
                                h = 1;
                            } else {
                                h = 2;
                            }
                        } else {
                            CountedCompleter<?> countedCompleter = r.completer;
                            r = countedCompleter;
                            if (countedCompleter == null) {
                                h = -1;
                                break;
                            }
                        }
                    }
                }
            }
            return h;
        }

        final boolean isApparentlyUnblocked() {
            Thread wt;
            Thread.State s;
            return (this.scanState < 0 || (wt = this.owner) == null || (s = wt.getState()) == Thread.State.BLOCKED || s == Thread.State.WAITING || s == Thread.State.TIMED_WAITING) ? false : true;
        }

        static {
            try {
                U = JSR166Helper.Unsafe.getUnsafe();
                QTOP = U.objectFieldOffset(WorkQueue.class.getDeclaredField("top"));
                QLOCK = U.objectFieldOffset(WorkQueue.class.getDeclaredField("qlock"));
                QCURRENTSTEAL = U.objectFieldOffset(WorkQueue.class.getDeclaredField("currentSteal"));
                ABASE = U.arrayBaseOffset(ForkJoinTask[].class);
                int scale = U.arrayIndexScale(ForkJoinTask[].class);
                if ((scale & (scale - 1)) != 0) {
                    throw new Error("data type scale not a power of two");
                }
                ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    private static final synchronized int nextPoolId() {
        int i = poolNumberSequence + 1;
        poolNumberSequence = i;
        return i;
    }

    private int lockRunState() {
        int rs = this.runState;
        if ((rs & 1) == 0) {
            int rs2 = rs | 1;
            if (U.compareAndSwapInt(this, RUNSTATE, rs, rs2)) {
                return rs2;
            }
        }
        return awaitRunStateLock();
    }

    private int awaitRunStateLock() {
        int ns;
        Object lock;
        boolean wasInterrupted = false;
        int spins = 2048;
        int r = 0;
        while (true) {
            int rs = this.runState;
            if ((rs & 1) == 0) {
                ns = rs | 1;
                if (U.compareAndSwapInt(this, RUNSTATE, rs, ns)) {
                    break;
                }
            } else if (r == 0) {
                r = ThreadLocalRandom.nextSecondarySeed();
            } else if (spins > 0) {
                int r2 = r ^ (r << 6);
                int r3 = r2 ^ (r2 >>> 21);
                r = r3 ^ (r3 << 7);
                if (r >= 0) {
                    spins--;
                }
            } else if ((rs & 4) == 0 || (lock = this.stealCounter) == null) {
                Thread.yield();
            } else if (U.compareAndSwapInt(this, RUNSTATE, rs, rs | 2)) {
                synchronized (lock) {
                    if ((this.runState & 2) != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            if (!(Thread.currentThread() instanceof ForkJoinWorkerThread)) {
                                wasInterrupted = true;
                            }
                        }
                    } else {
                        lock.notifyAll();
                    }
                }
            } else {
                continue;
            }
        }
        if (wasInterrupted) {
            try {
                Thread.currentThread().interrupt();
            } catch (SecurityException e2) {
            }
        }
        return ns;
    }

    private void unlockRunState(int oldRunState, int newRunState) {
        if (!U.compareAndSwapInt(this, RUNSTATE, oldRunState, newRunState)) {
            Object lock = this.stealCounter;
            this.runState = newRunState;
            if (lock != null) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }
        }
    }

    private boolean createWorker() {
        ForkJoinWorkerThreadFactory fac = this.factory;
        Throwable ex = null;
        ForkJoinWorkerThread wt = null;
        if (fac != null) {
            try {
                ForkJoinWorkerThread forkJoinWorkerThreadNewThread = fac.newThread(this);
                wt = forkJoinWorkerThreadNewThread;
                if (forkJoinWorkerThreadNewThread != null) {
                    wt.start();
                    return true;
                }
            } catch (Throwable rex) {
                ex = rex;
            }
        }
        deregisterWorker(wt, ex);
        return false;
    }

    private void tryAddWorker(long c) {
        boolean add = false;
        do {
            long nc = (AC_MASK & (c + AC_UNIT)) | (TC_MASK & (c + TC_UNIT));
            if (this.ctl == c) {
                int rs = lockRunState();
                int stop = rs & 536870912;
                if (stop == 0) {
                    add = U.compareAndSwapLong(this, CTL, c, nc);
                }
                unlockRunState(rs, rs & (-2));
                if (stop == 0) {
                    if (add) {
                        createWorker();
                        return;
                    }
                } else {
                    return;
                }
            }
            long j = this.ctl;
            c = j;
            if ((j & ADD_WORKER) == 0) {
                return;
            }
        } while (((int) c) == 0);
    }

    final WorkQueue registerWorker(ForkJoinWorkerThread wt) {
        wt.setDaemon(true);
        Thread.UncaughtExceptionHandler handler = this.ueh;
        if (handler != null) {
            wt.setUncaughtExceptionHandler(handler);
        }
        WorkQueue w = new WorkQueue(this, wt);
        int i = 0;
        int mode = this.config & (-65536);
        int rs = lockRunState();
        try {
            WorkQueue[] workQueueArr = this.workQueues;
            WorkQueue[] ws = workQueueArr;
            if (workQueueArr != null) {
                int length = ws.length;
                int n = length;
                if (length > 0) {
                    int s = this.indexSeed + SEED_INCREMENT;
                    this.indexSeed = s;
                    int m = n - 1;
                    i = ((s << 1) | 1) & m;
                    if (ws[i] != null) {
                        int probes = 0;
                        int step = n <= 4 ? 2 : ((n >>> 1) & 65534) + 2;
                        while (true) {
                            int i2 = (i + step) & m;
                            i = i2;
                            if (ws[i2] == null) {
                                break;
                            }
                            probes++;
                            if (probes >= n) {
                                int i3 = n << 1;
                                n = i3;
                                WorkQueue[] workQueueArr2 = (WorkQueue[]) Arrays.copyOf(ws, i3);
                                ws = workQueueArr2;
                                this.workQueues = workQueueArr2;
                                m = n - 1;
                                probes = 0;
                            }
                        }
                    }
                    w.hint = s;
                    w.config = i | mode;
                    w.scanState = i;
                    ws[i] = w;
                }
            }
            wt.setName(this.workerNamePrefix.concat(Integer.toString(i >>> 1)));
            return w;
        } finally {
            unlockRunState(rs, rs & (-2));
        }
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [long, org.ehcache.impl.internal.concurrent.JSR166Helper$Unsafe] */
    final void deregisterWorker(ForkJoinWorkerThread wt, Throwable ex) {
        ?? r0;
        long c;
        WorkQueue[] ws;
        int m;
        WorkQueue w = null;
        if (wt != null) {
            WorkQueue workQueue = wt.workQueue;
            w = workQueue;
            if (workQueue != null) {
                int idx = w.config & 65535;
                int rs = lockRunState();
                WorkQueue[] ws2 = this.workQueues;
                if (ws2 != null && ws2.length > idx && ws2[idx] == w) {
                    ws2[idx] = null;
                }
                unlockRunState(rs, rs & (-2));
            }
        }
        do {
            r0 = U;
            long j = CTL;
            c = this.ctl;
        } while (!r0.compareAndSwapLong(this, r0, c, (AC_MASK & (c - AC_UNIT)) | (TC_MASK & (c - TC_UNIT)) | (4294967295L & c)));
        if (w != null) {
            w.qlock = -1;
            w.transferStealCount(this);
            w.cancelAll();
        }
        while (true) {
            if (tryTerminate(false, false) || w == null || w.array == null || (this.runState & 536870912) != 0 || (ws = this.workQueues) == null || (m = ws.length - 1) < 0) {
                break;
            }
            long c2 = this.ctl;
            int sp = (int) c2;
            if (sp != 0) {
                if (tryRelease(c2, ws[sp & m], AC_UNIT)) {
                    break;
                }
            } else if (ex != null && (c2 & ADD_WORKER) != 0) {
                tryAddWorker(c2);
            }
        }
        if (ex == null) {
            ForkJoinTask.helpExpungeStaleExceptions();
        } else {
            ForkJoinTask.rethrow(ex);
        }
    }

    final void signalWork(WorkQueue[] ws, WorkQueue q) {
        int i;
        WorkQueue v;
        while (true) {
            long c = this.ctl;
            if (c < 0) {
                int sp = (int) c;
                if (sp == 0) {
                    if ((c & ADD_WORKER) != 0) {
                        tryAddWorker(c);
                        return;
                    }
                    return;
                }
                if (ws != null && ws.length > (i = sp & 65535) && (v = ws[i]) != null) {
                    int vs = (sp + 65536) & Integer.MAX_VALUE;
                    int d = sp - v.scanState;
                    long nc = (UC_MASK & (c + AC_UNIT)) | (4294967295L & v.stackPred);
                    if (d == 0 && U.compareAndSwapLong(this, CTL, c, nc)) {
                        v.scanState = vs;
                        Thread p = v.parker;
                        if (p != null) {
                            U.unpark(p);
                            return;
                        }
                        return;
                    }
                    if (q != null && q.base == q.top) {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private boolean tryRelease(long c, WorkQueue v, long inc) {
        int sp = (int) c;
        int vs = (sp + 65536) & Integer.MAX_VALUE;
        if (v != null && v.scanState == sp) {
            long nc = (UC_MASK & (c + inc)) | (4294967295L & v.stackPred);
            if (U.compareAndSwapLong(this, CTL, c, nc)) {
                v.scanState = vs;
                Thread p = v.parker;
                if (p != null) {
                    U.unpark(p);
                    return true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    final void runWorker(WorkQueue w) {
        w.growArray();
        int seed = w.hint;
        int i = seed == 0 ? 1 : seed;
        while (true) {
            int r = i;
            ForkJoinTask<?> t = scan(w, r);
            if (t != null) {
                w.runTask(t);
            } else if (!awaitWork(w, r)) {
                return;
            }
            int r2 = r ^ (r << 13);
            int r3 = r2 ^ (r2 >>> 17);
            i = r3 ^ (r3 << 5);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:46:0x0125 A[PHI: r14
  0x0125: PHI (r14v3 int) = (r14v1 int), (r14v6 int) binds: [B:43:0x0116, B:45:0x0122] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v15 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private org.ehcache.impl.internal.concurrent.ForkJoinTask<?> scan(org.ehcache.impl.internal.concurrent.ForkJoinPool.WorkQueue r10, int r11) {
        /*
            Method dump skipped, instructions count: 407
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ForkJoinPool.scan(org.ehcache.impl.internal.concurrent.ForkJoinPool$WorkQueue, int):org.ehcache.impl.internal.concurrent.ForkJoinTask");
    }

    private boolean awaitWork(WorkQueue w, int r) {
        WorkQueue[] ws;
        int j;
        WorkQueue v;
        long deadline;
        long parkTime;
        long prevctl;
        if (w == null || w.qlock < 0) {
            return false;
        }
        int pred = w.stackPred;
        int spins = 2048;
        while (true) {
            int ss = w.scanState;
            if (ss < 0) {
                if (spins > 0) {
                    int r2 = r ^ (r << 6);
                    int r3 = r2 ^ (r2 >>> 21);
                    r = r3 ^ (r3 << 7);
                    if (r >= 0) {
                        spins--;
                        if (spins == 0 && pred != 0 && (ws = this.workQueues) != null && (j = pred & 65535) < ws.length && (v = ws[j]) != null && (v.parker == null || v.scanState >= 0)) {
                            spins = 2048;
                        }
                    }
                } else {
                    if (w.qlock < 0) {
                        return false;
                    }
                    if (Thread.interrupted()) {
                        continue;
                    } else {
                        long c = this.ctl;
                        int ac = ((int) (c >> 48)) + (this.config & 65535);
                        if ((ac <= 0 && tryTerminate(false, false)) || (this.runState & 536870912) != 0) {
                            return false;
                        }
                        if (ac <= 0 && ss == ((int) c)) {
                            prevctl = (UC_MASK & (c + AC_UNIT)) | (4294967295L & pred);
                            int t = (short) (c >>> 32);
                            if (t > 2 && U.compareAndSwapLong(this, CTL, c, prevctl)) {
                                return false;
                            }
                            parkTime = IDLE_TIMEOUT * (t >= 0 ? 1 : 1 - t);
                            deadline = (System.nanoTime() + parkTime) - TIMEOUT_SLOP;
                        } else {
                            deadline = 0;
                            parkTime = 0;
                            prevctl = 0;
                        }
                        Thread wt = Thread.currentThread();
                        U.putObject(wt, PARKBLOCKER, this);
                        w.parker = wt;
                        if (w.scanState < 0 && this.ctl == c) {
                            U.park(false, parkTime);
                        }
                        U.putOrderedObject(w, QPARKER, null);
                        U.putObject(wt, PARKBLOCKER, null);
                        if (w.scanState >= 0) {
                            return true;
                        }
                        if (parkTime != 0 && this.ctl == c && deadline - System.nanoTime() <= 0 && U.compareAndSwapLong(this, CTL, c, prevctl)) {
                            return false;
                        }
                    }
                }
            } else {
                return true;
            }
        }
    }

    final int helpComplete(WorkQueue w, CountedCompleter<?> task, int maxTasks) {
        int m;
        CountedCompleter<?> p;
        int s = 0;
        WorkQueue[] ws = this.workQueues;
        if (ws != null && (m = ws.length - 1) > 0 && task != null && w != null) {
            int mode = w.config;
            int r = w.hint ^ w.top;
            int origin = r & m;
            int h = 1;
            int k = origin;
            int oldSum = 0;
            int checkSum = 0;
            while (true) {
                int i = task.status;
                s = i;
                if (i < 0) {
                    break;
                }
                if (h == 1 && (p = w.popCC(task, mode)) != null) {
                    p.doExec();
                    if (maxTasks != 0) {
                        maxTasks--;
                        if (maxTasks == 0) {
                            break;
                        }
                    }
                    origin = k;
                    checkSum = 0;
                    oldSum = 0;
                } else {
                    WorkQueue q = ws[k];
                    if (q == null) {
                        h = 0;
                    } else {
                        int iPollAndExecCC = q.pollAndExecCC(task);
                        h = iPollAndExecCC;
                        if (iPollAndExecCC < 0) {
                            checkSum += h;
                        }
                    }
                    if (h > 0) {
                        if (h == 1 && maxTasks != 0) {
                            maxTasks--;
                            if (maxTasks == 0) {
                                break;
                            }
                        }
                        int r2 = r ^ (r << 13);
                        int r3 = r2 ^ (r2 >>> 17);
                        r = r3 ^ (r3 << 5);
                        int i2 = r & m;
                        k = i2;
                        origin = i2;
                        checkSum = 0;
                        oldSum = 0;
                    } else {
                        int i3 = (k + 1) & m;
                        k = i3;
                        if (i3 == origin) {
                            int i4 = oldSum;
                            int i5 = checkSum;
                            oldSum = i5;
                            if (i4 == i5) {
                                break;
                            }
                            checkSum = 0;
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        return s;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0062, code lost:
    
        r14.hint = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x007c, code lost:
    
        r1 = r0.base;
        r11 = r11 + r1;
        r0 = r0.currentJoin;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0095, code lost:
    
        if (r13.status < 0) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x009f, code lost:
    
        if (r14.currentJoin != r13) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00a9, code lost:
    
        if (r0.currentSteal == r13) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00b7, code lost:
    
        if ((r1 - r0.top) >= 0) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00ba, code lost:
    
        r0 = r0.array;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00c2, code lost:
    
        if (r0 == null) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00c5, code lost:
    
        r0 = r0.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00cb, code lost:
    
        if (r0 > 0) goto L41;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00ce, code lost:
    
        r13 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d3, code lost:
    
        if (r0 != null) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00d9, code lost:
    
        r14 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00e0, code lost:
    
        r1 = (r0 - 1) & r1;
        r21 = getAt(r0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00f8, code lost:
    
        if (r0.base != r1) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00fd, code lost:
    
        if (r21 != null) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x010d, code lost:
    
        if (casAt(r0, r1, r21, null) == false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0110, code lost:
    
        r0.base = r1 + 1;
        r0 = r7.currentSteal;
        r0 = r7.top;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0125, code lost:
    
        org.ehcache.impl.internal.concurrent.ForkJoinPool.U.putOrderedObject(r7, org.ehcache.impl.internal.concurrent.ForkJoinPool.QCURRENTSTEAL, r21);
        r21.doExec();
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x013b, code lost:
    
        if (r8.status < 0) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0144, code lost:
    
        if (r7.top == r0) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0147, code lost:
    
        r0 = r7.pop();
        r21 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x014e, code lost:
    
        if (r0 != null) goto L90;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0151, code lost:
    
        org.ehcache.impl.internal.concurrent.ForkJoinPool.U.putOrderedObject(r7, org.ehcache.impl.internal.concurrent.ForkJoinPool.QCURRENTSTEAL, r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0165, code lost:
    
        if (r7.base == r7.top) goto L86;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0168, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void helpStealer(org.ehcache.impl.internal.concurrent.ForkJoinPool.WorkQueue r7, org.ehcache.impl.internal.concurrent.ForkJoinTask<?> r8) {
        /*
            Method dump skipped, instructions count: 382
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ForkJoinPool.helpStealer(org.ehcache.impl.internal.concurrent.ForkJoinPool$WorkQueue, org.ehcache.impl.internal.concurrent.ForkJoinTask):void");
    }

    private boolean tryCompensate(WorkQueue w) {
        boolean canBlock;
        WorkQueue[] ws;
        int m;
        int pc;
        if (w == null || w.qlock < 0 || (ws = this.workQueues) == null || (m = ws.length - 1) <= 0 || (pc = this.config & 65535) == 0) {
            canBlock = false;
        } else {
            long c = this.ctl;
            int sp = (int) c;
            if (sp != 0) {
                canBlock = tryRelease(c, ws[sp & m], 0L);
            } else {
                int ac = ((int) (c >> 48)) + pc;
                int tc = ((short) (c >> 32)) + pc;
                int nbusy = 0;
                for (int i = 0; i <= m; i++) {
                    WorkQueue v = ws[((i << 1) | 1) & m];
                    if (v != null) {
                        if ((v.scanState & 1) != 0) {
                            break;
                        }
                        nbusy++;
                    }
                }
                if (nbusy != (tc << 1) || this.ctl != c) {
                    canBlock = false;
                } else if (tc >= pc && ac > 1 && w.isEmpty()) {
                    long nc = (AC_MASK & (c - AC_UNIT)) | (281474976710655L & c);
                    canBlock = U.compareAndSwapLong(this, CTL, c, nc);
                } else {
                    if (tc >= 32767 || (this == common && tc >= pc + commonMaxSpares)) {
                        throw new RejectedExecutionException("Thread limit exceeded replacing blocked worker");
                    }
                    boolean add = false;
                    long nc2 = (AC_MASK & c) | (TC_MASK & (c + TC_UNIT));
                    int rs = lockRunState();
                    if ((rs & 536870912) == 0) {
                        add = U.compareAndSwapLong(this, CTL, c, nc2);
                    }
                    unlockRunState(rs, rs & (-2));
                    canBlock = add && createWorker();
                }
            }
        }
        return canBlock;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005e  */
    /* JADX WARN: Type inference failed for: r0v6, types: [org.ehcache.impl.internal.concurrent.JSR166Helper$Unsafe] */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5 */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7 */
    /* JADX WARN: Type inference failed for: r3v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final int awaitJoin(org.ehcache.impl.internal.concurrent.ForkJoinPool.WorkQueue r8, org.ehcache.impl.internal.concurrent.ForkJoinTask<?> r9, long r10) {
        /*
            Method dump skipped, instructions count: 206
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ForkJoinPool.awaitJoin(org.ehcache.impl.internal.concurrent.ForkJoinPool$WorkQueue, org.ehcache.impl.internal.concurrent.ForkJoinTask, long):int");
    }

    private WorkQueue findNonEmptyStealQueue() {
        int m;
        int r = ThreadLocalRandom.nextSecondarySeed();
        WorkQueue[] ws = this.workQueues;
        if (ws != null && (m = ws.length - 1) > 0) {
            int origin = r & m;
            int k = origin;
            int oldSum = 0;
            int checkSum = 0;
            while (true) {
                WorkQueue q = ws[k];
                if (q != null) {
                    int b = q.base;
                    if (b - q.top < 0) {
                        return q;
                    }
                    checkSum += b;
                }
                int i = (k + 1) & m;
                k = i;
                if (i == origin) {
                    int i2 = oldSum;
                    int i3 = checkSum;
                    oldSum = i3;
                    if (i2 != i3) {
                        checkSum = 0;
                    } else {
                        return null;
                    }
                }
            }
        } else {
            return null;
        }
    }

    final void helpQuiescePool(WorkQueue w) {
        ForkJoinTask<?> t;
        ForkJoinTask<?> ps = w.currentSteal;
        boolean active = true;
        while (true) {
            if ((w.config & 65536) != 0) {
                w.pollAndExecAll();
            } else {
                w.popAndExecAll();
            }
            WorkQueue q = findNonEmptyStealQueue();
            if (q != null) {
                if (!active) {
                    active = true;
                    U.getAndAddLong(this, CTL, AC_UNIT);
                }
                int b = q.base;
                if (b - q.top < 0 && (t = q.pollAt(b)) != null) {
                    U.putOrderedObject(w, QCURRENTSTEAL, t);
                    t.doExec();
                    int i = w.nsteals + 1;
                    w.nsteals = i;
                    if (i < 0) {
                        w.transferStealCount(this);
                    }
                }
            } else if (active) {
                long c = this.ctl;
                long nc = (AC_MASK & (AC_MASK - AC_UNIT)) | (281474976710655L & c);
                if (((int) (nc >> 48)) + (this.config & 65535) <= 0) {
                    break;
                } else if (U.compareAndSwapLong(this, CTL, c, nc)) {
                    active = false;
                }
            } else {
                long c2 = this.ctl;
                if (((int) (c2 >> 48)) + (this.config & 65535) <= 0 && U.compareAndSwapLong(this, CTL, c2, c2 + AC_UNIT)) {
                    break;
                }
            }
        }
        U.putOrderedObject(w, QCURRENTSTEAL, ps);
    }

    final ForkJoinTask<?> nextTaskFor(WorkQueue w) {
        ForkJoinTask<?> t;
        while (true) {
            ForkJoinTask<?> t2 = w.nextLocalTask();
            if (t2 != null) {
                return t2;
            }
            WorkQueue q = findNonEmptyStealQueue();
            if (q == null) {
                return null;
            }
            int b = q.base;
            if (b - q.top < 0 && (t = q.pollAt(b)) != null) {
                return t;
            }
        }
    }

    static int getSurplusQueuedTaskCount() {
        int i;
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
            ForkJoinPool pool = wt.pool;
            int p = pool.config & 65535;
            WorkQueue q = wt.workQueue;
            int n = q.top - q.base;
            int a = ((int) (pool.ctl >> 48)) + p;
            int p2 = p >>> 1;
            if (a > p2) {
                i = 0;
            } else {
                int p3 = p2 >>> 1;
                if (a > p3) {
                    i = 1;
                } else {
                    int p4 = p3 >>> 1;
                    i = a > p4 ? 2 : a > (p4 >>> 1) ? 4 : 8;
                }
            }
            return n - i;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:107:0x022d, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:?, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0138, code lost:
    
        if ((r8.runState & 1073741824) != 0) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x013b, code lost:
    
        r0 = lockRunState();
        unlockRunState(r0, (r0 & (-2)) | 1073741824);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0150, code lost:
    
        monitor-enter(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0151, code lost:
    
        notifyAll();
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0157, code lost:
    
        monitor-exit(r8);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v18 */
    /* JADX WARN: Type inference failed for: r2v4 */
    /* JADX WARN: Type inference failed for: r8v0, types: [java.lang.Object, long, org.ehcache.impl.internal.concurrent.ForkJoinPool] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean tryTerminate(boolean r9, boolean r10) {
        /*
            Method dump skipped, instructions count: 559
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ForkJoinPool.tryTerminate(boolean, boolean):boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x013b A[Catch: all -> 0x0184, TryCatch #0 {all -> 0x0184, blocks: (B:39:0x012b, B:43:0x0146, B:45:0x0159, B:46:0x0173, B:41:0x013b), top: B:74:0x012b }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0146 A[Catch: all -> 0x0184, PHI: r17
  0x0146: PHI (r17v4 'a' org.ehcache.impl.internal.concurrent.ForkJoinTask<?>[]) = 
  (r17v3 'a' org.ehcache.impl.internal.concurrent.ForkJoinTask<?>[])
  (r17v5 'a' org.ehcache.impl.internal.concurrent.ForkJoinTask<?>[])
 binds: [B:40:0x0138, B:42:0x0143] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {all -> 0x0184, blocks: (B:39:0x012b, B:43:0x0146, B:45:0x0159, B:46:0x0173, B:41:0x013b), top: B:74:0x012b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void externalSubmit(org.ehcache.impl.internal.concurrent.ForkJoinTask<?> r9) {
        /*
            Method dump skipped, instructions count: 542
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ForkJoinPool.externalSubmit(org.ehcache.impl.internal.concurrent.ForkJoinTask):void");
    }

    final void externalPush(ForkJoinTask<?> task) {
        int m;
        WorkQueue q;
        int r = ThreadLocalRandom.getProbe();
        int rs = this.runState;
        WorkQueue[] ws = this.workQueues;
        if (ws != null && (m = ws.length - 1) > 0 && (q = ws[m & r & 126]) != null && r != 0 && rs > 0 && U.compareAndSwapInt(q, QLOCK, 0, 1)) {
            ForkJoinTask<?>[] a = q.array;
            if (a != null) {
                int b = q.base;
                int al = a.length;
                int s = q.top;
                if (al > 0) {
                    int am = al - 1;
                    int j = am & s;
                    int n = s - b;
                    if (n < am) {
                        setAt(a, j, task);
                        U.putOrderedInt(q, QTOP, s + 1);
                        U.putOrderedInt(q, QLOCK, 0);
                        if (n <= 1) {
                            signalWork(ws, q);
                            return;
                        }
                        return;
                    }
                }
            }
            U.compareAndSwapInt(q, QLOCK, 1, 0);
        }
        externalSubmit(task);
    }

    static WorkQueue commonSubmitterQueue() {
        WorkQueue[] ws;
        int m;
        ForkJoinPool p = common;
        int r = ThreadLocalRandom.getProbe();
        if (p == null || (ws = p.workQueues) == null || (m = ws.length - 1) <= 0) {
            return null;
        }
        return ws[m & r & 126];
    }

    final boolean tryExternalUnpush(ForkJoinTask<?> task) {
        int m;
        WorkQueue w;
        ForkJoinTask<?>[] a;
        int r = ThreadLocalRandom.getProbe();
        WorkQueue[] ws = this.workQueues;
        if (ws != null && (m = ws.length - 1) > 0 && (w = ws[m & r & 126]) != null && (a = w.array) != null) {
            int b = w.base;
            int al = a.length;
            int s = w.top;
            if (s != b && al > 0 && U.compareAndSwapInt(w, QLOCK, 0, 1)) {
                int i = (al - 1) & (s - 1);
                if (w.top == s && w.array == a && casAt(a, i, task, null)) {
                    U.putOrderedInt(w, QTOP, s - 1);
                    U.putOrderedInt(w, QLOCK, 0);
                    return true;
                }
                U.compareAndSwapInt(w, QLOCK, 1, 0);
                return false;
            }
            return false;
        }
        return false;
    }

    final int externalHelpComplete(CountedCompleter<?> task, int maxTasks) {
        int n;
        int r = ThreadLocalRandom.getProbe();
        WorkQueue[] ws = this.workQueues;
        if (ws == null || (n = ws.length) == 0) {
            return 0;
        }
        return helpComplete(ws[(n - 1) & r & 126], task, maxTasks);
    }

    public ForkJoinPool() {
        this(Math.min(32767, Runtime.getRuntime().availableProcessors()), defaultForkJoinWorkerThreadFactory, null, false);
    }

    public ForkJoinPool(int parallelism) {
        this(parallelism, defaultForkJoinWorkerThreadFactory, null, false);
    }

    public ForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, boolean asyncMode) {
        this(checkParallelism(parallelism), checkFactory(factory), handler, asyncMode ? 65536 : 0, "ForkJoinPool-" + nextPoolId() + "-worker-");
        checkPermission();
    }

    private static int checkParallelism(int parallelism) {
        if (parallelism <= 0 || parallelism > 32767) {
            throw new IllegalArgumentException();
        }
        return parallelism;
    }

    private static ForkJoinWorkerThreadFactory checkFactory(ForkJoinWorkerThreadFactory factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        return factory;
    }

    private ForkJoinPool(int parallelism, ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, int mode, String workerNamePrefix) {
        this.workerNamePrefix = workerNamePrefix;
        this.factory = factory;
        this.ueh = handler;
        this.config = (parallelism & 65535) | mode;
        long np = -parallelism;
        this.ctl = ((np << 48) & AC_MASK) | ((np << 32) & TC_MASK);
    }

    public static ForkJoinPool commonPool() {
        return common;
    }

    public <T> T invoke(ForkJoinTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        externalPush(task);
        return task.join();
    }

    public void execute(ForkJoinTask<?> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        externalPush(task);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.Executor
    public void execute(Runnable runnable) {
        ForkJoinTask<?> job;
        if (runnable == 0) {
            throw new NullPointerException();
        }
        if (runnable instanceof ForkJoinTask) {
            job = (ForkJoinTask) runnable;
        } else {
            job = new ForkJoinTask.RunnableExecuteAction(runnable);
        }
        externalPush(job);
    }

    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        externalPush(task);
        return task;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> ForkJoinTask<T> submit(Callable<T> task) {
        ForkJoinTask<T> job = new ForkJoinTask.AdaptedCallable<>(task);
        externalPush(job);
        return job;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> ForkJoinTask<T> submit(Runnable task, T result) {
        ForkJoinTask<T> job = new ForkJoinTask.AdaptedRunnable<>(task, result);
        externalPush(job);
        return job;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public ForkJoinTask<?> submit(Runnable runnable) {
        ForkJoinTask<?> job;
        if (runnable == 0) {
            throw new NullPointerException();
        }
        if (runnable instanceof ForkJoinTask) {
            job = (ForkJoinTask) runnable;
        } else {
            job = new ForkJoinTask.AdaptedRunnableAction(runnable);
        }
        externalPush(job);
        return job;
    }

    @Override // java.util.concurrent.AbstractExecutorService, java.util.concurrent.ExecutorService
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) {
        ArrayList<Future<T>> futures = new ArrayList<>(tasks.size());
        boolean done = false;
        try {
            for (Callable<T> t : tasks) {
                ForkJoinTask<T> f = new ForkJoinTask.AdaptedCallable<>(t);
                futures.add(f);
                externalPush(f);
            }
            int size = futures.size();
            for (int i = 0; i < size; i++) {
                ((ForkJoinTask) futures.get(i)).quietlyJoin();
            }
            done = true;
            return futures;
        } finally {
            if (!done) {
                int size2 = futures.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    futures.get(i2).cancel(false);
                }
            }
        }
    }

    public ForkJoinWorkerThreadFactory getFactory() {
        return this.factory;
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.ueh;
    }

    public int getParallelism() {
        int par = this.config & 65535;
        if (par > 0) {
            return par;
        }
        return 1;
    }

    public static int getCommonPoolParallelism() {
        return commonParallelism;
    }

    public int getPoolSize() {
        return (this.config & 65535) + ((short) (this.ctl >>> 32));
    }

    public boolean getAsyncMode() {
        return (this.config & 65536) != 0;
    }

    public int getRunningThreadCount() {
        int rc = 0;
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 1; i < ws.length; i += 2) {
                WorkQueue w = ws[i];
                if (w != null && w.isApparentlyUnblocked()) {
                    rc++;
                }
            }
        }
        return rc;
    }

    public int getActiveThreadCount() {
        int r = (this.config & 65535) + ((int) (this.ctl >> 48));
        if (r <= 0) {
            return 0;
        }
        return r;
    }

    public boolean isQuiescent() {
        return (this.config & 65535) + ((int) (this.ctl >> 48)) <= 0;
    }

    public long getStealCount() {
        AtomicLong sc = this.stealCounter;
        long count = sc == null ? 0L : sc.get();
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 1; i < ws.length; i += 2) {
                WorkQueue w = ws[i];
                if (w != null) {
                    count += w.nsteals;
                }
            }
        }
        return count;
    }

    public long getQueuedTaskCount() {
        long count = 0;
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 1; i < ws.length; i += 2) {
                WorkQueue w = ws[i];
                if (w != null) {
                    count += w.queueSize();
                }
            }
        }
        return count;
    }

    public int getQueuedSubmissionCount() {
        int count = 0;
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 0; i < ws.length; i += 2) {
                WorkQueue w = ws[i];
                if (w != null) {
                    count += w.queueSize();
                }
            }
        }
        return count;
    }

    public boolean hasQueuedSubmissions() {
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 0; i < ws.length; i += 2) {
                WorkQueue w = ws[i];
                if (w != null && !w.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    protected ForkJoinTask<?> pollSubmission() {
        ForkJoinTask<?> t;
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 0; i < ws.length; i += 2) {
                WorkQueue w = ws[i];
                if (w != null && (t = w.poll()) != null) {
                    return t;
                }
            }
            return null;
        }
        return null;
    }

    protected int drainTasksTo(Collection<? super ForkJoinTask<?>> c) {
        int count = 0;
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (WorkQueue w : ws) {
                if (w != null) {
                    while (true) {
                        ForkJoinTask<?> t = w.poll();
                        if (t != null) {
                            c.add(t);
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public String toString() {
        long qt = 0;
        long qs = 0;
        int rc = 0;
        AtomicLong sc = this.stealCounter;
        long st = sc == null ? 0L : sc.get();
        long c = this.ctl;
        WorkQueue[] ws = this.workQueues;
        if (ws != null) {
            for (int i = 0; i < ws.length; i++) {
                WorkQueue w = ws[i];
                if (w != null) {
                    int size = w.queueSize();
                    if ((i & 1) == 0) {
                        qs += size;
                    } else {
                        qt += size;
                        st += w.nsteals;
                        if (w.isApparentlyUnblocked()) {
                            rc++;
                        }
                    }
                }
            }
        }
        int pc = this.config & 65535;
        int tc = pc + ((short) (c >>> 32));
        int ac = pc + ((int) (c >> 48));
        if (ac < 0) {
            ac = 0;
        }
        int rs = this.runState;
        String level = (rs & 1073741824) != 0 ? "Terminated" : (rs & 536870912) != 0 ? "Terminating" : (rs & Integer.MIN_VALUE) != 0 ? "Shutting down" : "Running";
        return super.toString() + PropertyAccessor.PROPERTY_KEY_PREFIX + level + ", parallelism = " + pc + ", size = " + tc + ", active = " + ac + ", running = " + rc + ", steals = " + st + ", tasks = " + qt + ", submissions = " + qs + "]";
    }

    @Override // java.util.concurrent.ExecutorService
    public void shutdown() {
        checkPermission();
        tryTerminate(false, true);
    }

    @Override // java.util.concurrent.ExecutorService
    public List<Runnable> shutdownNow() {
        checkPermission();
        tryTerminate(true, true);
        return Collections.emptyList();
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isTerminated() {
        return (this.runState & 1073741824) != 0;
    }

    public boolean isTerminating() {
        int rs = this.runState;
        return (rs & 536870912) != 0 && (rs & 1073741824) == 0;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean isShutdown() {
        return (this.runState & Integer.MIN_VALUE) != 0;
    }

    @Override // java.util.concurrent.ExecutorService
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (this == common) {
            awaitQuiescence(timeout, unit);
            return false;
        }
        long nanos = unit.toNanos(timeout);
        if (isTerminated()) {
            return true;
        }
        if (nanos <= 0) {
            return false;
        }
        long deadline = System.nanoTime() + nanos;
        synchronized (this) {
            while (!isTerminated()) {
                if (nanos <= 0) {
                    return false;
                }
                long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
                wait(millis > 0 ? millis : 1L);
                nanos = deadline - System.nanoTime();
            }
            return true;
        }
    }

    public boolean awaitQuiescence(long timeout, TimeUnit unit) {
        WorkQueue[] ws;
        int m;
        WorkQueue q;
        long nanos = unit.toNanos(timeout);
        Thread thread = Thread.currentThread();
        if (thread instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread wt = (ForkJoinWorkerThread) thread;
            if (wt.pool == this) {
                helpQuiescePool(wt.workQueue);
                return true;
            }
        }
        long startTime = System.nanoTime();
        int r = 0;
        boolean found = true;
        while (!isQuiescent() && (ws = this.workQueues) != null && (m = ws.length - 1) > 0) {
            if (!found) {
                if (System.nanoTime() - startTime > nanos) {
                    return false;
                }
                Thread.yield();
            }
            found = false;
            int j = (m + 1) << 2;
            while (true) {
                if (j >= 0) {
                    int i = r;
                    r++;
                    int k = i & m;
                    if (k <= m && k >= 0 && (q = ws[k]) != null) {
                        int b = q.base;
                        if (b - q.top < 0) {
                            found = true;
                            ForkJoinTask<?> t = q.pollAt(b);
                            if (t != null) {
                                t.doExec();
                            }
                        }
                    }
                    j--;
                }
            }
        }
        return true;
    }

    static void quiesceCommonPool() {
        common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    public static void managedBlock(ManagedBlocker blocker) throws InterruptedException {
        ForkJoinWorkerThread wt;
        ForkJoinPool p;
        Thread t = Thread.currentThread();
        if ((t instanceof ForkJoinWorkerThread) && (p = (wt = (ForkJoinWorkerThread) t).pool) != null) {
            WorkQueue w = wt.workQueue;
            while (!blocker.isReleasable()) {
                if (p.tryCompensate(w)) {
                    while (!blocker.isReleasable() && !blocker.block()) {
                        try {
                        } catch (Throwable th) {
                            U.getAndAddLong(p, CTL, AC_UNIT);
                            throw th;
                        }
                    }
                    U.getAndAddLong(p, CTL, AC_UNIT);
                    return;
                }
            }
            return;
        }
        while (!blocker.isReleasable() && !blocker.block()) {
        }
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new ForkJoinTask.AdaptedRunnable(runnable, value);
    }

    @Override // java.util.concurrent.AbstractExecutorService
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ForkJoinTask.AdaptedCallable(callable);
    }

    static {
        try {
            U = JSR166Helper.Unsafe.getUnsafe();
            CTL = U.objectFieldOffset(ForkJoinPool.class.getDeclaredField("ctl"));
            RUNSTATE = U.objectFieldOffset(ForkJoinPool.class.getDeclaredField("runState"));
            STEALCOUNTER = U.objectFieldOffset(ForkJoinPool.class.getDeclaredField("stealCounter"));
            PARKBLOCKER = U.objectFieldOffset(Thread.class.getDeclaredField("parkBlocker"));
            QTOP = U.objectFieldOffset(WorkQueue.class.getDeclaredField("top"));
            QLOCK = U.objectFieldOffset(WorkQueue.class.getDeclaredField("qlock"));
            QSCANSTATE = U.objectFieldOffset(WorkQueue.class.getDeclaredField("scanState"));
            QPARKER = U.objectFieldOffset(WorkQueue.class.getDeclaredField("parker"));
            QCURRENTSTEAL = U.objectFieldOffset(WorkQueue.class.getDeclaredField("currentSteal"));
            QCURRENTJOIN = U.objectFieldOffset(WorkQueue.class.getDeclaredField("currentJoin"));
            ABASE = U.arrayBaseOffset(ForkJoinTask[].class);
            int scale = U.arrayIndexScale(ForkJoinTask[].class);
            if ((scale & (scale - 1)) != 0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
            commonMaxSpares = 256;
            defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
            modifyThreadPermission = new RuntimePermission("modifyThread");
            common = (ForkJoinPool) AccessController.doPrivileged(new PrivilegedAction<ForkJoinPool>() { // from class: org.ehcache.impl.internal.concurrent.ForkJoinPool.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ForkJoinPool run() {
                    return ForkJoinPool.makeCommonPool();
                }
            });
            int par = common.config & 65535;
            commonParallelism = par > 0 ? par : 1;
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ForkJoinPool makeCommonPool() throws NumberFormatException {
        int parallelism = -1;
        ForkJoinWorkerThreadFactory factory = null;
        Thread.UncaughtExceptionHandler handler = null;
        try {
            String pp = System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism");
            String fp = System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
            String hp = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
            String mp = System.getProperty("java.util.concurrent.ForkJoinPool.common.maximumSpares");
            if (pp != null) {
                parallelism = Integer.parseInt(pp);
            }
            if (fp != null) {
                factory = (ForkJoinWorkerThreadFactory) ClassLoader.getSystemClassLoader().loadClass(fp).newInstance();
            }
            if (hp != null) {
                handler = (Thread.UncaughtExceptionHandler) ClassLoader.getSystemClassLoader().loadClass(hp).newInstance();
            }
            if (mp != null) {
                commonMaxSpares = Integer.parseInt(mp);
            }
        } catch (Exception e) {
        }
        if (factory == null) {
            if (System.getSecurityManager() == null) {
                factory = defaultForkJoinWorkerThreadFactory;
            } else {
                factory = new InnocuousForkJoinWorkerThreadFactory();
            }
        }
        if (parallelism < 0) {
            int iAvailableProcessors = Runtime.getRuntime().availableProcessors() - 1;
            parallelism = iAvailableProcessors;
            if (iAvailableProcessors <= 0) {
                parallelism = 1;
            }
        }
        if (parallelism > 32767) {
            parallelism = 32767;
        }
        return new ForkJoinPool(parallelism, factory, handler, 0, "ForkJoinPool.commonPool-worker-");
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinPool$InnocuousForkJoinWorkerThreadFactory.class */
    static final class InnocuousForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory {
        private static final AccessControlContext innocuousAcc;

        InnocuousForkJoinWorkerThreadFactory() {
        }

        static {
            Permissions innocuousPerms = new Permissions();
            innocuousPerms.add(ForkJoinPool.modifyThreadPermission);
            innocuousPerms.add(new RuntimePermission("enableContextClassLoaderOverride"));
            innocuousPerms.add(new RuntimePermission("modifyThreadGroup"));
            innocuousAcc = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, innocuousPerms)});
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
        public final ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
            return (ForkJoinWorkerThread.InnocuousForkJoinWorkerThread) AccessController.doPrivileged(new PrivilegedAction<ForkJoinWorkerThread>() { // from class: org.ehcache.impl.internal.concurrent.ForkJoinPool.InnocuousForkJoinWorkerThreadFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public ForkJoinWorkerThread run() {
                    return new ForkJoinWorkerThread.InnocuousForkJoinWorkerThread(pool);
                }
            }, innocuousAcc);
        }
    }
}
