package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.concurrent.GuardedBy;
import org.aspectj.lang.JoinPoint;
import redis.clients.jedis.Protocol;

@Beta
/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Monitor.class */
public final class Monitor {
    private final boolean fair;
    private final ReentrantLock lock;

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private Guard activeGuards;

    @Beta
    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/Monitor$Guard.class */
    public static abstract class Guard {
        final Monitor monitor;
        final Condition condition;

        @GuardedBy("monitor.lock")
        int waiterCount = 0;

        @GuardedBy("monitor.lock")
        Guard next;

        public abstract boolean isSatisfied();

        protected Guard(Monitor monitor) {
            this.monitor = (Monitor) Preconditions.checkNotNull(monitor, Protocol.SENTINEL_MONITOR);
            this.condition = monitor.lock.newCondition();
        }
    }

    public Monitor() {
        this(false);
    }

    public Monitor(boolean fair) {
        this.activeGuards = null;
        this.fair = fair;
        this.lock = new ReentrantLock(fair);
    }

    public void enter() {
        this.lock.lock();
    }

    public void enterInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean enter(long time, TimeUnit unit) {
        boolean zTryLock;
        long timeoutNanos = unit.toNanos(time);
        ReentrantLock lock = this.lock;
        if (!this.fair && lock.tryLock()) {
            return true;
        }
        long deadline = System.nanoTime() + timeoutNanos;
        boolean interrupted = Thread.interrupted();
        while (true) {
            try {
                try {
                    zTryLock = lock.tryLock(timeoutNanos, TimeUnit.NANOSECONDS);
                    break;
                } catch (InterruptedException e) {
                    interrupted = true;
                    timeoutNanos = deadline - System.nanoTime();
                }
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return zTryLock;
    }

    public boolean enterInterruptibly(long time, TimeUnit unit) throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public boolean tryEnter() {
        return this.lock.tryLock();
    }

    public void enterWhen(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                await(guard, signalBeforeWaiting);
            }
            satisfied = true;
        } finally {
            if (!satisfied) {
                leave();
            }
        }
    }

