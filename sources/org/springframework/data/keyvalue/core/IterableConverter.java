package org.springframework.data.keyvalue.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/core/IterableConverter.class */
public final class IterableConverter {
    private IterableConverter() {
    }

    public static <T> List<T> toList(Iterable<T> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        if (source instanceof List) {
            return (List) source;
        }
        if (source instanceof Collection) {
            return new ArrayList((Collection) source);
        }
        List<T> result = new ArrayList<>();
        for (T value : source) {
            result.add(value);
        }
        return result;
    }
}
