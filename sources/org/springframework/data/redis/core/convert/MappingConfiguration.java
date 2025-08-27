package org.springframework.data.redis.core.convert;

import org.springframework.data.redis.core.index.ConfigurableIndexDefinitionProvider;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/MappingConfiguration.class */
public class MappingConfiguration {
    private final ConfigurableIndexDefinitionProvider indexConfiguration;
    private final KeyspaceConfiguration keyspaceConfiguration;

    public MappingConfiguration(ConfigurableIndexDefinitionProvider indexConfiguration, KeyspaceConfiguration keyspaceConfiguration) {
        this.indexConfiguration = indexConfiguration;
        this.keyspaceConfiguration = keyspaceConfiguration;
    }

    public ConfigurableIndexDefinitionProvider getIndexConfiguration() {
        return this.indexConfiguration;
    }

    public KeyspaceConfiguration getKeyspaceConfiguration() {
        return this.keyspaceConfiguration;
    }
}
