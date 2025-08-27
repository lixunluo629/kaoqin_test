package org.apache.commons.collections4;

import java.util.Map;

/* loaded from: commons-collections4-4.1.jar:org/apache/commons/collections4/Put.class */
public interface Put<K, V> {
    void clear();

    Object put(K k, V v);

    void putAll(Map<? extends K, ? extends V> map);
}
