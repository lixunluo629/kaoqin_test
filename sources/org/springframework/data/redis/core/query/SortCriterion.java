package org.springframework.data.redis.core.query;

import org.springframework.data.redis.connection.SortParameters;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/query/SortCriterion.class */
public interface SortCriterion<K> {
    SortCriterion<K> limit(long j, long j2);

    SortCriterion<K> limit(SortParameters.Range range);

    SortCriterion<K> order(SortParameters.Order order);

    SortCriterion<K> alphabetical(boolean z);

    SortCriterion<K> get(String str);

    SortQuery<K> build();
}
