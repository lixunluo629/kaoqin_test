package io.netty.util.internal;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/util/internal/LongCounter.class */
public interface LongCounter {
    void add(long j);

    void increment();

    void decrement();

    long value();
}
