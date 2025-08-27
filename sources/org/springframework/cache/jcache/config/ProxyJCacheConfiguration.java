package org.springframework.cache.jcache.config;

import org.springframework.cache.config.CacheManagementConfigUtils;
import org.springframework.cache.jcache.interceptor.BeanFactoryJCacheOperationSourceAdvisor;
import org.springframework.cache.jcache.interceptor.JCacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

@Configuration
@Role(2)
/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/config/ProxyJCacheConfiguration.class */
public class ProxyJCacheConfiguration extends AbstractJCacheConfiguration {
    @Bean(name = {CacheManagementConfigUtils.JCACHE_ADVISOR_BEAN_NAME})
    @Role(2)
    public BeanFactoryJCacheOperationSourceAdvisor cacheAdvisor() {
        BeanFactoryJCacheOperationSourceAdvisor advisor = new BeanFactoryJCacheOperationSourceAdvisor();
        advisor.setCacheOperationSource(cacheOperationSource());
        advisor.setAdvice(cacheInterceptor());
        advisor.setOrder(((Integer) this.enableCaching.getNumber("order")).intValue());
        return advisor;
    }

    @Bean(name = {"jCacheInterceptor"})
    @Role(2)
    public JCacheInterceptor cacheInterceptor() {
        JCacheInterceptor interceptor = new JCacheInterceptor();
        interceptor.setCacheOperationSource(cacheOperationSource());
        if (this.errorHandler != null) {
            interceptor.setErrorHandler(this.errorHandler);
        }
        return interceptor;
    }
}
