package org.springframework.cache.jcache.interceptor;

import java.util.Collection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/DefaultJCacheOperationSource.class */
public class DefaultJCacheOperationSource extends AnnotationJCacheOperationSource implements BeanFactoryAware, InitializingBean, SmartInitializingSingleton {
    private CacheManager cacheManager;
    private CacheResolver cacheResolver;
    private CacheResolver exceptionCacheResolver;
    private KeyGenerator keyGenerator = new SimpleKeyGenerator();
    private KeyGenerator adaptedKeyGenerator;
    private BeanFactory beanFactory;

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return this.cacheManager;
    }

    public void setCacheResolver(CacheResolver cacheResolver) {
        this.cacheResolver = cacheResolver;
    }

    public CacheResolver getCacheResolver() {
        return this.cacheResolver;
    }

    public void setExceptionCacheResolver(CacheResolver exceptionCacheResolver) {
        this.exceptionCacheResolver = exceptionCacheResolver;
    }

    public CacheResolver getExceptionCacheResolver() {
        return this.exceptionCacheResolver;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public KeyGenerator getKeyGenerator() {
        return this.keyGenerator;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.adaptedKeyGenerator = new KeyGeneratorAdapter(this, this.keyGenerator);
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() {
        Assert.notNull(getDefaultCacheResolver(), "Cache resolver should have been initialized");
    }

    @Override // org.springframework.cache.jcache.interceptor.AnnotationJCacheOperationSource
    protected <T> T getBean(Class<T> cls) {
        try {
            return (T) this.beanFactory.getBean(cls);
        } catch (NoUniqueBeanDefinitionException e) {
            throw new IllegalStateException("No unique [" + cls.getName() + "] bean found in application context - mark one as primary, or declare a more specific implementation type for your cache", e);
        } catch (NoSuchBeanDefinitionException e2) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("No bean of type [" + cls.getName() + "] found in application context", e2);
            }
            return (T) BeanUtils.instantiateClass(cls);
        }
    }

    protected CacheManager getDefaultCacheManager() {
        if (this.cacheManager == null) {
            try {
                this.cacheManager = (CacheManager) this.beanFactory.getBean(CacheManager.class);
            } catch (NoUniqueBeanDefinitionException e) {
                throw new IllegalStateException("No unique bean of type CacheManager found. Mark one as primary or declare a specific CacheManager to use.");
            } catch (NoSuchBeanDefinitionException e2) {
                throw new IllegalStateException("No bean of type CacheManager found. Register a CacheManager bean or remove the @EnableCaching annotation from your configuration.");
            }
        }
        return this.cacheManager;
    }

    @Override // org.springframework.cache.jcache.interceptor.AnnotationJCacheOperationSource
    protected CacheResolver getDefaultCacheResolver() {
        if (this.cacheResolver == null) {
            this.cacheResolver = new SimpleCacheResolver(getDefaultCacheManager());
        }
        return this.cacheResolver;
    }

    @Override // org.springframework.cache.jcache.interceptor.AnnotationJCacheOperationSource
    protected CacheResolver getDefaultExceptionCacheResolver() {
        if (this.exceptionCacheResolver == null) {
            this.exceptionCacheResolver = new LazyCacheResolver();
        }
        return this.exceptionCacheResolver;
    }

    @Override // org.springframework.cache.jcache.interceptor.AnnotationJCacheOperationSource
    protected KeyGenerator getDefaultKeyGenerator() {
        return this.adaptedKeyGenerator;
    }

    /* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/cache/jcache/interceptor/DefaultJCacheOperationSource$LazyCacheResolver.class */
    class LazyCacheResolver implements CacheResolver {
        private CacheResolver cacheResolver;

        LazyCacheResolver() {
        }

        @Override // org.springframework.cache.interceptor.CacheResolver
        public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
            if (this.cacheResolver == null) {
                this.cacheResolver = new SimpleExceptionCacheResolver(DefaultJCacheOperationSource.this.getDefaultCacheManager());
            }
            return this.cacheResolver.resolveCaches(context);
        }
    }
}
