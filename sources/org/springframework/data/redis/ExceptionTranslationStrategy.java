package org.springframework.data.redis;

import org.springframework.dao.DataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/ExceptionTranslationStrategy.class */
public interface ExceptionTranslationStrategy {
    DataAccessException translate(Exception exc);
}
