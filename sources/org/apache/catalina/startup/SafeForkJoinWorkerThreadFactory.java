package org.apache.catalina.startup;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/SafeForkJoinWorkerThreadFactory.class */
public class SafeForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
    @Override // java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory
    public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
        return new SafeForkJoinWorkerThread(pool);
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/startup/SafeForkJoinWorkerThreadFactory$SafeForkJoinWorkerThread.class */
    private static class SafeForkJoinWorkerThread extends ForkJoinWorkerThread {
        protected SafeForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool);
            setContextClassLoader(ForkJoinPool.class.getClassLoader());
        }
    }
}
