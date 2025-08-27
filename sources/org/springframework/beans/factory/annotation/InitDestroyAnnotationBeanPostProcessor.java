package org.springframework.beans.factory.annotation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/InitDestroyAnnotationBeanPostProcessor.class */
public class InitDestroyAnnotationBeanPostProcessor implements DestructionAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, PriorityOrdered, Serializable {
    private Class<? extends Annotation> initAnnotationType;
    private Class<? extends Annotation> destroyAnnotationType;
    protected transient Log logger = LogFactory.getLog(getClass());
    private int order = Integer.MAX_VALUE;
    private final transient Map<Class<?>, LifecycleMetadata> lifecycleMetadataCache = new ConcurrentHashMap(256);

    public void setInitAnnotationType(Class<? extends Annotation> initAnnotationType) {
        this.initAnnotationType = initAnnotationType;
    }

    public void setDestroyAnnotationType(Class<? extends Annotation> destroyAnnotationType) {
        this.destroyAnnotationType = destroyAnnotationType;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    @Override // org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (beanType != null) {
            LifecycleMetadata metadata = findLifecycleMetadata(beanType);
            metadata.checkConfigMembers(beanDefinition);
        }
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
        try {
            metadata.invokeInitMethods(bean, beanName);
            return bean;
        } catch (InvocationTargetException ex) {
            throw new BeanCreationException(beanName, "Invocation of init method failed", ex.getTargetException());
        } catch (Throwable ex2) {
            throw new BeanCreationException(beanName, "Failed to invoke init method", ex2);
        }
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override // org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
        try {
            metadata.invokeDestroyMethods(bean, beanName);
        } catch (InvocationTargetException ex) {
            String msg = "Invocation of destroy method failed on bean with name '" + beanName + "'";
            if (this.logger.isDebugEnabled()) {
                this.logger.warn(msg, ex.getTargetException());
            } else {
                this.logger.warn(msg + ": " + ex.getTargetException());
            }
        } catch (Throwable ex2) {
            this.logger.error("Failed to invoke destroy method on bean with name '" + beanName + "'", ex2);
        }
    }

    @Override // org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor
    public boolean requiresDestruction(Object bean) {
        return findLifecycleMetadata(bean.getClass()).hasDestroyMethods();
    }

    private LifecycleMetadata findLifecycleMetadata(Class<?> clazz) {
        LifecycleMetadata lifecycleMetadata;
        if (this.lifecycleMetadataCache == null) {
            return buildLifecycleMetadata(clazz);
        }
        LifecycleMetadata metadata = this.lifecycleMetadataCache.get(clazz);
        if (metadata == null) {
            synchronized (this.lifecycleMetadataCache) {
                LifecycleMetadata metadata2 = this.lifecycleMetadataCache.get(clazz);
                if (metadata2 == null) {
                    metadata2 = buildLifecycleMetadata(clazz);
                    this.lifecycleMetadataCache.put(clazz, metadata2);
                }
                lifecycleMetadata = metadata2;
            }
            return lifecycleMetadata;
        }
        return metadata;
    }

    private LifecycleMetadata buildLifecycleMetadata(final Class<?> clazz) throws SecurityException, IllegalArgumentException {
        final boolean debug = this.logger.isDebugEnabled();
        LinkedList<LifecycleElement> initMethods = new LinkedList<>();
        LinkedList<LifecycleElement> destroyMethods = new LinkedList<>();
        Class<?> targetClass = clazz;
        do {
            final LinkedList<LifecycleElement> currInitMethods = new LinkedList<>();
            final LinkedList<LifecycleElement> currDestroyMethods = new LinkedList<>();
            ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback() { // from class: org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor.1
                @Override // org.springframework.util.ReflectionUtils.MethodCallback
                public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
                    if (InitDestroyAnnotationBeanPostProcessor.this.initAnnotationType != null && method.getAnnotation(InitDestroyAnnotationBeanPostProcessor.this.initAnnotationType) != null) {
                        LifecycleElement element = new LifecycleElement(method);
                        currInitMethods.add(element);
                        if (debug) {
                            InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found init method on class [" + clazz.getName() + "]: " + method);
                        }
                    }
                    if (InitDestroyAnnotationBeanPostProcessor.this.destroyAnnotationType != null && method.getAnnotation(InitDestroyAnnotationBeanPostProcessor.this.destroyAnnotationType) != null) {
                        currDestroyMethods.add(new LifecycleElement(method));
                        if (debug) {
                            InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Found destroy method on class [" + clazz.getName() + "]: " + method);
                        }
                    }
                }
            });
            initMethods.addAll(0, currInitMethods);
            destroyMethods.addAll(currDestroyMethods);
            targetClass = targetClass.getSuperclass();
            if (targetClass == null) {
                break;
            }
        } while (targetClass != Object.class);
        return new LifecycleMetadata(clazz, initMethods, destroyMethods);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        this.logger = LogFactory.getLog(getClass());
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/InitDestroyAnnotationBeanPostProcessor$LifecycleMetadata.class */
    private class LifecycleMetadata {
        private final Class<?> targetClass;
        private final Collection<LifecycleElement> initMethods;
        private final Collection<LifecycleElement> destroyMethods;
        private volatile Set<LifecycleElement> checkedInitMethods;
        private volatile Set<LifecycleElement> checkedDestroyMethods;

        public LifecycleMetadata(Class<?> targetClass, Collection<LifecycleElement> initMethods, Collection<LifecycleElement> destroyMethods) {
            this.targetClass = targetClass;
            this.initMethods = initMethods;
            this.destroyMethods = destroyMethods;
        }

        public void checkConfigMembers(RootBeanDefinition beanDefinition) {
            Set<LifecycleElement> checkedInitMethods = new LinkedHashSet<>(this.initMethods.size());
            for (LifecycleElement element : this.initMethods) {
                String methodIdentifier = element.getIdentifier();
                if (!beanDefinition.isExternallyManagedInitMethod(methodIdentifier)) {
                    beanDefinition.registerExternallyManagedInitMethod(methodIdentifier);
                    checkedInitMethods.add(element);
                    if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
                        InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Registered init method on class [" + this.targetClass.getName() + "]: " + element);
                    }
                }
            }
            Set<LifecycleElement> checkedDestroyMethods = new LinkedHashSet<>(this.destroyMethods.size());
            for (LifecycleElement element2 : this.destroyMethods) {
                String methodIdentifier2 = element2.getIdentifier();
                if (!beanDefinition.isExternallyManagedDestroyMethod(methodIdentifier2)) {
                    beanDefinition.registerExternallyManagedDestroyMethod(methodIdentifier2);
                    checkedDestroyMethods.add(element2);
                    if (InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled()) {
                        InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Registered destroy method on class [" + this.targetClass.getName() + "]: " + element2);
                    }
                }
            }
            this.checkedInitMethods = checkedInitMethods;
            this.checkedDestroyMethods = checkedDestroyMethods;
        }

        public void invokeInitMethods(Object target, String beanName) throws Throwable {
            Collection<LifecycleElement> initMethodsToIterate = this.checkedInitMethods != null ? this.checkedInitMethods : this.initMethods;
            if (!initMethodsToIterate.isEmpty()) {
                boolean debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
                for (LifecycleElement element : initMethodsToIterate) {
                    if (debug) {
                        InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking init method on bean '" + beanName + "': " + element.getMethod());
                    }
                    element.invoke(target);
                }
            }
        }

        public void invokeDestroyMethods(Object target, String beanName) throws Throwable {
            Collection<LifecycleElement> destroyMethodsToUse = this.checkedDestroyMethods != null ? this.checkedDestroyMethods : this.destroyMethods;
            if (!destroyMethodsToUse.isEmpty()) {
                boolean debug = InitDestroyAnnotationBeanPostProcessor.this.logger.isDebugEnabled();
                for (LifecycleElement element : destroyMethodsToUse) {
                    if (debug) {
                        InitDestroyAnnotationBeanPostProcessor.this.logger.debug("Invoking destroy method on bean '" + beanName + "': " + element.getMethod());
                    }
                    element.invoke(target);
                }
            }
        }

        public boolean hasDestroyMethods() {
            Collection<LifecycleElement> destroyMethodsToUse = this.checkedDestroyMethods != null ? this.checkedDestroyMethods : this.destroyMethods;
            return !destroyMethodsToUse.isEmpty();
        }
    }

    /* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/InitDestroyAnnotationBeanPostProcessor$LifecycleElement.class */
    private static class LifecycleElement {
        private final Method method;
        private final String identifier;

        public LifecycleElement(Method method) {
            if (method.getParameterTypes().length != 0) {
                throw new IllegalStateException("Lifecycle method annotation requires a no-arg method: " + method);
            }
            this.method = method;
            this.identifier = Modifier.isPrivate(method.getModifiers()) ? ClassUtils.getQualifiedMethodName(method) : method.getName();
        }

        public Method getMethod() {
            return this.method;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public void invoke(Object target) throws Throwable {
            ReflectionUtils.makeAccessible(this.method);
            this.method.invoke(target, (Object[]) null);
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof LifecycleElement)) {
                return false;
            }
            LifecycleElement otherElement = (LifecycleElement) other;
            return this.identifier.equals(otherElement.identifier);
        }

        public int hashCode() {
            return this.identifier.hashCode();
        }
    }
}
