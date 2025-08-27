package org.apache.commons.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/reflect/ConstructorUtils.class */
public class ConstructorUtils {
    public static Object invokeConstructor(Class cls, Object arg) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        return invokeConstructor(cls, new Object[]{arg});
    }

    public static Object invokeConstructor(Class cls, Object[] args) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        if (null == args) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeConstructor(cls, args, parameterTypes);
    }

    public static Object invokeConstructor(Class cls, Object[] args, Class[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, InvocationTargetException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Constructor ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
        if (null == ctor) {
            throw new NoSuchMethodException(new StringBuffer().append("No such accessible constructor on object: ").append(cls.getName()).toString());
        }
        return ctor.newInstance(args);
    }

    public static Object invokeExactConstructor(Class cls, Object arg) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        return invokeExactConstructor(cls, new Object[]{arg});
    }

    public static Object invokeExactConstructor(Class cls, Object[] args) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        if (null == args) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactConstructor(cls, args, parameterTypes);
    }

    public static Object invokeExactConstructor(Class cls, Object[] args, Class[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Constructor ctor = getAccessibleConstructor(cls, parameterTypes);
        if (null == ctor) {
            throw new NoSuchMethodException(new StringBuffer().append("No such accessible constructor on object: ").append(cls.getName()).toString());
        }
        return ctor.newInstance(args);
    }

    public static Constructor getAccessibleConstructor(Class cls, Class parameterType) {
        return getAccessibleConstructor(cls, new Class[]{parameterType});
    }

    public static Constructor getAccessibleConstructor(Class cls, Class[] parameterTypes) {
        try {
            return getAccessibleConstructor(cls.getConstructor(parameterTypes));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Constructor getAccessibleConstructor(Constructor ctor) {
        if (MemberUtils.isAccessible(ctor) && Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) {
            return ctor;
        }
        return null;
    }

    public static Constructor getMatchingAccessibleConstructor(Class cls, Class[] parameterTypes) throws NoSuchMethodException, SecurityException {
        Constructor ctor;
        try {
            Constructor ctor2 = cls.getConstructor(parameterTypes);
            MemberUtils.setAccessibleWorkaround(ctor2);
            return ctor2;
        } catch (NoSuchMethodException e) {
            Constructor result = null;
            Constructor[] ctors = cls.getConstructors();
            for (int i = 0; i < ctors.length; i++) {
                if (ClassUtils.isAssignable(parameterTypes, (Class[]) ctors[i].getParameterTypes(), true) && (ctor = getAccessibleConstructor(ctors[i])) != null) {
                    MemberUtils.setAccessibleWorkaround(ctor);
                    if (result == null || MemberUtils.compareParameterTypes(ctor.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
                        result = ctor;
                    }
                }
            }
            return result;
        }
    }
}
