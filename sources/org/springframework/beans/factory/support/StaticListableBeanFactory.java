package org.springframework.beans.factory.support;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/support/StaticListableBeanFactory.class */
public class StaticListableBeanFactory implements ListableBeanFactory {
    private final Map<String, Object> beans;

    public StaticListableBeanFactory() {
        this.beans = new LinkedHashMap();
    }

    public StaticListableBeanFactory(Map<String, Object> beans) {
        Assert.notNull(beans, "Beans Map must not be null");
        this.beans = beans;
    }

    public void addBean(String name, Object bean) {
        this.beans.put(name, bean);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Object getBean(String name) throws BeansException {
        String beanName = BeanFactoryUtils.transformedBeanName(name);
        Object bean = this.beans.get(beanName);
        if (bean == null) {
            throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
        }
        if (BeanFactoryUtils.isFactoryDereference(name) && !(bean instanceof FactoryBean)) {
            throw new BeanIsNotAFactoryException(beanName, bean.getClass());
        }
        if ((bean instanceof FactoryBean) && !BeanFactoryUtils.isFactoryDereference(name)) {
            try {
                return ((FactoryBean) bean).getObject();
            } catch (Exception ex) {
                throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
            }
        }
        return bean;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(String str, Class<T> cls) throws BeansException {
        T t = (T) getBean(str);
        if (cls != null && !cls.isInstance(t)) {
            throw new BeanNotOfRequiredTypeException(str, cls, t.getClass());
        }
        return t;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Object getBean(String name, Object... args) throws BeansException {
        if (!ObjectUtils.isEmpty(args)) {
            throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
        }
        return getBean(name);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(Class<T> cls) throws BeansException {
        String[] beanNamesForType = getBeanNamesForType((Class<?>) cls);
        if (beanNamesForType.length == 1) {
            return (T) getBean(beanNamesForType[0], cls);
        }
        if (beanNamesForType.length > 1) {
            throw new NoUniqueBeanDefinitionException((Class<?>) cls, beanNamesForType);
        }
        throw new NoSuchBeanDefinitionException((Class<?>) cls);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(Class<T> cls, Object... objArr) throws BeansException {
        if (!ObjectUtils.isEmpty(objArr)) {
            throw new UnsupportedOperationException("StaticListableBeanFactory does not support explicit bean creation arguments");
        }
        return (T) getBean(cls);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean containsBean(String name) {
        return this.beans.containsKey(name);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isSingleton(String name) throws BeansException {
        Object bean = getBean(name);
        return (bean instanceof FactoryBean) && ((FactoryBean) bean).isSingleton();
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isPrototype(String name) throws BeansException {
        Object bean = getBean(name);
        return ((bean instanceof SmartFactoryBean) && ((SmartFactoryBean) bean).isPrototype()) || ((bean instanceof FactoryBean) && !((FactoryBean) bean).isSingleton());
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        Class<?> type = getType(name);
        return type != null && typeToMatch.isAssignableFrom(type);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        Class<?> type = getType(name);
        return typeToMatch == null || (type != null && typeToMatch.isAssignableFrom(type));
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        String beanName = BeanFactoryUtils.transformedBeanName(name);
        Object bean = this.beans.get(beanName);
        if (bean == null) {
            throw new NoSuchBeanDefinitionException(beanName, "Defined beans are [" + StringUtils.collectionToCommaDelimitedString(this.beans.keySet()) + "]");
        }
        if ((bean instanceof FactoryBean) && !BeanFactoryUtils.isFactoryDereference(name)) {
            return ((FactoryBean) bean).getObjectType();
        }
        return bean.getClass();
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public boolean containsBeanDefinition(String name) {
        return this.beans.containsKey(name);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public int getBeanDefinitionCount() {
        return this.beans.size();
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory, org.springframework.beans.factory.support.BeanDefinitionRegistry
    public String[] getBeanDefinitionNames() {
        return StringUtils.toStringArray(this.beans.keySet());
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForType(ResolvableType type) {
        Class<?> resolved;
        boolean isFactoryType = false;
        if (type != null && (resolved = type.resolve()) != null && FactoryBean.class.isAssignableFrom(resolved)) {
            isFactoryType = true;
        }
        List<String> matches = new ArrayList<>();
        for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
            String name = entry.getKey();
            Object beanInstance = entry.getValue();
            if ((beanInstance instanceof FactoryBean) && !isFactoryType) {
                Class<?> objectType = ((FactoryBean) beanInstance).getObjectType();
                if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
                    matches.add(name);
                }
            } else if (type == null || type.isInstance(beanInstance)) {
                matches.add(name);
            }
        }
        return StringUtils.toStringArray(matches);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanNamesForType(ResolvableType.forClass(type));
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return getBeanNamesForType(ResolvableType.forClass(type));
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeansOfType(type, true, true);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        boolean isFactoryType = type != null && FactoryBean.class.isAssignableFrom(type);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
            String beanName = entry.getKey();
            Object beanInstance = entry.getValue();
            if ((beanInstance instanceof FactoryBean) && !isFactoryType) {
                FactoryBean<?> factory = (FactoryBean) beanInstance;
                Class<?> objectType = factory.getObjectType();
                if (includeNonSingletons || factory.isSingleton()) {
                    if (objectType != null && (type == null || type.isAssignableFrom(objectType))) {
                        linkedHashMap.put(beanName, getBean(beanName, type));
                    }
                }
            } else if (type == null || type.isInstance(beanInstance)) {
                if (isFactoryType) {
                    beanName = "&" + beanName;
                }
                linkedHashMap.put(beanName, beanInstance);
            }
        }
        return linkedHashMap;
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType) {
        List<String> results = new ArrayList<>();
        for (String beanName : this.beans.keySet()) {
            if (findAnnotationOnBean(beanName, annotationType) != null) {
                results.add(beanName);
            }
        }
        return StringUtils.toStringArray(results);
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException {
        Map<String, Object> results = new LinkedHashMap<>();
        for (String beanName : this.beans.keySet()) {
            if (findAnnotationOnBean(beanName, annotationType) != null) {
                results.put(beanName, getBean(beanName));
            }
        }
        return results;
    }

    @Override // org.springframework.beans.factory.ListableBeanFactory
    public <A extends Annotation> A findAnnotationOnBean(String str, Class<A> cls) throws NoSuchBeanDefinitionException {
        Class<?> type = getType(str);
        if (type != null) {
            return (A) AnnotationUtils.findAnnotation(type, (Class) cls);
        }
        return null;
    }
}
