package org.terracotta.statistics.archive;

/* loaded from: ehcache-3.2.3.jar:org/terracotta/statistics/archive/DevNull.class */
public class DevNull implements SampleSink<Object> {
    public static final SampleSink<Object> DEV_NULL = new DevNull();

    private DevNull() {
    }

    @Override // org.terracotta.statistics.archive.SampleSink
    public void accept(Object object) {
    }
}
