package org.springframework.data.redis.connection.convert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/MapConverter.class */
public class MapConverter<S, T> implements Converter<Map<S, S>, Map<T, T>> {
    private Converter<S, T> itemConverter;

    public MapConverter(Converter<S, T> itemConverter) {
        this.itemConverter = itemConverter;
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Map<T, T> convert(Map<S, S> source) {
        Map<T, T> results;
        if (source == null) {
            return null;
        }
        if (source instanceof LinkedHashMap) {
            results = new LinkedHashMap<>();
        } else {
            results = new HashMap<>();
        }
        for (Map.Entry<S, S> result : source.entrySet()) {
            results.put(this.itemConverter.convert(result.getKey()), this.itemConverter.convert(result.getValue()));
        }
        return results;
    }
}
