package org.springframework.data.redis.core.query;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.redis.connection.SortParameters;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/query/DefaultSortCriterion.class */
class DefaultSortCriterion<K> implements SortCriterion<K> {
    private final K key;
    private String by;
    private final List<String> getKeys = new ArrayList(4);
    private SortParameters.Range limit;
    private SortParameters.Order order;
    private Boolean alpha;

    DefaultSortCriterion(K key) {
        this.key = key;
    }

    @Override // org.springframework.data.redis.core.query.SortCriterion
    public SortCriterion<K> alphabetical(boolean alpha) {
        this.alpha = Boolean.valueOf(alpha);
        return this;
    }

    @Override // org.springframework.data.redis.core.query.SortCriterion
    public SortQuery<K> build() {
        return new DefaultSortQuery(this.key, this.by, this.limit, this.order, this.alpha, this.getKeys);
    }

    @Override // org.springframework.data.redis.core.query.SortCriterion
    public SortCriterion<K> limit(long offset, long count) {
        this.limit = new SortParameters.Range(offset, count);
        return this;
    }

    @Override // org.springframework.data.redis.core.query.SortCriterion
    public SortCriterion<K> limit(SortParameters.Range range) {
        this.limit = range;
        return this;
    }

    @Override // org.springframework.data.redis.core.query.SortCriterion
    public SortCriterion<K> order(SortParameters.Order order) {
        this.order = order;
        return this;
    }

    @Override // org.springframework.data.redis.core.query.SortCriterion
    public SortCriterion<K> get(String getPattern) {
        this.getKeys.add(getPattern);
        return this;
    }

    SortCriterion<K> addBy(String keyPattern) {
        this.by = keyPattern;
        return this;
    }
}
