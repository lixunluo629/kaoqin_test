package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/SerializingExecutor.class */
final class SerializingExecutor implements Executor {
    private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
    private final Executor executor;

    @GuardedBy("internalLock")
    private final Queue<Runnable> waitQueue = new ArrayDeque();

    @GuardedBy("internalLock")
    private boolean isThreadScheduled = false;
    private final TaskRunner taskRunner = new TaskRunner();
    private final Object internalLock = new Object() { // from class: com.google.common.util.concurrent.SerializingExecutor.1
        public String toString() {
            String strValueOf = String.valueOf(super.toString());
            return strValueOf.length() != 0 ? "SerializingExecutor lock: ".concat(strValueOf) : new String("SerializingExecutor lock: ");
        }
    };

    public SerializingExecutor(Executor executor) {
        Preconditions.checkNotNull(executor, "'executor' must not be null.");
        this.executor = executor;
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable r) {
        Preconditions.checkNotNull(r, "'r' must not be null.");
        boolean scheduleTaskRunner = false;
        synchronized (this.internalLock) {
            this.waitQueue.add(r);
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            boolean threw = true;
            try {
                this.executor.execute(this.taskRunner);
                threw = false;
                if (0 != 0) {
                    synchronized (this.internalLock) {
                        this.isThreadScheduled = false;
                    }
                }
            } catch (Throwable th) {
                if (threw) {
                    synchronized (this.internalLock) {
                        this.isThreadScheduled = false;
                    }
                }
                throw th;
            }
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/SerializingExecutor$TaskRunner.class */
    private class TaskRunner implements Runnable {
        private TaskRunner() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:18:0x0046, code lost:
        
            r0.run();
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x004f, code lost:
        
            r10 = move-exception;
         */
        /* JADX WARN: Code restructure failed: missing block: B:20:0x0050, code lost:
        
            r0 = com.google.common.util.concurrent.SerializingExecutor.log;
            r1 = java.util.logging.Level.SEVERE;
            r2 = java.lang.String.valueOf(java.lang.String.valueOf(r0));
            r0.log(r1, new java.lang.StringBuilder(35 + r2.length()).append("Exception while executing runnable ").append(r2).toString(), (java.lang.Throwable) r10);
         */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0086  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00ae  */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00d3 A[ORIG_RETURN, RETURN] */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                Method dump skipped, instructions count: 212
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.SerializingExecutor.TaskRunner.run():void");
        }
    }
}
