package org.springframework.cache.interceptor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/BeanFactoryCacheOperationSourceAdvisor.class */
public class BeanFactoryCacheOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private CacheOperationSource cacheOperationSource;
    private final CacheOperationSourcePointcut pointcut = new CacheOperationSourcePointcut() { // from class: org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor.1
        @Override // org.springframework.cache.interceptor.CacheOperationSourcePointcut
        protected CacheOperationSource getCacheOperationSource() {
            return BeanFactoryCacheOperationSourceAdvisor.this.cacheOperationSource;
        }
    };

    public void setCacheOperationSource(CacheOperationSource cacheOperationSource) {
        this.cacheOperationSource = cacheOperationSource;
    }

    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    @Override // org.springframework.aop.PointcutAdvisor
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
