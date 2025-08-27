package org.terracotta.offheapstore.util;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/offheapstore/util/Retryer.class */
public class Retryer {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) Retryer.class);
    private final ScheduledThreadPoolExecutor executor;
    private final long minimumDelay;
    private final long maximumDelay;
    private final TimeUnit unit;

    public Retryer(long minDelay, long maxDelay, TimeUnit unit, ThreadFactory threadFactory) {
        if (unit == null) {
            throw new IllegalArgumentException("Time unit must be non-null");
        }
        if (minDelay <= 0) {
            throw new IllegalArgumentException("Minimum delay must be greater than zero");
        }
        if (maxDelay < minDelay) {
            throw new IllegalArgumentException("Maximum delay cannot be less than minimum delay");
        }
        if (threadFactory == null) {
            throw new IllegalArgumentException("Thread factory must be non-null");
        }
        this.minimumDelay = minDelay;
        this.maximumDelay = maxDelay;
        this.unit = unit;
        this.executor = new ScheduledThreadPoolExecutor(1, threadFactory);
    }

    public void completeAsynchronously(Runnable task) {
        scheduleTask(task, 0L);
    }

    public void shutdownNow() {
        this.executor.shutdownNow();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleTask(final Runnable task, final long delay) {
        if (this.executor.isShutdown()) {
        }
        this.executor.schedule(new Runnable() { // from class: org.terracotta.offheapstore.util.Retryer.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    task.run();
                } catch (Throwable t) {
                    long nextDelay = Retryer.this.nextDelay(delay);
                    Retryer.LOGGER.warn(task + " failed, retrying in " + nextDelay + SymbolConstants.SPACE_SYMBOL + Retryer.this.unit.toString().toLowerCase(), t);
                    Retryer.this.scheduleTask(task, nextDelay);
                }
            }
        }, delay, this.unit);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long nextDelay(long delay) {
        if (delay < this.minimumDelay) {
            return this.minimumDelay;
        }
        if (delay >= this.maximumDelay) {
            return this.maximumDelay;
        }
        return Math.min(delay * 2, this.maximumDelay);
    }
}
