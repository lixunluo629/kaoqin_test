package org.springframework.beans.factory.annotation;

import java.lang.reflect.Method;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/annotation/BeanFactoryAnnotationUtils.class */
public abstract class BeanFactoryAnnotationUtils {
    public static <T> T qualifiedBeanOfType(BeanFactory beanFactory, Class<T> cls, String str) throws BeansException {
        Assert.notNull(beanFactory, "BeanFactory must not be null");
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            return (T) qualifiedBeanOfType((ConfigurableListableBeanFactory) beanFactory, (Class) cls, str);
        }
        if (beanFactory.containsBean(str)) {
            return (T) beanFactory.getBean(str, cls);
        }
        throw new NoSuchBeanDefinitionException(str, "No matching " + cls.getSimpleName() + " bean found for bean name '" + str + "'! (Note: Qualifier matching not supported because given BeanFactory does not implement ConfigurableListableBeanFactory.)");
    }

    private static <T> T qualifiedBeanOfType(ConfigurableListableBeanFactory configurableListableBeanFactory, Class<T> cls, String str) {
        String str2 = null;
        for (String str3 : BeanFactoryUtils.beanNamesForTypeIncludingAncestors((ListableBeanFactory) configurableListableBeanFactory, (Class<?>) cls)) {
            if (isQualifierMatch(str, str3, configurableListableBeanFactory)) {
                if (str2 != null) {
                    throw new NoUniqueBeanDefinitionException((Class<?>) cls, str2, str3);
                }
                str2 = str3;
            }
        }
        if (str2 != null) {
            return (T) configurableListableBeanFactory.getBean(str2, cls);
        }
        if (configurableListableBeanFactory.containsBean(str)) {
            return (T) configurableListableBeanFactory.getBean(str, cls);
        }
        throw new NoSuchBeanDefinitionException(str, "No matching " + cls.getSimpleName() + " bean found for qualifier '" + str + "' - neither qualifier match nor bean name match!");
    }

    private static boolean isQualifierMatch(String qualifier, String beanName, ConfigurableListableBeanFactory bf) {
        Qualifier targetAnnotation;
        Method factoryMethod;
        Qualifier targetAnnotation2;
        if (bf.containsBean(beanName)) {
            try {
                BeanDefinition bd = bf.getMergedBeanDefinition(beanName);
                if (bd instanceof AbstractBeanDefinition) {
                    AbstractBeanDefinition abd = (AbstractBeanDefinition) bd;
                    AutowireCandidateQualifier candidate = abd.getQualifier(Qualifier.class.getName());
                    if ((candidate != null && qualifier.equals(candidate.getAttribute("value"))) || qualifier.equals(beanName) || ObjectUtils.containsElement(bf.getAliases(beanName), qualifier)) {
                        return true;
                    }
                }
                if ((bd instanceof RootBeanDefinition) && (factoryMethod = ((RootBeanDefinition) bd).getResolvedFactoryMethod()) != null && (targetAnnotation2 = (Qualifier) AnnotationUtils.getAnnotation(factoryMethod, Qualifier.class)) != null) {
                    return qualifier.equals(targetAnnotation2.value());
                }
                Class<?> beanType = bf.getType(beanName);
                if (beanType != null && (targetAnnotation = (Qualifier) AnnotationUtils.getAnnotation(beanType, Qualifier.class)) != null) {
                    return qualifier.equals(targetAnnotation.value());
                }
                return false;
            } catch (NoSuchBeanDefinitionException e) {
                return false;
            }
        }
        return false;
    }
}
