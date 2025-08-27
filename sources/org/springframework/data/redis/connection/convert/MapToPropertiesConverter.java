package org.springframework.data.redis.connection.convert;

import java.util.Map;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/MapToPropertiesConverter.class */
public enum MapToPropertiesConverter implements Converter<Map<?, ?>, Properties> {
    INSTANCE;

    @Override // org.springframework.core.convert.converter.Converter
    public Properties convert(Map<?, ?> source) {
        Properties p = new Properties();
        if (source != null) {
            p.putAll(source);
        }
        return p;
    }
}
