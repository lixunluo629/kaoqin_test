package org.springframework.data.redis.core.index;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/IndexConfiguration.class */
public class IndexConfiguration implements ConfigurableIndexDefinitionProvider {
    private final Set<IndexDefinition> definitions = new CopyOnWriteArraySet();

    public IndexConfiguration() {
        for (IndexDefinition initial : initialConfiguration()) {
            addIndexDefinition(initial);
        }
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinitionProvider
    public boolean hasIndexFor(Serializable keyspace) {
        return !getIndexDefinitionsFor(keyspace).isEmpty();
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinitionProvider
    public boolean hasIndexFor(Serializable keyspace, String path) {
        return !getIndexDefinitionsFor(keyspace, path).isEmpty();
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinitionProvider
    public Set<IndexDefinition> getIndexDefinitionsFor(Serializable keyspace, String path) {
        return getIndexDefinitions(keyspace, path, Object.class);
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinitionProvider
    public Set<IndexDefinition> getIndexDefinitionsFor(Serializable keyspace) {
        Set<IndexDefinition> indexDefinitions = new LinkedHashSet<>();
        for (IndexDefinition indexDef : this.definitions) {
            if (indexDef.getKeyspace().equals(keyspace)) {
                indexDefinitions.add(indexDef);
            }
        }
        return indexDefinitions;
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinitionRegistry
    public void addIndexDefinition(IndexDefinition indexDefinition) {
        Assert.notNull(indexDefinition, "RedisIndexDefinition must not be null in order to be added.");
        this.definitions.add(indexDefinition);
    }

    private Set<IndexDefinition> getIndexDefinitions(Serializable keyspace, String path, Class<?> type) {
        Set<IndexDefinition> def = new LinkedHashSet<>();
        for (IndexDefinition indexDef : this.definitions) {
            if (ClassUtils.isAssignable(type, indexDef.getClass()) && indexDef.getKeyspace().equals(keyspace)) {
                if (indexDef instanceof PathBasedRedisIndexDefinition) {
                    if (ObjectUtils.nullSafeEquals(((PathBasedRedisIndexDefinition) indexDef).getPath(), path)) {
                        def.add(indexDef);
                    }
                } else {
                    def.add(indexDef);
                }
            }
        }
        return def;
    }

    protected Iterable<? extends IndexDefinition> initialConfiguration() {
        return Collections.emptySet();
    }
}
