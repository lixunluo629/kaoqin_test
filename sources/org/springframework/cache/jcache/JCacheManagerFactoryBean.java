package org.springframework.cache.jcache;

import java.net.URI;
import java.util.Properties;
import javax.cache.CacheManager;
import javax.cache.Caching;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/JCacheManagerFactoryBean.class */
public class JCacheManagerFactoryBean implements FactoryBean<CacheManager>, BeanClassLoaderAware, InitializingBean, DisposableBean {
    private URI cacheManagerUri;
    private Properties cacheManagerProperties;
    private ClassLoader beanClassLoader;
    private CacheManager cacheManager;

    public void setCacheManagerUri(URI cacheManagerUri) {
        this.cacheManagerUri = cacheManagerUri;
    }

    public void setCacheManagerProperties(Properties cacheManagerProperties) {
        this.cacheManagerProperties = cacheManagerProperties;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.beanClassLoader = classLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.cacheManager = Caching.getCachingProvider().getCacheManager(this.cacheManagerUri, this.beanClassLoader, this.cacheManagerProperties);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public CacheManager getObject() {
        return this.cacheManager;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }

    @Override // org.springframework.beans.factory.DisposableBean
    public void destroy() {
        this.cacheManager.close();
    }
}
