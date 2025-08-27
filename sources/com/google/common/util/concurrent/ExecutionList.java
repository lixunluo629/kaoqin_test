package com.google.common.util.concurrent;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import org.apache.ibatis.ognl.OgnlContext;

/* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ExecutionList.class */
public final class ExecutionList {

    @VisibleForTesting
    static final Logger log = Logger.getLogger(ExecutionList.class.getName());

    @GuardedBy(OgnlContext.THIS_CONTEXT_KEY)
    private RunnableExecutorPair runnables;

    @GuardedBy(OgnlContext.THIS_CONTEXT_KEY)
    private boolean executed;

    public void add(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        synchronized (this) {
            if (!this.executed) {
                this.runnables = new RunnableExecutorPair(runnable, executor, this.runnables);
            } else {
                executeListener(runnable, executor);
            }
        }
    }

    public void execute() {
        RunnableExecutorPair reversedList;
        synchronized (this) {
            if (this.executed) {
                return;
            }
            this.executed = true;
            RunnableExecutorPair list = this.runnables;
            this.runnables = null;
            RunnableExecutorPair runnableExecutorPair = null;
            while (true) {
                reversedList = runnableExecutorPair;
                if (list == null) {
                    break;
                }
                RunnableExecutorPair tmp = list;
                list = list.next;
                tmp.next = reversedList;
                runnableExecutorPair = tmp;
            }
            while (reversedList != null) {
                executeListener(reversedList.runnable, reversedList.executor);
                reversedList = reversedList.next;
            }
        }
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            Logger logger = log;
            Level level = Level.SEVERE;
            String strValueOf = String.valueOf(String.valueOf(runnable));
            String strValueOf2 = String.valueOf(String.valueOf(executor));
            logger.log(level, new StringBuilder(57 + strValueOf.length() + strValueOf2.length()).append("RuntimeException while executing runnable ").append(strValueOf).append(" with executor ").append(strValueOf2).toString(), (Throwable) e);
        }
    }

    /* loaded from: guava-18.0.jar:com/google/common/util/concurrent/ExecutionList$RunnableExecutorPair.class */
    private static final class RunnableExecutorPair {
        final Runnable runnable;
        final Executor executor;

        @Nullable
        RunnableExecutorPair next;

        RunnableExecutorPair(Runnable runnable, Executor executor, RunnableExecutorPair next) {
            this.runnable = runnable;
            this.executor = executor;
            this.next = next;
        }
    }
}
