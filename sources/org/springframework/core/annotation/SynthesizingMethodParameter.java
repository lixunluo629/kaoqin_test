package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.springframework.core.MethodParameter;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/SynthesizingMethodParameter.class */
public class SynthesizingMethodParameter extends MethodParameter {
    public SynthesizingMethodParameter(Method method, int parameterIndex) {
        super(method, parameterIndex);
    }

    public SynthesizingMethodParameter(Method method, int parameterIndex, int nestingLevel) {
        super(method, parameterIndex, nestingLevel);
    }

    public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex) {
        super(constructor, parameterIndex);
    }

    public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        super(constructor, parameterIndex, nestingLevel);
    }

    protected SynthesizingMethodParameter(SynthesizingMethodParameter original) {
        super(original);
    }

    @Override // org.springframework.core.MethodParameter
    protected <A extends Annotation> A adaptAnnotation(A a) {
        return (A) AnnotationUtils.synthesizeAnnotation((Annotation) a, getAnnotatedElement());
    }

    @Override // org.springframework.core.MethodParameter
    protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
        return AnnotationUtils.synthesizeAnnotationArray(annotations, getAnnotatedElement());
    }

    @Override // org.springframework.core.MethodParameter
    /* renamed from: clone */
    public SynthesizingMethodParameter mo7600clone() {
        return new SynthesizingMethodParameter(this);
    }
}
