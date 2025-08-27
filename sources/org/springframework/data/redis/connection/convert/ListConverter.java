package org.springframework.data.redis.connection.convert;

import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/ListConverter.class */
public class ListConverter<S, T> implements Converter<List<S>, List<T>> {
    private Converter<S, T> itemConverter;

    public ListConverter(Converter<S, T> itemConverter) {
        this.itemConverter = itemConverter;
    }

    @Override // org.springframework.core.convert.converter.Converter
    public List<T> convert(List<S> source) {
        if (source == null) {
            return null;
        }
        List<T> results = new ArrayList<>();
        for (S result : source) {
            results.add(this.itemConverter.convert(result));
        }
        return results;
    }
}
