package org.springframework.boot.jackson;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.lang.reflect.Modifier;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.ResolvableType;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/jackson/JsonComponentModule.class */
public class JsonComponentModule extends SimpleModule implements BeanFactoryAware {
    private BeanFactory beanFactory;

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    public void registerJsonComponents() throws BeansException {
        BeanFactory parentBeanFactory = this.beanFactory;
        while (true) {
            BeanFactory beanFactory = parentBeanFactory;
            if (beanFactory != null) {
                if (beanFactory instanceof ListableBeanFactory) {
                    addJsonBeans((ListableBeanFactory) beanFactory);
                }
                parentBeanFactory = beanFactory instanceof HierarchicalBeanFactory ? ((HierarchicalBeanFactory) beanFactory).getParentBeanFactory() : null;
            } else {
                return;
            }
        }
    }

    private void addJsonBeans(ListableBeanFactory beanFactory) throws BeansException {
        Map<String, Object> beans = beanFactory.getBeansWithAnnotation(JsonComponent.class);
        for (Object bean : beans.values()) {
            addJsonBean(bean);
        }
    }

    private void addJsonBean(Object bean) {
        if (bean instanceof JsonSerializer) {
            addSerializerWithDeducedType((JsonSerializer) bean);
        }
        if (bean instanceof JsonDeserializer) {
            addDeserializerWithDeducedType((JsonDeserializer) bean);
        }
        for (Class<?> innerClass : bean.getClass().getDeclaredClasses()) {
            if (!Modifier.isAbstract(innerClass.getModifiers()) && (JsonSerializer.class.isAssignableFrom(innerClass) || JsonDeserializer.class.isAssignableFrom(innerClass))) {
                try {
                    addJsonBean(innerClass.newInstance());
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> void addSerializerWithDeducedType(JsonSerializer<T> serializer) {
        ResolvableType type = ResolvableType.forClass(JsonSerializer.class, serializer.getClass());
        addSerializer(type.resolveGeneric(new int[0]), serializer);
    }

    private <T> void addDeserializerWithDeducedType(JsonDeserializer<T> deserializer) {
        ResolvableType type = ResolvableType.forClass(JsonDeserializer.class, deserializer.getClass());
        addDeserializer(type.resolveGeneric(new int[0]), deserializer);
    }
}
