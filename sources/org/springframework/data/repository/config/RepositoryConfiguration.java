package org.springframework.data.repository.config;

import org.springframework.core.type.filter.TypeFilter;
import org.springframework.data.repository.config.RepositoryConfigurationSource;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/config/RepositoryConfiguration.class */
public interface RepositoryConfiguration<T extends RepositoryConfigurationSource> {
    Iterable<String> getBasePackages();

    Iterable<String> getImplementationBasePackages();

    String getRepositoryInterface();

    Object getQueryLookupStrategyKey();

    String getNamedQueriesLocation();

    String getImplementationClassName();

    String getImplementationBeanName();

    @Deprecated
    String getRepositoryFactoryBeanName();

    String getRepositoryBaseClassName();

    Object getSource();

    T getConfigurationSource();

    boolean isLazyInit();

    Iterable<TypeFilter> getExcludeFilters();
}
