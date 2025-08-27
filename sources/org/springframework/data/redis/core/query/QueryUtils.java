package org.springframework.data.redis.core.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.redis.connection.DefaultSortParameters;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.serializer.RedisSerializer;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/query/QueryUtils.class */
public abstract class QueryUtils {
    public static <K> SortParameters convertQuery(SortQuery<K> query, RedisSerializer<String> stringSerializer) {
        return new DefaultSortParameters(stringSerializer.serialize(query.getBy()), query.getLimit(), serialize(query.getGetPattern(), stringSerializer), query.getOrder(), query.isAlphabetic());
    }

    private static byte[][] serialize(List<String> strings, RedisSerializer<String> stringSerializer) {
        List<byte[]> raw;
        if (strings == null) {
            raw = Collections.emptyList();
        } else {
            raw = new ArrayList<>(strings.size());
            for (String key : strings) {
                raw.add(stringSerializer.serialize(key));
            }
        }
        return (byte[][]) raw.toArray((Object[]) new byte[raw.size()]);
    }
}
