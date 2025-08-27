package org.ehcache.impl.config.loaderwriter.writebehind;

import org.ehcache.spi.loaderwriter.WriteBehindConfiguration;
import org.ehcache.spi.loaderwriter.WriteBehindProvider;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/loaderwriter/writebehind/DefaultWriteBehindConfiguration.class */
public class DefaultWriteBehindConfiguration implements WriteBehindConfiguration {
    private final WriteBehindConfiguration.BatchingConfiguration batchingConfig;
    private final int concurrency;
    private final int queueSize;
    private final String executorAlias;

    public DefaultWriteBehindConfiguration(String executorAlias, int concurrency, int queueSize, WriteBehindConfiguration.BatchingConfiguration batchingConfig) {
        this.concurrency = concurrency;
        this.queueSize = queueSize;
        this.executorAlias = executorAlias;
        this.batchingConfig = batchingConfig;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration
    public int getConcurrency() {
        return this.concurrency;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration
    public int getMaxQueueSize() {
        return this.queueSize;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration
    public String getThreadPoolAlias() {
        return this.executorAlias;
    }

    @Override // org.ehcache.spi.loaderwriter.WriteBehindConfiguration
    public WriteBehindConfiguration.BatchingConfiguration getBatchingConfiguration() {
        return this.batchingConfig;
    }

    @Override // org.ehcache.spi.service.ServiceConfiguration
    public Class<WriteBehindProvider> getServiceType() {
        return WriteBehindProvider.class;
    }
}
