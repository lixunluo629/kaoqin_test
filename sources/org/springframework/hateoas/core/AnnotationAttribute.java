package org.springframework.hateoas.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/core/AnnotationAttribute.class */
public class AnnotationAttribute {
    private final Class<? extends Annotation> annotationType;
    private final String attributeName;

    public AnnotationAttribute(Class<? extends Annotation> annotationType) {
        this(annotationType, null);
    }

    public AnnotationAttribute(Class<? extends Annotation> annotationType, String attributeName) {
        Assert.notNull(annotationType);
        this.annotationType = annotationType;
        this.attributeName = attributeName;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return this.annotationType;
    }

    public String getValueFrom(MethodParameter parameter) {
        Assert.notNull(parameter, "MethodParameter must not be null!");
        Annotation annotation = parameter.getParameterAnnotation(this.annotationType);
        if (annotation == null) {
            return null;
        }
        return getValueFrom(annotation);
    }

    public String getValueFrom(AnnotatedElement annotatedElement) {
        Assert.notNull(annotatedElement, "Annotated element must not be null!");
        Annotation annotation = annotatedElement.getAnnotation(this.annotationType);
        if (annotation == null) {
            return null;
        }
        return getValueFrom(annotation);
    }

    public String getValueFrom(Annotation annotation) {
        Assert.notNull(annotation, "Annotation must not be null!");
        return (String) (this.attributeName == null ? AnnotationUtils.getValue(annotation) : AnnotationUtils.getValue(annotation, this.attributeName));
    }
}
