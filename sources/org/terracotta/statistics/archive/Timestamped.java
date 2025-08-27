package org.terracotta.statistics.archive;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/Timestamped.class */
public interface Timestamped<T> {
    T getSample();

    long getTimestamp();
}
