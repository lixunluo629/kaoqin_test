package org.springframework.aop.support.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.util.Assert;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/support/annotation/AnnotationMethodMatcher.class */
public class AnnotationMethodMatcher extends StaticMethodMatcher {
    private final Class<? extends Annotation> annotationType;

    public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "Annotation type must not be null");
        this.annotationType = annotationType;
    }

    @Override // org.springframework.aop.MethodMatcher
    public boolean matches(Method method, Class<?> targetClass) {
        if (method.isAnnotationPresent(this.annotationType)) {
            return true;
        }
        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        return specificMethod != method && specificMethod.isAnnotationPresent(this.annotationType);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AnnotationMethodMatcher)) {
            return false;
        }
        AnnotationMethodMatcher otherMm = (AnnotationMethodMatcher) other;
        return this.annotationType.equals(otherMm.annotationType);
    }

    public int hashCode() {
        return this.annotationType.hashCode();
    }

    public String toString() {
        return getClass().getName() + ": " + this.annotationType;
    }
}
