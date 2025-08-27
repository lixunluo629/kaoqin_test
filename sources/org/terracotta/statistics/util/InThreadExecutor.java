package org.terracotta.statistics.util;

import java.util.concurrent.Executor;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/util/InThreadExecutor.class */
public final class InThreadExecutor implements Executor {
    public static final Executor INSTANCE = new InThreadExecutor();

    private InThreadExecutor() {
    }

    @Override // java.util.concurrent.Executor
    public void execute(Runnable r) {
        r.run();
    }
}
