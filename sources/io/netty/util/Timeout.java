package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/Timeout.class */
public interface Timeout {
    Timer timer();

    TimerTask task();

    boolean isExpired();

    boolean isCancelled();

    boolean cancel();
}
