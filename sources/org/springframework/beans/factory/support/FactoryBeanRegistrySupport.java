package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/FactoryBeanRegistrySupport.class */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap(16);

    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        try {
            if (System.getSecurityManager() != null) {
                return (Class) AccessController.doPrivileged(new PrivilegedAction<Class<?>>() { // from class: org.springframework.beans.factory.support.FactoryBeanRegistrySupport.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Class<?> run() {
                        return factoryBean.getObjectType();
                    }
                }, getAccessControlContext());
            }
            return factoryBean.getObjectType();
        } catch (Throwable ex) {
            this.logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying that it should return null if the type of its object cannot be determined yet", ex);
            return null;
        }
    }

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object object = this.factoryBeanObjectCache.get(beanName);
        if (object != NULL_OBJECT) {
            return object;
        }
        return null;
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) throws BeanCreationException {
        if (factory.isSingleton() && containsSingleton(beanName)) {
            synchronized (getSingletonMutex()) {
                Object object = this.factoryBeanObjectCache.get(beanName);
                if (object == null) {
                    object = doGetObjectFromFactoryBean(factory, beanName);
                    Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
                    if (alreadyThere != null) {
                        object = alreadyThere;
                    } else {
                        if (object != null && shouldPostProcess) {
                            if (isSingletonCurrentlyInCreation(beanName)) {
                                return object;
                            }
                            beforeSingletonCreation(beanName);
                            try {
                                try {
                                    object = postProcessObjectFromFactoryBean(object, beanName);
                                    afterSingletonCreation(beanName);
                                } catch (Throwable ex) {
                                    throw new BeanCreationException(beanName, "Post-processing of FactoryBean's singleton object failed", ex);
                                }
                            } catch (Throwable th) {
                                afterSingletonCreation(beanName);
                                throw th;
                            }
                        }
                        if (containsSingleton(beanName)) {
                            this.factoryBeanObjectCache.put(beanName, object != null ? object : NULL_OBJECT);
                        }
                    }
                }
                return object != NULL_OBJECT ? object : null;
            }
        }
        Object object2 = doGetObjectFromFactoryBean(factory, beanName);
        if (object2 != null && shouldPostProcess) {
            try {
                object2 = postProcessObjectFromFactoryBean(object2, beanName);
            } catch (Throwable ex2) {
                throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex2);
            }
        }
        return object2;
    }

    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, String beanName) throws BeanCreationException {
        Object object;
        try {
            if (System.getSecurityManager() != null) {
                AccessControlContext acc = getAccessControlContext();
                try {
                    object = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() { // from class: org.springframework.beans.factory.support.FactoryBeanRegistrySupport.2
                        @Override // java.security.PrivilegedExceptionAction
                        public Object run() throws Exception {
                            return factory.getObject();
                        }
                    }, acc);
                } catch (PrivilegedActionException pae) {
                    throw pae.getException();
                }
            } else {
                object = factory.getObject();
            }
            if (object == null && isSingletonCurrentlyInCreation(beanName)) {
                throw new BeanCurrentlyInCreationException(beanName, "FactoryBean which is currently in creation returned null from getObject");
            }
            return object;
        } catch (FactoryBeanNotInitializedException ex) {
            throw new BeanCurrentlyInCreationException(beanName, ex.toString());
        } catch (Throwable ex2) {
            throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex2);
        }
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }

    protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException {
        if (!(beanInstance instanceof FactoryBean)) {
            throw new BeanCreationException(beanName, "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (FactoryBean) beanInstance;
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    protected void removeSingleton(String beanName) {
        synchronized (getSingletonMutex()) {
            super.removeSingleton(beanName);
            this.factoryBeanObjectCache.remove(beanName);
        }
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    protected void clearSingletonCache() {
        synchronized (getSingletonMutex()) {
            super.clearSingletonCache();
            this.factoryBeanObjectCache.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AccessControlContext getAccessControlContext() {
        return AccessController.getContext();
    }
}
