package org.springframework.web.method;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/ControllerAdviceBean.class */
public class ControllerAdviceBean implements Ordered {
    private final Object bean;
    private final BeanFactory beanFactory;
    private final int order;
    private final Set<String> basePackages;
    private final List<Class<?>> assignableTypes;
    private final List<Class<? extends Annotation>> annotations;

    public ControllerAdviceBean(Object bean) {
        this(bean, (BeanFactory) null);
    }

    public ControllerAdviceBean(String beanName, BeanFactory beanFactory) {
        this((Object) beanName, beanFactory);
    }

    private ControllerAdviceBean(Object bean, BeanFactory beanFactory) throws NoSuchBeanDefinitionException {
        Class<?> beanType;
        this.bean = bean;
        this.beanFactory = beanFactory;
        if (bean instanceof String) {
            String beanName = (String) bean;
            Assert.hasText(beanName, "Bean name must not be null");
            Assert.notNull(beanFactory, "BeanFactory must not be null");
            if (!beanFactory.containsBean(beanName)) {
                throw new IllegalArgumentException("BeanFactory [" + beanFactory + "] does not contain specified controller advice bean '" + beanName + "'");
            }
            beanType = this.beanFactory.getType(beanName);
            this.order = initOrderFromBeanType(beanType);
        } else {
            Assert.notNull(bean, "Bean must not be null");
            beanType = bean.getClass();
            this.order = initOrderFromBean(bean);
        }
        ControllerAdvice annotation = (ControllerAdvice) AnnotatedElementUtils.findMergedAnnotation(beanType, ControllerAdvice.class);
        if (annotation != null) {
            this.basePackages = initBasePackages(annotation);
            this.assignableTypes = Arrays.asList(annotation.assignableTypes());
            this.annotations = Arrays.asList(annotation.annotations());
        } else {
            this.basePackages = Collections.emptySet();
            this.assignableTypes = Collections.emptyList();
            this.annotations = Collections.emptyList();
        }
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public Class<?> getBeanType() {
        Class<?> clazz = this.bean instanceof String ? this.beanFactory.getType((String) this.bean) : this.bean.getClass();
        return ClassUtils.getUserClass(clazz);
    }

    public Object resolveBean() {
        return this.bean instanceof String ? this.beanFactory.getBean((String) this.bean) : this.bean;
    }

    public boolean isApplicableToBeanType(Class<?> beanType) {
        if (!hasSelectors()) {
            return true;
        }
        if (beanType != null) {
            for (String basePackage : this.basePackages) {
                if (beanType.getName().startsWith(basePackage)) {
                    return true;
                }
            }
            for (Class<?> clazz : this.assignableTypes) {
                if (ClassUtils.isAssignable(clazz, beanType)) {
                    return true;
                }
            }
            for (Class<? extends Annotation> annotationClass : this.annotations) {
                if (AnnotationUtils.findAnnotation(beanType, (Class) annotationClass) != null) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean hasSelectors() {
        return (this.basePackages.isEmpty() && this.assignableTypes.isEmpty() && this.annotations.isEmpty()) ? false : true;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ControllerAdviceBean)) {
            return false;
        }
        ControllerAdviceBean otherAdvice = (ControllerAdviceBean) other;
        return this.bean.equals(otherAdvice.bean) && this.beanFactory == otherAdvice.beanFactory;
    }

    public int hashCode() {
        return this.bean.hashCode();
    }

    public String toString() {
        return this.bean.toString();
    }

    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext applicationContext) {
        List<ControllerAdviceBean> beans = new ArrayList<>();
        for (String name : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, (Class<?>) Object.class)) {
            if (applicationContext.findAnnotationOnBean(name, ControllerAdvice.class) != null) {
                beans.add(new ControllerAdviceBean(name, (BeanFactory) applicationContext));
            }
        }
        return beans;
    }

    private static int initOrderFromBean(Object bean) {
        return bean instanceof Ordered ? ((Ordered) bean).getOrder() : initOrderFromBeanType(bean.getClass());
    }

    private static int initOrderFromBeanType(Class<?> beanType) {
        return OrderUtils.getOrder(beanType, Integer.MAX_VALUE).intValue();
    }

    private static Set<String> initBasePackages(ControllerAdvice annotation) {
        Set<String> basePackages = new LinkedHashSet<>();
        for (String basePackage : annotation.basePackages()) {
            if (StringUtils.hasText(basePackage)) {
                basePackages.add(adaptBasePackage(basePackage));
            }
        }
        for (Class<?> markerClass : annotation.basePackageClasses()) {
            basePackages.add(adaptBasePackage(ClassUtils.getPackageName(markerClass)));
        }
        return basePackages;
    }

    private static String adaptBasePackage(String basePackage) {
        return basePackage.endsWith(".") ? basePackage : basePackage + ".";
    }
}
