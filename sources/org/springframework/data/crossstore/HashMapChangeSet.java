package org.springframework.data.crossstore;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.ConversionService;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/crossstore/HashMapChangeSet.class */
public class HashMapChangeSet implements ChangeSet {
    private final Map<String, Object> values;

    public HashMapChangeSet(Map<String, Object> values) {
        this.values = values;
    }

    public HashMapChangeSet() {
        this(new HashMap());
    }

    @Override // org.springframework.data.crossstore.ChangeSet
    public void set(String key, Object o) {
        this.values.put(key, o);
    }

    public String toString() {
        return "HashMapChangeSet: values=[" + this.values + "]";
    }

    @Override // org.springframework.data.crossstore.ChangeSet
    public Map<String, Object> getValues() {
        return Collections.unmodifiableMap(this.values);
    }

    @Override // org.springframework.data.crossstore.ChangeSet
    public Object removeProperty(String k) {
        return this.values.remove(k);
    }

    @Override // org.springframework.data.crossstore.ChangeSet
    public <T> T get(String str, Class<T> cls, ConversionService conversionService) {
        return (T) conversionService.convert(this.values.get(str), cls);
    }
}
