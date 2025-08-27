package org.ehcache.sizeof.filters;

import java.lang.reflect.Field;
import java.util.Collection;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/filters/SizeOfFilter.class */
public interface SizeOfFilter {
    Collection<Field> filterFields(Class<?> cls, Collection<Field> collection);

    boolean filterClass(Class<?> cls);
}
