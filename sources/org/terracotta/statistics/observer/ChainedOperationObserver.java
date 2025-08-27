package org.terracotta.statistics.observer;

import java.lang.Enum;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/observer/ChainedOperationObserver.class */
public interface ChainedOperationObserver<T extends Enum<T>> extends ChainedObserver {
    void begin(long j);

    void end(long j, T t);

    void end(long j, T t, long... jArr);
}
