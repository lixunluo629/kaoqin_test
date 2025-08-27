package org.springframework.data.redis.core.convert;

import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/SimpleIndexedPropertyValue.class */
public class SimpleIndexedPropertyValue implements IndexedData {
    private final String keyspace;
    private final String indexName;
    private final Object value;

    public SimpleIndexedPropertyValue(String keyspace, String indexName, Object value) {
        this.keyspace = keyspace;
        this.indexName = indexName;
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override // org.springframework.data.redis.core.convert.IndexedData
    public String getIndexName() {
        return this.indexName;
    }

    @Override // org.springframework.data.redis.core.convert.IndexedData
    public String getKeyspace() {
        return this.keyspace;
    }

    public int hashCode() {
        int result = 1 + ObjectUtils.nullSafeHashCode(this.keyspace);
        return result + ObjectUtils.nullSafeHashCode(this.indexName) + ObjectUtils.nullSafeHashCode(this.value);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof SimpleIndexedPropertyValue)) {
            return false;
        }
        SimpleIndexedPropertyValue that = (SimpleIndexedPropertyValue) obj;
        if (!ObjectUtils.nullSafeEquals(this.keyspace, that.keyspace) || !ObjectUtils.nullSafeEquals(this.indexName, that.indexName)) {
            return false;
        }
        return ObjectUtils.nullSafeEquals(this.value, that.value);
    }

    public String toString() {
        return "SimpleIndexedPropertyValue [keyspace=" + this.keyspace + ", indexName=" + this.indexName + ", value=" + this.value + "]";
    }
}
