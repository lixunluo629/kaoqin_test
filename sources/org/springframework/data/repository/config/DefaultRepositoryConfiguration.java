package org.springframework.data.repository.config;

import java.util.Collections;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/DefaultRepositoryConfiguration.class */
public class DefaultRepositoryConfiguration<T extends RepositoryConfigurationSource> implements RepositoryConfiguration<T> {
    public static final String DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX = "Impl";
    private static final QueryLookupStrategy.Key DEFAULT_QUERY_LOOKUP_STRATEGY = QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;
    private final T configurationSource;
    private final BeanDefinition definition;

    public DefaultRepositoryConfiguration(T configurationSource, BeanDefinition definition) {
        Assert.notNull(configurationSource, "ConfigurationSource must not be null!");
        Assert.notNull(definition, "BeanDefinition must not be null!");
        this.configurationSource = configurationSource;
        this.definition = definition;
    }

    public String getBeanId() {
        return StringUtils.uncapitalize(ClassUtils.getShortName(getRepositoryFactoryBeanName()));
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public Object getQueryLookupStrategyKey() {
        Object configuredStrategy = this.configurationSource.getQueryLookupStrategyKey();
        return configuredStrategy != null ? configuredStrategy : DEFAULT_QUERY_LOOKUP_STRATEGY;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public Iterable<String> getBasePackages() {
        return this.configurationSource.getBasePackages();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public Iterable<String> getImplementationBasePackages() {
        return Collections.singleton(ClassUtils.getPackageName(getRepositoryInterface()));
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public String getRepositoryInterface() {
        return this.definition.getBeanClassName();
    }

    public RepositoryConfigurationSource getConfigSource() {
        return this.configurationSource;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public String getNamedQueriesLocation() {
        return this.configurationSource.getNamedQueryLocation();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public String getImplementationClassName() {
        return ClassUtils.getShortName(getRepositoryInterface()) + getImplementationPostfix();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public String getImplementationBeanName() {
        return StringUtils.uncapitalize(getImplementationClassName());
    }

    public String getImplementationPostfix() {
        String configuredPostfix = this.configurationSource.getRepositoryImplementationPostfix();
        return StringUtils.hasText(configuredPostfix) ? configuredPostfix : DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public Object getSource() {
        return this.configurationSource.getSource();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public T getConfigurationSource() {
        return this.configurationSource;
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public String getRepositoryFactoryBeanName() {
        return this.configurationSource.getRepositoryFactoryBeanName();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public String getRepositoryBaseClassName() {
        return this.configurationSource.getRepositoryBaseClassName();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public boolean isLazyInit() {
        return this.definition.isLazyInit();
    }

    @Override // org.springframework.data.repository.config.RepositoryConfiguration
    public Iterable<TypeFilter> getExcludeFilters() {
        return this.configurationSource.getExcludeFilters();
    }
}
