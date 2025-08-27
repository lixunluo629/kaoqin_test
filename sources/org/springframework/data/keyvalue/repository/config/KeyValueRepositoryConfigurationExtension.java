package org.springframework.data.keyvalue.repository.config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.data.keyvalue.repository.query.KeyValuePartTreeQuery;
import org.springframework.data.keyvalue.repository.query.SpelQueryCreator;
import org.springframework.data.keyvalue.repository.support.KeyValueRepositoryFactoryBean;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/keyvalue/repository/config/KeyValueRepositoryConfigurationExtension.class */
public abstract class KeyValueRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {
    protected static final String MAPPING_CONTEXT_BEAN_NAME = "keyValueMappingContext";
    protected static final String KEY_VALUE_TEMPLATE_BEAN_REF_ATTRIBUTE = "keyValueTemplateRef";

    protected abstract String getDefaultKeyValueTemplateRef();

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtension
    public String getRepositoryFactoryClassName() {
        return KeyValueRepositoryFactoryBean.class.getName();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport, org.springframework.data.repository.config.RepositoryConfigurationExtension
    public String getModuleName() {
        return "KeyValue";
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport
    protected String getModulePrefix() {
        return "keyvalue";
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.singleton(KeyValueRepository.class);
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport, org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
        AnnotationAttributes attributes = config.getAttributes();
        builder.addPropertyReference("keyValueOperations", attributes.getString(KEY_VALUE_TEMPLATE_BEAN_REF_ATTRIBUTE));
        builder.addPropertyValue("queryCreator", getQueryCreatorType(config));
        builder.addPropertyValue("queryType", getQueryType(config));
        builder.addPropertyReference("mappingContext", MAPPING_CONTEXT_BEAN_NAME);
    }

    private static Class<?> getQueryCreatorType(AnnotationRepositoryConfigurationSource config) {
        AnnotationMetadata metadata = config.getEnableAnnotationMetadata();
        Map<String, Object> queryCreatorAnnotationAttributes = metadata.getAnnotationAttributes(QueryCreatorType.class.getName());
        if (queryCreatorAnnotationAttributes == null) {
            return SpelQueryCreator.class;
        }
        AnnotationAttributes queryCreatorAttributes = new AnnotationAttributes(queryCreatorAnnotationAttributes);
        return queryCreatorAttributes.getClass("value");
    }

    private static Class<?> getQueryType(AnnotationRepositoryConfigurationSource config) {
        AnnotationMetadata metadata = config.getEnableAnnotationMetadata();
        Map<String, Object> queryCreatorAnnotationAttributes = metadata.getAnnotationAttributes(QueryCreatorType.class.getName());
        if (queryCreatorAnnotationAttributes == null) {
            return KeyValuePartTreeQuery.class;
        }
        AnnotationAttributes queryCreatorAttributes = new AnnotationAttributes(queryCreatorAnnotationAttributes);
        return queryCreatorAttributes.getClass("repositoryQueryType");
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport, org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
        AbstractBeanDefinition beanDefinition;
        super.registerBeansForRoot(registry, configurationSource);
        RootBeanDefinition mappingContextDefinition = new RootBeanDefinition((Class<?>) KeyValueMappingContext.class);
        mappingContextDefinition.setSource(configurationSource.getSource());
        registerIfNotAlreadyRegistered(mappingContextDefinition, registry, MAPPING_CONTEXT_BEAN_NAME, configurationSource);
        String keyValueTemplateName = configurationSource.getAttribute(KEY_VALUE_TEMPLATE_BEAN_REF_ATTRIBUTE);
        if (getDefaultKeyValueTemplateRef().equals(keyValueTemplateName) && !registry.containsBeanDefinition(keyValueTemplateName) && (beanDefinition = getDefaultKeyValueTemplateBeanDefinition(configurationSource)) != null) {
            registerIfNotAlreadyRegistered(beanDefinition, registry, keyValueTemplateName, configurationSource.getSource());
        }
    }

    protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(RepositoryConfigurationSource configurationSource) {
        return null;
    }
}
