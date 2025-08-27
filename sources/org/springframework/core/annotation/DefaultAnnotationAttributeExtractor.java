package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/DefaultAnnotationAttributeExtractor.class */
class DefaultAnnotationAttributeExtractor extends AbstractAliasAwareAnnotationAttributeExtractor<Annotation> {
    DefaultAnnotationAttributeExtractor(Annotation annotation, Object annotatedElement) {
        super(annotation.annotationType(), annotatedElement, annotation);
    }

    @Override // org.springframework.core.annotation.AbstractAliasAwareAnnotationAttributeExtractor
    protected Object getRawAttributeValue(Method attributeMethod) {
        ReflectionUtils.makeAccessible(attributeMethod);
        return ReflectionUtils.invokeMethod(attributeMethod, getSource());
    }

    @Override // org.springframework.core.annotation.AbstractAliasAwareAnnotationAttributeExtractor
    protected Object getRawAttributeValue(String attributeName) {
        Method attributeMethod = ReflectionUtils.findMethod(getAnnotationType(), attributeName);
        return getRawAttributeValue(attributeMethod);
    }
}
