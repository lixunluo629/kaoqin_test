package org.springframework.boot.context.properties;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBindingPostProcessorRegistrar.class */
public class ConfigurationPropertiesBindingPostProcessorRegistrar implements ImportBeanDefinitionRegistrar {
    public static final String BINDER_BEAN_NAME = ConfigurationPropertiesBindingPostProcessor.class.getName();
    private static final String METADATA_BEAN_NAME = BINDER_BEAN_NAME + ".store";

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        if (!registry.containsBeanDefinition(BINDER_BEAN_NAME)) {
            BeanDefinitionBuilder meta = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) ConfigurationBeanFactoryMetaData.class);
            BeanDefinitionBuilder bean = BeanDefinitionBuilder.genericBeanDefinition((Class<?>) ConfigurationPropertiesBindingPostProcessor.class);
            bean.addPropertyReference("beanMetaDataStore", METADATA_BEAN_NAME);
            registry.registerBeanDefinition(BINDER_BEAN_NAME, bean.getBeanDefinition());
            registry.registerBeanDefinition(METADATA_BEAN_NAME, meta.getBeanDefinition());
        }
    }
}
