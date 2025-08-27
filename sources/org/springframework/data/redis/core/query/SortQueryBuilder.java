package org.springframework.data.redis.core.query;

import org.springframework.data.redis.connection.SortParameters;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/query/SortQueryBuilder.class */
public class SortQueryBuilder<K> extends DefaultSortCriterion<K> {
    private static final String NO_SORT_KEY = "~";

    @Override // org.springframework.data.redis.core.query.DefaultSortCriterion, org.springframework.data.redis.core.query.SortCriterion
    public /* bridge */ /* synthetic */ SortCriterion get(String str) {
        return super.get(str);
    }

    @Override // org.springframework.data.redis.core.query.DefaultSortCriterion, org.springframework.data.redis.core.query.SortCriterion
    public /* bridge */ /* synthetic */ SortCriterion order(SortParameters.Order order) {
        return super.order(order);
    }

    @Override // org.springframework.data.redis.core.query.DefaultSortCriterion, org.springframework.data.redis.core.query.SortCriterion
    public /* bridge */ /* synthetic */ SortCriterion limit(SortParameters.Range range) {
        return super.limit(range);
    }

    @Override // org.springframework.data.redis.core.query.DefaultSortCriterion, org.springframework.data.redis.core.query.SortCriterion
    public /* bridge */ /* synthetic */ SortCriterion limit(long j, long j2) {
        return super.limit(j, j2);
    }

    @Override // org.springframework.data.redis.core.query.DefaultSortCriterion, org.springframework.data.redis.core.query.SortCriterion
    public /* bridge */ /* synthetic */ SortQuery build() {
        return super.build();
    }

    @Override // org.springframework.data.redis.core.query.DefaultSortCriterion, org.springframework.data.redis.core.query.SortCriterion
    public /* bridge */ /* synthetic */ SortCriterion alphabetical(boolean z) {
        return super.alphabetical(z);
    }

    private SortQueryBuilder(K key) {
        super(key);
    }

    public static <K> SortQueryBuilder<K> sort(K key) {
        return new SortQueryBuilder<>(key);
    }

    public SortCriterion<K> by(String keyPattern) {
        return addBy(keyPattern);
    }

    public SortCriterion<K> noSort() {
        return by(NO_SORT_KEY);
    }
}
