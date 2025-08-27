package org.springframework.data.repository.core.support;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/RepositoryProxyPostProcessor.class */
public interface RepositoryProxyPostProcessor {
    void postProcess(ProxyFactory proxyFactory, RepositoryInformation repositoryInformation);
}
