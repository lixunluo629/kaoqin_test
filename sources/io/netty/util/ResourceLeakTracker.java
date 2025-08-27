package io.netty.util;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/ResourceLeakTracker.class */
public interface ResourceLeakTracker<T> {
    void record();

    void record(Object obj);

    boolean close(T t);
}
