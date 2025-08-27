package com.mysql.jdbc.util;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: mysql-connector-java-5.1.48.jar:com/mysql/jdbc/util/LRUCache.class */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1;
    protected int maxElements;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75f, true);
        this.maxElements = maxSize;
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > this.maxElements;
    }
}
