package org.hibernate.validator.internal.util.classhierarchy;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.Contracts;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/classhierarchy/ClassHierarchyHelper.class */
public class ClassHierarchyHelper {
    private ClassHierarchyHelper() {
    }

    public static <T> List<Class<? super T>> getHierarchy(Class<T> clazz, Filter... filters) {
        Contracts.assertNotNull(clazz);
        List<Class<? super T>> classes = CollectionHelper.newArrayList();
        List<Filter> allFilters = CollectionHelper.newArrayList();
        allFilters.addAll(Arrays.asList(filters));
        allFilters.add(Filters.excludeProxies());
        getHierarchy(clazz, classes, allFilters);
        return classes;
    }

    private static <T> void getHierarchy(Class<? super T> clazz, List<Class<? super T>> classes, Iterable<Filter> filters) {
        Class<? super T> superclass = clazz;
        while (true) {
            Class<? super T> current = superclass;
            if (current == null || classes.contains(current)) {
                return;
            }
            if (acceptedByAllFilters(current, filters)) {
                classes.add(current);
            }
            for (Class<?> currentInterface : current.getInterfaces()) {
                getHierarchy(currentInterface, classes, filters);
            }
            superclass = current.getSuperclass();
        }
    }

    private static boolean acceptedByAllFilters(Class<?> clazz, Iterable<Filter> filters) {
        for (Filter classFilter : filters) {
            if (!classFilter.accepts(clazz)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Set<Class<? super T>> getDirectlyImplementedInterfaces(Class<T> clazz) {
        Contracts.assertNotNull(clazz);
        Set<Class<? super T>> classes = CollectionHelper.newHashSet();
        getImplementedInterfaces(clazz, classes);
        return classes;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> void getImplementedInterfaces(Class<? super T> clazz, Set<Class<? super T>> set) {
        for (Class<?> currentInterface : clazz.getInterfaces()) {
            set.add(currentInterface);
            getImplementedInterfaces(currentInterface, set);
        }
    }
}
