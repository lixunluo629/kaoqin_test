package org.ehcache.impl.internal.concurrent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;
import org.ehcache.impl.internal.concurrent.ForkJoinPool;
import org.ehcache.impl.internal.concurrent.JSR166Helper;
import org.objectweb.asm.Opcodes;
import org.springframework.web.servlet.tags.BindTag;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinTask.class */
abstract class ForkJoinTask<V> implements Future<V>, Serializable {
    volatile int status;
    static final int DONE_MASK = -268435456;
    static final int NORMAL = -268435456;
    static final int CANCELLED = -1073741824;
    static final int EXCEPTIONAL = Integer.MIN_VALUE;
    static final int SIGNAL = 65536;
    static final int SMASK = 65535;
    private static final int EXCEPTION_MAP_CAPACITY = 32;
    private static final long serialVersionUID = -7721805057305804111L;
    private static final JSR166Helper.Unsafe U;
    private static final long STATUS;
    private static final ReentrantLock exceptionTableLock = new ReentrantLock();
    private static final ReferenceQueue<Object> exceptionTableRefQueue = new ReferenceQueue<>();
    private static final ExceptionNode[] exceptionTable = new ExceptionNode[32];

    public abstract V getRawResult();

    protected abstract void setRawResult(V v);

    protected abstract boolean exec();

    ForkJoinTask() {
    }

    private int setCompletion(int completion) {
        int s;
        do {
            s = this.status;
            if (s < 0) {
                return s;
            }
        } while (!U.compareAndSwapInt(this, STATUS, s, s | completion));
        if ((s >>> 16) != 0) {
            synchronized (this) {
                notifyAll();
            }
        }
        return completion;
    }

    final int doExec() {
        int i = this.status;
        int s = i;
        if (i >= 0) {
            try {
                boolean completed = exec();
                if (completed) {
                    s = setCompletion(-268435456);
                }
            } catch (Throwable rex) {
                return setExceptionalCompletion(rex);
            }
        }
        return s;
    }

    final void internalWait(long timeout) {
        int s = this.status;
        if (s >= 0 && U.compareAndSwapInt(this, STATUS, s, s | 65536)) {
            synchronized (this) {
                if (this.status >= 0) {
                    try {
                        wait(timeout);
                    } catch (InterruptedException e) {
                    }
                } else {
                    notifyAll();
                }
            }
        }
    }

