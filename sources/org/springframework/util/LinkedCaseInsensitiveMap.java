package org.springframework.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/util/LinkedCaseInsensitiveMap.class */
public class LinkedCaseInsensitiveMap<V> implements Map<String, V>, Serializable, Cloneable {
    private final LinkedHashMap<String, V> targetMap;
    private final HashMap<String, String> caseInsensitiveKeys;
    private final Locale locale;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public /* bridge */ /* synthetic */ Object put(String str, Object obj) {
        return put2(str, (String) obj);
    }

    public LinkedCaseInsensitiveMap() {
        this((Locale) null);
    }

    public LinkedCaseInsensitiveMap(Locale locale) {
        this(16, locale);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale) {
        this.targetMap = new LinkedHashMap<String, V>(initialCapacity) { // from class: org.springframework.util.LinkedCaseInsensitiveMap.1
            @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
            public boolean containsKey(Object key) {
                return LinkedCaseInsensitiveMap.this.containsKey(key);
            }

            @Override // java.util.LinkedHashMap
            protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
                boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
                if (doRemove) {
                    LinkedCaseInsensitiveMap.this.caseInsensitiveKeys.remove(LinkedCaseInsensitiveMap.this.convertKey(eldest.getKey()));
                }
                return doRemove;
            }
        };
        this.caseInsensitiveKeys = new HashMap<>(initialCapacity);
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap<V> other) {
        this.targetMap = (LinkedHashMap) other.targetMap.clone();
        this.caseInsensitiveKeys = (HashMap) other.caseInsensitiveKeys.clone();
        this.locale = other.locale;
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
        return (key instanceof String) && this.caseInsensitiveKeys.containsKey(convertKey((String) key));
    }

    @Override // java.util.Map
    public boolean containsValue(Object value) {
        return this.targetMap.containsValue(value);
    }

    @Override // java.util.Map
    public V get(Object key) {
        String caseInsensitiveKey;
        if ((key instanceof String) && (caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String) key))) != null) {
            return this.targetMap.get(caseInsensitiveKey);
        }
        return null;
    }

    @Override // java.util.Map
    public V getOrDefault(Object key, V defaultValue) {
        String caseInsensitiveKey;
        if ((key instanceof String) && (caseInsensitiveKey = this.caseInsensitiveKeys.get(convertKey((String) key))) != null) {
            return this.targetMap.get(caseInsensitiveKey);
        }
        return defaultValue;
    }

    /* renamed from: put, reason: avoid collision after fix types in other method */
    public V put2(String key, V value) {
        String oldKey = this.caseInsensitiveKeys.put(convertKey(key), key);
        if (oldKey != null && !oldKey.equals(key)) {
            this.targetMap.remove(oldKey);
        }
        return this.targetMap.put(key, value);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends V> map) {
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
            put2(entry.getKey(), (String) entry.getValue());
        }
    }

    @Override // java.util.Map
    public V remove(Object key) {
        String caseInsensitiveKey;
        if ((key instanceof String) && (caseInsensitiveKey = this.caseInsensitiveKeys.remove(convertKey((String) key))) != null) {
            return this.targetMap.remove(caseInsensitiveKey);
        }
        return null;
    }

    @Override // java.util.Map
    public void clear() {
        this.caseInsensitiveKeys.clear();
        this.targetMap.clear();
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.targetMap.keySet();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        return this.targetMap.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, V>> entrySet() {
        return this.targetMap.entrySet();
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public LinkedCaseInsensitiveMap<V> m8079clone() {
        return new LinkedCaseInsensitiveMap<>(this);
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

    public Locale getLocale() {
        return this.locale;
    }

    protected String convertKey(String key) {
        return key.toLowerCase(getLocale());
    }

    protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
        return false;
    }
}
