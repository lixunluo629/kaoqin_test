package org.springframework.data.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/AnnotationDetectionMethodCallback.class */
public class AnnotationDetectionMethodCallback<A extends Annotation> implements ReflectionUtils.MethodCallback {
    private static final String MULTIPLE_FOUND = "Found annotation %s both on %s and %s! Make sure only one of them is annotated with it!";
    private final boolean enforceUniqueness;
    private final Class<A> annotationType;
    private Method foundMethod;
    private A annotation;

    public AnnotationDetectionMethodCallback(Class<A> annotationType) {
        this(annotationType, false);
    }

    public AnnotationDetectionMethodCallback(Class<A> annotationType, boolean enforceUniqueness) {
        Assert.notNull(annotationType, "Annotation type must not be null!");
        this.annotationType = annotationType;
        this.enforceUniqueness = enforceUniqueness;
    }

    public Method getMethod() {
        return this.foundMethod;
    }

    public A getAnnotation() {
        return this.annotation;
    }

    public boolean hasFoundAnnotation() {
        return this.annotation != null;
    }

    @Override // org.springframework.util.ReflectionUtils.MethodCallback
    public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
        A a;
        if ((this.foundMethod == null || this.enforceUniqueness) && (a = (A) AnnotatedElementUtils.findMergedAnnotation(method, this.annotationType)) != null) {
            if (this.foundMethod != null && this.enforceUniqueness) {
                throw new IllegalStateException(String.format(MULTIPLE_FOUND, a.getClass().getName(), this.foundMethod, method));
            }
            this.annotation = a;
            this.foundMethod = method;
        }
    }
}
