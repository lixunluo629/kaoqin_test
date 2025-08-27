package org.springframework.data.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/FallbackExceptionTranslationStrategy.class */
public class FallbackExceptionTranslationStrategy extends PassThroughExceptionTranslationStrategy {
    public FallbackExceptionTranslationStrategy(Converter<Exception, DataAccessException> converter) {
        super(converter);
    }

    @Override // org.springframework.data.redis.PassThroughExceptionTranslationStrategy, org.springframework.data.redis.ExceptionTranslationStrategy
    public DataAccessException translate(Exception e) {
        DataAccessException translated = super.translate(e);
        return translated != null ? translated : getFallback(e);
    }

    protected RedisSystemException getFallback(Exception e) {
        return new RedisSystemException("Unknown redis exception", e);
    }
}
