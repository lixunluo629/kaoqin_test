package org.springframework.data.redis.core.index;

import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/SpelIndexDefinition.class */
public class SpelIndexDefinition extends RedisIndexDefinition {
    private final String expression;

    public SpelIndexDefinition(String keyspace, String expression, String indexName) {
        super(keyspace, null, indexName);
        this.expression = expression;
    }

    public String getExpression() {
        return this.expression;
    }

    @Override // org.springframework.data.redis.core.index.RedisIndexDefinition
    public int hashCode() {
        int result = super.hashCode();
        return result + ObjectUtils.nullSafeHashCode(this.expression);
    }

    @Override // org.springframework.data.redis.core.index.RedisIndexDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof SpelIndexDefinition)) {
            return false;
        }
        SpelIndexDefinition that = (SpelIndexDefinition) obj;
        return ObjectUtils.nullSafeEquals(this.expression, that.expression);
    }
}
