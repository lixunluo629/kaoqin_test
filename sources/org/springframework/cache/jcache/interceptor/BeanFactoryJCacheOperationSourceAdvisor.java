package org.springframework.cache.jcache.interceptor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/BeanFactoryJCacheOperationSourceAdvisor.class */
public class BeanFactoryJCacheOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private JCacheOperationSource cacheOperationSource;
    private final JCacheOperationSourcePointcut pointcut = new JCacheOperationSourcePointcut() { // from class: org.springframework.cache.jcache.interceptor.BeanFactoryJCacheOperationSourceAdvisor.1
        @Override // org.springframework.cache.jcache.interceptor.JCacheOperationSourcePointcut
        protected JCacheOperationSource getCacheOperationSource() {
            return BeanFactoryJCacheOperationSourceAdvisor.this.cacheOperationSource;
        }
    };

    public void setCacheOperationSource(JCacheOperationSource cacheOperationSource) {
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
