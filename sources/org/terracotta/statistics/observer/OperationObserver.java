package org.terracotta.statistics.observer;

import java.lang.Enum;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/observer/OperationObserver.class */
public interface OperationObserver<T extends Enum<T>> {
    void begin();

    void end(T t);

    void end(T t, long... jArr);
}
