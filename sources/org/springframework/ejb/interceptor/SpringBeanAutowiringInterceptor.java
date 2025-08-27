package org.springframework.ejb.interceptor;

import java.util.Map;
import java.util.WeakHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJBException;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.interceptor.InvocationContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/ejb/interceptor/SpringBeanAutowiringInterceptor.class */
public class SpringBeanAutowiringInterceptor {
    private final Map<Object, BeanFactoryReference> beanFactoryReferences = new WeakHashMap();

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.ejb.EJBException */
    @PostActivate
    @PostConstruct
    public void autowireBean(InvocationContext invocationContext) throws EJBException, BeanCreationException {
        doAutowireBean(invocationContext.getTarget());
        try {
            invocationContext.proceed();
        } catch (Error err) {
            doReleaseBean(invocationContext.getTarget());
            throw err;
        } catch (RuntimeException ex) {
            doReleaseBean(invocationContext.getTarget());
            throw ex;
        } catch (Exception ex2) {
            doReleaseBean(invocationContext.getTarget());
            throw new EJBException(ex2);
        }
    }

    protected void doAutowireBean(Object target) throws BeanCreationException {
        AutowiredAnnotationBeanPostProcessor bpp = new AutowiredAnnotationBeanPostProcessor();
        configureBeanPostProcessor(bpp, target);
        bpp.setBeanFactory(getBeanFactory(target));
        bpp.processInjection(target);
    }

    protected void configureBeanPostProcessor(AutowiredAnnotationBeanPostProcessor processor, Object target) {
    }

    protected BeanFactory getBeanFactory(Object target) throws IllegalStateException {
        BeanFactory factory = getBeanFactoryReference(target).getFactory();
        if (factory instanceof ApplicationContext) {
            factory = ((ApplicationContext) factory).getAutowireCapableBeanFactory();
        }
        return factory;
    }

    protected BeanFactoryReference getBeanFactoryReference(Object target) throws BeansException {
        String key = getBeanFactoryLocatorKey(target);
        BeanFactoryReference ref = getBeanFactoryLocator(target).useBeanFactory(key);
        this.beanFactoryReferences.put(target, ref);
        return ref;
    }

    protected BeanFactoryLocator getBeanFactoryLocator(Object target) {
        return ContextSingletonBeanFactoryLocator.getInstance();
    }

    protected String getBeanFactoryLocatorKey(Object target) {
        return null;
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.ejb.EJBException */
    @PreDestroy
    @PrePassivate
    public void releaseBean(InvocationContext invocationContext) throws EJBException {
        doReleaseBean(invocationContext.getTarget());
        try {
            invocationContext.proceed();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex2) {
            throw new EJBException(ex2);
        }
    }

    protected void doReleaseBean(Object target) {
        BeanFactoryReference ref = this.beanFactoryReferences.remove(target);
        if (ref != null) {
            ref.release();
        }
    }
}
