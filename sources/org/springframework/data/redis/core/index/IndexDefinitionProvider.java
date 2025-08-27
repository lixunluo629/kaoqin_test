package org.springframework.data.redis.core.index;

import java.io.Serializable;
import java.util.Set;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/IndexDefinitionProvider.class */
public interface IndexDefinitionProvider {
    boolean hasIndexFor(Serializable serializable);

    boolean hasIndexFor(Serializable serializable, String str);

    Set<IndexDefinition> getIndexDefinitionsFor(Serializable serializable);

    Set<IndexDefinition> getIndexDefinitionsFor(Serializable serializable, String str);
}
