package org.springframework.hateoas.config;

import java.lang.annotation.Annotation;
import javax.ws.rs.Path;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkBuilderFactory;
import org.springframework.hateoas.core.ControllerEntityLinksFactoryBean;
import org.springframework.hateoas.core.DelegatingEntityLinks;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilderFactory;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.plugin.core.support.PluginRegistryFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;

/* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/LinkBuilderBeanDefinitionRegistrar.class */
class LinkBuilderBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    private static final boolean IS_JAX_RS_PRESENT = ClassUtils.isPresent("javax.ws.rs.Path", ClassUtils.getDefaultClassLoader());

    LinkBuilderBeanDefinitionRegistrar() {
    }

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        BeanDefinitionBuilder registryFactoryBeanBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) PluginRegistryFactoryBean.class);
        registryFactoryBeanBuilder.addPropertyValue("type", EntityLinks.class);
        registryFactoryBeanBuilder.addPropertyValue("exclusions", DelegatingEntityLinks.class);
        AbstractBeanDefinition registryBeanDefinition = registryFactoryBeanBuilder.getBeanDefinition();
        registry.registerBeanDefinition("entityLinksPluginRegistry", registryBeanDefinition);
        BeanDefinitionBuilder delegateBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) DelegatingEntityLinks.class);
        delegateBuilder.addConstructorArgValue(registryBeanDefinition);
        BeanDefinitionBuilder builder = getEntityControllerLinksFor(Controller.class, ControllerLinkBuilderFactory.class);
        registry.registerBeanDefinition("controllerEntityLinks", builder.getBeanDefinition());
        delegateBuilder.addDependsOn("controllerEntityLinks");
        if (IS_JAX_RS_PRESENT) {
            JaxRsEntityControllerBuilderDefinitionBuilder definitionBuilder = new JaxRsEntityControllerBuilderDefinitionBuilder();
            registry.registerBeanDefinition("jaxRsEntityLinks", definitionBuilder.getBeanDefinition());
            delegateBuilder.addDependsOn("jaxRsEntityLinks");
        }
        AbstractBeanDefinition beanDefinition = delegateBuilder.getBeanDefinition();
        beanDefinition.setPrimary(true);
        registry.registerBeanDefinition("delegatingEntityLinks", beanDefinition);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BeanDefinitionBuilder getEntityControllerLinksFor(Class<? extends Annotation> type, Class<? extends LinkBuilderFactory<?>> linkBuilderFactoryType) {
        RootBeanDefinition definition = new RootBeanDefinition(linkBuilderFactoryType);
        definition.setAutowireMode(2);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) ControllerEntityLinksFactoryBean.class);
        builder.addPropertyValue(JamXmlElements.ANNOTATION, type);
        builder.addPropertyValue("linkBuilderFactory", definition);
        return builder;
    }

    /* loaded from: spring-hateoas-0.23.0.RELEASE.jar:org/springframework/hateoas/config/LinkBuilderBeanDefinitionRegistrar$JaxRsEntityControllerBuilderDefinitionBuilder.class */
    static class JaxRsEntityControllerBuilderDefinitionBuilder {
        JaxRsEntityControllerBuilderDefinitionBuilder() {
        }

        public BeanDefinition getBeanDefinition() {
            BeanDefinitionBuilder builder = LinkBuilderBeanDefinitionRegistrar.getEntityControllerLinksFor(Path.class, JaxRsLinkBuilderFactory.class);
            return builder.getBeanDefinition();
        }
    }
}
