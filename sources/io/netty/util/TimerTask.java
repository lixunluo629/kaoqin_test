package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/TimerTask.class */
public interface TimerTask {
    void run(Timeout timeout) throws Exception;
}
