package org.springframework.aop.framework.autoproxy;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Conventions;

/* loaded from: spring-aop-4.3.25.RELEASE.jar:org/springframework/aop/framework/autoproxy/AutoProxyUtils.class */
public abstract class AutoProxyUtils {
    public static final String PRESERVE_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(AutoProxyUtils.class, "preserveTargetClass");
    public static final String ORIGINAL_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(AutoProxyUtils.class, "originalTargetClass");

    public static boolean shouldProxyTargetClass(ConfigurableListableBeanFactory beanFactory, String beanName) throws NoSuchBeanDefinitionException {
        if (beanName != null && beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            return Boolean.TRUE.equals(bd.getAttribute(PRESERVE_TARGET_CLASS_ATTRIBUTE));
        }
        return false;
    }

    public static Class<?> determineTargetClass(ConfigurableListableBeanFactory beanFactory, String beanName) {
        if (beanName == null) {
            return null;
        }
        if (beanFactory.containsBeanDefinition(beanName)) {
            BeanDefinition bd = beanFactory.getMergedBeanDefinition(beanName);
            Class<?> targetClass = (Class) bd.getAttribute(ORIGINAL_TARGET_CLASS_ATTRIBUTE);
            if (targetClass != null) {
                return targetClass;
            }
        }
        return beanFactory.getType(beanName);
    }

    static void exposeTargetClass(ConfigurableListableBeanFactory beanFactory, String beanName, Class<?> targetClass) {
        if (beanName != null && beanFactory.containsBeanDefinition(beanName)) {
            beanFactory.getMergedBeanDefinition(beanName).setAttribute(ORIGINAL_TARGET_CLASS_ATTRIBUTE, targetClass);
        }
    }
}
