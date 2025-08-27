package org.hibernate.validator.internal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/CollectionHelper.class */
public final class CollectionHelper {

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/CollectionHelper$Partitioner.class */
    public interface Partitioner<K, V> {
        K getPartition(V v);
    }

    private CollectionHelper() {
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<>();
    }

    public static <K, V> HashMap<K, V> newHashMap(int size) {
        return new HashMap<>(size);
    }

    public static <K, V> HashMap<K, V> newHashMap(Map<K, V> map) {
        return new HashMap<>(map);
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public static <T> HashSet<T> newHashSet() {
        return new HashSet<>();
    }

    public static <T> HashSet<T> newHashSet(int size) {
        return new HashSet<>(size);
    }

    public static <T> HashSet<T> newHashSet(Collection<? extends T> c) {
        return new HashSet<>(c);
    }

    public static <T> HashSet<T> newHashSet(Collection<? extends T> s1, Collection<? extends T> s2) {
        HashSet<T> set = newHashSet((Collection) s1);
        set.addAll(s2);
        return set;
    }

    public static <T> HashSet<T> newHashSet(Iterable<? extends T> iterable) {
        HashSet<T> set = newHashSet();
        for (T t : iterable) {
            set.add(t);
        }
        return set;
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> ArrayList<T> newArrayList(int size) {
        return new ArrayList<>(size);
    }

    public static <T> ArrayList<T> newArrayList(Iterable<T>... iterables) {
        ArrayList<T> resultList = newArrayList();
        for (Iterable<T> oneIterable : iterables) {
            for (T oneElement : oneIterable) {
                resultList.add(oneElement);
            }
        }
        return resultList;
    }

    public static <T> Set<T> asSet(T... ts) {
        return new HashSet(Arrays.asList(ts));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.util.List] */
    /* JADX WARN: Type inference failed for: r0v17, types: [java.util.ArrayList] */
    public static <K, V> Map<K, List<V>> partition(List<V> list, Partitioner<K, V> partitioner) {
        if (list == null) {
            return Collections.emptyMap();
        }
        Map<K, List<V>> theValue = newHashMap();
        for (V v : list) {
            K key = partitioner.getPartition(v);
            V vNewArrayList = (List) theValue.get(key);
            if (vNewArrayList == null) {
                vNewArrayList = newArrayList();
                theValue.put(key, vNewArrayList);
            }
            vNewArrayList.add(v);
        }
        return theValue;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.util.Set] */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.util.Set] */
    /* JADX WARN: Type inference failed for: r0v17, types: [java.util.HashSet] */
    public static <K, V> Map<K, Set<V>> partition(Set<V> set, Partitioner<K, V> partitioner) {
        if (set == null) {
            return Collections.emptyMap();
        }
        Map<K, Set<V>> theValue = newHashMap();
        for (V v : set) {
            K key = partitioner.getPartition(v);
            V vNewHashSet = (Set) theValue.get(key);
            if (vNewHashSet == null) {
                vNewHashSet = newHashSet();
                theValue.put(key, vNewHashSet);
            }
            vNewHashSet.add(v);
        }
        return theValue;
    }
}
