package org.ehcache.sizeof;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
import org.ehcache.sizeof.filters.AnnotationSizeOfFilter;
import org.ehcache.sizeof.filters.SizeOfFilter;
import org.ehcache.sizeof.filters.TypeFilter;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/SizeOfFilterSource.class */
public final class SizeOfFilterSource implements Filter {
    private final CopyOnWriteArrayList<SizeOfFilter> filters = new CopyOnWriteArrayList<>();
    private final TypeFilter typeFilter = new TypeFilter();

    public SizeOfFilterSource(boolean registerAnnotationFilter) {
        this.filters.add(this.typeFilter);
        if (registerAnnotationFilter) {
            this.filters.add(new AnnotationSizeOfFilter());
        }
        applyMutators();
    }

    private void applyMutators() {
        applyMutators(SizeOfFilterSource.class.getClassLoader());
    }

    void applyMutators(ClassLoader classLoader) {
        ServiceLoader<FilterConfigurator> loader = ServiceLoader.load(FilterConfigurator.class, classLoader);
        Iterator i$ = loader.iterator();
        while (i$.hasNext()) {
            FilterConfigurator filterConfigurator = i$.next();
            filterConfigurator.configure(this);
        }
    }

    public SizeOfFilter[] getFilters() {
        List<SizeOfFilter> allFilters = new ArrayList<>(this.filters);
        return (SizeOfFilter[]) allFilters.toArray(new SizeOfFilter[allFilters.size()]);
    }

    @Override // org.ehcache.sizeof.Filter
    public void ignoreInstancesOf(Class clazz, boolean strict) {
        this.typeFilter.addClass(clazz, Modifier.isFinal(clazz.getModifiers()) || strict);
    }

    @Override // org.ehcache.sizeof.Filter
    public void ignoreField(Field field) {
        this.typeFilter.addField(field);
    }
}
