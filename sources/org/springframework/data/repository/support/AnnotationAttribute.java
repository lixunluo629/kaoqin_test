package org.springframework.data.repository.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/support/AnnotationAttribute.class */
class AnnotationAttribute {
    private final Class<? extends Annotation> annotationType;
    private final String attributeName;

    public AnnotationAttribute(Class<? extends Annotation> annotationType) {
        this(annotationType, null);
    }

    public AnnotationAttribute(Class<? extends Annotation> annotationType, String attributeName) {
        Assert.notNull(annotationType, "AnnotationType must not be null!");
        this.annotationType = annotationType;
        this.attributeName = attributeName;
    }

    public Class<? extends Annotation> getAnnotationType() {
        return this.annotationType;
    }

    public Object getValueFrom(MethodParameter parameter) {
        Assert.notNull(parameter, "MethodParameter must not be null!");
        Annotation annotation = parameter.getParameterAnnotation(this.annotationType);
        if (annotation == null) {
            return null;
        }
        return getValueFrom(annotation);
    }

    public Object getValueFrom(AnnotatedElement annotatedElement) {
        Assert.notNull(annotatedElement, "Annotated element must not be null!");
        Annotation annotation = annotatedElement.getAnnotation(this.annotationType);
        if (annotation == null) {
            return null;
        }
        return getValueFrom(annotation);
    }

    public Object getValueFrom(Annotation annotation) {
        Assert.notNull(annotation, "Annotation must not be null!");
        return (String) (this.attributeName == null ? AnnotationUtils.getValue(annotation) : AnnotationUtils.getValue(annotation, this.attributeName));
    }
}
