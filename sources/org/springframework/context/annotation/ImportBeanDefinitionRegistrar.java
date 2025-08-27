package org.springframework.context.annotation;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ImportBeanDefinitionRegistrar.class */
public interface ImportBeanDefinitionRegistrar {
    void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry);
}
