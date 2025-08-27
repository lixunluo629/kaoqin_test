package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/annotation/AnnotationAttributeExtractor.class */
interface AnnotationAttributeExtractor<S> {
    Class<? extends Annotation> getAnnotationType();

    Object getAnnotatedElement();

    S getSource();

    Object getAttributeValue(Method method);
}
