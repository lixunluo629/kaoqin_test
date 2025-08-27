package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/NoSuchBeanDefinitionException.class */
public class NoSuchBeanDefinitionException extends BeansException {
    private String beanName;
    private ResolvableType resolvableType;

    public NoSuchBeanDefinitionException(String name) {
        super("No bean named '" + name + "' available");
        this.beanName = name;
    }

    public NoSuchBeanDefinitionException(String name, String message) {
        super("No bean named '" + name + "' available: " + message);
        this.beanName = name;
    }

    public NoSuchBeanDefinitionException(Class<?> type) {
        this(ResolvableType.forClass(type));
    }

    public NoSuchBeanDefinitionException(Class<?> type, String message) {
        this(ResolvableType.forClass(type), message);
    }

    public NoSuchBeanDefinitionException(ResolvableType type) {
        super("No qualifying bean of type '" + type + "' available");
        this.resolvableType = type;
    }

    public NoSuchBeanDefinitionException(ResolvableType type, String message) {
        super("No qualifying bean of type '" + type + "' available: " + message);
        this.resolvableType = type;
    }

    @Deprecated
    public NoSuchBeanDefinitionException(Class<?> type, String dependencyDescription, String message) {
        super("No qualifying bean" + (!StringUtils.hasLength(dependencyDescription) ? " of type '" + ClassUtils.getQualifiedName(type) + "'" : "") + " found for dependency" + (StringUtils.hasLength(dependencyDescription) ? " [" + dependencyDescription + "]" : "") + ": " + message);
        this.resolvableType = ResolvableType.forClass(type);
    }

    public String getBeanName() {
        return this.beanName;
    }

    public Class<?> getBeanType() {
        if (this.resolvableType != null) {
            return this.resolvableType.resolve();
        }
        return null;
    }

    public ResolvableType getResolvableType() {
        return this.resolvableType;
    }

    public int getNumberOfBeansFound() {
        return 0;
    }
}
