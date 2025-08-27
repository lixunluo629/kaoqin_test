package org.ehcache.config.builders;

import java.util.HashSet;
import java.util.Set;
import org.ehcache.config.Builder;
import org.ehcache.impl.config.executor.PooledExecutionServiceConfiguration;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/PooledExecutionServiceConfigurationBuilder.class */
public class PooledExecutionServiceConfigurationBuilder implements Builder<PooledExecutionServiceConfiguration> {
    private Pool defaultPool;
    private final Set<Pool> pools = new HashSet();

    private PooledExecutionServiceConfigurationBuilder() {
    }

    private PooledExecutionServiceConfigurationBuilder(PooledExecutionServiceConfigurationBuilder other) {
        this.defaultPool = other.defaultPool;
        this.pools.addAll(other.pools);
    }

    public static PooledExecutionServiceConfigurationBuilder newPooledExecutionServiceConfigurationBuilder() {
        return new PooledExecutionServiceConfigurationBuilder();
    }

    public PooledExecutionServiceConfigurationBuilder defaultPool(String alias, int minSize, int maxSize) {
        PooledExecutionServiceConfigurationBuilder other = new PooledExecutionServiceConfigurationBuilder(this);
        other.defaultPool = new Pool(alias, minSize, maxSize);
        return other;
    }

    public PooledExecutionServiceConfigurationBuilder pool(String alias, int minSize, int maxSize) {
        PooledExecutionServiceConfigurationBuilder other = new PooledExecutionServiceConfigurationBuilder(this);
        other.pools.add(new Pool(alias, minSize, maxSize));
        return other;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.ehcache.config.Builder
    public PooledExecutionServiceConfiguration build() {
        PooledExecutionServiceConfiguration config = new PooledExecutionServiceConfiguration();
        if (this.defaultPool != null) {
            config.addDefaultPool(this.defaultPool.alias, this.defaultPool.minSize, this.defaultPool.maxSize);
        }
        for (Pool pool : this.pools) {
            config.addPool(pool.alias, pool.minSize, pool.maxSize);
        }
        return config;
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/config/builders/PooledExecutionServiceConfigurationBuilder$Pool.class */
    private static class Pool {
        private String alias;
        private int minSize;
        private int maxSize;

        Pool(String alias, int minSize, int maxSize) {
            this.alias = alias;
            this.minSize = minSize;
            this.maxSize = maxSize;
        }
    }
}
