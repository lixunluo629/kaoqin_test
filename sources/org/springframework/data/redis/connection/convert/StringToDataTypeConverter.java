package org.springframework.data.redis.connection.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.DataType;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/StringToDataTypeConverter.class */
public class StringToDataTypeConverter implements Converter<String, DataType> {
    @Override // org.springframework.core.convert.converter.Converter
    public DataType convert(String source) {
        if (source == null) {
            return null;
        }
        return DataType.fromCode(source);
    }
}
