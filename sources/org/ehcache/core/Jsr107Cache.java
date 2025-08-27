package org.ehcache.core;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.ehcache.Cache;
import org.ehcache.core.spi.function.BiFunction;
import org.ehcache.core.spi.function.Function;
import org.ehcache.core.spi.function.NullaryFunction;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/Jsr107Cache.class */
public interface Jsr107Cache<K, V> {
    Map<K, V> getAll(Set<? extends K> set);

    V getAndRemove(K k);

    V getAndPut(K k, V v);

    boolean remove(K k);

    void removeAll();

    void compute(K k, BiFunction<? super K, ? super V, ? extends V> biFunction, NullaryFunction<Boolean> nullaryFunction, NullaryFunction<Boolean> nullaryFunction2, NullaryFunction<Boolean> nullaryFunction3);

    void loadAll(Set<? extends K> set, boolean z, Function<Iterable<? extends K>, Map<K, V>> function);

    Iterator<Cache.Entry<K, V>> specIterator();

    V getNoLoader(K k);
}
