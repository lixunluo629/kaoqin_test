package org.springframework.data.redis.connection.convert;

import java.io.StringReader;
import java.util.Properties;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.RedisSystemException;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/StringToPropertiesConverter.class */
public class StringToPropertiesConverter implements Converter<String, Properties> {
    @Override // org.springframework.core.convert.converter.Converter
    public Properties convert(String source) {
        if (source == null) {
            return null;
        }
        Properties info = new Properties();
        StringReader stringReader = new StringReader(source);
        try {
            try {
                info.load(stringReader);
                stringReader.close();
                return info;
            } catch (Exception ex) {
                throw new RedisSystemException("Cannot read Redis info", ex);
            }
        } catch (Throwable th) {
            stringReader.close();
            throw th;
        }
    }
}
