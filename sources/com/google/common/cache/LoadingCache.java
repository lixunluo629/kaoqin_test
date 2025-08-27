package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@Beta
@GwtCompatible
/* loaded from: guava-18.0.jar:com/google/common/cache/LoadingCache.class */
public interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
    V get(K k) throws ExecutionException;

    V getUnchecked(K k);

    ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException;

    @Override // com.google.common.base.Function
    @Deprecated
    V apply(K k);

    void refresh(K k);

    @Override // com.google.common.cache.Cache
    ConcurrentMap<K, V> asMap();
}
