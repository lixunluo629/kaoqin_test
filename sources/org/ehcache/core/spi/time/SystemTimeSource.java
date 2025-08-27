package org.ehcache.core.spi.time;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/time/SystemTimeSource.class */
public class SystemTimeSource implements TimeSource {
    public static final TimeSource INSTANCE = new SystemTimeSource();

    private SystemTimeSource() {
    }

    @Override // org.ehcache.core.spi.time.TimeSource
    public long getTimeMillis() {
        return System.currentTimeMillis();
    }
}
