package org.springframework.boot.context.properties;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/EnableConfigurationPropertiesImportSelector.class */
class EnableConfigurationPropertiesImportSelector implements ImportSelector {
    EnableConfigurationPropertiesImportSelector() {
    }

    @Override // org.springframework.context.annotation.ImportSelector
    public String[] selectImports(AnnotationMetadata metadata) {
        MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(EnableConfigurationProperties.class.getName(), false);
        Object[] type = attributes != null ? (Object[]) attributes.getFirst("value") : null;
        if (type == null || type.length == 0) {
            return new String[]{ConfigurationPropertiesBindingPostProcessorRegistrar.class.getName()};
        }
        return new String[]{ConfigurationPropertiesBeanRegistrar.class.getName(), ConfigurationPropertiesBindingPostProcessorRegistrar.class.getName()};
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/properties/EnableConfigurationPropertiesImportSelector$ConfigurationPropertiesBeanRegistrar.class */
    public static class ConfigurationPropertiesBeanRegistrar implements ImportBeanDefinitionRegistrar {
        @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
            MultiValueMap<String, Object> attributes = metadata.getAllAnnotationAttributes(EnableConfigurationProperties.class.getName(), false);
            List<Class<?>> types = collectClasses((List) attributes.get("value"));
            for (Class<?> type : types) {
                String prefix = extractPrefix(type);
                String name = StringUtils.hasText(prefix) ? prefix + "-" + type.getName() : type.getName();
                if (!registry.containsBeanDefinition(name)) {
                    registerBeanDefinition(registry, type, name);
                }
            }
        }

        private String extractPrefix(Class<?> type) {
            ConfigurationProperties annotation = (ConfigurationProperties) AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
            if (annotation != null) {
                return annotation.prefix();
            }
            return "";
        }

        private List<Class<?>> collectClasses(List<Object> list) {
            ArrayList<Class<?>> result = new ArrayList<>();
            for (Object object : list) {
                for (Object value : (Object[]) object) {
                    if ((value instanceof Class) && value != Void.TYPE) {
                        result.add((Class) value);
                    }
                }
            }
            return result;
        }

        private void registerBeanDefinition(BeanDefinitionRegistry registry, Class<?> type, String name) throws BeanDefinitionStoreException {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(type);
            AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
            registry.registerBeanDefinition(name, beanDefinition);
            ConfigurationProperties properties = (ConfigurationProperties) AnnotationUtils.findAnnotation(type, ConfigurationProperties.class);
            Assert.notNull(properties, "No " + ConfigurationProperties.class.getSimpleName() + " annotation found on  '" + type.getName() + "'.");
        }
    }
}
