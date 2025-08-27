package org.ehcache.sizeof.filters;

import java.lang.reflect.Field;
import java.util.Collection;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/filters/CombinationSizeOfFilter.class */
public class CombinationSizeOfFilter implements SizeOfFilter {
    private final SizeOfFilter[] filters;

    public CombinationSizeOfFilter(SizeOfFilter... filters) {
        this.filters = filters;
    }

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public Collection<Field> filterFields(Class<?> klazz, Collection<Field> fields) {
        Collection<Field> current = fields;
        SizeOfFilter[] arr$ = this.filters;
        for (SizeOfFilter filter : arr$) {
            current = filter.filterFields(klazz, current);
        }
        return current;
    }

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public boolean filterClass(Class<?> klazz) {
        SizeOfFilter[] arr$ = this.filters;
        for (SizeOfFilter filter : arr$) {
            if (!filter.filterClass(klazz)) {
                return false;
            }
        }
        return true;
    }
}
