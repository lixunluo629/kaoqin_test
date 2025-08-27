package org.ehcache.sizeof.filters;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.ehcache.sizeof.util.WeakIdentityConcurrentMap;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/filters/TypeFilter.class */
public class TypeFilter implements SizeOfFilter {
    private final WeakIdentityConcurrentMap<Class<?>, Object> classesIgnored = new WeakIdentityConcurrentMap<>();
    private final WeakIdentityConcurrentMap<Class<?>, Object> superClasses = new WeakIdentityConcurrentMap<>();
    private final WeakIdentityConcurrentMap<Class<?>, ConcurrentMap<Field, Object>> fieldsIgnored = new WeakIdentityConcurrentMap<>();

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public Collection<Field> filterFields(Class<?> klazz, Collection<Field> fields) {
        ConcurrentMap<Field, Object> fieldsToIgnore = this.fieldsIgnored.get(klazz);
        if (fieldsToIgnore != null) {
            Iterator<Field> iterator = fields.iterator();
            while (iterator.hasNext()) {
                if (fieldsToIgnore.containsKey(iterator.next())) {
                    iterator.remove();
                }
            }
        }
        return fields;
    }

    @Override // org.ehcache.sizeof.filters.SizeOfFilter
    public boolean filterClass(Class<?> klazz) {
        if (!this.classesIgnored.containsKey(klazz)) {
            for (Class<?> aClass : this.superClasses.keySet()) {
                if (aClass.isAssignableFrom(klazz)) {
                    this.classesIgnored.put(klazz, this);
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void addClass(Class<?> classToFilterOut, boolean strict) {
        if (!strict) {
            this.superClasses.putIfAbsent(classToFilterOut, this);
        } else {
            this.classesIgnored.put(classToFilterOut, this);
        }
    }

    public void addField(Field fieldToFilterOut) {
        Class<?> klazz = fieldToFilterOut.getDeclaringClass();
        ConcurrentMap<Field, Object> fields = this.fieldsIgnored.get(klazz);
        if (fields == null) {
            fields = new ConcurrentHashMap();
            ConcurrentMap<Field, Object> previous = this.fieldsIgnored.putIfAbsent(klazz, fields);
            if (previous != null) {
                fields = previous;
            }
        }
        fields.put(fieldToFilterOut, this);
    }
}
