package org.springframework.boot.context.properties;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationBeanFactoryMetaData.class */
public class ConfigurationBeanFactoryMetaData implements BeanFactoryPostProcessor {
    private ConfigurableListableBeanFactory beanFactory;
    private Map<String, MetaData> beans = new HashMap();

    @Override // org.springframework.beans.factory.config.BeanFactoryPostProcessor
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        for (String name : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition definition = beanFactory.getBeanDefinition(name);
            String method = definition.getFactoryMethodName();
            String bean = definition.getFactoryBeanName();
            if (method != null && bean != null) {
                this.beans.put(name, new MetaData(bean, method));
            }
        }
    }

    public <A extends Annotation> Map<String, Object> getBeansWithFactoryAnnotation(Class<A> type) {
        Map<String, Object> result = new HashMap<>();
        for (String name : this.beans.keySet()) {
            if (findFactoryAnnotation(name, type) != null) {
                result.put(name, this.beanFactory.getBean(name));
            }
        }
        return result;
    }

    public <A extends Annotation> A findFactoryAnnotation(String str, Class<A> cls) throws SecurityException, IllegalArgumentException {
        Method methodFindFactoryMethod = findFactoryMethod(str);
        if (methodFindFactoryMethod != null) {
            return (A) AnnotationUtils.findAnnotation(methodFindFactoryMethod, (Class) cls);
        }
        return null;
    }

    private Method findFactoryMethod(String beanName) throws SecurityException, IllegalArgumentException {
        if (!this.beans.containsKey(beanName)) {
            return null;
        }
        final AtomicReference<Method> found = new AtomicReference<>(null);
        MetaData meta = this.beans.get(beanName);
        final String factory = meta.getMethod();
        Class<?> type = this.beanFactory.getType(meta.getBean());
        ReflectionUtils.doWithMethods(type, new ReflectionUtils.MethodCallback() { // from class: org.springframework.boot.context.properties.ConfigurationBeanFactoryMetaData.1
            @Override // org.springframework.util.ReflectionUtils.MethodCallback
            public void doWith(Method method) throws IllegalAccessException, IllegalArgumentException {
                if (method.getName().equals(factory)) {
                    found.compareAndSet(null, method);
                }
            }
        });
        return found.get();
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationBeanFactoryMetaData$MetaData.class */
    private static class MetaData {
        private String bean;
        private String method;

        MetaData(String bean, String method) {
            this.bean = bean;
            this.method = method;
        }

        public String getBean() {
            return this.bean;
        }

        public String getMethod() {
            return this.method;
        }
    }
}
