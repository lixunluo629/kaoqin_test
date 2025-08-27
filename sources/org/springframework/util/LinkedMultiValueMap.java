package org.springframework.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/LinkedMultiValueMap.class */
public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable, Cloneable {
    private static final long serialVersionUID = 3801124242820219131L;
    private final Map<K, List<V>> targetMap;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((LinkedMultiValueMap<K, V>) obj, (List) obj2);
    }

    public LinkedMultiValueMap() {
        this.targetMap = new LinkedHashMap();
    }

    public LinkedMultiValueMap(int initialCapacity) {
        this.targetMap = new LinkedHashMap(initialCapacity);
    }

    public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
        this.targetMap = new LinkedHashMap(otherMap);
    }

    @Override // org.springframework.util.MultiValueMap
    public V getFirst(K key) {
        List<V> values = this.targetMap.get(key);
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    @Override // org.springframework.util.MultiValueMap
    public void add(K key, V value) {
        List<V> values = this.targetMap.get(key);
        if (values == null) {
            values = new LinkedList();
            this.targetMap.put(key, values);
        }
        values.add(value);
    }

    @Override // org.springframework.util.MultiValueMap
    public void set(K key, V value) {
        List<V> values = new LinkedList<>();
        values.add(value);
        this.targetMap.put(key, values);
    }

    @Override // org.springframework.util.MultiValueMap
    public void setAll(Map<K, V> values) {
        for (Map.Entry<K, V> entry : values.entrySet()) {
            set(entry.getKey(), entry.getValue());
        }
    }

    @Override // org.springframework.util.MultiValueMap
    public Map<K, V> toSingleValueMap() {
        LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<>(this.targetMap.size());
        for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
            List<V> values = entry.getValue();
            if (values != null && !values.isEmpty()) {
                singleValueMap.put(entry.getKey(), values.get(0));
            }
        }
        return singleValueMap;
    }

    @Override // java.util.Map
    public int size() {
        return this.targetMap.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.targetMap.containsValue(value);
    }

    @Override // java.util.Map
    public List<V> get(Object key) {
        return this.targetMap.get(key);
    }

    public List<V> put(K key, List<V> value) {
        return this.targetMap.put(key, value);
    }

    @Override // java.util.Map
    public List<V> remove(Object key) {
        return this.targetMap.remove(key);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends List<V>> map) {
        this.targetMap.putAll(map);
    }

    @Override // java.util.Map
    public void clear() {
        this.targetMap.clear();
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    @Override // java.util.Map
    public Collection<List<V>> values() {
        return this.targetMap.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }

    public LinkedMultiValueMap<K, V> deepCopy() {
        LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap<>(this.targetMap.size());
        for (Map.Entry<K, List<V>> entry : this.targetMap.entrySet()) {
            copy.put((LinkedMultiValueMap<K, V>) entry.getKey(), (List) new LinkedList<>(entry.getValue()));
        }
        return copy;
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LinkedMultiValueMap<K, V> m8080clone() {
        return new LinkedMultiValueMap<>(this);
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        return this.targetMap.equals(obj);
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.targetMap.hashCode();
    }

    public String toString() {
        return this.targetMap.toString();
    }
}
