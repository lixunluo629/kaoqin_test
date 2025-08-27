package org.ehcache.impl.internal.classes;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/classes/ClassInstanceProviderConfiguration.class */
public class ClassInstanceProviderConfiguration<K, T> {
    private Map<K, ClassInstanceConfiguration<T>> defaults = new LinkedHashMap();

    public Map<K, ClassInstanceConfiguration<T>> getDefaults() {
        return this.defaults;
    }
}
