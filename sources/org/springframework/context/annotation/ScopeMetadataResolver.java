package org.springframework.context.annotation;

import org.springframework.beans.factory.config.BeanDefinition;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/ScopeMetadataResolver.class */
public interface ScopeMetadataResolver {
    ScopeMetadata resolveScopeMetadata(BeanDefinition beanDefinition);
}
