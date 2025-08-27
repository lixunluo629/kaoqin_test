package org.apache.commons.pool2.impl;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/EvictionTimer.class */
class EvictionTimer {
    private static ScheduledThreadPoolExecutor executor;
    private static int usageCount;

    private EvictionTimer() {
    }

    public String toString() {
        return "EvictionTimer []";
    }

    static synchronized void schedule(Runnable task, long delay, long period) {
        if (null == executor) {
            executor = new ScheduledThreadPoolExecutor(1, new EvictorThreadFactory());
        }
        usageCount++;
        executor.scheduleWithFixedDelay(task, delay, period, TimeUnit.MILLISECONDS);
    }

    static synchronized void cancel(TimerTask task, long timeout, TimeUnit unit) {
        task.cancel();
        usageCount--;
        if (usageCount == 0) {
            executor.shutdown();
            try {
                executor.awaitTermination(timeout, unit);
            } catch (InterruptedException e) {
            }
            executor.setCorePoolSize(0);
            executor = null;
        }
    }

    /* loaded from: commons-pool2-2.4.3.jar:org/apache/commons/pool2/impl/EvictionTimer$EvictorThreadFactory.class */
    private static class EvictorThreadFactory implements ThreadFactory {
        private EvictorThreadFactory() {
        }

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            final Thread t = new Thread(null, r, "commons-pool-evictor-thread");
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: org.apache.commons.pool2.impl.EvictionTimer.EvictorThreadFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    t.setContextClassLoader(EvictorThreadFactory.class.getClassLoader());
                    return null;
                }
            });
            return t;
        }
    }
}
