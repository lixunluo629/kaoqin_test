package org.ehcache.spi.loaderwriter;

import java.util.concurrent.TimeUnit;
import org.ehcache.spi.service.ServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/WriteBehindConfiguration.class */
public interface WriteBehindConfiguration extends ServiceConfiguration<WriteBehindProvider> {

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/spi/loaderwriter/WriteBehindConfiguration$BatchingConfiguration.class */
    public interface BatchingConfiguration {
        int getBatchSize();

        long getMaxDelay();

        TimeUnit getMaxDelayUnit();

        boolean isCoalescing();
    }

    int getConcurrency();

    int getMaxQueueSize();

    BatchingConfiguration getBatchingConfiguration();

    String getThreadPoolAlias();
}
