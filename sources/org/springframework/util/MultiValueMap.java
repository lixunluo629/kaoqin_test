package org.springframework.util;

import java.util.List;
import java.util.Map;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/MultiValueMap.class */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {
    V getFirst(K k);

    void add(K k, V v);

    void set(K k, V v);

    void setAll(Map<K, V> map);

    Map<K, V> toSingleValueMap();
}