    private int externalAwaitDone() {
        int i;
        int s = this instanceof CountedCompleter ? ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0) : ForkJoinPool.common.tryExternalUnpush(this) ? doExec() : 0;
        if (s >= 0) {
            int i2 = this.status;
            s = i2;
            if (i2 >= 0) {
                boolean interrupted = false;
                do {
                    if (U.compareAndSwapInt(this, STATUS, s, s | 65536)) {
                        synchronized (this) {
                            if (this.status >= 0) {
                                try {
                                    wait(0L);
                                } catch (InterruptedException e) {
                                    interrupted = true;
                                }
                            } else {
                                notifyAll();
                            }
                        }
                    }
                    i = this.status;
                    s = i;
                } while (i >= 0);
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return s;
    }

    private int externalInterruptibleAwaitDone() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int i = this.status;
        int s = i;
        if (i >= 0) {
            int iExternalHelpComplete = this instanceof CountedCompleter ? ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0) : ForkJoinPool.common.tryExternalUnpush(this) ? doExec() : 0;
            s = iExternalHelpComplete;
            if (iExternalHelpComplete >= 0) {
                while (true) {
                    int i2 = this.status;
                    s = i2;
                    if (i2 < 0) {
                        break;
                    }
                    if (U.compareAndSwapInt(this, STATUS, s, s | 65536)) {
                        synchronized (this) {
                            if (this.status >= 0) {
                                wait(0L);
                            } else {
                                notifyAll();
                            }
                        }
                    }
                }
            }
        }
        return s;
    }

    private int doJoin() {
        int s;
        int s2 = this.status;
        if (s2 < 0) {
            return s2;
        }
        Thread t = Thread.currentThread();
        if (!(t instanceof ForkJoinWorkerThread)) {
            return externalAwaitDone();
        }
        ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
        ForkJoinPool.WorkQueue w = wt.workQueue;
        return (!w.tryUnpush(this) || (s = doExec()) >= 0) ? wt.pool.awaitJoin(w, this, 0L) : s;
    }

    private int doInvoke() {
        int s = doExec();
        if (s < 0) {
            return s;
        }
        Thread t = Thread.currentThread();
        if (!(t instanceof ForkJoinWorkerThread)) {
            return externalAwaitDone();
        }
        ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
        return wt.pool.awaitJoin(wt.workQueue, this, 0L);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinTask$ExceptionNode.class */
    static final class ExceptionNode extends WeakReference<ForkJoinTask<?>> {
        final Throwable ex;
        ExceptionNode next;
        final long thrower;
        final int hashCode;

        ExceptionNode(ForkJoinTask<?> task, Throwable ex, ExceptionNode next) {
            super(task, ForkJoinTask.exceptionTableRefQueue);
            this.ex = ex;
            this.next = next;
            this.thrower = Thread.currentThread().getId();
            this.hashCode = System.identityHashCode(task);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0035, code lost:
    
        r0[r0] = new org.ehcache.impl.internal.concurrent.ForkJoinTask.ExceptionNode(r9, r10, r0[r0]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    final int recordExceptionalCompletion(java.lang.Throwable r10) {
        /*
            r9 = this;
            r0 = r9
            int r0 = r0.status
            r1 = r0
            r11 = r1
            if (r0 < 0) goto L7a
            r0 = r9
            int r0 = java.lang.System.identityHashCode(r0)
            r12 = r0
            java.util.concurrent.locks.ReentrantLock r0 = org.ehcache.impl.internal.concurrent.ForkJoinTask.exceptionTableLock
            r13 = r0
            r0 = r13
            r0.lock()
            expungeStaleExceptions()     // Catch: java.lang.Throwable -> L69
            org.ehcache.impl.internal.concurrent.ForkJoinTask$ExceptionNode[] r0 = org.ehcache.impl.internal.concurrent.ForkJoinTask.exceptionTable     // Catch: java.lang.Throwable -> L69
            r14 = r0
            r0 = r12
            r1 = r14
            int r1 = r1.length     // Catch: java.lang.Throwable -> L69
            r2 = 1
            int r1 = r1 - r2
            r0 = r0 & r1
            r15 = r0
            r0 = r14
            r1 = r15
            r0 = r0[r1]     // Catch: java.lang.Throwable -> L69
            r16 = r0
        L30:
            r0 = r16
            if (r0 != 0) goto L4b
            r0 = r14
            r1 = r15
            org.ehcache.impl.internal.concurrent.ForkJoinTask$ExceptionNode r2 = new org.ehcache.impl.internal.concurrent.ForkJoinTask$ExceptionNode     // Catch: java.lang.Throwable -> L69
            r3 = r2
            r4 = r9
            r5 = r10
            r6 = r14
            r7 = r15
            r6 = r6[r7]     // Catch: java.lang.Throwable -> L69
            r3.<init>(r4, r5, r6)     // Catch: java.lang.Throwable -> L69
            r0[r1] = r2     // Catch: java.lang.Throwable -> L69
            goto L61
        L4b:
            r0 = r16
            java.lang.Object r0 = r0.get()     // Catch: java.lang.Throwable -> L69
            r1 = r9
            if (r0 != r1) goto L57
            goto L61
        L57:
            r0 = r16
            org.ehcache.impl.internal.concurrent.ForkJoinTask$ExceptionNode r0 = r0.next     // Catch: java.lang.Throwable -> L69
            r16 = r0
            goto L30
        L61:
            r0 = r13
            r0.unlock()
            goto L73
        L69:
            r17 = move-exception
            r0 = r13
            r0.unlock()
            r0 = r17
            throw r0
        L73:
            r0 = r9
            r1 = -2147483648(0xffffffff80000000, float:-0.0)
            int r0 = r0.setCompletion(r1)
            r11 = r0
        L7a:
            r0 = r11
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ehcache.impl.internal.concurrent.ForkJoinTask.recordExceptionalCompletion(java.lang.Throwable):int");
    }

    private int setExceptionalCompletion(Throwable ex) {
        int s = recordExceptionalCompletion(ex);
        if ((s & (-268435456)) == Integer.MIN_VALUE) {
            internalPropagateException(ex);
        }
        return s;
    }

    void internalPropagateException(Throwable ex) {
    }

    static final void cancelIgnoringExceptions(ForkJoinTask<?> t) {
        if (t != null && t.status >= 0) {
            try {
                t.cancel(false);
            } catch (Throwable th) {
            }
        }
    }

    private void clearExceptionalCompletion() {
        int h = System.identityHashCode(this);
        ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            ExceptionNode[] t = exceptionTable;
            int i = h & (t.length - 1);
            ExceptionNode e = t[i];
            ExceptionNode pred = null;
            while (true) {
                if (e == null) {
                    break;
                }
                ExceptionNode next = e.next;
                if (e.get() == this) {
                    if (pred == null) {
                        t[i] = next;
                    } else {
                        pred.next = next;
                    }
                } else {
                    pred = e;
                    e = next;
                }
            }
            expungeStaleExceptions();
            this.status = 0;
            lock.unlock();
        } catch (Throwable th) {
            lock.unlock();
            throw th;
        }
    }

    private Throwable getThrowableException() throws SecurityException {
        Throwable ex;
        Throwable wx;
        if ((this.status & (-268435456)) != Integer.MIN_VALUE) {
            return null;
        }
        int h = System.identityHashCode(this);
        ReentrantLock lock = exceptionTableLock;
        lock.lock();
        try {
            expungeStaleExceptions();
            ExceptionNode[] t = exceptionTable;
            ExceptionNode e = t[h & (t.length - 1)];
            while (e != null) {
                if (e.get() == this) {
                    break;
                }
                e = e.next;
            }
            if (e == null || (ex = e.ex) == null) {
                return null;
            }
            if (e.thrower != Thread.currentThread().getId()) {
                try {
                    Constructor<?> noArgCtor = null;
                    Constructor<?>[] cs = ex.getClass().getConstructors();
                    for (Constructor<?> c : cs) {
                        Class<?>[] ps = c.getParameterTypes();
                        if (ps.length == 0) {
                            noArgCtor = c;
                        } else if (ps.length == 1 && ps[0] == Throwable.class) {
                            Throwable wx2 = (Throwable) c.newInstance(ex);
                            return wx2 == null ? ex : wx2;
                        }
                    }
                    if (noArgCtor != null && (wx = (Throwable) noArgCtor.newInstance(new Object[0])) != null) {
                        wx.initCause(ex);
                        return wx;
                    }
                } catch (Exception e2) {
                }
            }
            return ex;
        } finally {
            lock.unlock();
        }
    }

    private static void expungeStaleExceptions() {
        while (true) {
            Object x = exceptionTableRefQueue.poll();
            if (x != null) {
                if (x instanceof ExceptionNode) {
                    int hashCode = ((ExceptionNode) x).hashCode;
                    ExceptionNode[] t = exceptionTable;
                    int i = hashCode & (t.length - 1);
                    ExceptionNode e = t[i];
                    ExceptionNode pred = null;
                    while (true) {
                        if (e != null) {
                            ExceptionNode next = e.next;
                            if (e == x) {
                                if (pred == null) {
                                    t[i] = next;
                                } else {
                                    pred.next = next;
                                }
                            } else {
                                pred = e;
                                e = next;
                            }
                        }
                    }
                }
            } else {
                return;
            }
        }
    }

    static final void helpExpungeStaleExceptions() {
        ReentrantLock lock = exceptionTableLock;
        if (lock.tryLock()) {
            try {
                expungeStaleExceptions();
                lock.unlock();
            } catch (Throwable th) {
                lock.unlock();
                throw th;
            }
        }
    }

    static void rethrow(Throwable ex) {
        if (ex != null) {
            uncheckedThrow(ex);
        }
    }

    static <T extends Throwable> void uncheckedThrow(Throwable t) throws Throwable {
        throw t;
    }

    private void reportException(int s) {
        if (s == CANCELLED) {
            throw new CancellationException();
        }
        if (s == Integer.MIN_VALUE) {
            rethrow(getThrowableException());
        }
    }

    public final ForkJoinTask<V> fork() {
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            ((ForkJoinWorkerThread) t).workQueue.push(this);
        } else {
            ForkJoinPool.common.externalPush(this);
        }
        return this;
    }

    public final V join() {
        int s = doJoin() & (-268435456);
        if (s != -268435456) {
            reportException(s);
        }
        return getRawResult();
    }

    public final V invoke() {
        int s = doInvoke() & (-268435456);
        if (s != -268435456) {
            reportException(s);
        }
        return getRawResult();
    }

    public static void invokeAll(ForkJoinTask<?> t1, ForkJoinTask<?> t2) {
        t2.fork();
        int s1 = t1.doInvoke() & (-268435456);
        if (s1 != -268435456) {
            t1.reportException(s1);
        }
        int s2 = t2.doJoin() & (-268435456);
        if (s2 != -268435456) {
            t2.reportException(s2);
        }
    }

    public static void invokeAll(ForkJoinTask<?>... tasks) {
        Throwable ex = null;
        int last = tasks.length - 1;
        for (int i = last; i >= 0; i--) {
            ForkJoinTask<?> t = tasks[i];
            if (t == null) {
                if (ex == null) {
                    ex = new NullPointerException();
                }
            } else if (i != 0) {
                t.fork();
            } else if (t.doInvoke() < -268435456 && ex == null) {
                ex = t.getException();
            }
        }
        for (int i2 = 1; i2 <= last; i2++) {
            ForkJoinTask<?> t2 = tasks[i2];
            if (t2 != null) {
                if (ex != null) {
                    t2.cancel(false);
                } else if (t2.doJoin() < -268435456) {
                    ex = t2.getException();
                }
            }
        }
        if (ex != null) {
            rethrow(ex);
        }
    }

    public static <T extends ForkJoinTask<?>> Collection<T> invokeAll(Collection<T> tasks) {
        if (!(tasks instanceof RandomAccess) || !(tasks instanceof List)) {
            invokeAll((ForkJoinTask<?>[]) tasks.toArray(new ForkJoinTask[tasks.size()]));
            return tasks;
        }
        List<? extends ForkJoinTask<?>> ts = (List) tasks;
        Throwable ex = null;
        int last = ts.size() - 1;
        for (int i = last; i >= 0; i--) {
            ForkJoinTask<?> t = ts.get(i);
            if (t == null) {
                if (ex == null) {
                    ex = new NullPointerException();
                }
            } else if (i != 0) {
                t.fork();
            } else if (t.doInvoke() < -268435456 && ex == null) {
                ex = t.getException();
            }
        }
        for (int i2 = 1; i2 <= last; i2++) {
            ForkJoinTask<?> t2 = ts.get(i2);
            if (t2 != null) {
                if (ex != null) {
                    t2.cancel(false);
                } else if (t2.doJoin() < -268435456) {
                    ex = t2.getException();
                }
            }
        }
        if (ex != null) {
            rethrow(ex);
        }
        return tasks;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean mayInterruptIfRunning) {
        return (setCompletion(CANCELLED) & (-268435456)) == CANCELLED;
    }

    @Override // java.util.concurrent.Future
    public final boolean isDone() {
        return this.status < 0;
    }

    @Override // java.util.concurrent.Future
    public final boolean isCancelled() {
        return (this.status & (-268435456)) == CANCELLED;
    }

    public final boolean isCompletedAbnormally() {
        return this.status < -268435456;
    }

    public final boolean isCompletedNormally() {
        return (this.status & (-268435456)) == -268435456;
    }

    public final Throwable getException() {
        int s = this.status & (-268435456);
        if (s >= -268435456) {
            return null;
        }
        return s == CANCELLED ? new CancellationException() : getThrowableException();
    }

    public void completeExceptionally(Throwable ex) {
        setExceptionalCompletion(((ex instanceof RuntimeException) || (ex instanceof Error)) ? ex : new RuntimeException(ex));
    }

    public void complete(V value) {
        try {
            setRawResult(value);
            setCompletion(-268435456);
        } catch (Throwable rex) {
            setExceptionalCompletion(rex);
        }
    }

    public final void quietlyComplete() {
        setCompletion(-268435456);
    }

    @Override // java.util.concurrent.Future
    public final V get() throws ExecutionException, InterruptedException {
        Throwable ex;
        int s = (Thread.currentThread() instanceof ForkJoinWorkerThread ? doJoin() : externalInterruptibleAwaitDone()) & (-268435456);
        if (s == CANCELLED) {
            throw new CancellationException();
        }
        if (s == Integer.MIN_VALUE && (ex = getThrowableException()) != null) {
            throw new ExecutionException(ex);
        }
        return getRawResult();
    }

    @Override // java.util.concurrent.Future
    public final V get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException, SecurityException {
        int i;
        long nanos = unit.toNanos(timeout);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        int i2 = this.status;
        int s = i2;
        if (i2 >= 0 && nanos > 0) {
            long d = System.nanoTime() + nanos;
            long deadline = d == 0 ? 1L : d;
            Thread t = Thread.currentThread();
            if (t instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
                s = wt.pool.awaitJoin(wt.workQueue, this, deadline);
            } else {
                int iExternalHelpComplete = this instanceof CountedCompleter ? ForkJoinPool.common.externalHelpComplete((CountedCompleter) this, 0) : ForkJoinPool.common.tryExternalUnpush(this) ? doExec() : 0;
                s = iExternalHelpComplete;
                if (iExternalHelpComplete >= 0) {
                    while (true) {
                        int i3 = this.status;
                        s = i3;
                        if (i3 < 0) {
                            break;
                        }
                        long ns = i;
                        if (deadline - System.nanoTime() <= 0) {
                            break;
                        }
                        long ms = TimeUnit.NANOSECONDS.toMillis(ns);
                        if (i > 0) {
                            i = s;
                            if (U.compareAndSwapInt(this, STATUS, i, s | 65536)) {
                                synchronized (this) {
                                    if (this.status >= 0) {
                                        wait(ms);
                                    } else {
                                        notifyAll();
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        if (s >= 0) {
            s = this.status;
        }
        int s2 = s & (-268435456);
        if (s2 != -268435456) {
            if (s2 == CANCELLED) {
                throw new CancellationException();
            }
            if (s2 != Integer.MIN_VALUE) {
                throw new TimeoutException();
            }
            Throwable ex = getThrowableException();
            if (ex != null) {
                throw new ExecutionException(ex);
            }
        }
        return getRawResult();
    }

    public final void quietlyJoin() {
        doJoin();
    }

    public final void quietlyInvoke() {
        doInvoke();
    }

    public static void helpQuiesce() {
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
            wt.pool.helpQuiescePool(wt.workQueue);
        } else {
            ForkJoinPool.quiesceCommonPool();
        }
    }

    public void reinitialize() {
        if ((this.status & (-268435456)) == Integer.MIN_VALUE) {
            clearExceptionalCompletion();
        } else {
            this.status = 0;
        }
    }

    public static ForkJoinPool getPool() {
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) t).pool;
        }
        return null;
    }

    public static boolean inForkJoinPool() {
        return Thread.currentThread() instanceof ForkJoinWorkerThread;
    }

    public boolean tryUnfork() {
        Thread t = Thread.currentThread();
        return t instanceof ForkJoinWorkerThread ? ((ForkJoinWorkerThread) t).workQueue.tryUnpush(this) : ForkJoinPool.common.tryExternalUnpush(this);
    }

    public static int getQueuedTaskCount() {
        ForkJoinPool.WorkQueue q;
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            q = ((ForkJoinWorkerThread) t).workQueue;
        } else {
            q = ForkJoinPool.commonSubmitterQueue();
        }
        if (q == null) {
            return 0;
        }
        return q.queueSize();
    }

    public static int getSurplusQueuedTaskCount() {
        return ForkJoinPool.getSurplusQueuedTaskCount();
    }

    protected static ForkJoinTask<?> peekNextLocalTask() {
        ForkJoinPool.WorkQueue q;
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            q = ((ForkJoinWorkerThread) t).workQueue;
        } else {
            q = ForkJoinPool.commonSubmitterQueue();
        }
        if (q == null) {
            return null;
        }
        return q.peek();
    }

    protected static ForkJoinTask<?> pollNextLocalTask() {
        Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            return ((ForkJoinWorkerThread) t).workQueue.nextLocalTask();
        }
        return null;
    }

    protected static ForkJoinTask<?> pollTask() {
        Thread t = Thread.currentThread();
        if (!(t instanceof ForkJoinWorkerThread)) {
            return null;
        }
        ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
        return wt.pool.nextTaskFor(wt.workQueue);
    }

    public final short getForkJoinTaskTag() {
        return (short) this.status;
    }

    public final short setForkJoinTaskTag(short tag) {
        JSR166Helper.Unsafe unsafe;
        long j;
        int s;
        do {
            unsafe = U;
            j = STATUS;
            s = this.status;
        } while (!unsafe.compareAndSwapInt(this, j, s, (s & Opcodes.V_PREVIEW_EXPERIMENTAL) | (tag & 65535)));
        return (short) s;
    }

    public final boolean compareAndSetForkJoinTaskTag(short e, short tag) {
        int s;
        do {
            s = this.status;
            if (((short) s) != e) {
                return false;
            }
        } while (!U.compareAndSwapInt(this, STATUS, s, (s & Opcodes.V_PREVIEW_EXPERIMENTAL) | (tag & 65535)));
        return true;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinTask$AdaptedRunnable.class */
    static final class AdaptedRunnable<T> extends ForkJoinTask<T> implements RunnableFuture<T> {
        final Runnable runnable;
        T result;
        private static final long serialVersionUID = 5232453952276885070L;

        AdaptedRunnable(Runnable runnable, T result) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
            this.result = result;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final T getRawResult() {
            return this.result;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final void setRawResult(T v) {
            this.result = v;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public final void run() {
            invoke();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinTask$AdaptedRunnableAction.class */
    static final class AdaptedRunnableAction extends ForkJoinTask<Void> implements RunnableFuture<Void> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;

        AdaptedRunnableAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final void setRawResult(Void v) {
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public final void run() {
            invoke();
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinTask$RunnableExecuteAction.class */
    static final class RunnableExecuteAction extends ForkJoinTask<Void> {
        final Runnable runnable;
        private static final long serialVersionUID = 5232453952276885070L;

        RunnableExecuteAction(Runnable runnable) {
            if (runnable == null) {
                throw new NullPointerException();
            }
            this.runnable = runnable;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final Void getRawResult() {
            return null;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final void setRawResult(Void v) {
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final boolean exec() {
            this.runnable.run();
            return true;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        void internalPropagateException(Throwable ex) {
            rethrow(ex);
        }
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/ForkJoinTask$AdaptedCallable.class */
    static final class AdaptedCallable<T> extends ForkJoinTask<T> implements RunnableFuture<T> {
        final Callable<? extends T> callable;
        T result;
        private static final long serialVersionUID = 2838392045355241008L;

        AdaptedCallable(Callable<? extends T> callable) {
            if (callable == null) {
                throw new NullPointerException();
            }
            this.callable = callable;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final T getRawResult() {
            return this.result;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final void setRawResult(T v) {
            this.result = v;
        }

        @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
        public final boolean exec() {
            try {
                this.result = this.callable.call();
                return true;
            } catch (Error err) {
                throw err;
            } catch (RuntimeException rex) {
                throw rex;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        @Override // java.util.concurrent.RunnableFuture, java.lang.Runnable
        public final void run() {
            invoke();
        }
    }

    public static ForkJoinTask<?> adapt(Runnable runnable) {
        return new AdaptedRunnableAction(runnable);
    }

    public static <T> ForkJoinTask<T> adapt(Runnable runnable, T result) {
        return new AdaptedRunnable(runnable, result);
    }

    public static <T> ForkJoinTask<T> adapt(Callable<? extends T> callable) {
        return new AdaptedCallable(callable);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeObject(getException());
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();
        Object ex = s.readObject();
        if (ex != null) {
            setExceptionalCompletion((Throwable) ex);
        }
    }

    static {
        try {
            U = JSR166Helper.Unsafe.getUnsafe();
            STATUS = U.objectFieldOffset(ForkJoinTask.class.getDeclaredField(BindTag.STATUS_VARIABLE_NAME));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
