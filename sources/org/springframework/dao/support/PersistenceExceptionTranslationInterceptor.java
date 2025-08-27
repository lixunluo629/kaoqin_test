package org.springframework.dao.support;

import java.util.Map;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-tx-4.3.25.RELEASE.jar:org/springframework/dao/support/PersistenceExceptionTranslationInterceptor.class */
public class PersistenceExceptionTranslationInterceptor implements MethodInterceptor, BeanFactoryAware, InitializingBean {
    private volatile PersistenceExceptionTranslator persistenceExceptionTranslator;
    private boolean alwaysTranslate = false;
    private ListableBeanFactory beanFactory;

    public PersistenceExceptionTranslationInterceptor() {
    }

    public PersistenceExceptionTranslationInterceptor(PersistenceExceptionTranslator pet) {
        Assert.notNull(pet, "PersistenceExceptionTranslator must not be null");
        this.persistenceExceptionTranslator = pet;
    }

    public PersistenceExceptionTranslationInterceptor(ListableBeanFactory beanFactory) {
        Assert.notNull(beanFactory, "ListableBeanFactory must not be null");
        this.beanFactory = beanFactory;
    }

    public void setPersistenceExceptionTranslator(PersistenceExceptionTranslator pet) {
        this.persistenceExceptionTranslator = pet;
    }

    public void setAlwaysTranslate(boolean alwaysTranslate) {
        this.alwaysTranslate = alwaysTranslate;
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.persistenceExceptionTranslator == null) {
            if (!(beanFactory instanceof ListableBeanFactory)) {
                throw new IllegalArgumentException("Cannot use PersistenceExceptionTranslator autodetection without ListableBeanFactory");
            }
            this.beanFactory = (ListableBeanFactory) beanFactory;
        }
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.persistenceExceptionTranslator == null && this.beanFactory == null) {
            throw new IllegalArgumentException("Property 'persistenceExceptionTranslator' is required");
        }
    }

    @Override // org.aopalliance.intercept.MethodInterceptor
    public Object invoke(MethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        } catch (RuntimeException ex) {
            if (!this.alwaysTranslate && ReflectionUtils.declaresException(mi.getMethod(), ex.getClass())) {
                throw ex;
            }
            if (this.persistenceExceptionTranslator == null) {
                this.persistenceExceptionTranslator = detectPersistenceExceptionTranslators(this.beanFactory);
            }
            throw DataAccessUtils.translateIfNecessary(ex, this.persistenceExceptionTranslator);
        }
    }

    protected PersistenceExceptionTranslator detectPersistenceExceptionTranslators(ListableBeanFactory beanFactory) throws BeansException {
        Map<String, PersistenceExceptionTranslator> pets = BeanFactoryUtils.beansOfTypeIncludingAncestors(beanFactory, PersistenceExceptionTranslator.class, false, false);
        ChainedPersistenceExceptionTranslator cpet = new ChainedPersistenceExceptionTranslator();
        for (PersistenceExceptionTranslator pet : pets.values()) {
            cpet.addDelegate(pet);
        }
        return cpet;
    }
}
