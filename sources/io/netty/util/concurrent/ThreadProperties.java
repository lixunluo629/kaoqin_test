package io.netty.util.concurrent;

import java.lang.Thread;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/concurrent/ThreadProperties.class */
public interface ThreadProperties {
    Thread.State state();

    int priority();

    boolean isInterrupted();

    boolean isDaemon();

    String name();

    long id();

    StackTraceElement[] stackTrace();

    boolean isAlive();
}
