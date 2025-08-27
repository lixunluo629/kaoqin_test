package org.ehcache.impl.internal.concurrent;

import org.ehcache.impl.internal.concurrent.JSR166Helper;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/concurrent/CountedCompleter.class */
abstract class CountedCompleter<T> extends ForkJoinTask<T> {
    private static final long serialVersionUID = 5232453752276485070L;
    final CountedCompleter<?> completer;
    volatile int pending;
    private static final JSR166Helper.Unsafe U;
    private static final long PENDING;

    public abstract void compute();

    protected CountedCompleter(CountedCompleter<?> completer, int initialPendingCount) {
        this.completer = completer;
        this.pending = initialPendingCount;
    }

    protected CountedCompleter(CountedCompleter<?> completer) {
        this.completer = completer;
    }

    protected CountedCompleter() {
        this.completer = null;
    }

    public void onCompletion(CountedCompleter<?> caller) {
    }

    public boolean onExceptionalCompletion(Throwable ex, CountedCompleter<?> caller) {
        return true;
    }

    public final CountedCompleter<?> getCompleter() {
        return this.completer;
    }

    public final int getPendingCount() {
        return this.pending;
    }

    public final void setPendingCount(int count) {
        this.pending = count;
    }

    public final void addToPendingCount(int delta) {
        U.getAndAddInt(this, PENDING, delta);
    }

    public final boolean compareAndSetPendingCount(int expected, int count) {
        return U.compareAndSwapInt(this, PENDING, expected, count);
    }

    public final int decrementPendingCountUnlessZero() {
        int c;
        do {
            c = this.pending;
            if (c == 0) {
                break;
            }
        } while (!U.compareAndSwapInt(this, PENDING, c, c - 1));
        return c;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final CountedCompleter<?> getRoot() {
        CountedCompleter countedCompleter = this;
        while (true) {
            CountedCompleter countedCompleter2 = countedCompleter;
            CountedCompleter countedCompleter3 = countedCompleter2.completer;
            if (countedCompleter3 != null) {
                countedCompleter = countedCompleter3;
            } else {
                return countedCompleter2;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void tryComplete() {
        CountedCompleter a = this;
        CountedCompleter s = a;
        while (true) {
            int c = a.pending;
            if (c == 0) {
                a.onCompletion(s);
                CountedCompleter countedCompleter = a;
                s = countedCompleter;
                CountedCompleter countedCompleter2 = countedCompleter.completer;
                a = countedCompleter2;
                if (countedCompleter2 == null) {
                    s.quietlyComplete();
                    return;
                }
            } else if (U.compareAndSwapInt(a, PENDING, c, c - 1)) {
                return;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void propagateCompletion() {
        CountedCompleter countedCompleter = this;
        while (true) {
            int c = countedCompleter.pending;
            if (c == 0) {
                CountedCompleter countedCompleter2 = countedCompleter;
                CountedCompleter countedCompleter3 = countedCompleter2.completer;
                countedCompleter = countedCompleter3;
                if (countedCompleter3 == null) {
                    countedCompleter2.quietlyComplete();
                    return;
                }
            } else if (U.compareAndSwapInt(countedCompleter, PENDING, c, c - 1)) {
                return;
            }
        }
    }

    @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
    public void complete(T rawResult) {
        setRawResult(rawResult);
        onCompletion(this);
        quietlyComplete();
        CountedCompleter<?> p = this.completer;
        if (p != null) {
            p.tryComplete();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final CountedCompleter<?> firstComplete() {
        int c;
        do {
            c = this.pending;
            if (c == 0) {
                return this;
            }
        } while (!U.compareAndSwapInt(this, PENDING, c, c - 1));
        return null;
    }

    public final CountedCompleter<?> nextComplete() {
        CountedCompleter<?> p = this.completer;
        if (p != null) {
            return p.firstComplete();
        }
        quietlyComplete();
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void quietlyCompleteRoot() {
        CountedCompleter countedCompleter = this;
        while (true) {
            CountedCompleter countedCompleter2 = countedCompleter;
            CountedCompleter countedCompleter3 = countedCompleter2.completer;
            if (countedCompleter3 == null) {
                countedCompleter2.quietlyComplete();
                return;
            }
            countedCompleter = countedCompleter3;
        }
    }

    public final void helpComplete(int maxTasks) {
        if (maxTasks > 0 && this.status >= 0) {
            Thread t = Thread.currentThread();
            if (t instanceof ForkJoinWorkerThread) {
                ForkJoinWorkerThread wt = (ForkJoinWorkerThread) t;
                wt.pool.helpComplete(wt.workQueue, this, maxTasks);
            } else {
                ForkJoinPool.common.externalHelpComplete(this, maxTasks);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
    void internalPropagateException(Throwable ex) {
        CountedCompleter a = this;
        CountedCompleter s = a;
        while (a.onExceptionalCompletion(ex, s)) {
            CountedCompleter countedCompleter = a;
            s = countedCompleter;
            CountedCompleter countedCompleter2 = countedCompleter.completer;
            a = countedCompleter2;
            if (countedCompleter2 == null || a.status < 0 || a.recordExceptionalCompletion(ex) != Integer.MIN_VALUE) {
                return;
            }
        }
    }

    @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
    protected final boolean exec() {
        compute();
        return false;
    }

    @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
    public T getRawResult() {
        return null;
    }

    @Override // org.ehcache.impl.internal.concurrent.ForkJoinTask
    protected void setRawResult(T t) {
    }

    static {
        try {
            U = JSR166Helper.Unsafe.getUnsafe();
            PENDING = U.objectFieldOffset(CountedCompleter.class.getDeclaredField("pending"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
