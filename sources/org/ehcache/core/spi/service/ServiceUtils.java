package org.ehcache.core.spi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/core/spi/service/ServiceUtils.class */
public class ServiceUtils {
    private ServiceUtils() {
    }

    public static <T> Collection<T> findAmongst(Class<T> clazz, Collection<?> instances) {
        return findAmongst(clazz, instances.toArray());
    }

    public static <T> Collection<T> findAmongst(Class<T> clazz, Object... instances) {
        Collection<T> matches = new ArrayList<>();
        for (Object instance : instances) {
            if (instance != null && clazz.isAssignableFrom(instance.getClass())) {
                matches.add(clazz.cast(instance));
            }
        }
        return Collections.unmodifiableCollection(matches);
    }

    public static <T> T findSingletonAmongst(Class<T> cls, Collection<?> collection) {
        return (T) findSingletonAmongst(cls, collection.toArray());
    }

    public static <T> T findSingletonAmongst(Class<T> clazz, Object... instances) {
        Collection<T> matches = findAmongst(clazz, instances);
        if (matches.isEmpty()) {
            return null;
        }
        if (matches.size() == 1) {
            return matches.iterator().next();
        }
        throw new IllegalArgumentException("More than one " + clazz.getName() + " found");
    }
}
