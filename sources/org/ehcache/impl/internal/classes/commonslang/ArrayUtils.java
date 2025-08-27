package org.ehcache.impl.internal.classes.commonslang;

import java.lang.reflect.Array;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/classes/commonslang/ArrayUtils.class */
public class ArrayUtils {
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    public static Object[] nullToEmpty(Object[] array) {
        if (isEmpty(array)) {
            return EMPTY_OBJECT_ARRAY;
        }
        return array;
    }

    public static Class<?>[] nullToEmpty(Class<?>[] array) {
        if (isEmpty(array)) {
            return EMPTY_CLASS_ARRAY;
        }
        return array;
    }

    public static boolean isSameLength(Object[] array1, Object[] array2) {
        return getLength(array1) == getLength(array2);
    }

    public static int getLength(Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static boolean isEmpty(Object[] array) {
        return getLength(array) == 0;
    }
}
