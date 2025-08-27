package org.springframework.data.redis.core.convert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/RemoveIndexedData.class */
public class RemoveIndexedData implements IndexedData {
    private final IndexedData delegate;

    RemoveIndexedData(IndexedData delegate) {
        this.delegate = delegate;
    }

    @Override // org.springframework.data.redis.core.convert.IndexedData
    public String getIndexName() {
        return this.delegate.getIndexName();
    }

    @Override // org.springframework.data.redis.core.convert.IndexedData
    public String getKeyspace() {
        return this.delegate.getKeyspace();
    }

    public String toString() {
        return "RemoveIndexedData [indexName=" + getIndexName() + ", keyspace()=" + getKeyspace() + "]";
    }
}
