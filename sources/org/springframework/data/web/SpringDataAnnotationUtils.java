package org.springframework.data.web;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/web/SpringDataAnnotationUtils.class */
abstract class SpringDataAnnotationUtils {
    private SpringDataAnnotationUtils() {
    }

    public static void assertPageableUniqueness(MethodParameter parameter) {
        Method method = parameter.getMethod();
        if (containsMoreThanOnePageableParameter(method)) {
            Annotation[][] annotations = method.getParameterAnnotations();
            assertQualifiersFor(method.getParameterTypes(), annotations);
        }
    }

    private static boolean containsMoreThanOnePageableParameter(Method method) {
        boolean pageableFound = false;
        for (Class<?> type : method.getParameterTypes()) {
            if (pageableFound && type.equals(Pageable.class)) {
                return true;
            }
            if (type.equals(Pageable.class)) {
                pageableFound = true;
            }
        }
        return false;
    }

    public static <T> T getSpecificPropertyOrDefaultFromValue(Annotation annotation, String str) {
        Object defaultValue = AnnotationUtils.getDefaultValue(annotation, str);
        T t = (T) AnnotationUtils.getValue(annotation, str);
        return ObjectUtils.nullSafeEquals(defaultValue, t) ? (T) AnnotationUtils.getValue(annotation) : t;
    }

    public static void assertQualifiersFor(Class<?>[] parameterTypes, Annotation[][] annotations) {
        Set<String> values = new HashSet<>();
        for (int i = 0; i < annotations.length; i++) {
            if (Pageable.class.equals(parameterTypes[i])) {
                Qualifier qualifier = findAnnotation(annotations[i]);
                if (null == qualifier) {
                    throw new IllegalStateException("Ambiguous Pageable arguments in handler method. If you use multiple parameters of type Pageable you need to qualify them with @Qualifier");
                }
                if (values.contains(qualifier.value())) {
                    throw new IllegalStateException("Values of the user Qualifiers must be unique!");
                }
                values.add(qualifier.value());
            }
        }
    }

    public static Qualifier findAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Qualifier) {
                return (Qualifier) annotation;
            }
        }
        return null;
    }
}
