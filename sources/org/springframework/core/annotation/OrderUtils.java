package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import org.springframework.util.ClassUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/OrderUtils.class */
public abstract class OrderUtils {
    private static Class<? extends Annotation> priorityAnnotationType;

    static {
        priorityAnnotationType = null;
        try {
            priorityAnnotationType = ClassUtils.forName("javax.annotation.Priority", OrderUtils.class.getClassLoader());
        } catch (Throwable th) {
        }
    }

    public static Integer getOrder(Class<?> type) {
        return getOrder(type, null);
    }

    public static Integer getOrder(Class<?> type, Integer defaultOrder) {
        Order order = (Order) AnnotationUtils.findAnnotation(type, Order.class);
        if (order != null) {
            return Integer.valueOf(order.value());
        }
        Integer priorityOrder = getPriority(type);
        if (priorityOrder != null) {
            return priorityOrder;
        }
        return defaultOrder;
    }

    public static Integer getPriority(Class<?> type) {
        Annotation priority;
        if (priorityAnnotationType != null && (priority = AnnotationUtils.findAnnotation(type, (Class<Annotation>) priorityAnnotationType)) != null) {
            return (Integer) AnnotationUtils.getValue(priority);
        }
        return null;
    }
}
