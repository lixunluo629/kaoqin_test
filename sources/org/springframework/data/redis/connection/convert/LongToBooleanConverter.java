package org.springframework.data.redis.connection.convert;

import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/LongToBooleanConverter.class */
public class LongToBooleanConverter implements Converter<Long, Boolean> {
    @Override // org.springframework.core.convert.converter.Converter
    public Boolean convert(Long result) {
        if (result != null) {
            return Boolean.valueOf(result.longValue() == 1);
        }
        return null;
    }
}
