package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/cache/Weigher.class */
public interface Weigher<K, V> {
    int weigh(K k, V v);
}
