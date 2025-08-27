package org.terracotta.statistics.extended;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/extended/CountOperation.class */
public interface CountOperation<T> {
    long value(T t);

    long value(T... tArr);
}
