package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/condition/BeanTypeRegistry.class */
final class BeanTypeRegistry implements SmartInitializingSingleton {
    static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";
    private final DefaultListableBeanFactory beanFactory;
    private final Map<String, Class<?>> beanTypes = new HashMap();
    private final Map<String, RootBeanDefinition> beanDefinitions = new HashMap();
    private static final Log logger = LogFactory.getLog(BeanTypeRegistry.class);
    private static final String BEAN_NAME = BeanTypeRegistry.class.getName();

    private BeanTypeRegistry(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    static BeanTypeRegistry get(ListableBeanFactory beanFactory) throws BeanDefinitionStoreException {
        Assert.isInstanceOf(DefaultListableBeanFactory.class, beanFactory);
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        Assert.isTrue(listableBeanFactory.isAllowEagerClassLoading(), "Bean factory must allow eager class loading");
        if (!listableBeanFactory.containsLocalBean(BEAN_NAME)) {
            BeanDefinition bd = new RootBeanDefinition((Class<?>) BeanTypeRegistry.class);
            bd.getConstructorArgumentValues().addIndexedArgumentValue(0, beanFactory);
            listableBeanFactory.registerBeanDefinition(BEAN_NAME, bd);
        }
        return (BeanTypeRegistry) listableBeanFactory.getBean(BEAN_NAME, BeanTypeRegistry.class);
    }

    Set<String> getNamesForType(Class<?> type) {
        updateTypesIfNecessary();
        Set<String> matches = new LinkedHashSet<>();
        for (Map.Entry<String, Class<?>> entry : this.beanTypes.entrySet()) {
            if (entry.getValue() != null && type.isAssignableFrom(entry.getValue())) {
                matches.add(entry.getKey());
            }
        }
        return matches;
    }

    Set<String> getNamesForAnnotation(Class<? extends Annotation> annotation) {
        updateTypesIfNecessary();
        Set<String> matches = new LinkedHashSet<>();
        for (Map.Entry<String, Class<?>> entry : this.beanTypes.entrySet()) {
            if (entry.getValue() != null && AnnotationUtils.findAnnotation(entry.getValue(), (Class) annotation) != null) {
                matches.add(entry.getKey());
            }
        }
        return matches;
    }

    @Override // org.springframework.beans.factory.SmartInitializingSingleton
    public void afterSingletonsInstantiated() {
        this.beanTypes.clear();
        this.beanDefinitions.clear();
    }

    private RootBeanDefinition getBeanDefinition(String name) {
        try {
            return (RootBeanDefinition) this.beanFactory.getMergedBeanDefinition(name);
        } catch (BeanDefinitionStoreException ex) {
            logIgnoredError("unresolvable metadata in bean definition", name, ex);
            return null;
        }
    }

    private void logIgnoredError(String message, String name, Exception ex) {
        if (logger.isDebugEnabled()) {
            logger.debug("Ignoring " + message + " '" + name + "'", ex);
        }
    }

    private boolean requiresEagerInit(String factoryBeanName) {
        return (factoryBeanName == null || !this.beanFactory.isFactoryBean(factoryBeanName) || this.beanFactory.containsSingleton(factoryBeanName)) ? false : true;
    }

    private void updateTypesIfNecessary() {
        Iterator<String> names = this.beanFactory.getBeanNamesIterator();
        while (names.hasNext()) {
            String name = names.next();
            if (!this.beanTypes.containsKey(name)) {
                addBeanType(name);
            } else {
                updateBeanType(name);
            }
        }
    }

    private void addBeanType(String name) {
        if (this.beanFactory.containsSingleton(name)) {
            this.beanTypes.put(name, this.beanFactory.getType(name));
        } else if (!this.beanFactory.isAlias(name)) {
            addBeanTypeForNonAliasDefinition(name);
        }
    }

    private void addBeanTypeForNonAliasDefinition(String name) {
        RootBeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition != null) {
            addBeanTypeForNonAliasDefinition(name, beanDefinition);
        }
    }

    private void updateBeanType(String name) {
        RootBeanDefinition beanDefinition;
        RootBeanDefinition previous;
        if (!this.beanFactory.isAlias(name) && !this.beanFactory.containsSingleton(name) && (beanDefinition = getBeanDefinition(name)) != null && (previous = this.beanDefinitions.put(name, beanDefinition)) != null && !beanDefinition.equals(previous)) {
            addBeanTypeForNonAliasDefinition(name, beanDefinition);
        }
    }

