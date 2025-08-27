package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

@GwtCompatible(emulated = true)
/* loaded from: guava-18.0.jar:com/google/common/collect/Platform.class */
final class Platform {
    static <T> T[] newArray(T[] tArr, int i) {
        return (T[]) ((Object[]) Array.newInstance(tArr.getClass().getComponentType(), i));
    }

    static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        return Collections.newSetFromMap(map);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.common.collect.MapMaker] */
    static MapMaker tryWeakKeys(MapMaker mapMaker) {
        return mapMaker.weakKeys2();
    }

    static <K, V1, V2> SortedMap<K, V2> mapsTransformEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
        return fromMap instanceof NavigableMap ? Maps.transformEntries((NavigableMap) fromMap, (Maps.EntryTransformer) transformer) : Maps.transformEntriesIgnoreNavigable(fromMap, transformer);
    }

    static <K, V> SortedMap<K, V> mapsAsMapSortedSet(SortedSet<K> set, Function<? super K, V> function) {
        return set instanceof NavigableSet ? Maps.asMap((NavigableSet) set, (Function) function) : Maps.asMapSortedIgnoreNavigable(set, function);
    }

    static <E> SortedSet<E> setsFilterSortedSet(SortedSet<E> set, Predicate<? super E> predicate) {
        return set instanceof NavigableSet ? Sets.filter((NavigableSet) set, (Predicate) predicate) : Sets.filterSortedIgnoreNavigable(set, predicate);
    }

    static <K, V> SortedMap<K, V> mapsFilterSortedMap(SortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> predicate) {
        return map instanceof NavigableMap ? Maps.filterEntries((NavigableMap) map, (Predicate) predicate) : Maps.filterSortedIgnoreNavigable(map, predicate);
    }

    private Platform() {
    }
}
