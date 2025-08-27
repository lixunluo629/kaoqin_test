package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/AutowireCandidateResolver.class */
public interface AutowireCandidateResolver {
    boolean isAutowireCandidate(BeanDefinitionHolder beanDefinitionHolder, DependencyDescriptor dependencyDescriptor);

    Object getSuggestedValue(DependencyDescriptor dependencyDescriptor);

    Object getLazyResolutionProxyIfNecessary(DependencyDescriptor dependencyDescriptor, String str);
}
