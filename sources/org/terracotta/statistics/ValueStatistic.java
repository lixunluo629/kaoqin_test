package org.terracotta.statistics;

import java.lang.Number;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/ValueStatistic.class */
public interface ValueStatistic<T extends Number> {
    T value();
}
