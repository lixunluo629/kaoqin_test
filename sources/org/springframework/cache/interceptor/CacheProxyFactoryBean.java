package org.springframework.cache.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/CacheProxyFactoryBean.class */
public class CacheProxyFactoryBean extends AbstractSingletonProxyFactoryBean implements BeanFactoryAware, SmartInitializingSingleton {
    private final CacheInterceptor cacheInterceptor = new CacheInterceptor();
    private Pointcut pointcut = Pointcut.TRUE;

    public void setCacheOperationSources(CacheOperationSource... cacheOperationSources) {
        this.cacheInterceptor.setCacheOperationSources(cacheOperationSources);
    }

    public void setPointcut(Pointcut pointcut) {
        this.pointcut = pointcut;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.cacheInterceptor.setBeanFactory(beanFactory);
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() {
        this.cacheInterceptor.afterSingletonsInstantiated();
    }

    @Override // org.springframework.aop.framework.AbstractSingletonProxyFactoryBean
    protected Object createMainInterceptor() {
        this.cacheInterceptor.afterPropertiesSet();
        return new DefaultPointcutAdvisor(this.pointcut, this.cacheInterceptor);
    }
}
