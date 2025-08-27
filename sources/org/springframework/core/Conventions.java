package org.springframework.core;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/Conventions.class */
public abstract class Conventions {
    private static final String PLURAL_SUFFIX = "List";
    private static final Set<Class<?>> IGNORED_INTERFACES = Collections.unmodifiableSet(new HashSet(Arrays.asList(Serializable.class, Externalizable.class, Cloneable.class, Comparable.class)));

    public static String getVariableName(Object value) {
        Class<?> valueClass;
        Assert.notNull(value, "Value must not be null");
        boolean pluralize = false;
        if (value.getClass().isArray()) {
            valueClass = value.getClass().getComponentType();
            pluralize = true;
        } else if (value instanceof Collection) {
            Collection<?> collection = (Collection) value;
            if (collection.isEmpty()) {
                throw new IllegalArgumentException("Cannot generate variable name for an empty Collection");
            }
            Object valueToCheck = peekAhead(collection);
            valueClass = getClassForValue(valueToCheck);
            pluralize = true;
        } else {
            valueClass = getClassForValue(value);
        }
        String name = ClassUtils.getShortNameAsProperty(valueClass);
        return pluralize ? pluralize(name) : name;
    }

    public static String getVariableNameForParameter(MethodParameter parameter) {
        Class<?> valueClass;
        Assert.notNull(parameter, "MethodParameter must not be null");
        boolean pluralize = false;
        if (parameter.getParameterType().isArray()) {
            valueClass = parameter.getParameterType().getComponentType();
            pluralize = true;
        } else if (Collection.class.isAssignableFrom(parameter.getParameterType())) {
            valueClass = ResolvableType.forMethodParameter(parameter).asCollection().resolveGeneric(new int[0]);
            if (valueClass == null) {
                throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection parameter type");
            }
            pluralize = true;
        } else {
            valueClass = parameter.getParameterType();
        }
        String name = ClassUtils.getShortNameAsProperty(valueClass);
        return pluralize ? pluralize(name) : name;
    }

    public static String getVariableNameForReturnType(Method method) {
        return getVariableNameForReturnType(method, method.getReturnType(), null);
    }

    public static String getVariableNameForReturnType(Method method, Object value) {
        return getVariableNameForReturnType(method, method.getReturnType(), value);
    }

    public static String getVariableNameForReturnType(Method method, Class<?> resolvedType, Object value) {
        Class<?> valueClass;
        Assert.notNull(method, "Method must not be null");
        if (Object.class == resolvedType) {
            if (value == null) {
                throw new IllegalArgumentException("Cannot generate variable name for an Object return type with null value");
            }
            return getVariableName(value);
        }
        boolean pluralize = false;
        if (resolvedType.isArray()) {
            valueClass = resolvedType.getComponentType();
            pluralize = true;
        } else if (Collection.class.isAssignableFrom(resolvedType)) {
            valueClass = ResolvableType.forMethodReturnType(method).asCollection().resolveGeneric(new int[0]);
            if (valueClass == null) {
                if (!(value instanceof Collection)) {
                    throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and a non-Collection value");
                }
                Collection<?> collection = (Collection) value;
                if (collection.isEmpty()) {
                    throw new IllegalArgumentException("Cannot generate variable name for non-typed Collection return type and an empty Collection value");
                }
                Object valueToCheck = peekAhead(collection);
                valueClass = getClassForValue(valueToCheck);
            }
            pluralize = true;
        } else {
            valueClass = resolvedType;
        }
        String name = ClassUtils.getShortNameAsProperty(valueClass);
        return pluralize ? pluralize(name) : name;
    }

    public static String attributeNameToPropertyName(String attributeName) {
        Assert.notNull(attributeName, "'attributeName' must not be null");
        if (!attributeName.contains("-")) {
            return attributeName;
        }
        char[] chars = attributeName.toCharArray();
        char[] result = new char[chars.length - 1];
        int currPos = 0;
        boolean upperCaseNext = false;
        for (char c : chars) {
            if (c == '-') {
                upperCaseNext = true;
            } else if (upperCaseNext) {
                int i = currPos;
                currPos++;
                result[i] = Character.toUpperCase(c);
                upperCaseNext = false;
            } else {
                int i2 = currPos;
                currPos++;
                result[i2] = c;
            }
        }
        return new String(result, 0, currPos);
    }

    public static String getQualifiedAttributeName(Class<?> enclosingClass, String attributeName) {
        Assert.notNull(enclosingClass, "'enclosingClass' must not be null");
        Assert.notNull(attributeName, "'attributeName' must not be null");
        return enclosingClass.getName() + '.' + attributeName;
    }

    private static Class<?> getClassForValue(Object value) {
        Class<?> valueClass = value.getClass();
        if (Proxy.isProxyClass(valueClass)) {
            Class<?>[] ifcs = valueClass.getInterfaces();
            for (Class<?> ifc : ifcs) {
                if (!IGNORED_INTERFACES.contains(ifc)) {
                    return ifc;
                }
            }
        } else if (valueClass.getName().lastIndexOf(36) != -1 && valueClass.getDeclaringClass() == null) {
            valueClass = valueClass.getSuperclass();
        }
        return valueClass;
    }

    private static String pluralize(String name) {
        return name + PLURAL_SUFFIX;
    }

    private static <E> E peekAhead(Collection<E> collection) {
        Iterator<E> it = collection.iterator();
        if (!it.hasNext()) {
            throw new IllegalStateException("Unable to peek ahead in non-empty collection - no element found");
        }
        E value = it.next();
        if (value == null) {
            throw new IllegalStateException("Unable to peek ahead in non-empty collection - only null element found");
        }
        return value;
    }
}
