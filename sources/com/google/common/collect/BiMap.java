package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/collect/BiMap.class */
public interface BiMap<K, V> extends Map<K, V> {
    V put(@Nullable K k, @Nullable V v);

    V forcePut(@Nullable K k, @Nullable V v);

    void putAll(Map<? extends K, ? extends V> map);

    Set<V> values();

    BiMap<V, K> inverse();
}
