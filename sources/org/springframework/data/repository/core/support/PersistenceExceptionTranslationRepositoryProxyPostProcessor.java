package org.springframework.data.repository.core.support;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/PersistenceExceptionTranslationRepositoryProxyPostProcessor.class */
public class PersistenceExceptionTranslationRepositoryProxyPostProcessor implements RepositoryProxyPostProcessor {
    private final PersistenceExceptionTranslationInterceptor interceptor;

    public PersistenceExceptionTranslationRepositoryProxyPostProcessor(ListableBeanFactory beanFactory) throws BeansException {
        Assert.notNull(beanFactory, "BeanFactory must not be null!");
        this.interceptor = new PersistenceExceptionTranslationInterceptor();
        this.interceptor.setBeanFactory(beanFactory);
        this.interceptor.afterPropertiesSet();
    }

    @Override // org.springframework.data.repository.core.support.RepositoryProxyPostProcessor
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(this.interceptor);
    }
}
