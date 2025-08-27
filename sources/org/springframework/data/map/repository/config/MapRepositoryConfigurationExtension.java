package org.springframework.data.map.repository.config;

import java.util.Map;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.config.ParsingUtils;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension;
import org.springframework.data.map.MapKeyValueAdapter;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

/* loaded from: spring-data-keyvalue-1.2.23.RELEASE.jar:org/springframework/data/map/repository/config/MapRepositoryConfigurationExtension.class */
public class MapRepositoryConfigurationExtension extends KeyValueRepositoryConfigurationExtension {
    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension, org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport, org.springframework.data.repository.config.RepositoryConfigurationExtension
    public String getModuleName() {
        return "Map";
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension, org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport
    protected String getModulePrefix() {
        return BeanDefinitionParserDelegate.MAP_ELEMENT;
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension
    protected String getDefaultKeyValueTemplateRef() {
        return "mapKeyValueTemplate";
    }

    @Override // org.springframework.data.keyvalue.repository.config.KeyValueRepositoryConfigurationExtension
    protected AbstractBeanDefinition getDefaultKeyValueTemplateBeanDefinition(RepositoryConfigurationSource configurationSource) {
        BeanDefinitionBuilder adapterBuilder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) MapKeyValueAdapter.class);
        adapterBuilder.addConstructorArgValue(getMapTypeToUse(configurationSource));
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition((Class<?>) KeyValueTemplate.class);
        builder.addConstructorArgValue(ParsingUtils.getSourceBeanDefinition(adapterBuilder, configurationSource.getSource()));
        builder.setRole(1);
        return ParsingUtils.getSourceBeanDefinition(builder, configurationSource.getSource());
    }

    private static Class<? extends Map> getMapTypeToUse(RepositoryConfigurationSource source) {
        return (Class) ((AnnotationMetadata) source.getSource()).getAnnotationAttributes(EnableMapRepositories.class.getName()).get("mapType");
    }
}
