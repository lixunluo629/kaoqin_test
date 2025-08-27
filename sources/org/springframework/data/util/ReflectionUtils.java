package org.springframework.data.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ReflectionUtils.class */
public abstract class ReflectionUtils {
    private static final Class<?> JAVA8_STREAM_TYPE;

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ReflectionUtils$DescribedFieldFilter.class */
    public interface DescribedFieldFilter extends ReflectionUtils.FieldFilter {
        String getDescription();
    }

    static {
        Class<?> cls = null;
        try {
            cls = Class.forName("java.util.stream.Stream");
        } catch (ClassNotFoundException e) {
        }
        JAVA8_STREAM_TYPE = cls;
    }

    private ReflectionUtils() {
    }

    public static <T> T createInstanceIfPresent(String str, T t) {
        try {
            return (T) BeanUtils.instantiateClass(ClassUtils.getDefaultClassLoader().loadClass(str));
        } catch (Exception e) {
            return t;
        }
    }

    public static boolean isDefaultMethod(Method method) {
        return (method.getModifiers() & 1033) == 1 && method.getDeclaringClass().isInterface();
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/ReflectionUtils$AnnotationFieldFilter.class */
    public static class AnnotationFieldFilter implements DescribedFieldFilter {
        private final Class<? extends Annotation> annotationType;

        public AnnotationFieldFilter(Class<? extends Annotation> annotationType) {
            Assert.notNull(annotationType, "Annotation type must not be null!");
            this.annotationType = annotationType;
        }

        @Override // org.springframework.util.ReflectionUtils.FieldFilter
        public boolean matches(Field field) {
            return AnnotationUtils.getAnnotation(field, this.annotationType) != null;
        }

        @Override // org.springframework.data.util.ReflectionUtils.DescribedFieldFilter
        public String getDescription() {
            return String.format("Annotation filter for %s", this.annotationType.getName());
        }
    }

    public static Field findField(Class<?> type, final ReflectionUtils.FieldFilter filter) {
        return findField(type, new DescribedFieldFilter() { // from class: org.springframework.data.util.ReflectionUtils.1
            @Override // org.springframework.util.ReflectionUtils.FieldFilter
            public boolean matches(Field field) {
                return filter.matches(field);
            }

            @Override // org.springframework.data.util.ReflectionUtils.DescribedFieldFilter
            public String getDescription() {
                return String.format("FieldFilter %s", filter.toString());
            }
        }, false);
    }

    public static Field findField(Class<?> type, DescribedFieldFilter filter) {
        return findField(type, filter, true);
    }

    public static Field findField(Class<?> type, DescribedFieldFilter filter, boolean enforceUniqueness) {
        Assert.notNull(type, "Type must not be null!");
        Assert.notNull(filter, "Filter must not be null!");
        Field foundField = null;
        for (Class<?> targetClass = type; targetClass != Object.class; targetClass = targetClass.getSuperclass()) {
            for (Field field : targetClass.getDeclaredFields()) {
                if (filter.matches(field)) {
                    if (!enforceUniqueness) {
                        return field;
                    }
                    if (foundField != null && enforceUniqueness) {
                        throw new IllegalStateException(filter.getDescription());
                    }
                    foundField = field;
                }
            }
        }
        return foundField;
    }

    public static void setField(Field field, Object target, Object value) throws IllegalAccessException, IllegalArgumentException {
        org.springframework.util.ReflectionUtils.makeAccessible(field);
        org.springframework.util.ReflectionUtils.setField(field, target, value);
    }

    public static boolean isJava8StreamType(Class<?> type) {
        if (type == null || JAVA8_STREAM_TYPE == null) {
            return false;
        }
        return JAVA8_STREAM_TYPE.isAssignableFrom(type);
    }

    public static Constructor<?> findConstructor(Class<?> type, Object... constructorArguments) throws SecurityException {
        Assert.notNull(type, "Target type must not be null!");
        Assert.notNull(constructorArguments, "Constructor arguments must not be null!");
        for (Constructor<?> candidate : type.getDeclaredConstructors()) {
            Class<?>[] parameterTypes = candidate.getParameterTypes();
            if (argumentsMatch(parameterTypes, constructorArguments)) {
                return candidate;
            }
        }
        return null;
    }

    private static final boolean argumentsMatch(Class<?>[] parameterTypes, Object[] arguments) {
        if (parameterTypes.length != arguments.length) {
            return false;
        }
        int index = 0;
        for (Class<?> argumentType : parameterTypes) {
            Object argument = arguments[index];
            if (argumentType.isPrimitive() && argument == null) {
                return false;
            }
            if (argument != null && !ClassUtils.isAssignableValue(argumentType, argument)) {
                return false;
            }
            index++;
        }
        return true;
    }
}
