package org.springframework.data.redis.connection.convert;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/SetConverter.class */
public class SetConverter<S, T> implements Converter<Set<S>, Set<T>> {
    private Converter<S, T> itemConverter;

    public SetConverter(Converter<S, T> itemConverter) {
        this.itemConverter = itemConverter;
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Set<T> convert(Set<S> source) {
        Set<T> results;
        if (source == null) {
            return null;
        }
        if (source instanceof LinkedHashSet) {
            results = new LinkedHashSet<>();
        } else {
            results = new HashSet<>();
        }
        for (S result : source) {
            results.add(this.itemConverter.convert(result));
        }
        return results;
    }
}
