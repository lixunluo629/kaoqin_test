package org.ehcache.impl.internal.store.offheap;

import java.util.concurrent.TimeUnit;
import org.terracotta.offheapstore.buffersource.BufferSource;
import org.terracotta.offheapstore.buffersource.OffHeapBufferSource;
import org.terracotta.offheapstore.buffersource.TimingBufferSource;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/store/offheap/OffHeapStoreUtils.class */
public class OffHeapStoreUtils {
    private static final long SLOW_DELAY = 3000;
    private static final String SLOW_DELAY_PROPERTY = "slowAllocationDelay";
    private static final long CRITICAL_DELAY = 30000;
    private static final String CRITICAL_DELAY_PROPERTY = "criticalAllocationDelay";
    private static final boolean HALT_ON_CRITICAL_DELAY = true;
    private static final String HALT_ON_CRITICAL_DELAY_PROPERTY = "haltOnCriticalAllocationDelay";

    public static BufferSource getBufferSource() {
        long slowDelay = getAdvancedLongConfigProperty(SLOW_DELAY_PROPERTY, SLOW_DELAY);
        long critDelay = getAdvancedLongConfigProperty(CRITICAL_DELAY_PROPERTY, 30000L);
        boolean haltOnCrit = getAdvancedBooleanConfigProperty(HALT_ON_CRITICAL_DELAY_PROPERTY, true);
        return new TimingBufferSource(new OffHeapBufferSource(), slowDelay, TimeUnit.MILLISECONDS, critDelay, TimeUnit.MILLISECONDS, haltOnCrit);
    }

    public static long getAdvancedMemorySizeConfigProperty(String property, long defaultValue) {
        String globalPropertyKey = "net.sf.ehcache.offheap.config." + property;
        return MemorySizeParser.parse(System.getProperty(globalPropertyKey, Long.toString(defaultValue)));
    }

    public static long getAdvancedLongConfigProperty(String property, long defaultValue) {
        String globalPropertyKey = "net.sf.ehcache.offheap.config." + property;
        return Long.parseLong(System.getProperty(globalPropertyKey, Long.toString(defaultValue)));
    }

    public static boolean getAdvancedBooleanConfigProperty(String property, boolean defaultValue) {
        String globalPropertyKey = "net.sf.ehcache.offheap.config." + property;
        return Boolean.parseBoolean(System.getProperty(globalPropertyKey, Boolean.toString(defaultValue)));
    }
}
