package org.terracotta.statistics;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/Time.class */
public final class Time {
    private static volatile TimeSource TIME_SOURCE = new TimeSource() { // from class: org.terracotta.statistics.Time.1
        @Override // org.terracotta.statistics.Time.TimeSource
        public long time() {
            return System.nanoTime();
        }

        @Override // org.terracotta.statistics.Time.TimeSource
        public long absoluteTime() {
            return System.currentTimeMillis();
        }
    };

    /* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/Time$TimeSource.class */
    public interface TimeSource {
        long time();

        long absoluteTime();
    }

    private Time() {
    }

    public static long time() {
        return TIME_SOURCE.time();
    }

    public static long absoluteTime() {
        return TIME_SOURCE.absoluteTime();
    }
}
