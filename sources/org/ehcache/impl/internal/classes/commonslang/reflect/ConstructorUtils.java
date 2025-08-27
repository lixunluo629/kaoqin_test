package org.ehcache.impl.internal.classes.commonslang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.ehcache.impl.internal.classes.commonslang.ArrayUtils;
import org.ehcache.impl.internal.classes.commonslang.ClassUtils;
import org.ehcache.impl.internal.classes.commonslang.Validate;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/impl/internal/classes/commonslang/reflect/ConstructorUtils.class */
public class ConstructorUtils {
    public static <T> T invokeConstructor(Class<T> cls, Object... objArr) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        Object[] objArrNullToEmpty = ArrayUtils.nullToEmpty(objArr);
        return (T) invokeConstructor(cls, objArrNullToEmpty, ClassUtils.toClass(objArrNullToEmpty));
    }

    public static <T> T invokeConstructor(Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, InvocationTargetException {
        Object[] args2 = ArrayUtils.nullToEmpty(args);
        Constructor<T> ctor = getMatchingAccessibleConstructor(cls, ArrayUtils.nullToEmpty(parameterTypes));
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        return ctor.newInstance(args2);
    }

    public static <T> Constructor<T> getAccessibleConstructor(Constructor<T> ctor) {
        Validate.notNull(ctor, "constructor cannot be null", new Object[0]);
        if (MemberUtils.isAccessible(ctor) && isAccessible(ctor.getDeclaringClass())) {
            return ctor;
        }
        return null;
    }

    public static <T> Constructor<T> getMatchingAccessibleConstructor(Class<T> cls, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Constructor<T> accessibleConstructor;
        Validate.notNull(cls, "class cannot be null", new Object[0]);
        try {
            Constructor<T> ctor = cls.getConstructor(parameterTypes);
            MemberUtils.setAccessibleWorkaround(ctor);
            return ctor;
        } catch (NoSuchMethodException e) {
            Constructor<T> result = null;
            Constructor<?>[] ctors = cls.getConstructors();
            for (Constructor<?> ctor2 : ctors) {
                if (ClassUtils.isAssignable(parameterTypes, ctor2.getParameterTypes(), true) && (accessibleConstructor = getAccessibleConstructor(ctor2)) != null) {
                    MemberUtils.setAccessibleWorkaround(accessibleConstructor);
                    if (result == null || MemberUtils.compareParameterTypes(accessibleConstructor.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
                        result = accessibleConstructor;
                    }
                }
            }
            return result;
        }
    }

    private static boolean isAccessible(Class<?> type) {
        Class<?> enclosingClass = type;
        while (true) {
            Class<?> cls = enclosingClass;
            if (cls != null) {
                if (!Modifier.isPublic(cls.getModifiers())) {
                    return false;
                }
                enclosingClass = cls.getEnclosingClass();
            } else {
                return true;
            }
        }
    }
}
