package org.springframework.data.redis.core.query;

import java.util.List;
import org.springframework.data.redis.connection.SortParameters;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/query/DefaultSortQuery.class */
class DefaultSortQuery<K> implements SortQuery<K> {
    private final K key;
    private final Boolean alpha;
    private final SortParameters.Order order;
    private final SortParameters.Range limit;
    private final String by;
    private final List<String> gets;

    DefaultSortQuery(K key, String by, SortParameters.Range limit, SortParameters.Order order, Boolean alpha, List<String> gets) {
        this.key = key;
        this.by = by;
        this.limit = limit;
        this.order = order;
        this.alpha = alpha;
        this.gets = gets;
    }

    @Override // org.springframework.data.redis.core.query.SortQuery
    public String getBy() {
        return this.by;
    }

    @Override // org.springframework.data.redis.core.query.SortQuery
    public SortParameters.Range getLimit() {
        return this.limit;
    }

    @Override // org.springframework.data.redis.core.query.SortQuery
    public SortParameters.Order getOrder() {
        return this.order;
    }

    @Override // org.springframework.data.redis.core.query.SortQuery
    public Boolean isAlphabetic() {
        return this.alpha;
    }

    @Override // org.springframework.data.redis.core.query.SortQuery
    public K getKey() {
        return this.key;
    }

    @Override // org.springframework.data.redis.core.query.SortQuery
    public List<String> getGetPattern() {
        return this.gets;
    }

    public String toString() {
        return "DefaultSortQuery [alpha=" + this.alpha + ", by=" + this.by + ", gets=" + this.gets + ", key=" + this.key + ", limit=" + this.limit + ", order=" + this.order + "]";
    }
}
