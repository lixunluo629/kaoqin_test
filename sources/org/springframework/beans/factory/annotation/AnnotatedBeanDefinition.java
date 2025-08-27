package org.springframework.beans.factory.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/AnnotatedBeanDefinition.class */
public interface AnnotatedBeanDefinition extends BeanDefinition {
    AnnotationMetadata getMetadata();

    MethodMetadata getFactoryMethodMetadata();
}
