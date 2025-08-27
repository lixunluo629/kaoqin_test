package org.springframework.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/SimpleInstantiationStrategy.class */
public class SimpleInstantiationStrategy implements InstantiationStrategy {
    private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal<>();

    public static Method getCurrentlyInvokedFactoryMethod() {
        return currentlyInvokedFactoryMethod.get();
    }

    @Override // org.springframework.beans.factory.support.InstantiationStrategy
    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner) {
        Constructor<?> constructorToUse;
        if (bd.getMethodOverrides().isEmpty()) {
            synchronized (bd.constructorArgumentLock) {
                constructorToUse = (Constructor) bd.resolvedConstructorOrFactoryMethod;
                if (constructorToUse == null) {
                    final Class<?> clazz = bd.getBeanClass();
                    if (clazz.isInterface()) {
                        throw new BeanInstantiationException(clazz, "Specified class is an interface");
                    }
                    try {
                        if (System.getSecurityManager() != null) {
                            constructorToUse = (Constructor) AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<?>>() { // from class: org.springframework.beans.factory.support.SimpleInstantiationStrategy.1
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedExceptionAction
                                public Constructor<?> run() throws Exception {
                                    return clazz.getDeclaredConstructor((Class[]) null);
                                }
                            });
                        } else {
                            constructorToUse = clazz.getDeclaredConstructor((Class[]) null);
                        }
                        bd.resolvedConstructorOrFactoryMethod = constructorToUse;
                    } catch (Throwable ex) {
                        throw new BeanInstantiationException(clazz, "No default constructor found", ex);
                    }
                }
            }
            return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
        }
        return instantiateWithMethodInjection(bd, beanName, owner);
    }

    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner) {
        throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
    }

    @Override // org.springframework.beans.factory.support.InstantiationStrategy
    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, final Constructor<?> ctor, Object... args) {
        if (bd.getMethodOverrides().isEmpty()) {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.SimpleInstantiationStrategy.2
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        ReflectionUtils.makeAccessible((Constructor<?>) ctor);
                        return null;
                    }
                });
            }
            return BeanUtils.instantiateClass(ctor, args);
        }
        return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
    }

    protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner, Constructor<?> ctor, Object... args) {
        throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
    }

    @Override // org.springframework.beans.factory.support.InstantiationStrategy
    public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, Object factoryBean, final Method factoryMethod, Object... args) {
        try {
            if (System.getSecurityManager() != null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: org.springframework.beans.factory.support.SimpleInstantiationStrategy.3
                    @Override // java.security.PrivilegedAction
                    public Object run() {
                        ReflectionUtils.makeAccessible(factoryMethod);
                        return null;
                    }
                });
            } else {
                ReflectionUtils.makeAccessible(factoryMethod);
            }
            Method priorInvokedFactoryMethod = currentlyInvokedFactoryMethod.get();
            try {
                currentlyInvokedFactoryMethod.set(factoryMethod);
                Object objInvoke = factoryMethod.invoke(factoryBean, args);
                if (priorInvokedFactoryMethod != null) {
                    currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
                } else {
                    currentlyInvokedFactoryMethod.remove();
                }
                return objInvoke;
            } catch (Throwable th) {
                if (priorInvokedFactoryMethod != null) {
                    currentlyInvokedFactoryMethod.set(priorInvokedFactoryMethod);
                } else {
                    currentlyInvokedFactoryMethod.remove();
                }
                throw th;
            }
        } catch (IllegalAccessException ex) {
            throw new BeanInstantiationException(factoryMethod, "Cannot access factory method '" + factoryMethod.getName() + "'; is it public?", ex);
        } catch (IllegalArgumentException ex2) {
            throw new BeanInstantiationException(factoryMethod, "Illegal arguments to factory method '" + factoryMethod.getName() + "'; args: " + StringUtils.arrayToCommaDelimitedString(args), ex2);
        } catch (InvocationTargetException ex3) {
            String msg = "Factory method '" + factoryMethod.getName() + "' threw exception";
            if (bd.getFactoryBeanName() != null && (owner instanceof ConfigurableBeanFactory) && ((ConfigurableBeanFactory) owner).isCurrentlyInCreation(bd.getFactoryBeanName())) {
                msg = "Circular reference involving containing bean '" + bd.getFactoryBeanName() + "' - consider declaring the factory method as static for independence from its containing instance. " + msg;
            }
            throw new BeanInstantiationException(factoryMethod, msg, ex3.getTargetException());
        }
    }
}
