package org.springframework.data.auditing.config;

import java.lang.annotation.Annotation;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.auditing.AuditingHandler;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/auditing/config/AuditingBeanDefinitionRegistrarSupport.class */
public abstract class AuditingBeanDefinitionRegistrarSupport implements ImportBeanDefinitionRegistrar {
    private static final String AUDITOR_AWARE = "auditorAware";
    private static final String DATE_TIME_PROVIDER = "dateTimeProvider";
    private static final String MODIFY_ON_CREATE = "modifyOnCreation";
    private static final String SET_DATES = "dateTimeForNow";

    protected abstract Class<? extends Annotation> getAnnotation();

    protected abstract void registerAuditListenerBeanDefinition(BeanDefinition beanDefinition, BeanDefinitionRegistry beanDefinitionRegistry);

    protected abstract String getAuditingHandlerBeanName();

    @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        Assert.notNull(annotationMetadata, "AnnotationMetadata must not be null!");
        Assert.notNull(annotationMetadata, "BeanDefinitionRegistry must not be null!");
        AbstractBeanDefinition ahbd = registerAuditHandlerBeanDefinition(registry, getConfiguration(annotationMetadata));
        registerAuditListenerBeanDefinition(ahbd, registry);
    }

    private AbstractBeanDefinition registerAuditHandlerBeanDefinition(BeanDefinitionRegistry registry, AuditingConfiguration configuration) throws BeanDefinitionStoreException {
        Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
        Assert.notNull(configuration, "AuditingConfiguration must not be null!");
        AbstractBeanDefinition ahbd = getAuditHandlerBeanDefinitionBuilder(configuration).getBeanDefinition();
        registry.registerBeanDefinition(getAuditingHandlerBeanName(), ahbd);
        return ahbd;
    }

    protected BeanDefinitionBuilder getAuditHandlerBeanDefinitionBuilder(AuditingConfiguration configuration) {
        Assert.notNull(configuration, "AuditingConfiguration must not be null!");
        return configureDefaultAuditHandlerAttributes(configuration, BeanDefinitionBuilder.rootBeanDefinition((Class<?>) AuditingHandler.class));
    }

    protected BeanDefinitionBuilder configureDefaultAuditHandlerAttributes(AuditingConfiguration configuration, BeanDefinitionBuilder builder) {
        if (StringUtils.hasText(configuration.getAuditorAwareRef())) {
            builder.addPropertyValue(AUDITOR_AWARE, createLazyInitTargetSourceBeanDefinition(configuration.getAuditorAwareRef()));
        } else {
            builder.setAutowireMode(2);
        }
        builder.addPropertyValue(SET_DATES, Boolean.valueOf(configuration.isSetDates()));
        builder.addPropertyValue(MODIFY_ON_CREATE, Boolean.valueOf(configuration.isModifyOnCreate()));
        if (StringUtils.hasText(configuration.getDateTimeProviderRef())) {
            builder.addPropertyReference(DATE_TIME_PROVIDER, configuration.getDateTimeProviderRef());
        } else {
            builder.addPropertyValue(DATE_TIME_PROVIDER, CurrentDateTimeProvider.INSTANCE);
        }
        builder.setRole(2);
        return builder;
    }

    protected AuditingConfiguration getConfiguration(AnnotationMetadata annotationMetadata) {
        return new AnnotationAuditingConfiguration(annotationMetadata, getAnnotation());
    }

    protected void registerInfrastructureBeanWithId(AbstractBeanDefinition definition, String id, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        definition.setRole(2);
        registry.registerBeanDefinition(id, definition);
    }

    private BeanDefinition createLazyInitTargetSourceBeanDefinition(String auditorAwareRef) {
        BeanDefinitionBuilder targetSourceBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) LazyInitTargetSource.class);
        targetSourceBuilder.addPropertyValue("targetBeanName", auditorAwareRef);
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) ProxyFactoryBean.class);
        builder.addPropertyValue("targetSource", targetSourceBuilder.getBeanDefinition());
        return builder.getBeanDefinition();
    }
}
