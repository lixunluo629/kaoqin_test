package org.springframework.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/CollectionUtils.class */
public abstract class CollectionUtils {
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static List arrayToList(Object source) {
        return Arrays.asList(ObjectUtils.toObjectArray(source));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <E> void mergeArrayIntoCollection(Object array, Collection<E> collection) {
        if (collection == 0) {
            throw new IllegalArgumentException("Collection must not be null");
        }
        Object[] arr = ObjectUtils.toObjectArray(array);
        for (Object elem : arr) {
            collection.add(elem);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> void mergePropertiesIntoMap(Properties props, Map<K, V> map) {
        if (map == 0) {
            throw new IllegalArgumentException("Map must not be null");
        }
        if (props != null) {
            Enumeration<?> en = props.propertyNames();
            while (en.hasMoreElements()) {
                String key = (String) en.nextElement();
                Object value = props.get(key);
                if (value == null) {
                    value = props.getProperty(key);
                }
                map.put(key, value);
            }
        }
    }

    public static boolean contains(Iterator<?> iterator, Object element) {
        if (iterator != null) {
            while (iterator.hasNext()) {
                Object candidate = iterator.next();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean contains(Enumeration<?> enumeration, Object element) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                Object candidate = enumeration.nextElement();
                if (ObjectUtils.nullSafeEquals(candidate, element)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean containsInstance(Collection<?> collection, Object element) {
        if (collection != null) {
            for (Object candidate : collection) {
                if (candidate == element) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
        if (isEmpty(source) || isEmpty(candidates)) {
            return false;
        }
        for (Object candidate : candidates) {
            if (source.contains(candidate)) {
                return true;
            }
        }
        return false;
    }

    public static <E> E findFirstMatch(Collection<?> source, Collection<E> candidates) {
        if (isEmpty(source) || isEmpty((Collection<?>) candidates)) {
            return null;
        }
        for (E e : candidates) {
            if (source.contains(e)) {
                return e;
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
        if (isEmpty(collection)) {
            return null;
        }
        T value = null;
        for (Object obj : collection) {
            if (type == null || type.isInstance(obj)) {
                if (value != null) {
                    return null;
                }
                value = obj;
            }
        }
        return value;
    }

    public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
        if (isEmpty(collection) || ObjectUtils.isEmpty((Object[]) types)) {
            return null;
        }
        for (Class<?> type : types) {
            Object value = findValueOfType(collection, type);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public static boolean hasUniqueObject(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        boolean hasCandidate = false;
        Object candidate = null;
        for (Object elem : collection) {
            if (!hasCandidate) {
                hasCandidate = true;
                candidate = elem;
            } else if (candidate != elem) {
                return false;
            }
        }
        return true;
    }

    public static Class<?> findCommonElementType(Collection<?> collection) {
        if (isEmpty(collection)) {
            return null;
        }
        Class<?> candidate = null;
        for (Object val : collection) {
            if (val != null) {
                if (candidate == null) {
                    candidate = val.getClass();
                } else if (candidate != val.getClass()) {
                    return null;
                }
            }
        }
        return candidate;
    }

    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] aArr) {
        ArrayList arrayList = new ArrayList();
        while (enumeration.hasMoreElements()) {
            arrayList.add(enumeration.nextElement());
        }
        return (A[]) arrayList.toArray(aArr);
    }

    public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
        return enumeration != null ? new EnumerationIterator(enumeration) : Collections.EMPTY_SET.iterator();
    }

    public static <K, V> MultiValueMap<K, V> toMultiValueMap(Map<K, List<V>> map) {
        return new MultiValueMapAdapter(map);
    }

    public static <K, V> MultiValueMap<K, V> unmodifiableMultiValueMap(MultiValueMap<? extends K, ? extends V> map) {
        Assert.notNull(map, "'map' must not be null");
        Map<K, List<V>> result = new LinkedHashMap<>(map.size());
        for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
            result.put(entry.getKey(), Collections.unmodifiableList((List) entry.getValue()));
        }
        Map<K, List<V>> unmodifiableMap = Collections.unmodifiableMap(result);
        return toMultiValueMap(unmodifiableMap);
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/CollectionUtils$EnumerationIterator.class */
    private static class EnumerationIterator<E> implements Iterator<E> {
        private final Enumeration<E> enumeration;

        public EnumerationIterator(Enumeration<E> enumeration) {
            this.enumeration = enumeration;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.enumeration.hasMoreElements();
        }

        @Override // java.util.Iterator
        public E next() {
            return this.enumeration.nextElement();
        }

        @Override // java.util.Iterator
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported");
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/CollectionUtils$MultiValueMapAdapter.class */
    private static class MultiValueMapAdapter<K, V> implements MultiValueMap<K, V>, Serializable {
        private final Map<K, List<V>> map;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Map
        public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
            return put((MultiValueMapAdapter<K, V>) obj, (List) obj2);
        }

        public MultiValueMapAdapter(Map<K, List<V>> map) {
            Assert.notNull(map, "'map' must not be null");
            this.map = map;
        }

        @Override // org.springframework.util.MultiValueMap
        public void add(K key, V value) {
            List<V> values = this.map.get(key);
            if (values == null) {
                values = new LinkedList();
                this.map.put(key, values);
            }
            values.add(value);
        }

        @Override // org.springframework.util.MultiValueMap
        public V getFirst(K key) {
            List<V> values = this.map.get(key);
            if (values != null) {
                return values.get(0);
            }
            return null;
        }

        @Override // org.springframework.util.MultiValueMap
        public void set(K key, V value) {
            List<V> values = new LinkedList<>();
            values.add(value);
            this.map.put(key, values);
        }

        @Override // org.springframework.util.MultiValueMap
        public void setAll(Map<K, V> values) {
            for (Map.Entry<K, V> entry : values.entrySet()) {
                set(entry.getKey(), entry.getValue());
            }
        }

        @Override // org.springframework.util.MultiValueMap
        public Map<K, V> toSingleValueMap() {
            LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.map.size());
            for (Map.Entry<K, List<V>> entry : this.map.entrySet()) {
                singleValueMap.put(entry.getKey(), entry.getValue().get(0));
            }
            return singleValueMap;
        }

        @Override // java.util.Map
        public int size() {
            return this.map.size();
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override // java.util.Map
        public boolean containsKey(Object key) {
            return this.map.containsKey(key);
        }

        @Override // java.util.Map
        public boolean containsValue(Object value) {
            return this.map.containsValue(value);
        }

        @Override // java.util.Map
        public List<V> get(Object key) {
            return this.map.get(key);
        }

        public List<V> put(K key, List<V> value) {
            return this.map.put(key, value);
        }

        @Override // java.util.Map
        public List<V> remove(Object key) {
            return this.map.remove(key);
        }

        @Override // java.util.Map
        public void putAll(Map<? extends K, ? extends List<V>> map) {
            this.map.putAll(map);
        }

        @Override // java.util.Map
        public void clear() {
            this.map.clear();
        }

        @Override // java.util.Map
        public Set<K> keySet() {
            return this.map.keySet();
        }

        @Override // java.util.Map
        public Collection<List<V>> values() {
            return this.map.values();
        }

        @Override // java.util.Map
        public Set<Map.Entry<K, List<V>>> entrySet() {
            return this.map.entrySet();
        }

        @Override // java.util.Map
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            return this.map.equals(other);
        }

        @Override // java.util.Map
        public int hashCode() {
            return this.map.hashCode();
        }

        public String toString() {
            return this.map.toString();
        }
    }
}
