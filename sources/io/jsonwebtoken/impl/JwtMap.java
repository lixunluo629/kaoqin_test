package io.jsonwebtoken.impl;

import io.jsonwebtoken.lang.Assert;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/impl/JwtMap.class */
public class JwtMap implements Map<String, Object> {
    private final Map<String, Object> map;

    public JwtMap() {
        this(new LinkedHashMap());
    }

    public JwtMap(Map<String, Object> map) {
        Assert.notNull(map, "Map argument cannot be null.");
        this.map = map;
    }

    protected String getString(String name) {
        Object v = get(name);
        if (v != null) {
            return String.valueOf(v);
        }
        return null;
    }

    protected static Date toDate(Object v, String name) throws NumberFormatException {
        if (v == null) {
            return null;
        }
        if (v instanceof Date) {
            return (Date) v;
        }
        if (v instanceof Number) {
            long seconds = ((Number) v).longValue();
            long millis = seconds * 1000;
            return new Date(millis);
        }
        if (v instanceof String) {
            long seconds2 = Long.parseLong((String) v);
            long millis2 = seconds2 * 1000;
            return new Date(millis2);
        }
        throw new IllegalStateException("Cannot convert '" + name + "' value [" + v + "] to Date instance.");
    }

    protected void setValue(String name, Object v) {
        if (v == null) {
            this.map.remove(name);
        } else {
            this.map.put(name, v);
        }
    }

    protected Date getDate(String name) {
        Object v = this.map.get(name);
        return toDate(v, name);
    }

    protected void setDate(String name, Date d) {
        if (d == null) {
            this.map.remove(name);
        } else {
            long seconds = d.getTime() / 1000;
            this.map.put(name, Long.valueOf(seconds));
        }
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
    public boolean containsKey(Object o) {
        return this.map.containsKey(o);
    }

    @Override // java.util.Map
    public boolean containsValue(Object o) {
        return this.map.containsValue(o);
    }

    @Override // java.util.Map
    public Object get(Object o) {
        return this.map.get(o);
    }

    @Override // java.util.Map
    public Object put(String s, Object o) {
        if (o == null) {
            return this.map.remove(s);
        }
        return this.map.put(s, o);
    }

    @Override // java.util.Map
    public Object remove(Object o) {
        return this.map.remove(o);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends String, ? extends Object> map) {
        if (map == null) {
            return;
        }
        for (String s : map.keySet()) {
            this.map.put(s, map.get(s));
        }
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    public String toString() {
        return this.map.toString();
    }
}