    private void addBeanTypeForNonAliasDefinition(String name, RootBeanDefinition beanDefinition) {
        try {
            String factoryName = "&" + name;
            if (!beanDefinition.isAbstract() && !requiresEagerInit(beanDefinition.getFactoryBeanName())) {
                if (this.beanFactory.isFactoryBean(factoryName)) {
                    Class<?> factoryBeanGeneric = getFactoryBeanGeneric(this.beanFactory, beanDefinition);
                    this.beanTypes.put(name, factoryBeanGeneric);
                    this.beanTypes.put(factoryName, this.beanFactory.getType(factoryName));
                } else {
                    this.beanTypes.put(name, this.beanFactory.getType(name));
                }
            }
            this.beanDefinitions.put(name, beanDefinition);
        } catch (CannotLoadBeanClassException ex) {
            logIgnoredError("bean class loading failure for bean", name, ex);
        }
    }

    private Class<?> getFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition) {
        try {
            return doGetFactoryBeanGeneric(beanFactory, definition);
        } catch (Exception e) {
            return null;
        }
    }

    private Class<?> doGetFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition) throws Exception {
        if (StringUtils.hasLength(definition.getFactoryBeanName()) && StringUtils.hasLength(definition.getFactoryMethodName())) {
            return getConfigurationClassFactoryBeanGeneric(beanFactory, definition);
        }
        if (StringUtils.hasLength(definition.getBeanClassName())) {
            return getDirectFactoryBeanGeneric(beanFactory, definition);
        }
        return null;
    }

    private Class<?> getConfigurationClassFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition) throws Exception {
        Method method = getFactoryMethod(beanFactory, definition);
        Class<?> generic = ResolvableType.forMethodReturnType(method).as(FactoryBean.class).resolveGeneric(new int[0]);
        if ((generic == null || generic.equals(Object.class)) && definition.hasAttribute("factoryBeanObjectType")) {
            generic = getTypeFromAttribute(definition.getAttribute("factoryBeanObjectType"));
        }
        return generic;
    }

    private Method getFactoryMethod(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition) throws Exception {
        if (definition instanceof AnnotatedBeanDefinition) {
            MethodMetadata factoryMethodMetadata = ((AnnotatedBeanDefinition) definition).getFactoryMethodMetadata();
            if (factoryMethodMetadata instanceof StandardMethodMetadata) {
                return ((StandardMethodMetadata) factoryMethodMetadata).getIntrospectedMethod();
            }
        }
        BeanDefinition factoryDefinition = beanFactory.getBeanDefinition(definition.getFactoryBeanName());
        Class<?> factoryClass = ClassUtils.forName(factoryDefinition.getBeanClassName(), beanFactory.getBeanClassLoader());
        return getFactoryMethod(definition, factoryClass);
    }

    private Method getFactoryMethod(BeanDefinition definition, Class<?> factoryClass) {
        Method uniqueMethod = null;
        for (Method candidate : getCandidateFactoryMethods(definition, factoryClass)) {
            if (candidate.getName().equals(definition.getFactoryMethodName())) {
                if (uniqueMethod == null) {
                    uniqueMethod = candidate;
                } else if (!hasMatchingParameterTypes(candidate, uniqueMethod)) {
                    return null;
                }
            }
        }
        return uniqueMethod;
    }

    private Method[] getCandidateFactoryMethods(BeanDefinition definition, Class<?> factoryClass) {
        return shouldConsiderNonPublicMethods(definition) ? ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass.getMethods();
    }

    private boolean shouldConsiderNonPublicMethods(BeanDefinition definition) {
        return (definition instanceof AbstractBeanDefinition) && ((AbstractBeanDefinition) definition).isNonPublicAccessAllowed();
    }

    private boolean hasMatchingParameterTypes(Method candidate, Method current) {
        return Arrays.equals(candidate.getParameterTypes(), current.getParameterTypes());
    }

    private Class<?> getDirectFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition) throws LinkageError, ClassNotFoundException {
        Class<?> factoryBeanClass = ClassUtils.forName(definition.getBeanClassName(), beanFactory.getBeanClassLoader());
        Class<?> generic = ResolvableType.forClass(factoryBeanClass).as(FactoryBean.class).resolveGeneric(new int[0]);
        if ((generic == null || generic.equals(Object.class)) && definition.hasAttribute("factoryBeanObjectType")) {
            generic = getTypeFromAttribute(definition.getAttribute("factoryBeanObjectType"));
        }
        return generic;
    }

    private Class<?> getTypeFromAttribute(Object attribute) throws LinkageError, ClassNotFoundException {
        if (attribute instanceof Class) {
            return (Class) attribute;
        }
        if (attribute instanceof String) {
            return ClassUtils.forName((String) attribute, null);
        }
        return null;
    }
}
