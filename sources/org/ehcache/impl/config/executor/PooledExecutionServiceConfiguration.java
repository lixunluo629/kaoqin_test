package org.ehcache.impl.config.executor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.ehcache.core.spi.service.ExecutionService;
import org.ehcache.spi.service.ServiceCreationConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/executor/PooledExecutionServiceConfiguration.class */
public class PooledExecutionServiceConfiguration implements ServiceCreationConfiguration<ExecutionService> {
    private final Map<String, PoolConfiguration> poolConfigurations = new HashMap();
    private String defaultAlias;

    public void addDefaultPool(String alias, int minSize, int maxSize) {
        if (alias == null) {
            throw new NullPointerException("Pool alias cannot be null");
        }
        if (this.defaultAlias == null) {
            addPool(alias, minSize, maxSize);
            this.defaultAlias = alias;
            return;
        }
        throw new IllegalArgumentException("'" + this.defaultAlias + "' is already configured as the default pool");
    }

    public void addPool(String alias, int minSize, int maxSize) {
        if (alias == null) {
            throw new NullPointerException("Pool alias cannot be null");
        }
        if (this.poolConfigurations.containsKey(alias)) {
            throw new IllegalArgumentException("A pool with the alias '" + alias + "' is already configured");
        }
        this.poolConfigurations.put(alias, new PoolConfiguration(minSize, maxSize));
    }

    public Map<String, PoolConfiguration> getPoolConfigurations() {
        return Collections.unmodifiableMap(this.poolConfigurations);
    }

    public String getDefaultPoolAlias() {
        return this.defaultAlias;
    }

    @Override // org.ehcache.spi.service.ServiceCreationConfiguration
    public Class<ExecutionService> getServiceType() {
        return ExecutionService.class;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/config/executor/PooledExecutionServiceConfiguration$PoolConfiguration.class */
    public static final class PoolConfiguration {
        private final int minSize;
        private final int maxSize;

        private PoolConfiguration(int minSize, int maxSize) {
            this.minSize = minSize;
            this.maxSize = maxSize;
        }

        public int minSize() {
            return this.minSize;
        }

        public int maxSize() {
            return this.maxSize;
        }
    }
}
