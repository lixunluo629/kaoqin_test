package org.ehcache.impl.internal.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/util/ThreadFactoryUtil.class */
public class ThreadFactoryUtil {
    public static ThreadFactory threadFactory(final String alias) {
        return new ThreadFactory() { // from class: org.ehcache.impl.internal.util.ThreadFactoryUtil.1
            private final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            private final AtomicInteger threadCount = new AtomicInteger();
            private final String poolAlias;

            {
                this.poolAlias = alias == null ? "_default_" : alias;
            }

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                return new Thread(this.threadGroup, r, "Ehcache [" + this.poolAlias + "]-" + this.threadCount.getAndIncrement());
            }
        };
    }
}
