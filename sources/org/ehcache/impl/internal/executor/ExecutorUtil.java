package org.ehcache.impl.internal.executor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/ExecutorUtil.class */
public class ExecutorUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) ExecutorUtil.class);

    public static void shutdown(ExecutorService executor) {
        executor.shutdown();
        terminate(executor);
    }

    public static void shutdownNow(ExecutorService executor) {
        for (Runnable r : executor.shutdownNow()) {
            if (!(r instanceof FutureTask) || !((FutureTask) r).isCancelled()) {
                try {
                    r.run();
                } catch (Throwable t) {
                    LOGGER.warn("Exception executing task left in {}: {}", executor, t);
                }
            }
        }
        terminate(executor);
    }

    private static void terminate(ExecutorService executor) {
        boolean interrupted = false;
        while (!executor.awaitTermination(30L, TimeUnit.SECONDS)) {
            try {
                LOGGER.warn("Still waiting for termination of {}", executor);
            } catch (InterruptedException e) {
                interrupted = true;
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
    }

    public static <T> T waitFor(Future<T> future) throws ExecutionException {
        T t;
        boolean interrupted = false;
        while (true) {
            try {
                t = future.get();
                break;
            } catch (InterruptedException e) {
                interrupted = true;
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
        return t;
    }
}
