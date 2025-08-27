package org.springframework.boot.autoconfigure.integration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.IntegrationComponentScanRegistrar;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfigurationScanRegistrar.class */
class IntegrationAutoConfigurationScanRegistrar extends IntegrationComponentScanRegistrar implements BeanFactoryAware {
    private BeanFactory beanFactory;

    IntegrationAutoConfigurationScanRegistrar() {
    }

    @Override // org.springframework.beans.factory.BeanFactoryAware
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        super.registerBeanDefinitions(new IntegrationComponentScanConfigurationMetaData(this.beanFactory), registry);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfigurationScanRegistrar$IntegrationComponentScanConfigurationMetaData.class */
    private static class IntegrationComponentScanConfigurationMetaData extends StandardAnnotationMetadata {
        private final BeanFactory beanFactory;

        IntegrationComponentScanConfigurationMetaData(BeanFactory beanFactory) {
            super(IntegrationComponentScanConfiguration.class, true);
            this.beanFactory = beanFactory;
        }

        @Override // org.springframework.core.type.StandardAnnotationMetadata, org.springframework.core.type.AnnotatedTypeMetadata
        public Map<String, Object> getAnnotationAttributes(String annotationName) {
            Map<String, Object> attributes = super.getAnnotationAttributes(annotationName);
            if (IntegrationComponentScan.class.getName().equals(annotationName) && AutoConfigurationPackages.has(this.beanFactory)) {
                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                attributes = new LinkedHashMap(attributes);
                attributes.put("value", packages.toArray(new String[packages.size()]));
            }
            return attributes;
        }
    }

    @IntegrationComponentScan
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationAutoConfigurationScanRegistrar$IntegrationComponentScanConfiguration.class */
    private static class IntegrationComponentScanConfiguration {
        private IntegrationComponentScanConfiguration() {
        }
    }
}
