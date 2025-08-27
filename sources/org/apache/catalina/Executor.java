package org.apache.catalina;

import java.util.concurrent.TimeUnit;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/Executor.class */
public interface Executor extends java.util.concurrent.Executor, Lifecycle {
    String getName();

    void execute(Runnable runnable, long j, TimeUnit timeUnit);
}
