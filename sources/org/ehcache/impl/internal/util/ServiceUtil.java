package org.ehcache.impl.internal.util;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/util/ServiceUtil.class */
public final class ServiceUtil {
    private static final Future<?> COMPLETE_FUTURE = new Future<Void>() { // from class: org.ehcache.impl.internal.util.ServiceUtil.1
        @Override // java.util.concurrent.Future
        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        @Override // java.util.concurrent.Future
        public boolean isCancelled() {
            return false;
        }

        @Override // java.util.concurrent.Future
        public boolean isDone() {
            return true;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Void get() {
            return null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Future
        public Void get(long timeout, TimeUnit unit) {
            return null;
        }
    };

    private ServiceUtil() {
    }

    public static Future<?> completeFuture() {
        return COMPLETE_FUTURE;
    }
}
