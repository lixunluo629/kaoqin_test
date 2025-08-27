package org.ehcache.jsr107;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/jsr107/Unwrap.class */
final class Unwrap {
    static <T> T unwrap(Class<T> clazz, Object... obj) {
        if (clazz == null || obj == null) {
            throw new NullPointerException();
        }
        for (Object o : obj) {
            if (o != null && clazz.isAssignableFrom(o.getClass())) {
                return clazz.cast(o);
            }
        }
        throw new IllegalArgumentException("Cannot unwrap to " + clazz);
    }

    private Unwrap() {
    }
}
