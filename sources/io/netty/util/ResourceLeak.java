package io.netty.util;

@Deprecated
/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/ResourceLeak.class */
public interface ResourceLeak {
    void record();

    void record(Object obj);

    boolean close();
}
