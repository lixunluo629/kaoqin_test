package org.springframework.data.redis.core.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.redis.core.index.IndexDefinition;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/RedisIndexDefinition.class */
public abstract class RedisIndexDefinition implements IndexDefinition {
    private final String keyspace;
    private final String indexName;
    private final String path;
    private List<IndexDefinition.Condition<?>> conditions = new ArrayList();
    private IndexValueTransformer valueTransformer;

    protected RedisIndexDefinition(String keyspace, String path, String indexName) {
        this.keyspace = keyspace;
        this.indexName = indexName;
        this.path = path;
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinition
    public String getKeyspace() {
        return this.keyspace;
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinition
    public Collection<IndexDefinition.Condition<?>> getConditions() {
        return Collections.unmodifiableCollection(this.conditions);
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinition
    public IndexValueTransformer valueTransformer() {
        return this.valueTransformer != null ? this.valueTransformer : NoOpValueTransformer.INSTANCE;
    }

    @Override // org.springframework.data.redis.core.index.IndexDefinition
    public String getIndexName() {
        return this.indexName;
    }

    public String getPath() {
        return this.path;
    }

    protected void addCondition(IndexDefinition.Condition<?> condition) {
        Assert.notNull(condition, "Condition must not be null!");
        this.conditions.add(condition);
    }

    public void setValueTransformer(IndexValueTransformer valueTransformer) {
        this.valueTransformer = valueTransformer;
    }

    public int hashCode() {
        int result = ObjectUtils.nullSafeHashCode(this.indexName);
        return result + ObjectUtils.nullSafeHashCode(this.keyspace);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof RedisIndexDefinition)) {
            return false;
        }
        RedisIndexDefinition that = (RedisIndexDefinition) obj;
        if (!ObjectUtils.nullSafeEquals(this.keyspace, that.keyspace)) {
            return false;
        }
        return ObjectUtils.nullSafeEquals(this.indexName, that.indexName);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/RedisIndexDefinition$NoOpValueTransformer.class */
    public enum NoOpValueTransformer implements IndexValueTransformer {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Object convert(Object source) {
            return source;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/RedisIndexDefinition$LowercaseIndexValueTransformer.class */
    public enum LowercaseIndexValueTransformer implements IndexValueTransformer {
        INSTANCE;

        @Override // org.springframework.core.convert.converter.Converter
        public Object convert(Object source) {
            if (!(source instanceof String)) {
                return source;
            }
            return ((String) source).toLowerCase();
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/RedisIndexDefinition$CompositeValueTransformer.class */
    public static class CompositeValueTransformer implements IndexValueTransformer {
        private final List<IndexValueTransformer> transformers = new ArrayList();

        public CompositeValueTransformer(Collection<IndexValueTransformer> transformers) {
            this.transformers.addAll(transformers);
        }

        @Override // org.springframework.core.convert.converter.Converter
        public Object convert(Object source) {
            Object tmp = source;
            for (IndexValueTransformer transformer : this.transformers) {
                tmp = transformer.convert(tmp);
            }
            return tmp;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/RedisIndexDefinition$OrCondition.class */
    public static class OrCondition<T> implements IndexDefinition.Condition<T> {
        private final List<IndexDefinition.Condition<T>> conditions = new ArrayList();

        public OrCondition(Collection<IndexDefinition.Condition<T>> conditions) {
            this.conditions.addAll(conditions);
        }

        @Override // org.springframework.data.redis.core.index.IndexDefinition.Condition
        public boolean matches(T value, IndexDefinition.IndexingContext context) {
            for (IndexDefinition.Condition<T> condition : this.conditions) {
                if (condition.matches(value, context)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/RedisIndexDefinition$PathCondition.class */
    public static class PathCondition implements IndexDefinition.Condition<Object> {
        private final String path;

        public PathCondition(String path) {
            this.path = path;
        }

        @Override // org.springframework.data.redis.core.index.IndexDefinition.Condition
        public boolean matches(Object value, IndexDefinition.IndexingContext context) {
            if (!StringUtils.hasText(this.path)) {
                return true;
            }
            return ObjectUtils.nullSafeEquals(context.getPath(), this.path);
        }
    }
}
