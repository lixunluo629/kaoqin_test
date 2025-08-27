package org.springframework.data.redis.repository.configuration;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.convert.CustomConversions;
import org.springframework.data.redis.core.convert.MappingConfiguration;
import org.springframework.data.redis.core.convert.MappingRedisConverter;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/configuration/RedisRepositoryConfigurationExtension.class */
public class RedisRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {
    private static final String REDIS_CONVERTER_BEAN_NAME = "redisConverter";
    private static final String REDIS_REFERENCE_RESOLVER_BEAN_NAME = "redisReferenceResolver";
    private static final String REDIS_ADAPTER_BEAN_NAME = "redisKeyValueAdapter";
    private static final String REDIS_CUSTOM_CONVERSIONS_BEAN_NAME = "redisCustomConversions";

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension, org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport, org.springframework.data.repository.config.RepositoryConfigurationExtension
    public String getModuleName() {
        return "Redis";
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension, org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport
    protected String getModulePrefix() {
        return "redis";
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension
    protected String getDefaultKeyValueTemplateRef() {
        return "redisKeyValueTemplate";
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension, org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport, org.springframework.data.repository.config.RepositoryConfigurationExtension
    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
        String redisTemplateRef = configurationSource.getAttribute("redisTemplateRef");
        RootBeanDefinition mappingContextDefinition = createRedisMappingContext(configurationSource);
        mappingContextDefinition.setSource(configurationSource.getSource());
        registerIfNotAlreadyRegistered(mappingContextDefinition, registry, "keyValueMappingContext", configurationSource);
        RootBeanDefinition customConversions = new RootBeanDefinition((Class<?>) CustomConversions.class);
        registerIfNotAlreadyRegistered(customConversions, registry, REDIS_CUSTOM_CONVERSIONS_BEAN_NAME, configurationSource);
        RootBeanDefinition redisReferenceResolver = createRedisReferenceResolverDefinition(redisTemplateRef);
        redisReferenceResolver.setSource(configurationSource.getSource());
        registerIfNotAlreadyRegistered(redisReferenceResolver, registry, REDIS_REFERENCE_RESOLVER_BEAN_NAME, configurationSource);
        RootBeanDefinition redisConverterDefinition = createRedisConverterDefinition();
        redisConverterDefinition.setSource(configurationSource.getSource());
        registerIfNotAlreadyRegistered(redisConverterDefinition, registry, REDIS_CONVERTER_BEAN_NAME, configurationSource);
        RootBeanDefinition redisKeyValueAdapterDefinition = new RootBeanDefinition((Class<?>) RedisKeyValueAdapter.class);
        ConstructorArgumentValues constructorArgumentValuesForRedisKeyValueAdapter = new ConstructorArgumentValues();
        if (StringUtils.hasText(redisTemplateRef)) {
            constructorArgumentValuesForRedisKeyValueAdapter.addIndexedArgumentValue(0, new RuntimeBeanReference(redisTemplateRef));
        }
        constructorArgumentValuesForRedisKeyValueAdapter.addIndexedArgumentValue(1, new RuntimeBeanReference(REDIS_CONVERTER_BEAN_NAME));
        redisKeyValueAdapterDefinition.setConstructorArgumentValues(constructorArgumentValuesForRedisKeyValueAdapter);
        DirectFieldAccessor fieldAccessor = new DirectFieldAccessor(configurationSource);
        AnnotationAttributes attributes = (AnnotationAttributes) fieldAccessor.getPropertyValue("attributes");
        MutablePropertyValues redisKeyValueAdapterProps = new MutablePropertyValues();
        redisKeyValueAdapterProps.add("enableKeyspaceEvents", attributes.getEnum("enableKeyspaceEvents"));
        redisKeyValueAdapterProps.add("keyspaceNotificationsConfigParameter", attributes.getString("keyspaceNotificationsConfigParameter"));
        redisKeyValueAdapterDefinition.setPropertyValues(redisKeyValueAdapterProps);
        registerIfNotAlreadyRegistered(redisKeyValueAdapterDefinition, registry, REDIS_ADAPTER_BEAN_NAME, configurationSource);
        super.registerBeansForRoot(registry, configurationSource);
    }

    private RootBeanDefinition createRedisReferenceResolverDefinition(String redisTemplateRef) {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClassName("org.springframework.data.redis.core.convert.ReferenceResolverImpl");
        ConstructorArgumentValues constructorArgs = new ConstructorArgumentValues();
        constructorArgs.addIndexedArgumentValue(0, new RuntimeBeanReference(redisTemplateRef));
        beanDef.setConstructorArgumentValues(constructorArgs);
        return beanDef;
    }

    private RootBeanDefinition createRedisMappingContext(RepositoryConfigurationSource configurationSource) {
        ConstructorArgumentValues mappingContextArgs = new ConstructorArgumentValues();
        mappingContextArgs.addIndexedArgumentValue(0, createMappingConfigBeanDef(configurationSource));
        RootBeanDefinition mappingContextBeanDef = new RootBeanDefinition((Class<?>) RedisMappingContext.class);
        mappingContextBeanDef.setConstructorArgumentValues(mappingContextArgs);
        return mappingContextBeanDef;
    }

    private BeanDefinition createMappingConfigBeanDef(RepositoryConfigurationSource configurationSource) {
        DirectFieldAccessor dfa = new DirectFieldAccessor(configurationSource);
        AnnotationAttributes aa = (AnnotationAttributes) dfa.getPropertyValue("attributes");
        GenericBeanDefinition indexConfiguration = new GenericBeanDefinition();
        indexConfiguration.setBeanClass(aa.getClass("indexConfiguration"));
        GenericBeanDefinition keyspaceConfig = new GenericBeanDefinition();
        keyspaceConfig.setBeanClass(aa.getClass("keyspaceConfiguration"));
        ConstructorArgumentValues mappingConfigArgs = new ConstructorArgumentValues();
        mappingConfigArgs.addIndexedArgumentValue(0, indexConfiguration);
        mappingConfigArgs.addIndexedArgumentValue(1, keyspaceConfig);
        GenericBeanDefinition mappingConfigBeanDef = new GenericBeanDefinition();
        mappingConfigBeanDef.setBeanClass(MappingConfiguration.class);
        mappingConfigBeanDef.setConstructorArgumentValues(mappingConfigArgs);
        return mappingConfigBeanDef;
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension
    protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(RepositoryConfigurationSource configurationSource) {
        RootBeanDefinition keyValueTemplateDefinition = new RootBeanDefinition((Class<?>) RedisKeyValueTemplate.class);
        ConstructorArgumentValues constructorArgumentValuesForKeyValueTemplate = new ConstructorArgumentValues();
        constructorArgumentValuesForKeyValueTemplate.addIndexedArgumentValue(0, new RuntimeBeanReference(REDIS_ADAPTER_BEAN_NAME));
        constructorArgumentValuesForKeyValueTemplate.addIndexedArgumentValue(1, new RuntimeBeanReference("keyValueMappingContext"));
        keyValueTemplateDefinition.setConstructorArgumentValues(constructorArgumentValuesForKeyValueTemplate);
        return keyValueTemplateDefinition;
    }

    private RootBeanDefinition createRedisConverterDefinition() {
        RootBeanDefinition beanDef = new RootBeanDefinition();
        beanDef.setBeanClass(MappingRedisConverter.class);
        ConstructorArgumentValues args = new ConstructorArgumentValues();
        args.addIndexedArgumentValue(0, new RuntimeBeanReference("keyValueMappingContext"));
        beanDef.setConstructorArgumentValues(args);
        MutablePropertyValues props = new MutablePropertyValues();
        props.add("referenceResolver", new RuntimeBeanReference(REDIS_REFERENCE_RESOLVER_BEAN_NAME));
        props.add("customConversions", new RuntimeBeanReference(REDIS_CUSTOM_CONVERSIONS_BEAN_NAME));
        beanDef.setPropertyValues(props);
        return beanDef;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Collections.singleton(RedisHash.class);
    }
}
