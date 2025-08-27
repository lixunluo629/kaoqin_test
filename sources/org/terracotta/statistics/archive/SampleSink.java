package org.terracotta.statistics.archive;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/SampleSink.class */
public interface SampleSink<T> {
    void accept(T t);
}
