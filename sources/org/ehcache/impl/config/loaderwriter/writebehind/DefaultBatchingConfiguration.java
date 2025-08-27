package org.ehcache.impl.config.loaderwriter.writebehind;

import java.util.concurrent.TimeUnit;
import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/loaderwriter/writebehind/DefaultBatchingConfiguration.class */
public class DefaultBatchingConfiguration implements WriteBehindConfiguration.BatchingConfiguration {
    private final long maxDelay;
    private final TimeUnit maxDelayUnit;
    private final int batchSize;
    private final boolean coalescing;

    public DefaultBatchingConfiguration(long maxDelay, TimeUnit maxDelayUnit, int batchSize, boolean coalescing) {
        this.maxDelay = maxDelay;
        this.maxDelayUnit = maxDelayUnit;
        this.batchSize = batchSize;
        this.coalescing = coalescing;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration.BatchingConfiguration
    public long getMaxDelay() {
        return this.maxDelay;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration.BatchingConfiguration
    public TimeUnit getMaxDelayUnit() {
        return this.maxDelayUnit;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration.BatchingConfiguration
    public boolean isCoalescing() {
        return this.coalescing;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration.BatchingConfiguration
    public int getBatchSize() {
        return this.batchSize;
    }
}
