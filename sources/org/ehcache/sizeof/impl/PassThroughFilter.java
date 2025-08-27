package org.ehcache.sizeof.impl;

import java.lang.reflect.Field;
import java.util.Collection;
import org.ehcache.sizeof.filters.SizeOfFilter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/impl/PassThroughFilter.class */
public class PassThroughFilter implements SizeOfFilter {
    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public Collection<Field> filterFields(Class<?> klazz, Collection<Field> fields) {
        return fields;
    }

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public boolean filterClass(Class<?> klazz) {
        return true;
    }
}
