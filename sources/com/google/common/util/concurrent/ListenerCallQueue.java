package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ListenerCallQueue.class */
final class ListenerCallQueue<L> implements Runnable {
    private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
    private final L listener;
    private final Executor executor;

    @GuardedBy(OgnlContext.THIS_CONTEXT_KEY)
    private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();

    @GuardedBy(OgnlContext.THIS_CONTEXT_KEY)
    private boolean isThreadScheduled;

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ListenerCallQueue$Callback.class */
    static abstract class Callback<L> {
        private final String methodCall;

        abstract void call(L l);

        Callback(String methodCall) {
            this.methodCall = methodCall;
        }

        void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
            for (ListenerCallQueue<L> queue : queues) {
                queue.add(this);
            }
        }
    }

    ListenerCallQueue(L l, Executor executor) {
        this.listener = (L) Preconditions.checkNotNull(l);
        this.executor = (Executor) Preconditions.checkNotNull(executor);
    }

    synchronized void add(Callback<L> callback) {
        this.waitQueue.add(callback);
    }

    void execute() {
        boolean scheduleTaskRunner = false;
        synchronized (this) {
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            try {
                this.executor.execute(this);
            } catch (RuntimeException e) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                    Logger logger2 = logger;
                    Level level = Level.SEVERE;
                    String strValueOf = String.valueOf(String.valueOf(this.listener));
                    String strValueOf2 = String.valueOf(String.valueOf(this.executor));
                    logger2.log(level, new StringBuilder(42 + strValueOf.length() + strValueOf2.length()).append("Exception while running callbacks for ").append(strValueOf).append(" on ").append(strValueOf2).toString(), (Throwable) e);
                    throw e;
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0036, code lost:
    
        r0.call(r7.listener);
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0041, code lost:
    
        r10 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0042, code lost:
    
        r0 = com.google.common.util.concurrent.ListenerCallQueue.logger;
        r1 = java.util.logging.Level.SEVERE;
        r2 = java.lang.String.valueOf(java.lang.String.valueOf(r7.listener));
        r2 = java.lang.String.valueOf(java.lang.String.valueOf(((com.google.common.util.concurrent.ListenerCallQueue.Callback) r0).methodCall));
        r0.log(r1, new java.lang.StringBuilder((37 + r2.length()) + r2.length()).append("Exception while executing callback: ").append(r2).append(".").append(r2).toString(), (java.lang.Throwable) r10);
     */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0097  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00d0 A[ORIG_RETURN, RETURN] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 209
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ListenerCallQueue.run():void");
    }
}
