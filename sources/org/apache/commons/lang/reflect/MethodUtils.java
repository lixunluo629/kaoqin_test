package org.apache.commons.lang.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/reflect/MethodUtils.class */
public class MethodUtils {
    public static Object invokeMethod(Object object, String methodName, Object arg) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return invokeMethod(object, methodName, new Object[]{arg});
    }

    public static Object invokeMethod(Object object, String methodName, Object[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeMethod(object, methodName, args, parameterTypes);
    }

    public static Object invokeMethod(Object object, String methodName, Object[] args, Class[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(new StringBuffer().append("No such accessible method: ").append(methodName).append("() on object: ").append(object.getClass().getName()).toString());
        }
        return method.invoke(object, args);
    }

    public static Object invokeExactMethod(Object object, String methodName, Object arg) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return invokeExactMethod(object, methodName, new Object[]{arg});
    }

    public static Object invokeExactMethod(Object object, String methodName, Object[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactMethod(object, methodName, args, parameterTypes);
    }

    public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(new StringBuffer().append("No such accessible method: ").append(methodName).append("() on object: ").append(object.getClass().getName()).toString());
        }
        return method.invoke(object, args);
    }

    public static Object invokeExactStaticMethod(Class cls, String methodName, Object[] args, Class[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        Method method = getAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(new StringBuffer().append("No such accessible method: ").append(methodName).append("() on class: ").append(cls.getName()).toString());
        }
        return method.invoke(null, args);
    }

    public static Object invokeStaticMethod(Class cls, String methodName, Object arg) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return invokeStaticMethod(cls, methodName, new Object[]{arg});
    }

    public static Object invokeStaticMethod(Class cls, String methodName, Object[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeStaticMethod(cls, methodName, args, parameterTypes);
    }

    public static Object invokeStaticMethod(Class cls, String methodName, Object[] args, Class[] parameterTypes) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        if (parameterTypes == null) {
            parameterTypes = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException(new StringBuffer().append("No such accessible method: ").append(methodName).append("() on class: ").append(cls.getName()).toString());
        }
        return method.invoke(null, args);
    }

    public static Object invokeExactStaticMethod(Class cls, String methodName, Object arg) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        return invokeExactStaticMethod(cls, methodName, new Object[]{arg});
    }

    public static Object invokeExactStaticMethod(Class cls, String methodName, Object[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (args == null) {
            args = ArrayUtils.EMPTY_OBJECT_ARRAY;
        }
        int arguments = args.length;
        Class[] parameterTypes = new Class[arguments];
        for (int i = 0; i < arguments; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
    }

    public static Method getAccessibleMethod(Class cls, String methodName, Class parameterType) {
        return getAccessibleMethod(cls, methodName, new Class[]{parameterType});
    }

    public static Method getAccessibleMethod(Class cls, String methodName, Class[] parameterTypes) {
        try {
            return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getAccessibleMethod(Method method) throws NoSuchMethodException, SecurityException {
        if (!MemberUtils.isAccessible(method)) {
            return null;
        }
        Class cls = method.getDeclaringClass();
        if (Modifier.isPublic(cls.getModifiers())) {
            return method;
        }
        String methodName = method.getName();
        Class[] parameterTypes = method.getParameterTypes();
        Method method2 = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
        if (method2 == null) {
            method2 = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
        }
        return method2;
    }

    private static Method getAccessibleMethodFromSuperclass(Class cls, String methodName, Class[] parameterTypes) {
        Class superclass = cls.getSuperclass();
        while (true) {
            Class parentClass = superclass;
            if (parentClass != null) {
                if (Modifier.isPublic(parentClass.getModifiers())) {
                    try {
                        return parentClass.getMethod(methodName, parameterTypes);
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                }
                superclass = parentClass.getSuperclass();
            } else {
                return null;
            }
        }
    }

    private static Method getAccessibleMethodFromInterfaceNest(Class cls, String methodName, Class[] parameterTypes) throws NoSuchMethodException, SecurityException {
        Method method = null;
        while (cls != null) {
            Class[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (Modifier.isPublic(interfaces[i].getModifiers())) {
                    try {
                        method = interfaces[i].getDeclaredMethod(methodName, parameterTypes);
                    } catch (NoSuchMethodException e) {
                    }
                    if (method != null) {
                        break;
                    }
                    method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                    if (method != null) {
                        break;
                    }
                    cls = cls.getSuperclass();
                }
            }
            cls = cls.getSuperclass();
        }
        return method;
    }

    public static Method getMatchingAccessibleMethod(Class cls, String methodName, Class[] parameterTypes) throws NoSuchMethodException, SecurityException {
        Method accessibleMethod;
        try {
            Method method = cls.getMethod(methodName, parameterTypes);
            MemberUtils.setAccessibleWorkaround(method);
            return method;
        } catch (NoSuchMethodException e) {
            Method bestMatch = null;
            Method[] methods = cls.getMethods();
            int size = methods.length;
            for (int i = 0; i < size; i++) {
                if (methods[i].getName().equals(methodName) && ClassUtils.isAssignable(parameterTypes, (Class[]) methods[i].getParameterTypes(), true) && (accessibleMethod = getAccessibleMethod(methods[i])) != null && (bestMatch == null || MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
                    bestMatch = accessibleMethod;
                }
            }
            if (bestMatch != null) {
                MemberUtils.setAccessibleWorkaround(bestMatch);
            }
            return bestMatch;
        }
    }
}
