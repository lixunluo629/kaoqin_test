package org.springframework.plugin.core.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.plugin.core.support.PluginRegistryFactoryBean;
import org.springframework.util.StringUtils;

/* loaded from: spring-plugin-core-1.2.0.RELEASE.jar:org/springframework/plugin/core/config/PluginRegistriesBeanDefinitionRegistrar.class */
public class PluginRegistriesBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        Class<?>[] types = (Class[]) importingClassMetadata.getAnnotationAttributes(EnablePluginRegistries.class.getName()).get("value");
        for (Class<?> type : types) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PluginRegistryFactoryBean.class);
            builder.addPropertyValue("type", type);
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            Qualifier annotation = (Qualifier) type.getAnnotation(Qualifier.class);
            if (annotation != null) {
                AutowireCandidateQualifier qualifierMetadata = new AutowireCandidateQualifier((Class<?>) Qualifier.class);
                qualifierMetadata.setAttribute(AutowireCandidateQualifier.VALUE_KEY, annotation.value());
                beanDefinition.addQualifier(qualifierMetadata);
            }
            String beanName = annotation == null ? StringUtils.uncapitalize(type.getSimpleName() + "Registry") : annotation.value();
            registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
        }
    }
}
