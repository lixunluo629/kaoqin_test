package org.springframework.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/SimpleAutowireCandidateResolver.class */
public class SimpleAutowireCandidateResolver implements AutowireCandidateResolver {
    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    public boolean isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
        return bdHolder.getBeanDefinition().isAutowireCandidate();
    }

    public boolean isRequired(DependencyDescriptor descriptor) {
        return descriptor.isRequired();
    }

    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    public Object getSuggestedValue(DependencyDescriptor descriptor) {
        return null;
    }

    @Override // org.springframework.beans.factory.support.AutowireCandidateResolver
    public Object getLazyResolutionProxyIfNecessary(DependencyDescriptor descriptor, String beanName) {
        return null;
    }
}
