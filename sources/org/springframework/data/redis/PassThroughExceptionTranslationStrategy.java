package org.springframework.data.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/PassThroughExceptionTranslationStrategy.class */
public class PassThroughExceptionTranslationStrategy implements ExceptionTranslationStrategy {
    private Converter<Exception, DataAccessException> converter;

    public PassThroughExceptionTranslationStrategy(Converter<Exception, DataAccessException> converter) {
        this.converter = converter;
    }

    @Override // org.springframework.data.redis.ExceptionTranslationStrategy
    public DataAccessException translate(Exception e) {
        return this.converter.convert(e);
    }
}
