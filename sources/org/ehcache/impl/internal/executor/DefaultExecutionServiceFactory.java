package org.ehcache.impl.internal.executor;

import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.impl.config.executor.PooledExecutionServiceConfiguration;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/executor/DefaultExecutionServiceFactory.class */
public class DefaultExecutionServiceFactory implements ServiceFactory<ExecutionService> {
    @Override // org.ehcache.core.spi.service.ServiceFactory
    public /* bridge */ /* synthetic */ Service create(ServiceCreationConfiguration x0) {
        return create((ServiceCreationConfiguration<ExecutionService>) x0);
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public ExecutionService create(ServiceCreationConfiguration<ExecutionService> configuration) {
        if (configuration == null) {
            return new OnDemandExecutionService();
        }
        if (configuration instanceof PooledExecutionServiceConfiguration) {
            return new PooledExecutionService((PooledExecutionServiceConfiguration) configuration);
        }
        throw new IllegalArgumentException("Expected a configuration of type PooledExecutionServiceConfiguration but got " + configuration.getClass().getSimpleName());
    }

    @Override // org.ehcache.core.spi.service.ServiceFactory
    public Class<ExecutionService> getServiceType() {
        return ExecutionService.class;
    }
}
