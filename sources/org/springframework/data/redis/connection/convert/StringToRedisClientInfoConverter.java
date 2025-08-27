package org.springframework.data.redis.connection.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.types.RedisClientInfo;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/StringToRedisClientInfoConverter.class */
public class StringToRedisClientInfoConverter implements Converter<String[], List<RedisClientInfo>> {
    @Override // org.springframework.core.convert.converter.Converter
    public List<RedisClientInfo> convert(String[] lines) {
        if (lines == null) {
            return Collections.emptyList();
        }
        List<RedisClientInfo> infos = new ArrayList<>(lines.length);
        for (String line : lines) {
            infos.add(RedisClientInfo.RedisClientInfoBuilder.fromString(line));
        }
        return infos;
    }
}
