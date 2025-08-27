package org.ehcache.impl.config.loaderwriter.writebehind;

import org.ehcache.spi.loaderwriter.WriteBehindProvider;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/loaderwriter/writebehind/WriteBehindProviderConfiguration.class */
public class WriteBehindProviderConfiguration implements ServiceCreationConfiguration<WriteBehindProvider> {
    private final String threadPoolAlias;

    public WriteBehindProviderConfiguration(String threadPoolAlias) {
        this.threadPoolAlias = threadPoolAlias;
    }

    public String getThreadPoolAlias() {
        return this.threadPoolAlias;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<WriteBehindProvider> getServiceType() {
        return WriteBehindProvider.class;
    }
}
