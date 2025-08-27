package org.springframework.data.redis.core.query;

import java.util.List;
import org.springframework.data.redis.connection.SortParameters;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/query/SortQuery.class */
public interface SortQuery<K> {
    SortParameters.Order getOrder();

    Boolean isAlphabetic();

    SortParameters.Range getLimit();

    K getKey();

    String getBy();

    List<String> getGetPattern();
}