    public void enterWhenUninterruptibly(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        boolean signalBeforeWaiting = lock.isHeldByCurrentThread();
        lock.lock();
        boolean satisfied = false;
        try {
            if (!guard.isSatisfied()) {
                awaitUninterruptibly(guard, signalBeforeWaiting);
            }
            satisfied = true;
        } finally {
            if (!satisfied) {
                leave();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x006a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean enterWhen(com.google.common.util.concurrent.Monitor.Guard r7, long r8, java.util.concurrent.TimeUnit r10) throws java.lang.InterruptedException {
        /*
            r6 = this;
            r0 = r10
            r1 = r8
            long r0 = r0.toNanos(r1)
            r11 = r0
            r0 = r7
            com.google.common.util.concurrent.Monitor r0 = r0.monitor
            r1 = r6
            if (r0 == r1) goto L18
            java.lang.IllegalMonitorStateException r0 = new java.lang.IllegalMonitorStateException
            r1 = r0
            r1.<init>()
            throw r0
        L18:
            r0 = r6
            java.util.concurrent.locks.ReentrantLock r0 = r0.lock
            r13 = r0
            r0 = r13
            boolean r0 = r0.isHeldByCurrentThread()
            r14 = r0
            r0 = r6
            boolean r0 = r0.fair
            if (r0 != 0) goto L34
            r0 = r13
            boolean r0 = r0.tryLock()
            if (r0 != 0) goto L51
        L34:
            long r0 = java.lang.System.nanoTime()
            r1 = r11
            long r0 = r0 + r1
            r15 = r0
            r0 = r13
            r1 = r8
            r2 = r10
            boolean r0 = r0.tryLock(r1, r2)
            if (r0 != 0) goto L49
            r0 = 0
            return r0
        L49:
            r0 = r15
            long r1 = java.lang.System.nanoTime()
            long r0 = r0 - r1
            r11 = r0
        L51:
            r0 = 0
            r15 = r0
            r0 = 1
            r16 = r0
            r0 = r7
            boolean r0 = r0.isSatisfied()     // Catch: java.lang.Throwable -> La0
            if (r0 != 0) goto L6a
            r0 = r6
            r1 = r7
            r2 = r11
            r3 = r14
            boolean r0 = r0.awaitNanos(r1, r2, r3)     // Catch: java.lang.Throwable -> La0
            if (r0 == 0) goto L6e
        L6a:
            r0 = 1
            goto L6f
        L6e:
            r0 = 0
        L6f:
            r15 = r0
            r0 = 0
            r16 = r0
            r0 = r15
            r17 = r0
            r0 = r15
            if (r0 != 0) goto L9d
            r0 = r16
            if (r0 == 0) goto L8b
            r0 = r14
            if (r0 != 0) goto L8b
            r0 = r6
            r0.signalNextWaiter()     // Catch: java.lang.Throwable -> L93
        L8b:
            r0 = r13
            r0.unlock()
            goto L9d
        L93:
            r18 = move-exception
            r0 = r13
            r0.unlock()
            r0 = r18
            throw r0
        L9d:
            r0 = r17
            return r0
        La0:
            r19 = move-exception
            r0 = r15
            if (r0 != 0) goto Lc7
            r0 = r16
            if (r0 == 0) goto Lb5
            r0 = r14
            if (r0 != 0) goto Lb5
            r0 = r6
            r0.signalNextWaiter()     // Catch: java.lang.Throwable -> Lbd
        Lb5:
            r0 = r13
            r0.unlock()
            goto Lc7
        Lbd:
            r20 = move-exception
            r0 = r13
            r0.unlock()
            r0 = r20
            throw r0
        Lc7:
            r0 = r19
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterWhen(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0091, code lost:
    
        r0 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean enterWhenUninterruptibly(com.google.common.util.concurrent.Monitor.Guard r7, long r8, java.util.concurrent.TimeUnit r10) {
        /*
            Method dump skipped, instructions count: 229
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Monitor.enterWhenUninterruptibly(com.google.common.util.concurrent.Monitor$Guard, long, java.util.concurrent.TimeUnit):boolean");
    }

    public boolean enterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lock();
        boolean satisfied = false;
        try {
            boolean zIsSatisfied = guard.isSatisfied();
            satisfied = zIsSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return zIsSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public boolean enterIfInterruptibly(Guard guard) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        boolean satisfied = false;
        try {
            boolean zIsSatisfied = guard.isSatisfied();
            satisfied = zIsSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return zIsSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public boolean enterIf(Guard guard, long time, TimeUnit unit) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        if (!enter(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean zIsSatisfied = guard.isSatisfied();
            satisfied = zIsSatisfied;
            if (!satisfied) {
                this.lock.unlock();
            }
            return zIsSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                this.lock.unlock();
            }
            throw th;
        }
    }

    public boolean enterIfInterruptibly(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock(time, unit)) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean zIsSatisfied = guard.isSatisfied();
            satisfied = zIsSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return zIsSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public boolean tryEnterIf(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        ReentrantLock lock = this.lock;
        if (!lock.tryLock()) {
            return false;
        }
        boolean satisfied = false;
        try {
            boolean zIsSatisfied = guard.isSatisfied();
            satisfied = zIsSatisfied;
            if (!satisfied) {
                lock.unlock();
            }
            return zIsSatisfied;
        } catch (Throwable th) {
            if (!satisfied) {
                lock.unlock();
            }
            throw th;
        }
    }

    public void waitFor(Guard guard) throws InterruptedException {
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            await(guard, true);
        }
    }

    public void waitForUninterruptibly(Guard guard) {
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (!guard.isSatisfied()) {
            awaitUninterruptibly(guard, true);
        }
    }

    public boolean waitFor(Guard guard, long time, TimeUnit unit) throws InterruptedException {
        long timeoutNanos = unit.toNanos(time);
        if (!(guard.monitor == this) || !this.lock.isHeldByCurrentThread()) {
            throw new IllegalMonitorStateException();
        }
        return guard.isSatisfied() || awaitNanos(guard, timeoutNanos, true);
    }

    public boolean waitForUninterruptibly(Guard guard, long time, TimeUnit unit) {
        long timeoutNanos = unit.toNanos(time);
        if (!((guard.monitor == this) & this.lock.isHeldByCurrentThread())) {
            throw new IllegalMonitorStateException();
        }
        if (guard.isSatisfied()) {
            return true;
        }
        boolean signalBeforeWaiting = true;
        long deadline = System.nanoTime() + timeoutNanos;
        boolean interrupted = Thread.interrupted();
        while (true) {
            try {
                try {
                    boolean zAwaitNanos = awaitNanos(guard, timeoutNanos, signalBeforeWaiting);
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                    return zAwaitNanos;
                } catch (InterruptedException e) {
                    interrupted = true;
                    if (!guard.isSatisfied()) {
                        signalBeforeWaiting = false;
                        timeoutNanos = deadline - System.nanoTime();
                    } else {
                        if (1 != 0) {
                            Thread.currentThread().interrupt();
                        }
                        return true;
                    }
                }
            } catch (Throwable th) {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
                throw th;
            }
        }
    }

    public void leave() {
        ReentrantLock lock = this.lock;
        try {
            if (lock.getHoldCount() == 1) {
                signalNextWaiter();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean isFair() {
        return this.fair;
    }

    public boolean isOccupied() {
        return this.lock.isLocked();
    }

    public boolean isOccupiedByCurrentThread() {
        return this.lock.isHeldByCurrentThread();
    }

    public int getOccupiedDepth() {
        return this.lock.getHoldCount();
    }

    public int getQueueLength() {
        return this.lock.getQueueLength();
    }

    public boolean hasQueuedThreads() {
        return this.lock.hasQueuedThreads();
    }

    public boolean hasQueuedThread(Thread thread) {
        return this.lock.hasQueuedThread(thread);
    }

    public boolean hasWaiters(Guard guard) {
        return getWaitQueueLength(guard) > 0;
    }

    public int getWaitQueueLength(Guard guard) {
        if (guard.monitor != this) {
            throw new IllegalMonitorStateException();
        }
        this.lock.lock();
        try {
            int i = guard.waiterCount;
            this.lock.unlock();
            return i;
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private void signalNextWaiter() {
        Guard guard = this.activeGuards;
        while (true) {
            Guard guard2 = guard;
            if (guard2 != null) {
                if (!isSatisfied(guard2)) {
                    guard = guard2.next;
                } else {
                    guard2.condition.signal();
                    return;
                }
            } else {
                return;
            }
        }
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private boolean isSatisfied(Guard guard) {
        try {
            return guard.isSatisfied();
        } catch (Throwable throwable) {
            signalAllWaiters();
            throw Throwables.propagate(throwable);
        }
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private void signalAllWaiters() {
        Guard guard = this.activeGuards;
        while (true) {
            Guard guard2 = guard;
            if (guard2 != null) {
                guard2.condition.signalAll();
                guard = guard2.next;
            } else {
                return;
            }
        }
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private void beginWaitingFor(Guard guard) {
        int waiters = guard.waiterCount;
        guard.waiterCount = waiters + 1;
        if (waiters == 0) {
            guard.next = this.activeGuards;
            this.activeGuards = guard;
        }
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private void endWaitingFor(Guard guard) {
        int waiters = guard.waiterCount - 1;
        guard.waiterCount = waiters;
        if (waiters == 0) {
            Guard p = this.activeGuards;
            Guard pred = null;
            while (p != guard) {
                pred = p;
                p = p.next;
            }
            if (pred == null) {
                this.activeGuards = p.next;
            } else {
                pred.next = p.next;
            }
            p.next = null;
        }
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private void await(Guard guard, boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        do {
            try {
                guard.condition.await();
            } finally {
                endWaitingFor(guard);
            }
        } while (!guard.isSatisfied());
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private void awaitUninterruptibly(Guard guard, boolean signalBeforeWaiting) {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        do {
            try {
                guard.condition.awaitUninterruptibly();
            } finally {
                endWaitingFor(guard);
            }
        } while (!guard.isSatisfied());
    }

    @GuardedBy(JoinPoint.SYNCHRONIZATION_LOCK)
    private boolean awaitNanos(Guard guard, long nanos, boolean signalBeforeWaiting) throws InterruptedException {
        if (signalBeforeWaiting) {
            signalNextWaiter();
        }
        beginWaitingFor(guard);
        while (nanos >= 0) {
            try {
                nanos = guard.condition.awaitNanos(nanos);
                if (guard.isSatisfied()) {
                    endWaitingFor(guard);
                    return true;
                }
            } finally {
                endWaitingFor(guard);
            }
        }
        return false;
    }
}
